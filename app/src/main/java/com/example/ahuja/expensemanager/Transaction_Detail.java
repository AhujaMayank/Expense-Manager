package com.example.ahuja.expensemanager;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahuja.expensemanager.Adapter_and_dailogs.Adapter_rv_transaction;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.DateHandler;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.example.ahuja.expensemanager.database.getDate;

public class Transaction_Detail extends AppCompatActivity implements Adapter_rv_transaction.ListItemClickListener {
    int get_id=0;
    int no_day_to_move;
    private DateHandler dateHandler;
    Calendar d;
    Date t;
    String Date_store;
    FloatingActionButton fab;
    private Adapter_rv_transaction art;
    private RecyclerView rvTransaction;
    private LinearLayoutManager layoutManager;
    private Cursor cursor;
    private DbOpenHelper dbOpenHelper;
    private TextView curr_date;
    private Button frwd,bck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        no_day_to_move=0;
        dateHandler=new DateHandler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__detail);
        dbOpenHelper=new DbOpenHelper(this);
        layoutManager=new LinearLayoutManager(this);
        rvTransaction = findViewById(R.id.frag_rv_transaction);
        curr_date=findViewById(R.id.detail_date);
        frwd=findViewById(R.id.date_forward);
        bck=findViewById(R.id.date_backwards);
        set_rv_data();
        Date_store=dateHandler.getDate_store();
         if(getIntent().getBooleanExtra("visible",false)||
             getIntent().getIntExtra("reach",-1)==2){
              curr_date.setText(dateHandler.getDate_show());
              curr_date.setVisibility(View.VISIBLE);
              bck.setVisibility(View.VISIBLE);
              frwd.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_day_to_move++;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                dateHandler.move_date(no_day_to_move);
                            }
                            curr_date.setText(dateHandler.getDate_show());
                            Date_store=dateHandler.getDate_store();

                            //notify recycler view about date changed
                            notifyrecyclerView(Date_store);
                        }
                    }
            );
            bck.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            no_day_to_move--;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                dateHandler.move_date(no_day_to_move);
                            }
                            curr_date.setText(dateHandler.getDate_show());
                            Date_store=dateHandler.getDate_store();

                            //notify recycler View about Date changed
                            notifyrecyclerView(Date_store);
                        }
                    }
            );
         }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tran_detail_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchView searchView= (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchManager searchManager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName=new ComponentName(getApplicationContext(),Search_Activity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_add){
            Intent intent=new Intent(this,Insert_Transaction.class);
            intent.putExtra("calling_intent",2);
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void set_rv_data(){
        Bundle bundle=getIntent().getExtras();
        get_id=bundle.getInt("reach",-1);

        if(rvTransaction==null)
            Log.d("Check RV exists", "Recycler view not stored");
        else {
            rvTransaction.setLayoutManager(layoutManager);
            if (get_id == 1)//coming from details
                cursor = dbOpenHelper.Fetch_Database();
            if (get_id == 2)//coming from daily overview
                cursor = dbOpenHelper.Fetch_Database(bundle.getString("day"), bundle.getString("month")
                        , bundle.getString("year"));
            if (get_id == 3)//coming from monthly overview
                cursor = dbOpenHelper.Fetch_Database(bundle.getString("month"), bundle.getString("year"));
            if (get_id == 4)
                cursor = dbOpenHelper.Fetch_Database(bundle.getString("day"), bundle.getString("month")
                        , bundle.getString("year"));
            if (get_id == 5) {//coming from search activity
                   cursor = dbOpenHelper.Fetch_Database(bundle.getString("query"), -1);
                if (cursor.moveToFirst() == false && cursor.getCount() == 0) {
                    Toast.makeText(this, "No Record Found!", Toast.LENGTH_LONG).show();
                }
            }
            if(get_id==6){
                getDate gd=new getDate();
                cursor=dbOpenHelper.Fetch_Database(gd.getDay(),gd.getMonth(),gd.getYear());
            }
        }
        if (cursor==null)
            Toast.makeText(this, "Null Cursor retutrned", Toast.LENGTH_LONG).show();
        else {
            art = new Adapter_rv_transaction(cursor, this);
            rvTransaction.setAdapter(art);
            if (get_id==1) {
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        long id = (long) viewHolder.itemView.getTag();
                        dbOpenHelper.delete_data(String.valueOf(id));
                        cursor = dbOpenHelper.Fetch_Database();
                        art.swapCursor(cursor);
                    }
                }).attachToRecyclerView(rvTransaction);
            }

        }
    }

    @Override
    public void onItemClickListener(int ClickedIndex, RecyclerView.ViewHolder viewHolder) {
        long id = (long) viewHolder.itemView.getTag();
        Intent i = new Intent(this,one_transaction.class);
        i.putExtra("Id", id);
        startActivity(i);
    }

    public void notifyrecyclerView(String date){
        String[] d=date.split("/");
        cursor=dbOpenHelper.Fetch_Database(d[2],d[1],d[0]);
        art.swapCursor(cursor);
        SimpleDateFormat x=new SimpleDateFormat("yyyy/MM/dd");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(date.equals(x.format(Calendar.getInstance().getTime())))
                frwd.setVisibility(View.INVISIBLE);
            else if(getIntent().getBooleanExtra("visible",false)||
                    getIntent().getIntExtra("reach",-1)==2)
                frwd.setVisibility(View.VISIBLE);
        }
    }


//set Pie chart method




//    public void setPieChart(){
//        cursor.moveToFirst();
//        pieChart.setVisibility(View.VISIBLE);
//        int stat=0;
//        long inc=0,exp=0,clr=0,amnt=0,uclr=0;
//        while (cursor.isAfterLast()==false&&cursor.getCount()>0){
//            amnt=cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
//            switch(cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))){
//                case 1 :
//                    inc+=amnt;
//                    break;
//                case 2 :
//                    exp+=amnt;
//                    break;
//                case 3 : stat=cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Status));
//                         if(stat==1)
//                             clr+=amnt;
//                         if(stat==2)
//                             uclr+=amnt;
//                         break;
//            }
//            cursor.moveToNext();
//        }
//        entries.add(new PieEntry(inc,"Income"));
//        entries.add(new PieEntry(exp,"Expense"));
//        entries.add(new PieEntry(clr,"O-Cleared"));
//        entries.add(new PieEntry(uclr,"O-UnCleared"));
//        pieDataSet=new PieDataSet(entries,"Day's Type Distribution");
//        pieData=new PieData(pieDataSet);
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieChart.setData(pieData);
//        pieChart.invalidate();
//    }
}
