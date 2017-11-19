package com.example.gypc.e_dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by XUJIJUN on 2017/11/19.
 */

public class PersonCollectorDBDao {

    private MyDBHelper myDBHelper;
    private Cursor cursor;

    public PersonCollectorDBDao(Context context) {
        try {
            myDBHelper = new MyDBHelper(context);
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "constructor", e);
        }
    }

    private boolean personIdExists(int personId) {
        try {
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            cursor = db.query(MyDBHelper.TABLE_NAME,
                    new String[]{"PersonId"},
                    "PersonId = ?",
                    new String[]{ String.valueOf(personId) },
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "personIdExists", e);
            return false;
        }
    }

    public boolean addPersonId(int personId) {
        try {
            if (personIdExists(personId)) {
                Log.e("PersonCollectorDBDao", "addPerson error: person name exists");
                return false;
            }
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("PersonId", personId);
            db.insertOrThrow(MyDBHelper.TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "addPersonId ERROR: ", e);
            return false;
        }
    }

    public boolean deletePersonId(int personId) {
        try {
            if (!personIdExists(personId)) {
                Log.e("PersonCollectorDBDao", "deletePersonId error: personId not exists");
                return false;
            }
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db.delete(MyDBHelper.TABLE_NAME, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "deletePersonId", e);
            return false;
        }
    }

    public ArrayList<Integer> getPersonIds() {
        try {
            ArrayList<Integer> personIdsList = new ArrayList<>();
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            cursor = db.query(myDBHelper.TABLE_NAME,
                    myDBHelper.TABLE_COLS,
                    null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    personIdsList.add(cursor.getInt(0));
                }
            }

            return personIdsList;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "getPersonIds", e);
            return null;
        }
    }

    private class MyDBHelper extends SQLiteOpenHelper {
        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "e_dictionary_person_id_collected.db";
        public static final String TABLE_NAME = "PersonIdCollected";
        public final String[] TABLE_COLS = new String[]{ "PersonId" };


        public MyDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (PersonId INTEGER PRIMARY KEY);";
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

        @Override
        public synchronized void close() {
            super.close();
        }
    }
}
