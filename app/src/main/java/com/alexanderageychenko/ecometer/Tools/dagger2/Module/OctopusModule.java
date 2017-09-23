package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import com.alexanderageychenko.ecometer.Presenter.details.DetailsIPresenter;
import com.alexanderageychenko.ecometer.Presenter.details.IDetailsIPresenter;
import com.alexanderageychenko.ecometer.Presenter.home.HomeIPresenter;
import com.alexanderageychenko.ecometer.Presenter.home.IHomeIPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 16.04.2017.
 */
@Module
public class OctopusModule {
    @Provides
    @Singleton
    IHomeIPresenter provideIHomeOctopus() {
        return new HomeIPresenter();
    }

    @Provides
    @Singleton
    IDetailsIPresenter provideIDetailsOctopus() {
        return new DetailsIPresenter();
    }
}
