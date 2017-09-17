package com.alexanderageychenko.ecometer.Model.Depository.preloader;

import java.util.HashMap;
import java.util.HashSet;

import io.reactivex.Observable;

/**
 * Created by alexanderageychenko on 12/19/16.
 */

public interface IPreloaderDepository {
    void startLoading(PreloaderType type, Long id);
    void startLoading(PreloaderType type);
    boolean isLoading(PreloaderType type, Long id);
    boolean isLoading(PreloaderType type);
    void stopLoading(PreloaderType type, Long id);
    void stopLoading(PreloaderType type);
    Observable<HashMap<PreloaderType, HashSet<Long>>> getPreloaderObservable();
    Observable<HashSet<Long>> getPreloaderObservable(PreloaderType type);

    void blockClickable(boolean block);
    boolean isBlockClickable();
    Observable<Boolean> observerBlockClickable();
}
