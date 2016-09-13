package com.alexanderageychenko.ecometer.Model;

import com.alexanderageychenko.ecometer.Logic.DefaultMetersFiller;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class Depository {
    private static Depository instance = new Depository();
    public static Depository getInstance(){
        return instance;
    }
    private ArrayList<Meter> meters = new ArrayList<>();

    public ArrayList<Meter> getMeters() {
        if (meters.size() == 0){
            DefaultMetersFiller filer = new DefaultMetersFiller();
            return filer.getDefaultMeters();
        }
        return meters;
    }

    public void setMeters(ArrayList<Meter> meters) {
        this.meters = meters;
    }
}
