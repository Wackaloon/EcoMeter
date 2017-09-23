package com.alexanderageychenko.ecometer.Presenter.home;

import android.widget.Toast;

import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.Navigator.Navigator;
import com.alexanderageychenko.ecometer.Tools.RxTools;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.AppRxSchedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.AddValueScreen;
import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.DetailsScreen;
import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.MetersSettingsScreen;

/**
 * Created by Alexander on 16.04.2017.
 */

public class HomeIPresenter implements IHomeIPresenter {
    @Inject
    IMetersDepository iMetersDepository;
    @Inject
    @Named(AppRxSchedulers.PRESENTER_THREAD)
    Scheduler backgroundThread;
    @Inject
    MainNavigator homeNavigator;
    private Disposable metersSubscription;
    private BehaviorSubject<Collection<IMeter>> metersObservable = BehaviorSubject.create();
    private MetersConsumer consumer = new MetersConsumer();

    public HomeIPresenter() {
        Dagger.get().getInjector().inject(this);
        metersObservable.onNext(new ArrayList<IMeter>());
    }

    @Override
    public Observable<Collection<IMeter>> getMetersObservable() {
        return metersObservable;
    }

    private Function<Collection<IMeter>, Collection<IMeter>> sortFunc = new Function<Collection<IMeter>, Collection<IMeter>>() {
        @Override
        public Collection<IMeter> apply(Collection<IMeter> iMeters) throws Exception {
            ArrayList<IMeter> list = new ArrayList<IMeter>(iMeters);
            Collections.sort(list, new Comparator<IMeter>() {
                @Override
                public int compare(IMeter meter, IMeter t1) {
                    return meter.getId().compareTo(t1.getId());
                }
            });
            return list;
        }
    };

    @Override
    public void onStart() {
        Observable.just(iMetersDepository.getMeters())
                .map(sortFunc)
                .subscribe(consumer);

        metersSubscription = iMetersDepository.getMetersPublisher()
                .observeOn(backgroundThread)
                .map(sortFunc)
                .subscribe(consumer);

        iMetersDepository.requestMeters();
    }

    @Override
    public void onStop() {
        RxTools.Unsubscriber()
                .unsubscribe(metersSubscription);
    }

    @Override
    public void openMeterDetails(IMeter meter) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("meter_id", meter.getId());
        homeNavigator.openScreenToStack(new Navigator.Screen<>(DetailsScreen, params), R.anim.in_right, R.anim.no_anim);
    }

    @Override
    public void openAddValueToMeter(IMeter meter) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("meter_id", meter.getId());
        homeNavigator.openScreenToStack(new Navigator.Screen<>(AddValueScreen, params), R.anim.in_right, R.anim.no_anim);
    }

    @Override
    public void openSettings() {
        homeNavigator.openScreenToStack(new Navigator.Screen<>(MetersSettingsScreen, null), R.anim.in_right, R.anim.no_anim);
    }

    @Override
    public void openStatistics() {
        Toast.makeText(Dagger.get().getContext(), "Soon...", Toast.LENGTH_SHORT).show();
    }

    private class MetersConsumer implements Consumer<Collection<IMeter>> {
        @Override
        public void accept(Collection<IMeter> iMeters) throws Exception {
            metersObservable.onNext(iMeters);
        }
    }
}
