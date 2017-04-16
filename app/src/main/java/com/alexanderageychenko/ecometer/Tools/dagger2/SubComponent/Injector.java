package com.alexanderageychenko.ecometer.Tools.dagger2.SubComponent;

import com.alexanderageychenko.ecometer.View.add.AddValueFragment;
import com.alexanderageychenko.ecometer.View.details.DetailsFragment;
import com.alexanderageychenko.ecometer.View.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.View.home.HomeFragment;
import com.alexanderageychenko.ecometer.Octopus.home.HomeOctopus;
import com.alexanderageychenko.ecometer.View.settings.SettingsFragment;

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
    void inject(HomeOctopus HomeOctopus);
    void inject(SettingsFragment SettingsFragment);
}
