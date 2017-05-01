package com.alexanderageychenko.ecometer.Tools;

import com.alexanderageychenko.ecometer.Model.Entity.Meter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterPosition;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class DefaultMetersFiller {
    private ArrayList<Meter> meters = new ArrayList<>();

    public ArrayList<Meter> getDefaultMeters(){
        if (meters.size() == 0){
            meters.add(new Meter(MeterType.GAS, MeterPosition.KITCHEN, null, 1L));
            meters.add(new Meter(MeterType.WATER, MeterPosition.KITCHEN, null, 2L));
            meters.add(new Meter(MeterType.WATER, MeterPosition.TOILET, null, 3L));
            meters.add(new Meter(MeterType.ELECTRICITY, MeterPosition.STREET, null, 4L));
            Meter meter = meters.get(0);
            for (int i = 0; i < 40; ++i){
                Calendar c = Calendar.getInstance();
                int month = i/10;
                int day = (int) Math.round(i/0.8);
                c.set(2016, month, day);
                MeterValue value = new MeterValue(i*10l,c.getTime());
                meter.addValue(value);
            }
        }
        return meters;
    }
}