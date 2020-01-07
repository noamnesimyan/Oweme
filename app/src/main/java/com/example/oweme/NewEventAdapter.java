package com.example.oweme;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewEventAdapter extends RecyclerView.Adapter<NewEventAdapter.MyViewHolder> {

    private ArrayList<User> users;
    private ArrayList<String> uIDs;
    private String currentUserID;
    private Context mContext;
    private String members;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private CheckBox checkBox;
        private NewEventAdapter myAdapter;
        private static String members;

        public MyViewHolder(final View item, NewEventAdapter myAdapter) {

            super(item);
            textView = item.findViewById(R.id.nickName);
            imageView = item.findViewById(R.id.photo);
            checkBox = item.findViewById(R.id.select);
            this.myAdapter = myAdapter;
            members = "";
        }

        public void bindData(final User user) {
            this.textView.setText(user.getNickName());
            //   this.imageView.setImageURI(Uri.parse((user.getUrlPhoto())));
            Glide.with(this.imageView.getContext())
                    .load(user.getUrlPhoto())
                    .into(this.imageView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.LTGRAY);
                        myAdapter.uIDs.add(user.getUserID());
                        members += ", " + user.getUserID();
                    } else {
                        ((LinearLayout) checkBox.getParent()).setBackgroundColor(Color.WHITE);
                        myAdapter.uIDs.remove(user.getUserID());
                        members.replace(", " + user.getUserID(), "");
                    }
                }
            });
        }

        public static String getMembers() {
            return members;
        }
    }

    public NewEventAdapter(Context context) {

        this.users = new ArrayList<User>();
        this.uIDs = new ArrayList<String>();
        this.currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mContext = context;
        getAllUsersFromFB();
    }

    private void getAllUsersFromFB() {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (!user.getUserID().equals(currentUserID)) {
                        users.add(user);
                    }

                }
                notifyDataSetChanged(); //updates the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @NonNull
    @Override
    public NewEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        NewEventAdapter.MyViewHolder vh = new MyViewHolder(newView, this);
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

    public void moveToNewEvent(Event event) {

        Intent i = new Intent(this.mContext, EventMenu.class);
        i.putExtra("EventName", event.getEventName());
        i.putExtra("EventID", event.getEid());
        this.uIDs.add(currentUserID);
        i.putStringArrayListExtra("SelectedUsers", this.uIDs);
        this.mContext.startActivity(i);
    }

    public String getMembers() {
        members = currentUserID + MyViewHolder.getMembers();
        return members;
    }



}
