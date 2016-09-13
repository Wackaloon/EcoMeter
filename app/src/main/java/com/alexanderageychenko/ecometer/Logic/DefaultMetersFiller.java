package com.alexanderageychenko.ecometer.Logic;

import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.Model.MeterPosition;
import com.alexanderageychenko.ecometer.Model.MeterType;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class DefaultMetersFiller {
    private ArrayList<Meter> meters = new ArrayList<>();

    public ArrayList<Meter> getDefaultMeters(){
        if (meters.size() == 0){
            meters.add(new Meter(MeterType.GAS, MeterPosition.KITCHEN, null));
            meters.add(new Meter(MeterType.WATER, MeterPosition.KITCHEN, null));
            meters.add(new Meter(MeterType.WATER, MeterPosition.TOILET, null));
            meters.add(new Meter(MeterType.ELECTRICITY, MeterPosition.KITCHEN, null));
            for (Meter meter : meters){
                meter.addValue(0l);
            }
        }
        return meters;
    }
}
