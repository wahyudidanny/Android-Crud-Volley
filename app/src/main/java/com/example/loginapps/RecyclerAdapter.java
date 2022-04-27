package com.example.loginapps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static final String TAG = "Some activity";
    private ArrayList<Data> arrayList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public RecyclerAdapter(ArrayList<Data> arrayList, OnNoteListener onNoteListener){
            this.arrayList = arrayList;
            this.mOnNoteListener = onNoteListener;
    }



    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

     //   public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position)

        Button testbutton;
        holder.id.setText(arrayList.get(position).getId());
        holder.item_code.setText(arrayList.get(position).getItem_code());
        holder.item_name.setText(arrayList.get(position).getItem_name());
        holder.item_price.setText(Double.toString(arrayList.get(position).getItemPrice()));
        holder.item_stock.setText(String.valueOf(arrayList.get(position).getStock()));
       /* holder.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Intent intent  = new Intent(android.content.Intent, UpdateDataDialog.class);
               // startActivity(intent);
                Log.d(TAG, "onNoteClick: " + position);
            //View.OnClickListener(testbutton, position);
              //  Intent intent  = new Intent(this, SomeActivity.class);
              //  startActivity(intent);

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id,item_code,item_name,item_price,item_stock;
        //Button testButton;
        OnNoteListener onNoteListener;

        public MyViewHolder(View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.txt_id);
            item_code = (TextView) itemView.findViewById(R.id.item_code);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_price = (TextView) itemView.findViewById(R.id.price);
            item_stock = (TextView) itemView.findViewById(R.id.stock);
            this.onNoteListener = onNoteListener;
            //testButton = (Button) itemView.findViewById(R.id.btn_editData);
            //testButton.setOnClickListener(this);
            itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
           // onNoteListener.onButtonEditClick(getAdapterPosition());
            }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
     //   void onButtonEditClick(int position);
    }



}
