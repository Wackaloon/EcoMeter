package com.alexanderageychenko.ecometer.Model.Depository.preloader;

import java.util.HashMap;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by alexanderageychenko on 12/19/16
 */

public class PreloaderDepository implements IPreloaderDepository {
    private BehaviorSubject<HashMap<PreloaderType, HashSet<Long>>> preloaderSubject = BehaviorSubject.createDefault(new HashMap<PreloaderType, HashSet<Long>>());
    private PublishSubject<Boolean> blockClickableSubject = PublishSubject.create();
    private boolean blockClickable = false;

    public PreloaderDepository() {
    }

    @Override
    public void startLoading(PreloaderType type, Long id) {
        if (type == PreloaderType.GET_ADS_CONTENT) {
            blockClickable(true);
        }
        if (id == null)
            id = -1L;
        if (!getMap().containsKey(type))  getMap().put(type, new HashSet<>());
        if (!getMap().get(type).contains(id)) {
            getMap().get(type).add(id);
        }
        publish();
    }

    @Override
    public boolean isLoading(PreloaderType type, Long id) {
        if (id == null)
            id = -1L;
        if (!getMap().containsKey(type))  getMap().put(type, new HashSet<>());
        return getMap().get(type).contains(id);
    }

    @Override
    public void stopLoading(PreloaderType type, Long id) {
        if (type == PreloaderType.GET_ADS_CONTENT) {
            blockClickable(false);
        }
        if (id == null)
            id = -1L;
        if (!getMap().containsKey(type))  getMap().put(type, new HashSet<>());
        getMap().get(type).remove(id);
        publish();
    }

    @Override
    public void startLoading(PreloaderType type) {
        startLoading(type, null);
    }

    @Override
    public boolean isLoading(PreloaderType type) {
        return isLoading(type, null);
    }

    @Override
    public void stopLoading(PreloaderType type) {
        stopLoading(type, null);
    }

    private synchronized HashMap<PreloaderType, HashSet<Long>> getMap() {
        return preloaderSubject.getValue();
    }

    private void publish() {
        preloaderSubject.onNext(getMap());
    }

    @Override
    public Observable<HashMap<PreloaderType, HashSet<Long>>> getPreloaderObservable() {
        return preloaderSubject;
    }

    @Override
    public Observable<HashSet<Long>> getPreloaderObservable(final PreloaderType type) {
        if (!getMap().containsKey(type))  getMap().put(type, new HashSet<>());
        return preloaderSubject
                .observeOn(Schedulers.newThread())
                .map(preloaderTypeHashSetHashMap -> preloaderTypeHashSetHashMap.get(type));
    }

    @Override
    public void blockClickable(boolean block) {
        blockClickable = block;
        blockClickableSubject.onNext(block);
    }

    @Override
    public boolean isBlockClickable() {
        return blockClickable;
    }

    @Override
    public Observable<Boolean> observerBlockClickable() {
        return blockClickableSubject;
    }
}
