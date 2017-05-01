package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.content.Context;

import com.alexanderageychenko.ecometer.Model.DataBase.IMetersDAO;
import com.alexanderageychenko.ecometer.Model.DataBase.MetersDAO;
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

    @Singleton
    @Provides
    IMetersDAO provideIMetersDAO(Context context){
        return new MetersDAO(context);
    }
}
