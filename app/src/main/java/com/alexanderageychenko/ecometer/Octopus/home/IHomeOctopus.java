package com.alexanderageychenko.ecometer.Octopus.home;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Octopus.Octopus;

import java.util.Collection;

import io.reactivex.Observable;

/**
 * Created by Alexander on 16.04.2017.
 */

public interface IHomeOctopus extends Octopus {

    void openMeterDetails(IMeter meter);

    void openAddValueToMeter(IMeter meter);

    void openSettings();

    void openStatistics();

    Observable<Collection<IMeter>> getMetersObservable();

}
