package com.example.gypc.e_dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by XUJIJUN on 2017/11/20.
 */

public class PersonDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "e_dictionary_person.db";
    public static final String TABLE_NAME = "Persons";
    public final String[] TABLE_COLS = new String[]{ "PersonId", "AvatarIndex", "Name", "Country", "NickName", "StartYear", "EndYear", "Birthplace" };

    private PersonDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private volatile static PersonDBHelper instance;
    public static PersonDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (PersonDBHelper.class) {
                if (instance == null) {
                    instance = new PersonDBHelper(context);
                }
            }
        }
        return instance;
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
            Log.e("PersonDBHelper", "create table ERROR: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        } catch (Exception e) {
            Log.e("PersonDBHelper", "upgrade table ERROR: ", e);
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
