package com.example.oweme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopUpAdapter extends RecyclerView.Adapter{

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<User> users;
    private String expenseID;
    private String eventID;
    private String usersIDs;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userNickName;
        private ImageView picture;

        public MyViewHolder(@NonNull View itemView, PopUpAdapter myAdapter) {
            super(itemView);
            userNickName = itemView.findViewById(R.id.nickName);
            picture = itemView.findViewById(R.id.picture);
        }

        public void bindData(final User user) {
            this.userNickName.setText(user.getNickName());
            Glide.with(this.picture.getContext()).load(user.getUrlPhoto()).into(this.picture);
        }
    }

    public PopUpAdapter(String expenseID, String eventID) {
        this.users = new ArrayList<User>();
        this.expenseID = expenseID;
        this.eventID = eventID;
        getUsersIDs();
    }

    private void getUsersIDs() {
        database.getReference().child("Events").child(eventID).child("Expenses").child(expenseID).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersIDs = dataSnapshot.getValue().toString();
                getUsersFromUsersIDs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsersFromUsersIDs() {
        String[] allIDs = usersIDs.split(",");
        for(int i = 0; i < allIDs.length; i++) {
            database.getReference().child("Users").child(allIDs[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.add(dataSnapshot.getValue(User.class));
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
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info, parent, false);
        PopUpAdapter.MyViewHolder vh = new PopUpAdapter.MyViewHolder(newView, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PopUpAdapter.MyViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
