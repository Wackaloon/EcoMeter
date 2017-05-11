package com.alexanderageychenko.ecometer.Octopus.details;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;
import com.alexanderageychenko.ecometer.Octopus.Octopus;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Alexander on 11.05.2017.
 */

public interface IDetailsOctopus extends Octopus {
    Observable<ArrayList<MeterValue>> getMeterValuesObservable();

    Observable<MeterType> getMeterTypeObservable();

    Observable<String> getMeterFullnameObservable();

    Observable<String> getMeterValuePerDayObservable();

    Observable<String> getMeterValuePerMonthObservable();

    IMeter getMeter();

    int getItemPosition(MeterValue value);

}
