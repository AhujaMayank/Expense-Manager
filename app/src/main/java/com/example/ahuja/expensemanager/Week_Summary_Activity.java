package com.example.ahuja.expensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.ahuja.expensemanager.Adapter_and_dailogs.AxisFormatter_Week;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.DateHandler;
import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class Week_Summary_Activity extends AppCompatActivity {
    //Household stuff
    int no_of_week_back=0;
    SimpleDateFormat show,store;
    Cursor cursor;
    DbOpenHelper dbOpenHelper;
    DateHandler dateHandler;
    //for graphs
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    List<Entry> lineEntries= new ArrayList<>();
    List<BarEntry> barEntries=new ArrayList<>();
    //to store and show date
    String dstore,dshow;
    //Budget Week
      TextView budget_expense,endstart;
      long exp=0,tot_exp=0;
    //top spent card View setables
      TextView dt,des,amt,tp,nospend;
      CardView cardView;
      AppCompatImageView imageView;
      String dat,desc;
      long amnt=0,tpe=0;
    //Last Week
      TextView last_amt,lpd;
      long last_amnt=0;
      Calendar calendar;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week__summary);
        show=new SimpleDateFormat("MMMM,dd");
        store=new SimpleDateFormat("yyyy/MM/dd");
        budget_expense=findViewById(R.id.budget_expense);
        endstart=findViewById(R.id.tv_week_start_end);
        dt=findViewById(R.id.top_spent_date);
        tp=findViewById(R.id.top_spent_type);
        des=findViewById(R.id.top_spent_description);
        amt=findViewById(R.id.top_spent_amount);
        imageView=findViewById(R.id.top_imageview);
        cardView=findViewById(R.id.top_imagecard);
        nospend=findViewById(R.id.tv_top_no_spend);
        last_amt=findViewById(R.id.last_week_amnt);
        lpd=findViewById(R.id.last_week_perDay);
        barChart=findViewById(R.id.week_bar);
        lineChart=findViewById(R.id.budget_line);
        dbOpenHelper=new DbOpenHelper(this);
        calendar=Calendar.getInstance();

          set_bar();
          set_line();
          set_top_card();
          set_last_card();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void set_bar(){
        calendar=Calendar.getInstance();
        int n=getIntent().getIntExtra("goback",0);
        if(n>0)
            calendar.add(Calendar.WEEK_OF_YEAR,-1*n);
        int dno=calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR,(Calendar.SUNDAY-dno));
        Date d=calendar.getTime();
        dstore=store.format(d);
        dshow=show.format(d);
        endstart.setText(dshow+" - ");
        for(int i=0;i<7;i++){
            String[] dte=dstore.split("/");
            cursor=dbOpenHelper.Fetch_Database(dte[2],dte[1],dte[0]);
            cursor.moveToFirst();
            exp=0;
            while(!cursor.isAfterLast()){
                if(cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=4&&
                        cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=6) {
                    exp+=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                }
                cursor.moveToNext();
            }
            //Toast.makeText(this,"Date : "+dstore,Toast.LENGTH_SHORT).show();
            barEntries.add(new BarEntry(i,exp));
            calendar.add(Calendar.DAY_OF_YEAR,1);
            d=calendar.getTime();
            dstore=store.format(d);
            dshow=show.format(d);
        }
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        d=calendar.getTime();
        endstart.setText(endstart.getText().toString()+show.format(d));
        barDataSet=new BarDataSet(barEntries,"");
        barDataSet.setColor(Color.parseColor("#ffffff"));
        barData=new BarData(barDataSet);
        barData.setValueTextColor(Color.parseColor("#ffffff"));
        barChart.setDescription(null);
        barChart.setData(barData);
        barChart.setDrawValueAboveBar(true);
        barChart.getData().setValueTextSize(12f);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setValueFormatter(new AxisFormatter_Week());
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void set_line(){
        tot_exp=0;
        calendar=Calendar.getInstance();
        int n=getIntent().getIntExtra("goback",0);
        calendar.add(Calendar.WEEK_OF_YEAR,-1*n);
        int dno=calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR,(Calendar.SUNDAY-dno));
        Date d=calendar.getTime();
        dstore=store.format(d);
        dshow=show.format(d);
        for(int i=0;i<7;i++){
            String[] dte=dstore.split("/");
            cursor=dbOpenHelper.Fetch_Database(dte[2],dte[1],dte[0]);
            cursor.moveToFirst();
            exp=0;
            while(!cursor.isAfterLast()){
                if(cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=4&&
                        cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))!=6) {
                    exp+=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                }
                cursor.moveToNext();
            }
            calendar.add(Calendar.DAY_OF_YEAR,1);
            d=calendar.getTime();
            dstore=store.format(d);
            dshow=show.format(d);
            tot_exp+=exp;
            lineEntries.add(new BarEntry(i,exp));
        }
        lineChart.setDescription(null);
        lineDataSet=new LineDataSet(lineEntries,"");
        lineDataSet.setColor(Color.parseColor("#ffffff"));
        lineDataSet.setCircleColorHole(Color.parseColor("#ffffff"));
        lineDataSet.setCircleColor(Color.parseColor("#ffffff"));
        lineData=new LineData(lineDataSet);
        lineChart.getXAxis().setValueFormatter(new AxisFormatter_Week());
        lineChart.setData(lineData);
        lineDataSet.setValueTextSize(12f);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        lineData.setValueTextColor(Color.parseColor("#ffffff"));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setScaleEnabled(false);
        lineChart.getXAxis().setTextSize(12f);
        int no=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        long perday=0;
        if(n>0)
            perday=tot_exp/7;
        else
         perday=tot_exp/no;

        budget_expense.setText(String.valueOf(perday));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void set_top_card() {
        amnt=0;
        boolean flag=false;
        calendar=Calendar.getInstance();
        int n=getIntent().getIntExtra("goback",0);
        calendar.add(Calendar.WEEK_OF_YEAR,-1*n);
        int dno=calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR,(Calendar.SUNDAY-dno));
        Date d=calendar.getTime();

        dstore=store.format(d);
        dshow=show.format(d);

        for (int i = 0; i < 7; i++) {

            String[] dte = dstore.split("/");
            cursor = dbOpenHelper.Fetch_Database(dte[2], dte[1], dte[0]);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type)) != 4 &&
                        cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type)) != 6) {

                    if(amnt<cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt)))
                    {
                        desc=cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Desc));
                        tpe=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Type));
                        amnt=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                        dat=dshow;
                        flag=true;
                    }
                }
                cursor.moveToNext();
            }
            calendar.add(Calendar.DAY_OF_YEAR,1);
            d=calendar.getTime();
            dstore=store.format(d);
            dshow=show.format(d);
        }
        if(flag==true){
        switch((int)tpe){
                case 1 : tp.setText("Bill");
                    imageView.setImageResource(R.drawable.billdisp);
                    cardView.setCardBackgroundColor(Color.parseColor("#42f4f4"));
                    break;
                case 2 : tp.setText("Food");
                        imageView.setImageResource(R.drawable.fooddisp);
                        cardView.setCardBackgroundColor(Color.parseColor("#6d1bc4"));
                        break;
                case 3 : tp.setText("Entertainment");
                        imageView.setImageResource(R.drawable.enterdisp);
                        cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
                        break;
                case 4 : tp.setText("Income");
                        imageView.setImageResource(R.drawable.incdisp);
                        cardView.setCardBackgroundColor(Color.parseColor("#E5d22B"));
                            break;
                case 5 : tp.setText("Personnel");
                    imageView.setImageResource(R.drawable.persdisp);
                    cardView.setCardBackgroundColor(Color.parseColor("#176331"));

                    break;
                case 6 : tp.setText("Saving");
                    imageView.setImageResource(R.drawable.savedisp);
                    cardView.setCardBackgroundColor(Color.parseColor("#40d672"));
                    break;
               case 7 : tp.setText("Travel");
                   cardView.setCardBackgroundColor(Color.parseColor("#0000ff"));
                   imageView.setImageResource(R.drawable.traveldisp);

                   break;
                case 8 : tp.setText("recoverable");
                    cardView.setCardBackgroundColor(Color.parseColor("#772a25"));
                    imageView.setImageResource(R.drawable.recoverdisp);

                    break;

                default: //do nothing

            }
            des.setText(desc);
            amt.setText(String .valueOf(amnt));
            dt.setText(dat);
        }
        else{
            nospend.setVisibility(View.VISIBLE);
            des.setVisibility(View.INVISIBLE);
            dt.setVisibility(View.INVISIBLE);
            tp.setVisibility(View.INVISIBLE);
            amt.setVisibility(View.INVISIBLE);
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void set_last_card() {
        last_amnt=0;
        calendar=Calendar.getInstance();
        int an=getIntent().getIntExtra("goback",0);
        calendar.add(Calendar.WEEK_OF_YEAR,-1*an);
        int n=calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR,Calendar.SUNDAY-n);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        int dno=calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR,(Calendar.SUNDAY-dno));
        Date d=calendar.getTime();
        dstore=store.format(d);
        dshow=show.format(d);
        for (int i = 0; i < 7; i++) {
            String[] dte = dstore.split("/");
            cursor = dbOpenHelper.Fetch_Database(dte[2], dte[1], dte[0]);
            cursor.moveToFirst();
        //    Toast.makeText(this,"Date :"+dshow,Toast.LENGTH_SHORT).show();
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type)) != 4 &&
                        cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type)) != 6) {
                    last_amnt += cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
                }
                cursor.moveToNext();
            }
            calendar.add(Calendar.DAY_OF_YEAR,1);
            d=calendar.getTime();
            dstore=store.format(d);
            dshow=show.format(d);

        }

        last_amt.setText(String.valueOf(last_amnt));
        long x= (last_amnt/7);
        lpd.setText(String.valueOf(x)+" per Day");
    }

   //onClick methods
    public void weekAddSpend(View view){
        startActivity(new Intent(view.getContext(),Insert_Transaction.class));
    }
    public void weekSeeAll(View view){
        Intent i=new Intent(view.getContext(),Transaction_Detail.class);
        i.putExtra("reach",7);
        startActivity(i);
     }
    public void lastWeekSet(View view){
        Intent i=new Intent(view.getContext(),Week_Summary_Activity.class);
        int nobw=getIntent().getIntExtra("goback",0);
        i.putExtra("goback",nobw+1);
        startActivity(i);
//        Toast.makeText(this,"Nothing to show",Toast.LENGTH_SHORT);
    }
}
