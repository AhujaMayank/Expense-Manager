package com.example.ahuja.expensemanager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbObject;
import com.example.ahuja.expensemanager.database.DbOpenHelper;

import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class Insert_Transaction extends AppCompatActivity {
    String Des,Amt,Date;
    TextInputEditText desc,amnt;
    TextView date;
    TextView category;
    DbOpenHelper dbOpenHelper;
    CheckableImageButton bill,food,entertainment,family,saving,personel,travel,recoverable;
    SimpleDateFormat store_sdf,show_sdf;
    Button save;
    int Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Type=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__transaction);
        dbOpenHelper=new DbOpenHelper(this);
        bill=findViewById(R.id.insert_type_bill);
        food=findViewById(R.id.insert_type_food);
        entertainment=findViewById(R.id.insert_type_entertainment);
        family=findViewById(R.id.insert_type_family);
        saving=findViewById(R.id.insert_type_saving);
        personel=findViewById(R.id.insert_type_personel);
        travel=findViewById(R.id.insert_type_travel);
        recoverable=findViewById(R.id.insert_type_recoverable);
        date=findViewById(R.id.insert_date);
        desc=findViewById(R.id.insert_desc);
        amnt=findViewById(R.id.insert_amnt);
        category=findViewById(R.id.insert_type_view);
        Button badd=findViewById(R.id.buttonadd);
        final Date d =Calendar.getInstance().getTime();
        store_sdf=new SimpleDateFormat("yyyy/MM/dd");
        show_sdf=new SimpleDateFormat("MMMM,dd");
        date.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar newCalendar = Calendar.getInstance();
                        int month,year,dte;
                        if(getIntent().getIntExtra("reach",-1)==1){
                             String d[]=getIntent().getStringExtra("date").split("/");
                             month=Integer.parseInt(d[1])-1;
                             year=Integer.parseInt(d[0]);
                             dte=Integer.parseInt(d[2]);
                        }
                        else {
                            month = newCalendar.get(Calendar.MONTH);  // Current month
                            year = newCalendar.get(Calendar.YEAR);   // Current year
                            dte = newCalendar.get(Calendar.DATE);
                        }
                        showDatePicker(Insert_Transaction.this, year, month, dte);

                    }
                }
        );
        Date=store_sdf.format(d);
        date.setText(show_sdf.format(d));
        save=findViewById(R.id.b_add);

        if(getIntent().getIntExtra("reach",-1)==1){
            set_data();
            save.setText("Update");
            badd.setText("Cancel");
            badd.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(view.getContext(),one_transaction.class);
                            intent.putExtra("Id",getIntent().getLongExtra("Id",-1));
                            startActivity(intent);
                        }
                    }
            );
            save.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] broken=Date.split("/");
                            ContentValues cv=new ContentValues();
                            cv.put(Contract.DbContract.Col_Desc,desc.getText().toString());
                            cv.put(Contract.DbContract.Col_Type,Type);
                            cv.put(Contract.DbContract.Col_Day,broken[2]);
                            cv.put(Contract.DbContract.Col_Month,broken[1]);
                            cv.put(Contract.DbContract.Col_Year,broken[0]);
                            cv.put(Contract.DbContract.Col_Amnt,Float.parseFloat(amnt.getText().toString()));
                            dbOpenHelper.Update_Database(getIntent().getLongExtra("Id",-1),cv);
                            Intent intent=new Intent(view.getContext(),one_transaction.class);
                            intent.putExtra("Id",getIntent().getLongExtra("Id",-1));
                            startActivity(intent);

                        }
                    }
            );


        }
        else{
            badd.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(amnt.getText().toString().equals(null))
                                Toast.makeText(view.getContext(),"Can not save the Record",Toast.LENGTH_SHORT).show();
                            else
                                saveRecord(view);
                        }
                    }
            );

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(amnt.getText().toString().equals(null))
                        Toast.makeText(view.getContext(),"Can not save the Record",Toast.LENGTH_SHORT).show();
                    else{
                        saveRecord(view);
                        Intent i = null;
                        if (getIntent().getIntExtra("calling_intent", -1) == 1)
                            i = new Intent(view.getContext(), MainActivity.class);
                        if (getIntent().getIntExtra("calling_intent", -1) == 2)
                            i = new Intent(view.getContext(), Transaction_Detail.class);
                        if (i != null)
                            startActivity(i);
                    }
                }
            });

        }


    }

    public void saveRecord(View view){

         String[] date_broken=Date.split("/");
         Des= desc.getText().toString();
         Amt= amnt.getText().toString();
         DbObject dbObject=new DbObject(date_broken[2],date_broken[1],date_broken[0],Des,Type,Float.parseFloat(Amt));
         dbOpenHelper.insert_data(dbObject);
//         Toast.makeText(this,"Row inserted : "+ no,Toast.LENGTH_LONG).show();

         }
    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDay) {

        // Creating datePicker dialog object
        // It requires context and listener that is used when a date is selected by the user.

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    //This method is called when the user has finished selecting a date.
                    // Arguments passed are selected year, month and day
                    @Override
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {

                        // To get epoch, You can store this date(in epoch) in database
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        Date d =calendar.getTime();
                        // Setting date selected in the edit text
                        Date=store_sdf.format(d);
                        date.setText(show_sdf.format(d));
                    }
                }, initialYear, initialMonth, initialDay);

        //Call show() to simply show the dialog
        datePickerDialog.show();

    }
    //on Click methods for ImageButton
    @SuppressLint("RestrictedApi")
    public void food(View view){
        Type=2;
        food.setChecked(true);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Food");
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void entertainment(View view){
        food.setChecked(false);
        entertainment.setChecked(true);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Entertainment");
        Type=3;
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void bill(View view){
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(true);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Bill");
        Type=1;
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void personel(View view){
        Type=5;
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(true);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Personnel");
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void family(View view){
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(true);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Family");
        Type=4;
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void saving(View view){
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(true);
        recoverable.setChecked(false);
        travel.setChecked(false);
        category.setText("Saving");
        Type=6;
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void recoverable(View view){
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(true);
        travel.setChecked(false);
        category.setText("Recoverable");
        Type=8;
        correctState();
    }
    @SuppressLint("RestrictedApi")
    public void travel(View view){
        Type=7;
        food.setChecked(false);
        entertainment.setChecked(false);
        bill.setChecked(false);
        personel.setChecked(false);
        family.setChecked(false);
        saving.setChecked(false);
        recoverable.setChecked(false);
        travel.setChecked(true);
        category.setText("Travel");
        correctState();

    }
    public void set_data(){
        Bundle bundle=getIntent().getExtras();
        desc.setText(bundle.getString("desc"));
        amnt.setText(String.valueOf(bundle.getFloat("amount")));
        String sdate=bundle.getString("date");
        Date s=null;
        try {
            s=new SimpleDateFormat("yyyy/MM/dd").parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(show_sdf.format(s));
        switch (bundle.getInt("type")){

            case 1 : bill(new View(this));
                break;
            case 2 : food(new View(this));
                break;
            case 3 : entertainment(new View(this));
                break;
            case 4 : family(new View(this));
                break;
            case 5 : personel(new View(this));
                break;
            case 6 : saving(new View(this));
                break;
            case 7 : travel(new View(this));
                break;
            case 8 : recoverable(new View(this));
                break;

            default: //do nothing
        }


    }
    @SuppressLint("RestrictedApi")
    public void correctState(){
        if(bill.isChecked()){
            bill.setImageResource(R.drawable.billdisp);
            bill.setBackgroundColor(Color.parseColor("#42f4f4"));
        }
        else{
            bill.setImageResource(R.drawable.receipt);
            bill.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(food.isChecked()){
            food.setImageResource(R.drawable.fooddisp);
            food.setBackgroundColor(Color.parseColor("#6d1bc4"));
        }
        else{
            food.setImageResource(R.drawable.restaurant);
            food.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(entertainment.isChecked()){
            entertainment.setImageResource(R.drawable.enterdisp);
            entertainment.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else{
            entertainment.setImageResource(R.drawable.clapperboard);
            entertainment.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(family.isChecked()){
            family.setImageResource(R.drawable.incdisp);
            family.setBackgroundColor(Color.parseColor("#E5d22B"));
        }
        else{
            family.setImageResource(R.drawable.incomes);
            family.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(personel.isChecked()){
            personel.setImageResource(R.drawable.persdisp);
            personel.setBackgroundColor(Color.parseColor("#176331"));
        }
        else{
            personel.setImageResource(R.drawable.avatar);
            personel.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(saving.isChecked()){
            saving.setImageResource(R.drawable.savedisp);
            saving.setBackgroundColor(Color.parseColor("#40d672"));
        }
        else{
            saving.setImageResource(R.drawable.saving);
            saving.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(travel.isChecked()){
            travel.setImageResource(R.drawable.traveldisp);
            travel.setBackgroundColor(Color.parseColor("#0000ff"));
        }
        else{
            travel.setImageResource(R.drawable.plane);
            travel.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(recoverable.isChecked()){
            recoverable.setImageResource(R.drawable.recoverdisp);
            recoverable.setBackgroundColor(Color.parseColor("#772a25"));
        }
        else{
            recoverable.setImageResource(R.drawable.icon);
            recoverable.setBackgroundColor(Color.parseColor("#ffffff"));
        }




    }
}
