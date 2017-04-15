package com.alexanderageychenko.ecometer.Logic.dagger2;

import com.alexanderageychenko.ecometer.Fragments.add.AddValueFragment;
import com.alexanderageychenko.ecometer.Fragments.details.DetailsFragment;
import com.alexanderageychenko.ecometer.Fragments.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.Fragments.home.HomeFragment;
import com.alexanderageychenko.ecometer.Fragments.settings.SettingsFragment;

import dagger.Subcomponent;

/**
 * Created by Alexander on 16.04.2017.
 */
@Subcomponent
public interface Injector {

    void inject(AddValueFragment AddValueFragment);
    void inject(EditMeterFragment EditMeterFragment);
    void inject(DetailsFragment DetailsFragment);
    void inject(HomeFragment HomeFragment);
    void inject(SettingsFragment SettingsFragment);
}
