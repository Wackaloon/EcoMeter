package com.alexanderageychenko.ecometer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class MeterValue {
    @SerializedName("value")
    @Expose
    Long value;
    @SerializedName("date")
    @Expose
    Date date;

    public MeterValue(Long value, Date date) {
        this.value = value;
        this.date = date;
    }
}
