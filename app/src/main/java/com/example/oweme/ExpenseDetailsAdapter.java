package com.example.oweme;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpenseDetailsAdapter extends RecyclerView.Adapter {

    private ArrayList<User> users;
    private ArrayList<String> checkedUsers;
    private ArrayList<String> usersIPaidFor;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private CheckBox checkBox;
        private ExpenseDetailsAdapter myAdapter;

        public MyViewHolder(final View item, ExpenseDetailsAdapter myAdapter) {

            super(item);
            textView = item.findViewById(R.id.nickName);
            imageView = item.findViewById(R.id.photo);
            checkBox = item.findViewById(R.id.select);
            this.myAdapter = myAdapter;

        }

        public void bindData(final User user) {
            this.textView.setText(user.getNickName());
            Glide.with(this.imageView.getContext())
                    .load(user.getUrlPhoto())
                    .into(this.imageView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked) {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.LTGRAY);
                        myAdapter.usersIPaidFor.add(user.getUserID());
                    }
                    else {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.WHITE);
                        myAdapter.usersIPaidFor.remove(user.getUserID());
                    }
                }
            });
        }
    }

    public ExpenseDetailsAdapter(ArrayList<String> checkedUsers) {

        this.users = new ArrayList<User>();
        this.checkedUsers = checkedUsers;
        this.usersIPaidFor = new ArrayList<String>();
        getAllCheckedUsers(this.checkedUsers);
    }

    private void getAllCheckedUsers(ArrayList<String> checkedUsers) {

        for(int i = 0; i < checkedUsers.size(); i++) {

            FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();
            myDatabase.getReference().child("Users").child(checkedUsers.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.member, parent, false);
        ExpenseDetailsAdapter.MyViewHolder vh = new ExpenseDetailsAdapter.MyViewHolder(newView, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).bindData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public ArrayList<String> getUsersIPaidFor()
    {
        return this.usersIPaidFor;
    }
}
