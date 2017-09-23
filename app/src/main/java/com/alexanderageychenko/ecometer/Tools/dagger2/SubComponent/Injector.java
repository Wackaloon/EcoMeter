package com.alexanderageychenko.ecometer.Tools.dagger2.SubComponent;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.Model.Depository.MetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.rest.Rest;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;
import com.alexanderageychenko.ecometer.Presenter.details.DetailsIPresenter;
import com.alexanderageychenko.ecometer.Presenter.home.HomeIPresenter;
import com.alexanderageychenko.ecometer.View.add.AddValueFragment;
import com.alexanderageychenko.ecometer.View.details.DetailsFragment;
import com.alexanderageychenko.ecometer.View.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.View.home.HomeFragment;
import com.alexanderageychenko.ecometer.View.meters_settings.MetersSettingsFragment;

import dagger.Subcomponent;

/**
 * Created by Alexander on 16.04.2017.
 */
@Subcomponent
public interface Injector {

    void inject(AddValueFragment AddValueFragment);
    void inject(EditMeterFragment EditMeterFragment);
    void inject(DetailsFragment DetailsFragment);
    void inject(DetailsIPresenter DetailsOctopus);
    void inject(HomeFragment HomeFragment);
    void inject(HomeIPresenter HomeOctopus);
    void inject(MetersSettingsFragment MetersSettingsFragment);
    void inject(MetersDepository MetersDepository);
    void inject(Rest Rest);
    void inject(ExFragment ExFragment);
    void inject(MainActivity MainActivity);
}
