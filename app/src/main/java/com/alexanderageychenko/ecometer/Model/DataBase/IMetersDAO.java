package com.alexanderageychenko.ecometer.Model.DataBase;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;

import java.util.ArrayList;
import java.util.Collection;

public interface IMetersDAO {
    ArrayList<Meter> get();
    void set(Collection<IMeter> meters);
}
