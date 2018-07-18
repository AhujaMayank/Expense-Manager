package com.example.ahuja.expensemanager.database;

/**
 * Created by Ahuja on 4/7/2018.
 */

public class DbObject {

     private String Day,month,year;
     private String Desc;
     private int Type;
     private float Amnt;

    public DbObject(String day, String month, String year, String desc, int type, float amnt) {
        Day = day;
        this.month = month;
        this.year = year;
        Desc = desc;
        Type = type;
        Amnt = amnt;


    }


    public String getDay() {
        return Day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDesc() {
        return Desc;
    }

    public int getType() {
        return Type;
    }

    public float getAmnt() {
        return Amnt;
    }
}
