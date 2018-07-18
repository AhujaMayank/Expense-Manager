package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahuja.expensemanager.R;
import com.example.ahuja.expensemanager.database.Contract;
import com.example.ahuja.expensemanager.database.DbOpenHelper;
import com.example.ahuja.expensemanager.database.getDate;

public class Adap_daily_View extends RecyclerView.Adapter<Adap_daily_View.Daily_ViewHolder> {
    DbOpenHelper dbOpenHelper;
    Cursor cursor;

    private String Date;
    Long ex,in;
    int day;
    getDate gd;
    String Day;
    public Adap_daily_View(Context context,int day) {
        dbOpenHelper=new DbOpenHelper(context);
        this.day=day;
        gd=new getDate();
        ex= Long.valueOf(0);
        in= Long.valueOf(0);
        Date="Null Cursor Returned";
     }

    @Override
    public Daily_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View v= layoutInflater.inflate(R.layout.item_rv_daily_view,parent,false);

        return new Daily_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Daily_ViewHolder holder, int position) {
        in= Long.valueOf(0);
        ex= Long.valueOf(0);
        day=Integer.parseInt(gd.getDay())-position;
        if(day<9)
          Day="0"+String.valueOf(day);
        else
          Day=String.valueOf(day);
        cursor=dbOpenHelper.Fetch_Database(Day,gd.getMonth(),gd.getYear());
        if(cursor!=null) {
            cursor.moveToFirst();
            Date = String.valueOf(day) + "," +
                    gd.get_full_month(cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Month)));
            while (!cursor.isAfterLast()) {
                    ex += cursor.getLong(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
            }

                cursor.moveToNext();

            }

        holder.exp.setText(String.valueOf(ex));
        holder.dt.setText(Date);
        holder.inc.setText(String.valueOf(in));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class Daily_ViewHolder extends RecyclerView.ViewHolder{

        public TextView dt,inc,exp;

        public Daily_ViewHolder(View itemView) {
            super(itemView);
            dt=itemView.findViewById(R.id.daily_date);
            inc=itemView.findViewById(R.id.daily_inc);
            exp=itemView.findViewById(R.id.daily_exp);
        }
    }
}
