package com.alexanderageychenko.ecometer.Model.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class MeterValue {
    @SerializedName("id")
    @Expose
    Long id;
    @SerializedName("value")
    @Expose
    Long value;
    @SerializedName("date")
    @Expose
    Date date;

    public MeterValue(Long value, Date date) {
        this.value = value;
        this.date = date;
        this.id = new Date().getTime();
    }

    public Long getId() {
        if (id == null) id = new Date().getTime();
        return id;
    }

    public Long getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public String getValueString() {
        if (value != null)
            return String.valueOf(value);
        else
            return "0";
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("", Locale.US);
        simpleDateFormat.applyPattern("dd MMM yyyy");
        Date date = null;
        if (this.date != null)
            date = this.date;
        if (date == null)
            date = Calendar.getInstance().getTime();
        return simpleDateFormat.format(date);
    }
}
