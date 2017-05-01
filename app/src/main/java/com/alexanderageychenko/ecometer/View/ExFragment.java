package com.alexanderageychenko.ecometer.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import java.lang.reflect.Field;

import static android.content.ContentValues.TAG;


/**
 * Created by alexanderaheychenko on 13.09.16.
 */
public abstract class ExFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {
    protected int real_width, real_height;

    Animation.AnimationListener animatorListener;

    private Float elevator;

    public int getReal_width() {
        return real_width;
    }

    public int getReal_height() {
        return real_height;
    }

    public ExFragment setAnimatorListener(Animation.AnimationListener animatorListener) {
        this.animatorListener = animatorListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (elevator != null) ViewCompat.setElevation(view, elevator);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGlobalLayout() {
        if (getView() != null) {
            real_width = getView().getWidth();
            real_height = getView().getHeight();
        }
    }

    public void setElevator(Float e) {
        elevator = e;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("DEBUG", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("DEBUG", "onActivityCreated");
    }

    @Override
    public void onDetach() {
        Log.d("DEBUG", "onDetach");
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onStop();
    }


    // Arbitrary value; set it to some reasonable default
    private static final int DEFAULT_CHILD_ANIMATION_DURATION = 250;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final Fragment parent = getParentFragment();
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);

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
        } catch (NoSuchFieldException|IllegalAccessException|Resources.NotFoundException ex) {
            Log.w(TAG, "Unable to load next animation from parent.", ex);
            return defValue;
        }
    }

    public boolean popBackStack() {
        return false;
    }

    protected Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return getResources().getDrawable(id, getActivity().getTheme());
        } else {
            //noinspection deprecation
            return getResources().getDrawable(id);
        }
    }

    protected int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, getActivity().getTheme());
        } else {
            //noinspection deprecation
            return getResources().getColor(id);
        }
    }

}
