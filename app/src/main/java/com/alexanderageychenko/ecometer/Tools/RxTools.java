package com.alexanderageychenko.ecometer.Tools;

import io.reactivex.disposables.Disposable;

/**
 * Created by Alexander on 11.05.2017.
 */

public class RxTools {

    public class Unsubscriber{
        public Unsubscriber unsubscribe(Disposable disposable){
            if (disposable != null) disposable.dispose();
            return this;
        }
    }
    public static Unsubscriber Unsubscriber(){
        return new RxTools().new Unsubscriber();
    }
}
