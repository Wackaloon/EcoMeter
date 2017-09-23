package com.alexanderageychenko.ecometer.Presenter.home;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Presenter.IPresenter;

import java.util.Collection;

import io.reactivex.Observable;

/**
 * Created by Alexander on 16.04.2017.
 */

public interface IHomeIPresenter extends IPresenter {

    void openMeterDetails(IMeter meter);

    void openAddValueToMeter(IMeter meter);

    void openSettings();

    void openStatistics();

    Observable<Collection<IMeter>> getMetersObservable();

}
