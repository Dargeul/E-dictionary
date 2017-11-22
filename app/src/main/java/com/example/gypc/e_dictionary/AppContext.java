package com.example.gypc.e_dictionary;

import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by XUJIJUN on 2017/11/20.
 */

public class AppContext extends Application {

    private PersonDBDao personDBDao;
    private PersonCollectorDBDao personCollectorDBDao;
    private static AppContext instance;
    private ArrayList<Person> globalPersonsList;
    private ArrayList<Integer> globalPersonIdsCollectedList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        personDBDao = PersonDBDao.getInstance(this);
        personCollectorDBDao = PersonCollectorDBDao.getInstance(this);
    }

    public static AppContext getInstance() {
        return instance;
    }

    // 获取全局初始人物数组
    public List<Person> getGlobalPersonsList() {
        globalPersonsList = personDBDao.getPersons();
        return globalPersonsList;
    }

    // 获取收藏人物ID数组
    public List<Integer> getGlobalPersonIdsCollectedList() {
        globalPersonIdsCollectedList = personCollectorDBDao.getPersonIds();
        return globalPersonIdsCollectedList;
    }

    public PersonDBDao getPersonDBDao() {
        return personDBDao;
    }

    public PersonCollectorDBDao getPersonCollectorDBDao() {
        return personCollectorDBDao;
    }
}
