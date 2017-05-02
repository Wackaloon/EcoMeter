package com.alexanderageychenko.ecometer.Octopus.home;

import android.content.Intent;
import android.widget.Toast;

import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 16.04.2017.
 */

public class HomeOctopus implements IHomeOctopus {
    private IView iView;
    @Inject
    IMetersDepository iMetersDepository;
    private Disposable metersSubscription;

    public HomeOctopus() {
        Dagger.get().getInjector().inject(this);
    }

    @Override
    public void setIView(IView view) {
        this.iView = view;
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
    private Consumer<Collection<IMeter>> consumer = new Consumer<Collection<IMeter>>() {
        @Override
        public void accept(Collection<IMeter> iMeters) throws Exception {
            iView.setMeters(iMeters);
        }
    };

    @Override
    public void onStart() {
        Observable.just(iMetersDepository.getMeters())
                .map(sortFunc)
                .subscribe(consumer);

        metersSubscription = iMetersDepository.getMetersPublisher()
                .observeOn(Schedulers.newThread())
                .map(sortFunc)
                .subscribe(consumer);

        iMetersDepository.requestMeters();
    }
    @Override
    public void onStop() {
        metersSubscription.dispose();
    }

    @Override
    public void openMeterDetails(IMeter meter) {
        iMetersDepository.selectMeter(meter.getId());
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_DETAILS));
    }
    @Override
    public void openAddValueToMeter(IMeter meter) {
        iMetersDepository.selectMeter(meter.getId());
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_ADD_VALUE));
    }

    @Override
    public void openSettings() {
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_SETTINGS));
    }

    @Override
    public void openStatistics() {
        Toast.makeText(Dagger.get().getContext(), "Soon...", Toast.LENGTH_SHORT).show();
    }
}
