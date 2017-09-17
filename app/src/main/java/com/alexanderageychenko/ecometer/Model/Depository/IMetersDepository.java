package com.alexanderageychenko.ecometer.Model.Depository;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;

import java.util.Collection;

import io.reactivex.Observable;

/**
 * Created by Alexander on 15.04.2017.
 */

public interface IMetersDepository {
    //for data source to set new meters
    void setMeters(Collection<IMeter> iMeters);
    //for presenter to get existing meters
    Collection<IMeter> getMeters();
    void addMeter(IMeter meter);
    IMeter getMeter(Long id);
    // for presenter to subscribe for meters changes
    Observable<Collection<IMeter>> getMetersPublisher();
    //for presenter to request new meters data
    void requestMeters();
    //select meter for further operations

    void saveMeters();
}
