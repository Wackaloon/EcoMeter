package com.alexanderageychenko.ecometer.Model.DataBase;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alexander on 01.05.2017.
 */

public interface IMetersDAO {
    ArrayList<Meter> get();

    void add(Collection<IMeter> meters);
}
