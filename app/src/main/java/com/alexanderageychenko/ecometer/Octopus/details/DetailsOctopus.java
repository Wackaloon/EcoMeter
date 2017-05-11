package com.alexanderageychenko.ecometer.Octopus.details;

import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Alexander on 11.05.2017.
 */

public class DetailsOctopus implements IDetailsOctopus {

    @Inject
    IMetersDepository iMetersDepository;

    private BehaviorSubject<ArrayList<MeterValue>> meterValuesObservable = BehaviorSubject
            .createDefault(new ArrayList<MeterValue>());
    private BehaviorSubject<MeterType> meterTypeObservable = BehaviorSubject.create();
    private BehaviorSubject<String> meterFullnameObservable = BehaviorSubject.createDefault("");
    private BehaviorSubject<String> meterValuePerDayObservable = BehaviorSubject.createDefault("");
    private BehaviorSubject<String> meterValuePerMonthObservable = BehaviorSubject.createDefault("");
    private IMeter meter;

    public DetailsOctopus() {
        Dagger.get().getInjector().inject(this);
    }

    @Override
    public Observable<ArrayList<MeterValue>> getMeterValuesObservable() {
        return meterValuesObservable;
    }

    @Override
    public Observable<MeterType> getMeterTypeObservable() {
        return meterTypeObservable;
    }

    @Override
    public Observable<String> getMeterFullnameObservable() {
        return meterFullnameObservable;
    }

    @Override
    public Observable<String> getMeterValuePerDayObservable() {
        return meterValuePerDayObservable;
    }

    @Override
    public Observable<String> getMeterValuePerMonthObservable() {
        return meterValuePerMonthObservable;
    }

    @Override
    public IMeter getMeter() {
        return meter;
    }

    @Override
    public int getItemPosition(MeterValue value) {
        return meter.getAllValues().indexOf(value);
    }

    @Override
    public void onStart() {
        meter = iMetersDepository.getSelectedMeter();
        meterTypeObservable.onNext(meter.getType());
        meterFullnameObservable.onNext(meter.getFullName());
        meterValuePerDayObservable.onNext(meter.getMeanValuePerDayString());
        meterValuePerMonthObservable.onNext(meter.getMeanValuePerMonthString());
        meterValuesObservable.onNext(meter.getAllValues());
    }

    @Override
    public void onStop() {

    }
}
