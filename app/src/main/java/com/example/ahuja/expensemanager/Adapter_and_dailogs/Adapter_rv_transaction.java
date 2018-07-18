package com.example.ahuja.expensemanager.Adapter_and_dailogs;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahuja.expensemanager.R;
import com.example.ahuja.expensemanager.database.Contract;

import static android.content.ContentValues.TAG;

/**
 * Created by Ahuja on 4/5/2018.
 */

public class Adapter_rv_transaction extends RecyclerView.Adapter<Adapter_rv_transaction.TransactionViewHolder> {

    private Cursor cursor;
    final private ListItemClickListener listener;
    public interface ListItemClickListener{
        void onItemClickListener(int ClickedIndex,RecyclerView.ViewHolder viewHolder);
    }

    public Adapter_rv_transaction(Cursor cursor,ListItemClickListener listener) {
         this.cursor = cursor;
         this.listener=listener;
    }
    //sometimes a view group is used to get the context.
    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_rv_transaction,parent,false);
        return  new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {


        if (!cursor.moveToPosition(position))
            return;
         long id=cursor.getLong(cursor.getColumnIndex(Contract.DbContract._ID));
         String Date=cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Day)) + "/"
                     + cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Month))+"/"
                     +cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Year));
         String Description=cursor.getString(cursor.getColumnIndex(Contract.DbContract.Col_Desc));
         float Amount= cursor.getFloat(cursor.getColumnIndex(Contract.DbContract.Col_Amnt));
         holder.item_date.setText(Date);
         holder.item_amount.setText(String.valueOf(Amount));
         holder.item_description.setText(Description);
         holder.itemView.setTag(id);
         switch (cursor.getInt(cursor.getColumnIndex(Contract.DbContract.Col_Type))){

             case 1 : holder.item_type.setText("Bill");
                 holder.imageView.setImageResource(R.drawable.billdisp);
                      holder.dispcard.setCardBackgroundColor(Color.parseColor("#42f4f4"));
                      break;
             case 2 : holder.item_type.setText("Food");
                 holder.imageView.setImageResource(R.drawable.fooddisp);
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#6d1bc4"));
                      break;
             case 3 : holder.item_type.setText("Entertainment");
                 holder.imageView.setImageResource(R.drawable.enterdisp);
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#FF0000"));
                     break;
             case 4 : holder.item_type.setText("Income");
                 holder.imageView.setImageResource(R.drawable.incdisp);
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#E5d22B"));
                      break;
             case 5 : holder.item_type.setText("Personnel");
                 holder.imageView.setImageResource(R.drawable.persdisp);
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#176331"));
                 break;
             case 6 : holder.item_type.setText("Saving");
                 holder.imageView.setImageResource(R.drawable.savedisp);
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#40d672"));
                 break;
             case 7 : holder.item_type.setText("Travel");
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#0000ff"));
                 holder.imageView.setImageResource(R.drawable.traveldisp);
                 break;
             case 8 : holder.item_type.setText("recoverable");
                 holder.dispcard.setCardBackgroundColor(Color.parseColor("#772a25"));
                 holder.imageView.setImageResource(R.drawable.recoverdisp);
                 break;

             default: //do nothing
        }
    }

    @Override
    public int getItemCount() {
        Log.d( TAG, "getItemCount: "+cursor.getCount());
        return cursor.getCount();
    }
//    //will be used when new data is stored
    public void swapCursor(Cursor newcursor){
        if(cursor!=null)
            cursor.close();
        cursor=newcursor;
        if(cursor!=null)
            this.notifyDataSetChanged();
    }


   //class is made to store views.so that when a gone item form list reappears it doesn't have to create the item view again
    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
          public TextView item_date,item_description,item_amount,item_type;
          public CardView dispcard;
          public AppCompatImageView imageView;
          public TransactionViewHolder(View itemView) {
                super(itemView);
                item_type=itemView.findViewById(R.id.item_type);
                dispcard=itemView.findViewById(R.id.imagecard);
                imageView=itemView.findViewById(R.id.imageview);
                item_amount= itemView.findViewById(R.id.item_amount);
                item_date=itemView.findViewById(R.id.item_date);
                item_description=itemView.findViewById(R.id.item_description);
                itemView.setOnClickListener(this);
        }

       @Override
       public void onClick(View view) {
           int clickedPosition=getAdapterPosition();
           listener.onItemClickListener(clickedPosition,this);
       }
   }

}
