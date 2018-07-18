package com.example.ahuja.expensemanager;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahuja.expensemanager.Adapter_and_dailogs.Dailog_statusCanged;
import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class one_transaction extends AppCompatActivity{
    private TableLayout tl;

    private String Date,Des;
    private int Type;
    private Float ant;
    SimpleDateFormat sdf;
    private String new_date;
    DbOpenHelper dbOpenHelper;
    TextView date,amnt,type,desc,statusSet;
    TextView status;
    Button save,cancel;
    Cursor cursor;
    Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_transaction);
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        dbOpenHelper = new DbOpenHelper(this);
        tl = findViewById(R.id.tl_onetrans);
        date = findViewById(R.id.tv_tableDateSet);
        amnt = findViewById(R.id.tv_tableAmntSet);
        desc = findViewById(R.id.tv_tableDescSet);
        type = findViewById(R.id.tv_tableTypeSet);
        id = getIntent().getLongExtra("Id", -1);
        cursor = dbOpenHelper.Fetch_Database(id);
        if (id == -1)
            Toast.makeText(this, "wrong id fetched", Toast.LENGTH_SHORT).show();
        int l = cursor.getCount();
        Toast.makeText(this, "Curser count :" + l, Toast.LENGTH_SHORT).show();
        cursor.moveToFirst();
        Date = cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Year))
                + "/" + cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Month))
                + "/" + cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Day));
        ant = cursor.getFloat(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
        Des = cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Desc));
        Type = cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type));

        date.setText(Date);
        desc.setText(Des);
        amnt.setText(String.valueOf(ant));
        String t, s;
        switch (Type) {
            case 1:
                type.setText("Bill");
                break;
            case 2:
                type.setText("Food");
                break;
            case 3:
                type.setText("Entertainment");
                break;
            case 4:
                type.setText("Income");
                break;
            case 5:
                type.setText("Personnel");
                break;
            case 6:
                type.setText("Saving");
                break;
            case 7:
                type.setText("Travel");
                break;
            case 8:
                type.setText("recoverable");
                break;

            default: //do nothing

        }

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.onetrans_act_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_edit){
            send_date();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void send_date(){
        Intent intent=new Intent(this,Insert_Transaction.class);
        intent.putExtra("reach",1);
        intent.putExtra("type",Type);
        intent.putExtra("date",date.getText().toString());
        intent.putExtra("desc",desc.getText().toString());
        intent.putExtra("amount",ant);
        intent.putExtra("Id",id);
        startActivity(intent);

    }
}
