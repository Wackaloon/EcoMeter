package com.alexanderageychenko.ecometer.Model.Entity;

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

    @SerializedName("id")
    @Expose
    private Long id;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setMeterType(MeterType meterType) {
        this.meterType = meterType;
    }

    @Override
    public void setMeterPosition(MeterPosition meterPosition) {
        this.meterPosition = meterPosition;
    }

    @Override
    public void addValue(MeterValue value){
        values.add(value);
    }


    public Meter(MeterType meterType, MeterPosition meterPosition, String name, Long id) {
        this.meterType = meterType;
        this.meterPosition = meterPosition;
        this.name = name;
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
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
        if (values != null) {
            double daysCount = 0;
            for (int i = 1; i < values.size(); ++i) {
                MeterValue value = values.get(i);
                MeterValue prevValue = values.get(i - 1);
                Date date = value.getDate();
                Date prevDate = prevValue.getDate();
                long diff = date.getTime() - prevDate.getTime();
                double days = diff / (24 * 60 * 60 * 1000);
                daysCount += days;
                long valueDiff = value.getValue() - prevValue.getValue();
                double perDay = valueDiff / days;
                result += (perDay);
            }
            result /= daysCount;
        }
        return result;
    }

    @Override
    public Double getMeanValuePerMonth() {
        Double result = 0.0;
        if (values != null && values.size() == 1) {
            return values.get(0).value.doubleValue();
        }
        if (values != null && values.size() == 0) {
            return result;
        }
        if (values != null) {
            Calendar c = Calendar.getInstance();
            boolean newMonth = false;
            long montlyValue = 0;
            int monthsCount = 1;
            for (int i = 1; i < values.size(); ++i) {
                MeterValue value = values.get(i);
                MeterValue prevValue = values.get(i - 1);
                Date date = value.getDate();
                Date prevDate = prevValue.getDate();
                c.setTime(date);
                int month = c.get(Calendar.MONTH);
                c.setTime(prevDate);
                int monthPrev = c.get(Calendar.MONTH);
                if (month == monthPrev){
                    montlyValue += value.value-prevValue.value;
                }else{
                    result += montlyValue;
                    monthsCount++;
                    montlyValue = value.value-prevValue.value;
                }

            }
            result /= monthsCount;
        }
        return result;
    }

    @Override
    public String getMeanValuePerDayString() {
        return String.format("%.2f", getMeanValuePerDay());
    }

    @Override
    public String getMeanValuePerMonthString() {
        return String.format("%.2f",getMeanValuePerMonth());
    }

    @Override
    public MeterType getType() {
        return meterType;
    }

    @Override
    public MeterPosition getPosition() {
        return meterPosition;
    }

    @Override
    public void setValue(Long newValue) {
        this.valueLast = new MeterValue(newValue, Calendar.getInstance().getTime());
    }

    @Override
    public void applyValue() {
        if (valueLast != null)
            values.add(valueLast);
        valueLast = null;
    }
}