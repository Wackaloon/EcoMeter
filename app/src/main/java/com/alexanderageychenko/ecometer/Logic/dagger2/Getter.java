package com.alexanderageychenko.ecometer.Logic.dagger2;

import android.app.Application;

import dagger.Subcomponent;

/**
 * Created by Alexander on 16.04.2017.
 */
@Subcomponent
public interface Getter {
    DepositoryComponent depository();
    Application getApplicationContext();
}
