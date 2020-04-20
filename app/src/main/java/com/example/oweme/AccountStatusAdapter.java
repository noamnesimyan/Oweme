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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AccountStatusAdapter extends RecyclerView.Adapter {

    private List<Depth> depths;
    private MyDataBase myLocalDB;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView debtDetails;
        private ImageView picture;
        String nickName = "";
        private FirebaseDatabase database = FirebaseDatabase.getInstance();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.debtDetails = itemView.findViewById(R.id.nickName);
            this.picture = itemView.findViewById(R.id.picture);
        }

        public void bindData(final Depth depth) {

            getNickNameFromUID(depth.getUserID());
            if(depth.getAmount() > 0) {
                this.debtDetails.setText(nickName + " owe you " + depth.getAmount() + " NIS!");
            }
            else {
                this.debtDetails.setText("You owe " + nickName  + " " + (-depth.getAmount()) + " NIS!");
            }
            database.getReference().child("Users").child(depth.getUserID()).child("urlPhoto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Glide.with(picture.getContext()).load(dataSnapshot.getValue(String.class)).into(picture);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        private void getNickNameFromUID(String userID) {
            database.getReference().child("Users").child(userID).child("nickName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nickName = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
                //addOnCompleteListener before setting the text in bindData
            });
        }
    }

    public AccountStatusAdapter(Context context) {
        this.context = context;
        myLocalDB = MyDataBase.getInstance(this.context);
        this.depths = myLocalDB.getAllDepths();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info, parent, false);
        AccountStatusAdapter.MyViewHolder vh = new AccountStatusAdapter.MyViewHolder(newView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AccountStatusAdapter.MyViewHolder)holder).bindData(depths.get(position));
    }

    @Override
    public int getItemCount() {
        return depths.size();
    }
}
