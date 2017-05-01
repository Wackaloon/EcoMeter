package com.alexanderageychenko.ecometer.Octopus.home;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Octopus.Octopus;

import java.util.Collection;

/**
 * Created by Alexander on 16.04.2017.
 */

public interface IHomeOctopus extends Octopus<IHomeOctopus.IView> {

    void openMeterDetails(IMeter meter);

    void openAddValueToMeter(IMeter meter);

    interface IView extends Octopus.IView {

        void setMeters(Collection<IMeter> meters);
    }
}
