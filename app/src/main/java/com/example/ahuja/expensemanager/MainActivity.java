package com.example.ahuja.expensemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import java.util.*;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.ArrayRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ahuja.expensemanager.Adapter_and_dailogs.Adap_daily_View;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.Adapter_rv_transaction;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.AxisFormatter_month;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.detail_fragment;
import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.example.ahuja.expensemanager.database.getDate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class MainActivity extends AppCompatActivity {

    DbOpenHelper dbOpenHelper;
    TextView ex,in,dt;
    TextView budget_per;
    BarChart pieChart,barmonth;
    List<BarEntry> entries=new ArrayList();
    List<BarEntry> entries2=new ArrayList<BarEntry>();
    BarData barData;
    BarDataSet barDataSet[]=new BarDataSet[2];
    TextView date,exp,inc,out;
    CardView daily_cardview,card;
    TextView tv_mspent,tv_msafe;
    ProgressBar pb_budget;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_act_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_selected=item.getItemId();
        if(id_selected==R.id.act_main_details){

            Intent i =new Intent(MainActivity.this,Transaction_Detail.class);
            i.putExtra("reach",1);
            startActivity(i);
            return true;
        }
        else if(id_selected==R.id.act_main_add){
            Intent i = new Intent(MainActivity.this,Insert_Transaction.class);
            i.putExtra("calling_intent",1);
            startActivity(i);
            return true;
        }
        else if(id_selected==R.id.act_main_daywise){
            Intent i = new Intent(MainActivity.this,Transaction_Detail.class);
            i.putExtra("visible",true);
            i.putExtra("reach",6);
            startActivity(i);
            return true;
        }
        else if(id_selected==R.id.act_main_weeksum) {
            Intent i=new Intent(MainActivity.this,Week_Summary_Activity.class);
            i.putExtra("reach",1);
            startActivity(i);
            return true;

        }
        else
         return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbOpenHelper=new DbOpenHelper(this);
        ex=findViewById(R.id.daily_exp);
        in=findViewById(R.id.daily_inc);
        dt=findViewById(R.id.daily_date);
        tv_msafe=findViewById(R.id.tv_main_msafe);
        tv_mspent=findViewById(R.id.tv_main_mspent);
        pb_budget=findViewById(R.id.pb_main_budget);
        card=findViewById(R.id.card);
        pieChart=findViewById(R.id.pc_main_act);
        barmonth=findViewById(R.id.main_act_bar_month);
        budget_per=findViewById(R.id.tv_budget_percentage);
        getDate gd=new getDate();

        setbudget();
        setbarchart();
        setPieChart();
        set_daily_card();
        card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDate gd=new getDate();
                        String dta=dt.getText().toString();
                        String[] dt=dta.split("/");
                        Intent i=new Intent(view.getContext(),Transaction_Detail.class);
                        i.putExtra("reach",2);
                        i.putExtra("visible",true);
                        i.putExtra("day",gd.getDay());
                        i.putExtra("month",gd.getMonth());
                        i.putExtra("year",gd.getYear());
                        startActivity(i);

                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        pieChart.notifyDataSetChanged();
        barmonth.notifyDataSetChanged();
        pieChart.invalidate();
        barmonth.invalidate();
        set_daily_card();
        setbudget();
        super.onResume();
    }


    public void setbarchart(){
        long fbal=0;
        long exp=0;
        getDate gd=new getDate();
        int day=Integer.parseInt(gd.getDay());
        int i=7;
        String s;
        while(i>=1) {
            fbal=0;

            exp=0;
            if(day<10)
                s="0"+String.valueOf(day);
            else
                s=String.valueOf(day);
            Cursor cursor=dbOpenHelper.Fetch_Database(s,gd.getMonth(),gd.getYear());
            if(cursor!=null&&cursor.getCount()>0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if(cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=4&&
                            cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=6){
                         exp+=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));

                    }
                    cursor.moveToNext();
                }
                //Log.d("Cursor incorrect", "Cursor cout : "+cursor.getCount()+"Date : "+String.valueOf(day)+gd.getMonth()+gd.getYear()+"|| Balance : "+bal);
            }

            //   Log.d("Cursor incorrect", "Cursor cout : "+cursor.getCount()+"|| Date : "+String.valueOf(day)+gd.getMonth()+gd.getYear()+"|| Balance : "+bal);
                fbal=exp;
                entries2.add(new BarEntry(i,fbal));
            day--;
            i--;
        }
        pieChart.getXAxis().setGranularity(1f);
        barDataSet[1]=new BarDataSet(entries2,"");
        barData = new BarData(barDataSet[1]);
        barData.setBarWidth(.8f);
        pieChart.setTouchEnabled(false);
        barDataSet[1].setValueTextColor(Color.parseColor("#ffffff"));
        barDataSet[1].setColor(Color.parseColor("#ffffff"));
        //pieChart.setBackgroundColor(Color.parseColor("#3A4856"));
        pieChart.setData(barData);
        Description d=null;
        pieChart.setDescription(d);
        pieChart.getXAxis().setValueFormatter(new XaxisFormatter(pieChart));
        pieChart.setScaleXEnabled(true);
        pieChart.getXAxis().setDrawAxisLine(false);
        pieChart.setDrawValueAboveBar(true);
        pieChart.getData().setValueTextSize(12f);
        pieChart.setScaleXEnabled(false);
        pieChart.setHorizontalScrollBarEnabled(true);
        pieChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        pieChart.getXAxis().setDrawGridLines(false);
        pieChart.getXAxis().setTextSize(12f);
        pieChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        pieChart.getAxisRight().setEnabled(false);
        pieChart.getAxisLeft().setDrawLabels(false);
        pieChart.getAxisLeft().setDrawGridLines(false);
        pieChart.animateY(2000);
        pieChart.getXAxis().setAvoidFirstLastClipping(false);

//        pieChart.setOnChartGestureListener(
//                new OnChartGestureListener() {
//                    @Override
//                    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//                    }
//
//                    @Override
//                    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//                    }
//
//                    @Override
//                    public void onChartLongPressed(MotionEvent me) {
//
//                    }
//
//                    @Override
//                    public void onChartDoubleTapped(MotionEvent me) {
//
//                    }
//
//                    @Override
//                    public void onChartSingleTapped(MotionEvent me) {
//                        getDate gd=new getDate();
//
//                        int index=me.getActionIndex();
//                        String t;
//                        if(Integer.parseInt(gd.getDay())<10)
//                            t="0"+String.valueOf(Integer.parseInt(gd.getDay())-index);
//                        else
//                            t=String.valueOf(Integer.parseInt(gd.getDay())-index);
//                        if(me.getY()==0)
//                            Toast.makeText(getApplicationContext(),"No Expense on "+t+"/"+gd.get_full_month(),Toast.LENGTH_SHORT).show();
//                        else{
//                            Intent i=new Intent(getApplicationContext(),Transaction_Detail.class);
//                            i.putExtra("day",t);
//                            i.putExtra("month",gd.getMonth());
//                            i.putExtra("year",gd.getYear());
//                            i.putExtra("reach","4");
//                            startActivity(i);
//                        }
//                    }
//
//                    @Override
//                    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//
//                    }
//
//                    @Override
//                    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//
//                    }
//
//                    @Override
//                    public void onChartTranslate(MotionEvent me, float dX, float dY) {
//
//                    }
//                }
//        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setPieChart(){
        getDate gd=new getDate();

        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/yyyy");
        int m=calendar.get(Calendar.MONTH);
        calendar.add(Calendar.MONTH,-1*m);
        for(int i=0;i<12;i++) {
            date=calendar.getTime();
            String[] db=simpleDateFormat.format(date).split("/");
            Cursor cursor = dbOpenHelper.Fetch_Database(db[0], db[1]);
            cursor.moveToFirst();
            long cat = 0, amnt = 0, total = 0;
            while (cursor.isAfterLast() == false && cursor.getCount() > 0) {
                amnt = cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                total += amnt;
                switch (cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))) {
                    case 1:

                    case 2:

                    case 3:

                    case 5:

                    case 7:
                        cat += amnt;
                        break;
                    default://do nothing
                }
                cursor.moveToNext();
            }
            calendar.add(Calendar.MONTH,1);
            entries.add(new BarEntry(i,cat,"Ahuja"));
        }
        barDataSet[0]=new BarDataSet(entries,"");
        barData = new BarData(barDataSet[0]);
        barDataSet[0].setColor(Color.parseColor("#ffffff"));
        barData.setBarWidth(.8f);
        barmonth.setData(barData);
        barData.setDrawValues(true);
        barData.setValueTextColor(Color.parseColor("#ffffff"));
        barmonth.getXAxis().setDrawAxisLine(false);
        barmonth.getXAxis().setDrawGridLines(false);
        barmonth.getAxisRight().setEnabled(false);
        barmonth.getAxisLeft().setEnabled(false);
        barmonth.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barmonth.getXAxis().setTextSize(12f);
        barData.setValueTextSize(12f);
        barmonth.getXAxis().setXOffset(1f);
        barmonth.getXAxis().setGranularity(1f);
        barmonth.setScaleYEnabled(false);
        barmonth.setDescription(null);

        barmonth.getXAxis().setValueFormatter(new AxisFormatter_month());
        barData.setValueTextColor(Color.parseColor("#ffffff"));
        barmonth.getXAxis().setTextColor(Color.parseColor("#ffffff"));
    }


    public void set_daily_card(){
        Cursor cursor;
        getDate gd=new getDate();
        dt.setText("Today "+gd.getDay()+","+gd.get_full_month());

        int position=0;
        int day=0;
        long inc=0,exp=0;
        String Day,Date=null;
        day=Integer.parseInt(gd.getDay())-position;
        if(day<9)
            Day="0"+String.valueOf(day);
        else
            Day=String.valueOf(day);
        cursor=dbOpenHelper.Fetch_Database(Day,gd.getMonth(),gd.getYear());
        if(cursor.moveToFirst()!=false&&cursor.getCount()!=0) {
            cursor.moveToFirst();

            Date = String.valueOf(day) + "," +
                    gd.get_full_month(cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Month)));
            while (!cursor.isAfterLast()) {

                switch (cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))){
                    case 4 :
                        inc += cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                        break;
                    case 6 :
                        break;
                    default:
                        exp+=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                }


                cursor.moveToNext();
            }
        }
        ex.setText(String.valueOf(exp));
        in.setText(String.valueOf(inc));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setbudget(){
        getDate gd=new getDate();
        long budget=5000;
        long spent=0;
        String day=null;
        int d=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for(int i=1;i<=d;i++){
            if(i<10)
                day="0"+String.valueOf(i);
            else
                day=String.valueOf(i);
            Cursor cursor=dbOpenHelper.Fetch_Database(day,gd.getMonth(),gd.getYear());
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=4&&
                        cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=6) {
                    spent+=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                }
                cursor.moveToNext();
            }

        }
        int ld=0;
        long safe = budget - spent;
        switch (Calendar.getInstance().get(Calendar.MONTH)){
            case 1 :
            case 3 :
            case 5 :
            case 7 :
            case 8 :
            case 10 :
            case 12 : ld=31-d;
                      break;
            case 2 : ld=28-d;
                     break;
            default:ld=30-d;
        }
        safe=safe/ld;
        Calendar.getInstance().get(Calendar.IS_LEAP_MONTH);
        float progress = spent / 50;
        if(progress>100){
            tv_msafe.setText("0 per day");
        }
        else
            tv_msafe.setText(String.valueOf(safe)+" per day");
        if (progress < 40) {
            budget_per.setTextColor(Color.parseColor("#00FF00"));
        }
        else if(progress<75){
            budget_per.setTextColor(Color.parseColor("#ff6f00"));
        }
        else if(progress<100){
            budget_per.setTextColor(Color.parseColor("#E65100"));

        }
        else{
            budget_per.setTextColor(Color.parseColor("#FF0000"));
        }


        budget_per.setText("You have consumed "+String.valueOf(progress)+"% of your limit");
        spent=spent/d;
        tv_mspent.setText(String.valueOf(spent)+" per day");
        pb_budget.setProgress((int) progress);
        pb_budget.setIndeterminate(false);

    }



}

