package com.example.gypc.e_dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XUJIJUN on 2017/11/19.
 */

public class PersonCollectorDBDao {

    private AtomicInteger mDBOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDataBase;
    private Cursor cursor;

    private static PersonCollectorDBHelper myDBHelper;
    private static PersonCollectorDBDao instance;

    private PersonCollectorDBDao() {}

    public static PersonCollectorDBDao getInstance(Context context) {
        myDBHelper = PersonCollectorDBHelper.getInstance(context);
        if (instance == null) {
            synchronized (PersonDBDao.class) {
                if (instance == null) {
                    instance = new PersonCollectorDBDao();
                }
            }
        }
        return instance;
    }

    private SQLiteDatabase getDatabase() {
        if (mDBOpenCounter.incrementAndGet() == 1 || mDataBase == null) {
            mDataBase = myDBHelper.getWritableDatabase();
        }
        return mDataBase;
    }

    private void closeDatabase() {
        if (mDBOpenCounter.decrementAndGet() == 0) {
            mDataBase.close();
        }
    }

    public void closeDBConnection() {
        myDBHelper.close();
    }


    private boolean personIdExists(int personId) {
        try {
            mDataBase = getDatabase();
            cursor = mDataBase.query(PersonCollectorDBHelper.TABLE_NAME,
                    new String[]{"PersonId"},
                    "PersonId = ?",
                    new String[]{ String.valueOf(personId) },
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "personIdExists", e);
            return false;
        } finally {
            closeDatabase();
        }
    }

    public boolean addPersonId(int personId) {
        try {
            mDataBase = getDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("PersonId", personId);
            mDataBase.insertOrThrow(PersonCollectorDBHelper.TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "addPersonId ERROR: ", e);
            return false;
        } finally {
            closeDatabase();
        }
    }

    public boolean deletePersonId(int personId) {
        try {
            mDataBase = getDatabase();
            mDataBase.delete(PersonCollectorDBHelper.TABLE_NAME, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        } catch (Exception e) {
            Log.e("PersonCollectorDBDao", "deletePersonId", e);
            return false;
        } finally {
            closeDatabase();
        }
    }

    public ArrayList<Integer> getPersonIds() {
        try {
            ArrayList<Integer> personIdsList = new ArrayList<>();
            mDataBase = getDatabase();
            cursor = mDataBase.query(myDBHelper.TABLE_NAME,
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
        } finally {
            closeDatabase();
        }
    }
}
