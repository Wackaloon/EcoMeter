package com.alexanderageychenko.ecometer.Model.Depository;

import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class MetersDepository implements IMetersDepository {
    private MetersDAO metersDAO;
    private Long selectedMeterId;
    private HashMap<Long, IMeter> meters = new HashMap<>();
    private PublishSubject<Collection<IMeter>> metersPublisher = PublishSubject.create();

    @Override
    public void setMeters(Collection<IMeter> iMeters) {
        meters.clear();
        for (IMeter m : iMeters)
            meters.put(m.getId(), m);
        saveToDB();

    }

    private void saveToDB(){
        Observable.just(meters.values()) // use  to imitate rest request
                .observeOn(Schedulers.io())  //io for DB usage
                .subscribe(new Consumer<Collection<IMeter>>() {
                    @Override
                    public void accept(Collection<IMeter> time) throws Exception {
                        if (metersDAO == null) {
                            metersDAO = new MetersDAO(MainApplication.getInstance());
                        }
                        metersDAO.add(meters.values());
                    }
                });
    }

    @Override
    public Collection<IMeter> getMeters() {
        if (meters.isEmpty()) {
//            DefaultMetersFiller filer = new DefaultMetersFiller();
//            ArrayList<Meter> defaultMeters = filer.getDefaultMeters();
//            for (IMeter m : defaultMeters)
//                meters.put(m.getId(), m);
        }
        return meters.values();
    }

    @Override
    public void addMeter(IMeter meter) {
        meters.put(meter.getId(), meter);
        saveToDB();
    }

    @Override
    public IMeter getMeter(Long id) {
        return meters.get(id);
    }

    @Override
    public Observable<Collection<IMeter>> getMetersPublisher() {
        return metersPublisher;
    }

    @Override
    public void requestMeters() {
        Observable.timer(1, TimeUnit.SECONDS) // use  to imitate rest request
                .observeOn(Schedulers.io())  //io for DB usage
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        if (metersDAO == null) {
                            metersDAO = new MetersDAO(MainApplication.getInstance());
                        }
                        if (meters.isEmpty()) {//load meters from DAO
                            ArrayList<Meter> defaultMeters = metersDAO.get();
                            for (IMeter m : defaultMeters)
                                meters.put(m.getId(), m);
                        }
                        metersPublisher.onNext(meters.values());
                    }
                });

    }

    @Override
    public void selectMeter(Long meterId) {
        selectedMeterId = meterId;
    }

    @Override
    public IMeter getSelectedMeter() {
        return meters.get(selectedMeterId);
    }

}

