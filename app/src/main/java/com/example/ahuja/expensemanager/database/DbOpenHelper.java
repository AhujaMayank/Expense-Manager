package com.example.ahuja.expensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Ahuja on 4/5/2018.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String Db_Name="records.db";
    private static final int Db_Version=1;
    public DbOpenHelper(Context context) {
        super(context, Db_Name, null, Db_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       sqLiteDatabase.execSQL("CREATE TABLE " + Contract.DbContract.TableName + " ("
               + Contract.DbContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
               + Contract.DbContract.Col_Desc +  " TEXT ,"
               + Contract.DbContract.Col_Type +  " INTEGER NOT NULL, "
              // + Contract.DbContract.Col_Status + " INTEGER NOT NULL, "
               + Contract.DbContract.Col_Day +   " TEXT NOT NULL, "
               + Contract.DbContract.Col_Month + " TEXT NOT NULL, "
               + Contract.DbContract.Col_Year +  " TEXT NOT NULL, "
               + Contract.DbContract.Col_Amnt +  " INTEGER NOT NULL" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String del=" DROP TABLE IF EXISTS " + Contract.DbContract.TableName;
        sqLiteDatabase.execSQL(del);
        onCreate(sqLiteDatabase);

    }
    //insert record
    public long insert_data(DbObject dbObject){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Contract.DbContract.Col_Desc,dbObject.getDesc());
        cv.put(Contract.DbContract.Col_Type,dbObject.getType());
       // cv.put(Contract.DbContract.Col_Status,dbObject.getStatus());
        cv.put(Contract.DbContract.Col_Day,dbObject.getDay());
        cv.put(Contract.DbContract.Col_Month,dbObject.getMonth());
        cv.put(Contract.DbContract.Col_Year,dbObject.getYear());
        cv.put(Contract.DbContract.Col_Amnt,dbObject.getAmnt());
        long no=sqLiteDatabase.insert(Contract.DbContract.TableName,null,cv);
        sqLiteDatabase.close();
        return no;
    }
    //delete a record
    public void delete_data(String id){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL("Delete From " + Contract.DbContract.TableName + " Where _id = " + id);
        sqLiteDatabase.close();
    }
    //fetch the data
    public Cursor Fetch_Database(){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName
                ,null
                ,null
                ,null
                ,null
                ,null
                , null
                ,null);
        //mistake being made was loss of connection due to .close() function being used by me.
    }
    public Cursor Fetch_Database(String Day,String Month,String Year){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Day + "=? AND "+
                        Contract.DbContract.Col_Month +"=? AND "+
                        Contract.DbContract.Col_Year +"=?",
                new String[]{Day, Month, Year},
                null,
                null,
                null,
                null);
    }
    public Cursor Fetch_Database(String Day,String Month,String Year,int orderby){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Day + "=? AND "+
                        Contract.DbContract.Col_Month +"=? AND "+
                        Contract.DbContract.Col_Year +"=?",
                new String[]{Day, Month, Year},
                null,
                null,
                Contract.DbContract.Col_Amnt+" DESC",
                null);
    }
    public Cursor Fetch_Database(String Month,String Year){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Month +"=? AND "+
                        Contract.DbContract.Col_Year +"=?",
                new String[]{Month, Year},
                null,
                null,
                null,
                null);
    }
    public Cursor Fetch_Database(String Year){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Year +"=?",
                new String[]{Year},
                null,
                null,
                null,
                null);
    }
    public Cursor Fetch_Database(String Month,String Year,int type){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Month +"=? AND "+
                        Contract.DbContract.Col_Year +"=? AND "+
                        Contract.DbContract.Col_Type +"=?",
                new String[]{Month, Year,String.valueOf(type)},
                null,
                null,
                null,
                null);
    }

    public Cursor Fetch_Database(long id){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String Id=String.valueOf(id);
        return sqLiteDatabase.query(Contract.DbContract.TableName
                ,null
                ,Contract.DbContract._ID+"=?"
                , new String[]{Id}
                ,null
                ,null
                ,null
                ,null);
        //mistake being made was loss of connection due to .close() function being used by me.
    }
    public Cursor Fetch_Database(String des,int todiff){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String s="%"+des+"%";
        return sqLiteDatabase.query(Contract.DbContract.TableName,
                null,
                Contract.DbContract.Col_Desc +" LIKE ?",
                new String[]{s},
                null,
                null,
                null,
                null);
    }
    public void Update_Database(long id,ContentValues contentValues){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String Id=String.valueOf(id);
        sqLiteDatabase.update(Contract.DbContract.TableName,
                contentValues,
                Contract.DbContract._ID+"=?", new String[]{Id});

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchThisWeekData(){
//        Calendar calendar=Calendar.getInstance();
//        int n=calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.add(Calendar.DAY_OF_YEAR,(Calendar.SUNDAY-n));
//        java.util.Date d=calendar.getTime();
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
//        String[] dstore=simpleDateFormat.format(d).split("/");
//        for(int i=0)

    }


}
