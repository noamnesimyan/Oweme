package com.example.oweme;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<User> users;
    private ArrayList<String> uIDs;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private CheckBox checkBox;
        private MyAdapter myAdapter;

        public MyViewHolder(final View item, MyAdapter myAdapter) {
            super(item);
            textView = item.findViewById(R.id.nickName);
            imageView = item.findViewById(R.id.photo);
            checkBox = item.findViewById(R.id.select);
            this.myAdapter = myAdapter;

        }

        public void bindData(final User user) {
            this.textView.setText(user.getNickName());
       //   this.imageView.setImageURI(Uri.parse((user.getUrlPhoto())));
            Glide.with(this.imageView.getContext())
                    .load(user.getUrlPhoto())
                    .into( this.imageView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked) {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.LTGRAY);
                        myAdapter.uIDs.add(user.getUid());
                    }
                    else {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.WHITE);
                        myAdapter.uIDs.remove(user.getUid());
                    }
                }
            });
        }
    }

    public MyAdapter() {
        this.users = new ArrayList<User>();
        this.uIDs = new ArrayList<String>();
        getAllUsersFromFB();
    }

    private void getAllUsersFromFB(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users") .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    users.add(userSnapshot.getValue(User.class));
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(newView, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
