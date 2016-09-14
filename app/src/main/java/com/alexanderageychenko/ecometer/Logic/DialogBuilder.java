package com.alexanderageychenko.ecometer.Logic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alexanderageychenko.ecometer.Model.DeleteMeterListener;
import com.alexanderageychenko.ecometer.Model.DetailsEditListener;
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.Model.MeterValue;
import com.alexanderageychenko.ecometer.R;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by alexanderageychenko on 13.09.16.
 */
public abstract class DialogBuilder {
    static public Dialog getProgressDialog(Context context) {
        Dialog progressDialog;
        //progressDialog = new AlertDialog.Builder(context).setMessage("Waiting for the server…").setCancelable(false).create();
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setMessage("Waiting for the server…");

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static Dialog getExitDialog(final Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure that you want to close the application?").create();

        return alertDialog;
    }

    public static Dialog getEditDetailsDialog(final Activity activity, final Meter meter, final int position, final DetailsEditListener detailsEditListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Edit Value");
        final String blockCharacterSet = "0123456789";
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && !blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setFilters(new InputFilter[]{filter});
        input.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        alertDialog.setView(input);
        alertDialog.setMessage("Input new value");
        alertDialog.setPositiveButton("Apply",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        Long valueLong = null;
                        try {
                            valueLong = Long.parseLong(value);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (valueLong != null){
                            MeterValue valueMeter = meter.getAllValues().get(position);
                            valueMeter.setValue(valueLong);
                        }
                        if (detailsEditListener != null)
                        detailsEditListener.valueWasEdited();
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return alertDialog.create();
    }

    public static Dialog getExitEditDateDialog(final Activity activity, final Meter meter, final int position, final DetailsEditListener detailsEditListener) {

        final Calendar calendar = Calendar.getInstance();

        final MeterValue value = meter.getAllValues().get(position);
        Date date = value.getDate();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                calendar.set(arg1, arg2, arg3);
                value.setDate(calendar.getTime());
                if (detailsEditListener != null)
                    detailsEditListener.valueWasEdited();
                // arg1 = year
                // arg2 = month
                // arg3 = day
            }
        };
        return new DatePickerDialog(activity, myDateListener, year, month, day);
    }

    public static Dialog getDeleteMeterDialog(final Activity activity, final DeleteMeterListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) listener.delete();
                dialog.dismiss();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure that you want to delete this meter?").create();

        return alertDialog;
    }


}
