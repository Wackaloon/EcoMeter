package com.alexanderageychenko.ecometer.Tools;

import com.annimon.stream.Stream;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by alexanderageychenko on 9/17/17.
 */

public class RxCustomUtils {
    public static class Unsubscriber {
        public Unsubscriber() {
        }

        public static void stUnsubscribe(Disposable subscription) {
            if (subscription != null) {
                subscription.dispose();
                subscription = null;
            }
        }

        public Unsubscriber unsubscribe(Disposable subscription) {
            stUnsubscribe(subscription);
            return this;
        }

        public static void unsubscribe(List<Disposable> subscription) {
            Stream.of(subscription).forEach(Unsubscriber::stUnsubscribe);
            subscription.clear();
        }
    }
}
