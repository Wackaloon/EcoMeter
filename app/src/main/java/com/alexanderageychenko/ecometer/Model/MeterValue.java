package com.alexanderageychenko.ecometer.Model;

import java.util.Date;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class MeterValue {
    Long value;
    Date date;

    public MeterValue(Long value, Date date) {
        this.value = value;
        this.date = date;
    }
}
