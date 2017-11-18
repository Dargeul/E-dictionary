package com.example.gypc.e_dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class DBDao {
    private static DBDao instance;

    private MyDBHelper myDBHelper;
    private Cursor cursor;


    private DBDao(Context context) {
        try {
            myDBHelper = new MyDBHelper(context);
        } catch (Exception e) {
            Log.e("DBDao", "cstr", e);
        }
    }

    public static DBDao getInstance(Context context) {
        if (instance == null)
            instance = new DBDao(context);
        return instance;
    }

    public boolean addPerson(Bundle dataBundle) {
        try {
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + myDBHelper.TABLE_NAME +
                    " (PersonId, AvatarIndex, Name, Country, NickName, StartYear, EndYear, Birthplace) " +
                    "  values (" +
                    "  " + dataBundle.getInt("personId")      + ", " +
                    "  " + dataBundle.getInt("avatarIndex")  + ", " +
                    " '" + dataBundle.getString("name")        + "', " +
                    " '" + dataBundle.getString("country")    + "', " +
                    " '" + dataBundle.getString("nickName")   + "', " +
                    "  " + dataBundle.getInt("startYear")     + ", " +
                    "  " + dataBundle.getInt("endYear")       + ", " +
                    " '" + dataBundle.getString("birthplace") + "', " +
                    ")");


            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e("DBDao", "initData ERROR: ", e);
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
            Log.e("DBDao", "queryPersons ERROR: ", e);
            return null;
        }
    }
}

