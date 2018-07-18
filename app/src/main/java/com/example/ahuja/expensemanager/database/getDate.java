package com.example.ahuja.expensemanager.database;

import android.icu.util.Calendar;
import android.os.Build;

import java.text.SimpleDateFormat;

/**
 * Created by Ahuja on 4/7/2018.
 */

public class getDate {
    private final String day;
    private final String month;
    private final String year;
    private final String Date;
    private String full=null;
    public getDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat s=new SimpleDateFormat("MMMM");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Date=sdf.format(Calendar.getInstance().getTime());
            full=s.format(Calendar.getInstance().getTime());
        }
        else
            Date=null;
        String[] args=Date.split("/");
        day=args[2];month=args[1];year=args[0];
    }

    public String get_full_month() {
      return full;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String get_full_month(String m){
        String f=null;
        switch(m){

            case "01" : f="January";
                        break;

            case "02" : f="February";
                         break;
            case "03" : f="March";
                        break;

            case "04" : f="April";
                        break;
            case "05" : f="May";
                        break;
            case "06" : f="June";
                        break;
            case "07" : f="July";
                        break;
            case "08" : f="August";
                        break;
            case "09" : f="September";
                        break;
            case "10" : f="October";
                        break;
            case "11" : f="November";
                        break;
            case "12" : f="December";
                        break;
        }
        return f;
    }
}
