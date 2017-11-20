package com.example.gypc.e_dictionary;

import android.app.Application;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by XUJIJUN on 2017/11/20.
 */

public class AppContext extends Application {

    private ReadWriteLock readWriteLock;
    private PersonDBDao personDBDao;
    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        readWriteLock = new ReentrantReadWriteLock(false);
        personDBDao = PersonDBDao.getInstance(this, readWriteLock);
    }

    public static AppContext getInstance() {
        return instance;
    }

    public PersonDBDao getPersonDBDao() {
        return personDBDao;
    }
}
