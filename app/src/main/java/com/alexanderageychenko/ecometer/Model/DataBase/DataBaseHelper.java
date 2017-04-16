package com.alexanderageychenko.ecometer.Model.DataBase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by alexanderageychenko 13.09.16.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cache.db";

    private static final String SQL_DROP_ENTRIES = "DROP TABLE IF EXISTS %s;";
    private static final String SQL_DELETE_ENTRIES = "DELETE FROM %s;";
    //private static final String SQL_DROP_ENTRIES = "select 'drop table ' || name || ';' from sqlite_master where type = 'table';";
    private static DataBaseHelper instance;

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static DataBaseHelper getInstance(Context context) {
        if (instance == null){
            instance = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL(MetersDAO.SQL_CREATE_ENTRIES);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void clearAllTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(SQL_DROP_ENTRIES, MetersDAO.TABLE_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format(SQL_DROP_ENTRIES, MetersDAO.TABLE_NAME));
        onCreate(db);
    }
}
