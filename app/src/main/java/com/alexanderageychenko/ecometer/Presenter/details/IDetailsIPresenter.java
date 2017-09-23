package com.alexanderageychenko.ecometer.Presenter.details;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;
import com.alexanderageychenko.ecometer.Presenter.IPresenter;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Alexander on 11.05.2017.
 */

public interface IDetailsIPresenter extends IPresenter {
    Observable<ArrayList<MeterValue>> getMeterValuesObservable();

    Observable<MeterType> getMeterTypeObservable();

    Observable<String> getMeterFullnameObservable();

    Observable<String> getMeterValuePerDayObservable();

    Observable<String> getMeterValuePerMonthObservable();

    IMeter getMeter();

    int getItemPosition(MeterValue value);

    void requestUpdate();

    void setMeterId(Long meterId);
}
