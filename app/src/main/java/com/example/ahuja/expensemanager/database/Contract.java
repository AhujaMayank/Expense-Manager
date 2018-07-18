package com.example.ahuja.expensemanager.database;

import android.provider.BaseColumns;

/**
 * Created by Ahuja on 4/5/2018.
 */

public class Contract  {

      public Contract(){}

      //main contract inherited _id column
    public static final class DbContract implements BaseColumns {

          public static final String TableName="transactions";
          public static final String Col_Desc="description";
          public static final String Col_Type="type";
          public static final String Col_Status="status";
          public static final String Col_Amnt="amount";
          public static final String Col_Day="day";
          public static final String Col_Month="month";
          public static final String Col_Year="year";
          public static final String Col_Hour="hour";
          public static final String Col_Minute="minute";
          public static final String Col_TimeStamp="timestamp";
      }


}
