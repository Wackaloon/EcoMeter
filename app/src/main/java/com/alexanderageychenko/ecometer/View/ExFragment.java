package com.alexanderageychenko.ecometer.View;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Model.Depository.preloader.IPreloaderDepository;
import com.alexanderageychenko.ecometer.Model.Depository.preloader.PreloaderType;
import com.alexanderageychenko.ecometer.Model.Entity.IPreloader;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.ResTools;
import com.alexanderageychenko.ecometer.Tools.RxCustomUtils;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.annimon.stream.Stream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by alexanderageychenko on 01.02.16
 */
public abstract class ExFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener, IPreloader {
    private static final String TAG = ExFragment.class.getSimpleName();
    // Arbitrary value; set it to some reasonable default
    private static final int DEFAULT_CHILD_ANIMATION_DURATION = 250;
    protected final List<Disposable> subscriptions = new ArrayList<>();

    @Inject
    protected
    MainNavigator homeNavigator;
    @Inject
    IPreloaderDepository iPreloaderDepository;
    protected MainNavigator usedNavigator;
    protected int real_width, real_height;
    protected Bitmap tmpBitmap;
    protected View preLoader, noInternet;
    protected TextView noInternetText;
    protected Timer timer;
    Animation.AnimationListener animatorListener;
    Disposable preloaderSubscription, timerShowSubscribtion;
    HashMap<PreloaderType, HashSet<Long>> preloaderType = new HashMap<>();
    //    private SwipeRefreshLayout swipeContainer;
    private Float elevator;
    private Disposable internetSubscription;
    private Disposable screnAvailableSubscriber;
    private boolean screenIsUnavailable = false;
    private boolean internetIsUnavailable = false;
    private boolean presentersActive = false;

    public ExFragment() {
        Dagger.get().getInjector().inject(this);
    }

    private static long getNextAnimationDuration(Fragment fragment, long defValue) {
        try {
            // Attempt to get the resource ID of the next animation that
            // will be applied to the given fragment.
            Field nextAnimField = Fragment.class.getDeclaredField("mNextAnim");
            nextAnimField.setAccessible(true);
            int nextAnimResource = nextAnimField.getInt(fragment);
            Animation nextAnim = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnimResource);

            // ...and if it can be loaded, return that animation's duration
            return (nextAnim == null) ? defValue : nextAnim.getDuration();
        } catch (NoSuchFieldException | IllegalAccessException | Resources.NotFoundException ex) {
            Log.w(TAG, "Unable to load next animation from parent.", ex);
            return defValue;
        }
    }

    public void onStartPresenters() {
        presentersActive = true;
    }

    public void onStopPresenters() {
        presentersActive = false;
    }

    public void setUsedNavigator(MainNavigator usedNavigator) {
        this.usedNavigator = usedNavigator;
    }

    private void log(String methodName) {
        String name = this.getClass().toString();
        String logName = name.replace("class com.jellychip.", "");
        Log.d("DEBUG_FC", logName + " : " + methodName);
    }

    protected void setPreloaderType(PreloaderType type) {
        setPreloaderType(type, null);
    }

    protected void setPreloaderType(HashMap<PreloaderType, HashSet<Long>> types) {
        preloaderType.putAll(types);
        preloaderSubscription = iPreloaderDepository.getPreloaderObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkLoadingState);
    }

    protected void setPreloaderType(final PreloaderType type, Long id) {
        if (id == null) id = -1L;
        HashSet<Long> ids = new HashSet<>();
        ids.add(id);
        preloaderType.put(type, ids);
        preloaderSubscription = iPreloaderDepository.getPreloaderObservable(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(longs -> checkLoadingState(longs, type));
    }

    private void checkLoadingState(HashSet<Long> longs, final PreloaderType type) {
        if (longs != null && !longs.isEmpty() && preloaderType.get(type) != null) {
            Long filtered = Stream.of(preloaderType.get(type))
                    .filter(longs::contains)
                    .findFirst()
                    .orElse(null);
            if (filtered != null)
                showLoading();
            else
                hideLoading();
        } else {
            hideLoading();
        }
    }

    private void checkLoadingState(HashMap<PreloaderType, HashSet<Long>> preloaderTypeHashSetHashMap) {
        boolean isLoadingSomething = false;
        for (PreloaderType type : preloaderType.keySet()) {
            if (isLoadingSomething)
                break;
            if (preloaderTypeHashSetHashMap.containsKey(type) && !preloaderTypeHashSetHashMap.get(type).isEmpty())
                for (Long id : preloaderType.get(type)) {
                    if (preloaderTypeHashSetHashMap.get(type).contains(id)) {
                        isLoadingSomething = true;
                        break;
                    }
                }
        }
        if (isLoadingSomething) {
            showLoading();
        } else {
            hideLoading();
        }
    }

    public ExFragment setAnimatorListener(Animation.AnimationListener animatorListener) {
        this.animatorListener = animatorListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        log("onViewCreated");
        if (elevator != null) ViewCompat.setElevation(view, elevator);
        view.setOnClickListener(v -> {
        });
        screenIsUnavailable = false;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGlobalLayout() {
        if (getView() != null) {
            real_width = getView().getWidth();
            real_height = getView().getHeight();
        }
    }

    protected void setPreloader(View view, boolean noNoIntenetScreen) {
//        preLoader = view.findViewById(R.id.preLoaderBar);
    }

    private void setupNoIntenet(View view) {
//        noInternet = view.findViewById(R.id.no_internet);
//        noInternetText = (TextView) view.findViewById(R.id.no_internet_text);
//        if (noInternet != null && noInternetText != null) {
//            noInternetText.setText("No Internet\nConnection");
//            FontTool.setTypeface(noInternetText, FontTools.FontType.SF_UI_DISPLAY_HEAVY, 0.32, 45, R.dimen.design_dp_40);
//            internetSubscription = iCheckInternet.getInternetStateObservable()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::noInternet, Throwable::printStackTrace);
//        }
    }

    protected void setPreloader(View view) {
        preLoader = view.findViewById(R.id.preLoaderBar);
        setupNoIntenet(view);
    }


    protected void setPreloaderWhite(View view) {
        preLoader = view.findViewById(R.id.preLoaderBar);
        preLoader.getLayoutParams().width = ResTools.getDimenPixelSize(getContext(), R.dimen.design_dp_43);
        preLoader.getLayoutParams().height = ResTools.getDimenPixelSize(getContext(), R.dimen.design_dp_43);
    }


    public void setElevator(Float e) {
        elevator = e;
    }

    @Override
    public void onAttach(Context context) {
        log("onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        log("onDestroyView");
        long time = System.currentTimeMillis();
        Log.d("B_TIME_TEST", "" + (System.currentTimeMillis() - time));
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        log("onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        log("onDetach");
        super.onDetach();
    }

    @Override
    public void onStart() {
        log("onStart");
        super.onStart();
        if (usedNavigator != null)
            subscriptions.add(
                    Observable.merge(Observable.just(usedNavigator.isActive()),
                            usedNavigator.observableActive())
                            .subscribe(aBoolean -> {
                                if (aBoolean && !presentersActive)
                                    onStartPresenters();
                                else
                                    onStopPresenters();
                            })
            );
        else
            onStartPresenters();
    }

    @Override
    public void onStop() {
        log("onStop");
        onStopPresenters();
        RxCustomUtils.Unsubscriber.unsubscribe(subscriptions);
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (tmpBitmap != null)
                tmpBitmap.recycle();
            tmpBitmap = null;
        }
        new RxCustomUtils.Unsubscriber()
                .unsubscribe(preloaderSubscription)
                .unsubscribe(screnAvailableSubscriber)
                .unsubscribe(internetSubscription);
        super.onStop();
    }

    protected BroadcastReceiver getLocalBroadcastReceiver() {
        return null;
    }

    protected View getPreLoader() {
        return preLoader;
    }

    public boolean popBackStack() {
        return popBackStack(R.anim.no_anim, R.anim.out_right);
    }

    public boolean popBackStack(int animIn, int animOut) {
        if (usedNavigator != null) {
            usedNavigator.backToStack(animIn, animOut);
            return true;
        } else return false;
    }

    protected Drawable getDrawable(int id) {
        return ResTools.getDrawable(getContext(), id);
    }

    protected int getColor(int id) {
        return ResTools.getColor(getContext(), id);
    }

    public boolean showLoading() {
        int defaultDelayForPreloaders = 150;
        return showLoading(defaultDelayForPreloaders);
    }

    public boolean showLoading(long delay) {
        //if already showing - go back
        if (timerShowSubscribtion != null && !timerShowSubscribtion.isDisposed())
            return false;
        // if preloader not initiated - go back
        if (getPreLoader() == null)
            return false;
        //if no drawable - create

        //set timer until preloader will be showed
        timerShowSubscribtion = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (getPreLoader() != null) getPreLoader().setVisibility(View.VISIBLE);
                });

        return true;
    }

    public void hideLoading() {
        Observable.just(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    new RxCustomUtils.Unsubscriber()
                            .unsubscribe(timerShowSubscribtion);
                    if (getPreLoader() != null)
                        getPreLoader().setVisibility(View.GONE);
                });
    }

    protected void hideLoadingHard() {
        if (noInternet != null && noInternet.getVisibility() == View.VISIBLE)
            new RxCustomUtils.Unsubscriber()
                    .unsubscribe(timerShowSubscribtion);
        Observable.just(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (getPreLoader() != null)
                        getPreLoader().setVisibility(View.GONE);
                });
    }

    @Override
    public void onResume() {
        log("onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        log("onPause");
        Log.d("PreLoader", "hideLoading: pause");
        hideLoadingHard();
        super.onPause();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final Fragment parent = getParentFragment();
        Animation anim;

        // Apply the workaround only if this is a child fragment, and the parent
        // is being removed.
        if (!enter && parent != null && parent.isRemoving()) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            Animation doNothingAnim = new AlphaAnimation(1, 1);
            doNothingAnim.setDuration(getNextAnimationDuration(parent, DEFAULT_CHILD_ANIMATION_DURATION));
            anim = doNothingAnim;
            if (animatorListener != null)
                anim.setAnimationListener(animatorListener);
        } else {
            anim = super.onCreateAnimation(transit, enter, nextAnim);
            if (nextAnim != 0) {
                anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                if (animatorListener != null)
                    anim.setAnimationListener(animatorListener);
            }
        }

        return anim;
    }


}
