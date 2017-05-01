package com.alexanderageychenko.ecometer.Model.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alexanderageychenko.ecometer.Tools.MyGsonBuilder;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by alexanderageychenko 13.09.16.
 */

public class MetersDAO implements IMetersDAO {
    public final static String TABLE_NAME = "Meter_LIST";
    public final static String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_NAME.ID + " INTEGER PRIMARY KEY," + COLUMN_NAME.JSON + " TEXT);";
    private final static String LOG_TAG = "SQL_" + TABLE_NAME;
    private static Executor executor = Executors.newSingleThreadExecutor();
    Context context;
    DataBaseHelper dataBaseHelper;

    public MetersDAO(Context context) {
        this.context = context;
        dataBaseHelper = DataBaseHelper.getInstance(context);
    }

    @Override
    public ArrayList<Meter> get() {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        ArrayList<Meter> hashMap = new ArrayList<>();
        Gson gson = MyGsonBuilder.build();
        Cursor c = db.query(
                TABLE_NAME,  // The table to query
                new String[]{COLUMN_NAME.ID, COLUMN_NAME.JSON},
                null,
                null,
                null,
                null,
                null
        );
        if (c.moveToFirst())
            do {
                String json = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME.JSON));
                //Log.d(LOG_TAG, "get " + json);
                hashMap.add(gson.fromJson(json, Meter.class));
            } while (c.moveToNext());
        c.close();
        return hashMap;
    }

    @Override
    public void add(final Collection<IMeter> meters) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_NAME + ";");
                Gson gson = MyGsonBuilder.build();
                for (IMeter meter : meters) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NAME.ID, meter.getId());
                    values.put(COLUMN_NAME.JSON, gson.toJson(meter, Meter.class));
                    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
        });
    }


    private static final class COLUMN_NAME {
        public static final String ID = "ID";
        public static final String JSON = "JSON";
    }
}
