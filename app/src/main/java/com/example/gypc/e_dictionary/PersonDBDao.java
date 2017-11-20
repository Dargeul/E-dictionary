package com.example.gypc.e_dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class PersonDBDao {

    private AtomicInteger mDBOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDataBase;

//    private Context mContext;
//    private ReadWriteLock myLock;
    private Cursor cursor;
//    public static final int CANNOT_ADD_PERSON_CODE = -1;
    private final int NO_PERSON_QUERY_RESULT_CODE = -1;

    private static PersonDBHelper myDBHelper;
    private static PersonDBDao instance;

    private PersonDBDao() {}

    public static PersonDBDao getInstance(Context context) {
        myDBHelper = PersonDBHelper.getInstance(context);
        if (instance == null) {
            synchronized (PersonDBDao.class) {
                if (instance == null) {
                    instance = new PersonDBDao();
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
//        myLock.readLock().lock();
//        SQLiteDatabase db = null;
        try {
            mDataBase = getDatabase();
            cursor = mDataBase.query(PersonDBHelper.TABLE_NAME,
                    new String[]{"PersonId"},
                    "PersonId = ?",
                    new String[]{ String.valueOf(personId) },
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("PersonDBDao", "personIdExists", e);
            return false;
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.readLock().unlock();
            closeDatabase();
        }
    }

    public int queryPersonIdByName(String personName) {
//        myLock.readLock().lock();
//        SQLiteDatabase db = null;
        try {
            mDataBase = getDatabase();
            cursor = mDataBase.query(myDBHelper.TABLE_NAME,
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
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.readLock().unlock();
            closeDatabase();
        }
    }

    public boolean personNameExists(String personName) {
        return queryPersonIdByName(personName) != NO_PERSON_QUERY_RESULT_CODE;
    }

    public boolean addPerson(Bundle dataBundle) {
//        myLock.writeLock().lock();
//        SQLiteDatabase db = null;
        try {
            mDataBase = getDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("AvatarIndex", dataBundle.getInt("avatarIndex"));
            contentValues.put("Name", dataBundle.getString("name"));
            contentValues.put("Country", dataBundle.getString("country"));
            contentValues.put("NickName", dataBundle.getString("nickName"));
            contentValues.put("StartYear", dataBundle.getInt("startYear"));
            contentValues.put("EndYear", dataBundle.getInt("endYear"));
            contentValues.put("Birthplace", dataBundle.getString("birthplace"));
            mDataBase.insertOrThrow(PersonDBHelper.TABLE_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e("PersonDBDao", "addPerson ERROR: ", e);
            return false;
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.writeLock().unlock();
            closeDatabase();
        }
    }

    public boolean deletePerson(int personId) {
//        myLock.writeLock().lock();
//        SQLiteDatabase db = null;
        try {
            mDataBase = getDatabase();
            mDataBase.delete(PersonDBHelper.TABLE_NAME, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        } catch (Exception e) {
            Log.e("PersonDBDao", "deletePerson", e);
            return false;
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.writeLock().unlock();
            closeDatabase();
        }
    }

    public boolean updatePerson(int personId, Bundle dataBundle) {
//        myLock.writeLock().lock();
//        SQLiteDatabase db = null;
        try {
            mDataBase = getDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("AvatarIndex", dataBundle.getInt("avatarIndex"));
            contentValues.put("Name", dataBundle.getString("name"));
            contentValues.put("Country", dataBundle.getString("country"));
            contentValues.put("NickName", dataBundle.getString("nickName"));
            contentValues.put("StartYear", dataBundle.getInt("startYear"));
            contentValues.put("EndYear", dataBundle.getInt("endYear"));
            contentValues.put("Birthplace", dataBundle.getString("birthplace"));
            mDataBase.update(PersonDBHelper.TABLE_NAME, contentValues, "PersonId = ?", new String[] { String.valueOf(personId) });

            return true;
        }catch (Exception e) {
            Log.e("PersonDBDao", "updatePerson", e);
            return false;
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.writeLock().unlock();
            closeDatabase();
        }
    }

    public ArrayList<Person> getPersons() {
//        myLock.readLock().lock();
//        SQLiteDatabase db = null;
        try {
            ArrayList<Person> resultList = new ArrayList<>();
            mDataBase = getDatabase();
            cursor = mDataBase.query(myDBHelper.TABLE_NAME,
                    myDBHelper.TABLE_COLS,
                    null, null, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getString(7)
                    );

                    resultList.add(person);
                }
            }

            return resultList;
        } catch (Exception e) {
            Log.e("PersonDBDao", "getPersons ERROR: ", e);
            return null;
        } finally {
//            if (db != null && db.isOpen())
//                db.close();
//            myLock.readLock().unlock();
            closeDatabase();
        }
    }
}

