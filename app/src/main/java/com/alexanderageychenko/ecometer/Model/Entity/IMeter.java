package com.alexanderageychenko.ecometer.Model.Entity;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public interface IMeter {

    String getName();

    void setMeterType(MeterType meterType);

    void setMeterPosition(MeterPosition meterPosition);

    void addValue(MeterValue value);

    void setName(String name);

    Long getId();

    String getFullName();

    String getLastValue();

    String getLastValueDate();

    ArrayList<MeterValue> getAllValues();

    Double getMeanValuePerDay();

    Double getMeanValuePerMonth();

    String getMeanValuePerDayString();

    String getMeanValuePerMonthString();

    MeterType getType();

    MeterPosition getPosition();

    void setValue(Long newValue);
    void applyValue();


}
