package com.example.gypc.e_dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "e_dictionary.db";
    public static final String TABLE_NAME = "Persons";
    public static final String[] TABLE_COLS = new String[]{ "PersonId", "AvatarIndex", "Name", "Country", "NickName", "StartYear", "EndYear", "Birthplace" };


    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "   PersonId INTEGER PRIMARY KEY   AUTOINCREMENT," +
                "   AvatarIndex    INTEGER   NOT NULL," +
                "   Name           TEXT      NOT NULL," +
                "   Country        TEXT      NOT NULL," +
                "   NickName       TEXT      NOT NULL," +
                "   StartYear      INTEGER   NOT NULL," +
                "   EndYear        INTEGER   NOT NULL," +
                "   Birthplace     TEXT      NOT NULL " +
                ");";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e("MyDBHelper", "create table ERROR: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        } catch (Exception e) {
            Log.e("MyDBHelper", "upgrade table ERROR: ", e);
        }
    }
}
