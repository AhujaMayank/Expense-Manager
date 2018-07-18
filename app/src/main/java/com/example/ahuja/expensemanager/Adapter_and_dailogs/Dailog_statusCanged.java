package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.example.ahuja.expensemanager.one_transaction;

/**
 * Created by Ahuja on 4/8/2018.
 */

public class Dailog_statusCanged extends DialogFragment {
     RadioGroup rg;
     Cursor cursor;
     long id;
     int s;
     public Dailog_statusCanged() {
    }

    @SuppressLint("ValidFragment")
    public Dailog_statusCanged(RadioGroup rg,Cursor cursor, long id, int s) {
        this.rg=rg;
        this.cursor = cursor;
        this.id = id;
        this.s = s;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


          final AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
            alert.setMessage("Change Status?");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Update_StatusIndatabase();
                    return;
            }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                return;

                }
        });
        return alert.create();

    }
    public void Update_StatusIndatabase(){
        ContentValues cv=new ContentValues();
        cv.put(Contract.DbContract.Col_Desc,cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Desc)));
        cv.put(Contract.DbContract.Col_Type,cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type)));
        cv.put(Contract.DbContract.Col_Status,s);
        cv.put(Contract.DbContract.Col_Day,cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Day)));
        cv.put(Contract.DbContract.Col_Month,cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Month)));
        cv.put(Contract.DbContract.Col_Year,cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Year)));
        cv.put(Contract.DbContract.Col_Amnt,cursor.getFloat(cursor.getColumnIndex(Contract.DbContract.Col_Amnt)));
        DbOpenHelper dbOpenHelper=new DbOpenHelper(getContext());
        dbOpenHelper.Update_Database(id,cv);

    }
}
