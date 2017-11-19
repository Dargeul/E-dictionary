package com.example.gypc.e_dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class PersonDBDao {

    private MyDBHelper myDBHelper;
    private Cursor cursor;

    private final int NO_PERSON_QUERY_RESULT_CODE = -1;

    public static final int CANNOT_ADD_PERSON_CODE = -1;


    public PersonDBDao(Context context) {
        try {
            myDBHelper = new MyDBHelper(context);
        } catch (Exception e) {
            Log.e("PersonDBDao", "constructor", e);
        }
    }

    private boolean personIdExists(int personId) {
        SQLiteDatabase db = null;
        try {
            db = myDBHelper.getReadableDatabase();
            cursor = db.query(MyDBHelper.TABLE_NAME,
                    new String[]{"PersonId"},
                    "PersonId = ?",
                    new String[]{ String.valueOf(personId) },
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("PersonDBDao", "personIdExists", e);
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private int queryPersonIdByName(String personName) {
        try {
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            cursor = db.query(myDBHelper.TABLE_NAME,
                               new String[]{ "PersonId" },
                               "Name = ?",
                               new String[]{ personName },
                               null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    return cursor.getInt(0);
                }
            }
            return NO_PERSON_QUERY_RESULT_CODE;

        } catch (Exception e) {
            Log.e("PersonDBDao", "queryPersonIdByName", e);
            return NO_PERSON_QUERY_RESULT_CODE;
        }
    }

    public boolean personNameExists(String personName) {
        return queryPersonIdByName(personName) != NO_PERSON_QUERY_RESULT_CODE;
    }

    public int addPerson(Bundle dataBundle) {
        try {
            String personName = dataBundle.getString("name");
            if (personNameExists(personName)) {
                Log.e("PersonDBDao", "addPerson error: person name exists");
                return CANNOT_ADD_PERSON_CODE;
            }
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + myDBHelper.TABLE_NAME +
                    " (AvatarIndex, Name, Country, NickName, StartYear, EndYear, Birthplace) " +
                    "  values (" +
                    "  " + dataBundle.getInt("avatarIndex")  + ", " +
                    " '" + personName                          + "', " +
                    " '" + dataBundle.getString("country")    + "', " +
                    " '" + dataBundle.getString("nickName")   + "', " +
                    "  " + dataBundle.getInt("startYear")     + ", " +
                    "  " + dataBundle.getInt("endYear")       + ", " +
                    " '" + dataBundle.getString("birthplace") + "')");

            db.setTransactionSuccessful();

            return queryPersonIdByName(personName);
        } catch (Exception e) {
            Log.e("PersonDBDao", "addPerson ERROR: ", e);
            return CANNOT_ADD_PERSON_CODE;
        }
    }

    public boolean deletePerson(int personId) {
        try {
            if (!personIdExists(personId)) {
                Log.e("PersonDBDao", "deletePerson error: personId not exists");
                return false;
            }
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db.delete(MyDBHelper.TABLE_NAME, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        } catch (Exception e) {
            Log.e("PersonDBDao", "deletePerson", e);
            return false;
        }
    }

    public boolean updatePerson(int personId, Bundle dataBundle) {
        try {
            if (!personIdExists(personId)) {
                Log.e("PersonDBDao", "updatePerson error: personId not exists");
                return false;
            }
            SQLiteDatabase db = myDBHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("AvatarIndex", dataBundle.getInt("avatarIndex"));
            contentValues.put("Name", dataBundle.getString("name"));
            contentValues.put("Country", dataBundle.getString("country"));
            contentValues.put("NickName", dataBundle.getString("nickName"));
            contentValues.put("StartYear", dataBundle.getInt("startYear"));
            contentValues.put("EndYear", dataBundle.getInt("endYear"));
            contentValues.put("Birthplace", dataBundle.getString("birthplace"));
            db.update(MyDBHelper.TABLE_NAME, contentValues, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        }catch (Exception e) {
            Log.e("PersonDBDao", "updatePerson", e);
            return false;
        }
    }

    public ArrayList<Person> getPersons() {
        try {
            ArrayList<Person> resultList = new ArrayList<>();
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            cursor = db.query(myDBHelper.TABLE_NAME,
                    myDBHelper.TABLE_COLS,
                    null, null, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.personId = cursor.getInt(0);
                    person.avatarIndex = cursor.getInt(1);
                    person.name = cursor.getString(2);
                    person.country = cursor.getString(3);
                    person.nickName = cursor.getString(4);
                    person.startYear = cursor.getInt(5);
                    person.endYear = cursor.getInt(6);
                    person.birthplace = cursor.getString(7);
                    resultList.add(person);
                }
            }

            return resultList;
        } catch (Exception e) {
            Log.e("PersonDBDao", "getPersons ERROR: ", e);
            return null;
        }
    }

    private class MyDBHelper extends SQLiteOpenHelper {
        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "e_dictionary_person.db";
        public static final String TABLE_NAME = "Persons";
        public final String[] TABLE_COLS = new String[]{ "PersonId", "AvatarIndex", "Name", "Country", "NickName", "StartYear", "EndYear", "Birthplace" };


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

        @Override
        public synchronized void close() {
            super.close();
        }
    }
}

