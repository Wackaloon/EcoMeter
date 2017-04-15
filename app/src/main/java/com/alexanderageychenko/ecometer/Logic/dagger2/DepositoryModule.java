package com.alexanderageychenko.ecometer.Logic.dagger2;

import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Depository.MetersDepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 15.04.2017.
 */

@Module
public class DepositoryModule {
    @Singleton
    @Provides
    IMetersDepository provideIMetersDepository(){
        return new MetersDepository();
    }
}
