package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahuja.expensemanager.R;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.example.ahuja.expensemanager.database.getDate;
import com.example.ahuja.expensemanager.one_transaction;

public class detail_fragment extends Fragment implements Adapter_rv_transaction.ListItemClickListener {
    public int get_id;
    public Context context;
    private Adapter_rv_transaction art;
    private RecyclerView rvTransaction;
    private LinearLayoutManager layoutManager;
    private Cursor cursor;
    private DbOpenHelper dbOpenHelper;
    private getDate gd;
    public String query,day,month,year;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        dbOpenHelper=new DbOpenHelper(getContext());
        layoutManager=new LinearLayoutManager(getContext());
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail,container);
    }

    @Override
    public void onItemClickListener(int ClickedIndex, RecyclerView.ViewHolder viewHolder) {
        long id = (long) viewHolder.itemView.getTag();
        Intent i = new Intent(getContext(),one_transaction.class);
        i.putExtra("Id", id);
        startActivity(i);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gd=new getDate();

          super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Bundle bundle=getArguments();
        get_id=bundle.getInt("reach",-1);
        rvTransaction = getActivity().findViewById(R.id.frag_rv_transaction);
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
                    Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (cursor==null)
            Toast.makeText(getContext(), "Null Cursor retutrned", Toast.LENGTH_LONG).show();
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

        super.onResume();
    }
}

