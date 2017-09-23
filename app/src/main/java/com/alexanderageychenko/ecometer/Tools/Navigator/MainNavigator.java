package com.alexanderageychenko.ecometer.Tools.Navigator;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MainNavigator extends NavigatorImpl<MainScreenType> {
    private final BehaviorSubject<Boolean> publishRelay = BehaviorSubject.create();
    private boolean active = true;
    private boolean alwaysActive = false;

    @Override
    protected MainScreenType getDefaultType() {
        return MainScreenType.MetersListScreen;
    }

    public boolean isActive() {
        if (alwaysActive) return true;
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (alwaysActive) return;
        publishRelay.onNext(active);
    }

    public void alwaysActive(Boolean freezing) {
        alwaysActive = freezing;
        if (!freezing) {
            publishRelay.onNext(active);
        } else if (!active) {
            publishRelay.onNext(true);
        }
    }

    public Observable<Boolean> observableActive() {
        return publishRelay;
    }
}