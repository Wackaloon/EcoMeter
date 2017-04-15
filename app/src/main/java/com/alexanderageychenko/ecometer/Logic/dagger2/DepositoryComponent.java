package com.alexanderageychenko.ecometer.Logic.dagger2;

import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;

import dagger.Subcomponent;

/**
 * Created by Alexander on 15.04.2017.
 */
@Subcomponent
public interface DepositoryComponent {
    IMetersDepository getIMetersDepository();
}
