package com.example.ahuja.expensemanager;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ahuja.expensemanager.Adapter_and_dailogs.Adapter_rv_transaction;
import com.example.ahuja.expensemanager.Adapter_and_dailogs.detail_fragment;
import com.example.ahuja.expensemanager.database.DbOpenHelper;

public class Search_Activity extends AppCompatActivity implements Adapter_rv_transaction.ListItemClickListener {
    private String query;
    FloatingActionButton fab;
    private Adapter_rv_transaction art;
    private RecyclerView rvTransaction;
    private LinearLayoutManager layoutManager;
    private Cursor cursor;
    private DbOpenHelper dbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        dbOpenHelper=new DbOpenHelper(this);
        layoutManager=new LinearLayoutManager(this);
        rvTransaction=findViewById(R.id.frag_rv_transaction);
        if(Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            query = getIntent().getStringExtra(SearchManager.QUERY);
        }
        set_rv_data();
        fab=findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
//        rvTransaction = findViewById(R.id.rv_transaction);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rvTransaction.setLayoutManager(new LinearLayoutManager(this));
//        if(cursor.getCount()==0&&cursor.moveToFirst()==false) {
//            Toast.makeText(this, "No Record Found!", Toast.LENGTH_LONG).show();
//            Intent i=new Intent(this,Transaction_Detail.class);
//            startActivity(i);
//
//        }else{
//            art = new Adapter_rv_transaction(cursor, this);
//            rvTransaction.setAdapter(art);
//
//                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                        return false;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        long id = (long) viewHolder.itemView.getTag();
//                        dbOpenHelper.delete_data(String.valueOf(id));
//                        cursor = dbOpenHelper.Fetch_Database();
//                        art.swapCursor(cursor);
//                    }
//                }).attachToRecyclerView(rvTransaction);
//
//
//        }
    }

    @Override
    public void onItemClickListener(int ClickedIndex,RecyclerView.ViewHolder viewHolder) {
        long id= (long) viewHolder.itemView.getTag();
        Intent i=new Intent(this,one_transaction.class);
        i.putExtra("Id",id);
        startActivity(i);
    }

    public void set_rv_data(){

        if(rvTransaction==null)
            Log.d("Check RV exists", "Recycler view not stored");
        else {
            rvTransaction.setLayoutManager(layoutManager);
                cursor = dbOpenHelper.Fetch_Database(query, -1);
                if (cursor.moveToFirst() == false && cursor.getCount() == 0) {
                    Toast.makeText(this, "No Record Found!", Toast.LENGTH_LONG).show();
                }
            }

        if(cursor==null)
            Toast.makeText(this, "Null Cursor retutrned", Toast.LENGTH_LONG).show();
        else {
            art = new Adapter_rv_transaction(cursor, this);
            rvTransaction.setAdapter(art);

        }
    }


}

