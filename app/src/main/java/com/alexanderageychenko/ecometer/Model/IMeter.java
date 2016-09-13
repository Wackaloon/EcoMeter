package com.alexanderageychenko.ecometer.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public interface IMeter {

    String getFullName();

    String getLastValue();

    String getLastValueDate();

    ArrayList<MeterValue> getAllValues();

    Double getMeanValuePerDay();

    Double getMeanValuePerMonth();

    MeterType getType();

    MeterPosition getPosition();


}
