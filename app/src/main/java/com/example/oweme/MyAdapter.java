package com.example.oweme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<User> mDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(View item) {
            super(item);
            textView = item.findViewById(R.id.nickName);
            imageView = item.findViewById(R.id.photo);
        }
    }

    public MyAdapter(ArrayList<User> myDataSet) {
        mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_new_event, parent, false);
        MyViewHolder vh = new MyViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(mDataSet.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
