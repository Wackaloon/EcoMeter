package com.alexanderageychenko.ecometer.Logic;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;

import com.alexanderageychenko.ecometer.R;


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


}
