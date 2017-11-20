package com.example.gypc.e_dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by XUJIJUN on 2017/11/20.
 */

public class PersonCollectorDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "e_dictionary_person_id_collected.db";
    public static final String TABLE_NAME = "PersonIdCollected";
    public final String[] TABLE_COLS = new String[]{ "PersonId" };

    private PersonCollectorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private volatile static PersonCollectorDBHelper instance;
    public static PersonCollectorDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (PersonCollectorDBHelper.class) {
                if (instance == null) {
                    instance = new PersonCollectorDBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (PersonId INTEGER PRIMARY KEY);";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e("PersonCollectorDBHelper", "create table ERROR: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        } catch (Exception e) {
            Log.e("PersonCollectorDBHelper", "upgrade table ERROR: ", e);
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
