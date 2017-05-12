package com.alexanderageychenko.ecometer.Model.Entity;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public interface IMeter {

    String getName();
    Long getId();
    String getFullName();
    String getLastValue();
    String getLastValueDate();
    ArrayList<MeterValue> getAllValues();
    MeterValue getItemById(Long id);
    void updateItem(MeterValue item);
    Double getMeanValuePerDay();
    Double getMeanValuePerMonth();
    String getMeanValuePerDayString();
    String getMeanValuePerMonthString();
    MeterType getType();
    MeterPosition getPosition();
    void setMeterType(MeterType meterType);
    void setMeterPosition(MeterPosition meterPosition);
    void addValue(MeterValue value);
    void setName(String name);
    void setValue(Long newValue);
    void applyValue();
}
