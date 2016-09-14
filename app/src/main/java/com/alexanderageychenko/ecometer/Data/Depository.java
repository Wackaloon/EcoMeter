package com.alexanderageychenko.ecometer.Data;

import com.alexanderageychenko.ecometer.Logic.DefaultMetersFiller;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.Meter;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class Depository {
    private MetersDAO metersDAO;
    private static Depository instance;
    public static Depository getInstance(){
        if (instance == null)
            init();
        return instance;
    }
    public static void init(){
        instance = new Depository();
    }

    public Depository() {
        metersDAO = new MetersDAO(MainApplication.getInstance());
        meters = metersDAO.get();
    }

    private ArrayList<Meter> meters = new ArrayList<>();

    public ArrayList<Meter> getMeters() {
        if (meters.size() == 0){
            DefaultMetersFiller filer = new DefaultMetersFiller();
            meters = filer.getDefaultMeters();
        }
        return meters;
    }

    public void setMeters(ArrayList<Meter> meters) {
        this.meters = meters;
    }

    public void saveToDB(){
        metersDAO.add(meters);
    }

    public void addNewMeter(Meter meter){
        meters.add(meter);
    }
}

