package com.example.gypc.e_dictionary;

import android.util.Log;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class Person {
    public int personId;
    public int avatarIndex;
    public String name;
    public String country;
    public String nickName;
    public int startYear;
    public int endYear;
    public String birthplace;

    public Person(int personId, int avatarIndex, String name, String country, String nickName, int startYear, int endYear, String birthplace) {
        this.personId = personId;
        this.avatarIndex = avatarIndex;
        this.name = name;
        this.country = country;
        this.nickName = nickName;
        this.startYear = startYear;
        this.endYear = endYear;
        this.birthplace = birthplace;
    }

    public String getName() {
        return name;
    }
}
