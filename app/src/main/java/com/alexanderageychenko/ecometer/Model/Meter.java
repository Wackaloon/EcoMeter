package com.alexanderageychenko.ecometer.Model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public class Meter implements IMeter {
    @SerializedName("type")
    @Expose
    private MeterType meterType;
    @SerializedName("position")
    @Expose
    private MeterPosition meterPosition;
    @SerializedName("name")
    @Expose
    private String name = null;
    @SerializedName("values")
    @Expose
    private ArrayList<MeterValue> values = new ArrayList<>();

    @SerializedName("value_last")
    @Expose
    private MeterValue valueLast = null;

    public void setNewValue(@NonNull Long value) {
        this.valueLast = new MeterValue(value, Calendar.getInstance().getTime());
    }

    public void applyNewValue() {
        if (valueLast != null)
            values.add(valueLast);
        valueLast = null;
    }

    public Meter(MeterType meterType, MeterPosition meterPosition, String name) {
        this.meterType = meterType;
        this.meterPosition = meterPosition;
        this.name = name;
    }

    public void addValue(Long value) {
        values.add(new MeterValue(value, Calendar.getInstance().getTime()));
    }

    @Override
    public String getFullName() {
        if (name != null)
            return name + " " + meterType + " " + meterPosition;
        else
            return meterType + " " + meterPosition;
    }

    @Override
    public String getLastValue() {
        if (values != null && values.size() > 0)
            return String.valueOf(values.get(values.size() - 1).value);
        else
            return "0";
    }

    @Override
    public String getLastValueDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("", Locale.US);
        simpleDateFormat.applyPattern("dd MMM yyyy");
        Date date = null;
        if (values != null && values.size() > 0)
            date = values.get(values.size() - 1).date;
        if (date == null)
            date = Calendar.getInstance().getTime();
        return simpleDateFormat.format(date);
    }

    @Override
    public ArrayList<MeterValue> getAllValues() {
        return values;
    }

    @Override
    public Double getMeanValuePerDay() {
        Double result = 0.0;
        if (values != null && values.size() == 1) {
            return values.get(0).value.doubleValue();
        }
        if (values != null && values.size() == 0) {
            return result;
        }
        if (values != null)
            for (MeterValue value : values) {
                result += value.value * 1f / values.size();
            }
        return result;
    }

    @Override
    public Double getMeanValuePerMonth() {
        Double result = 0.0;
        if (values.size() == 1) {
            return values.get(0).value.doubleValue();
        }
        if (values.size() == 0) {
            return result;
        }
        for (MeterValue value : values) {
            result += value.value * 1f / values.size();
        }
        return result;
    }

    @Override
    public MeterType getType() {
        return meterType;
    }

    @Override
    public MeterPosition getPosition() {
        return meterPosition;
    }
}
