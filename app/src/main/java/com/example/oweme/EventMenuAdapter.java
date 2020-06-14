package com.example.oweme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventMenuAdapter extends RecyclerView.Adapter {

    private ArrayList<Expense> expenses;
    private Context context;
    private String eventID;
    private boolean active;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView expenseDetails;
        private TextView members;
        private ImageView picture;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Context context;


        public MyViewHolder(final View item, Context context) {

            super(item);
            this.expenseDetails = item.findViewById(R.id.expenseDetails);
            this.members = item.findViewById(R.id.members);
            this.picture = item.findViewById(R.id.picture);
            this.context = context;
        }

        public void bindData(final Expense expense) {
            database.getReference().child("Users").child(expense.getOwner()).child("nickName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    expenseDetails.setText(dataSnapshot.getValue(String.class) + " paid " + expense.getAmount() + " NIS on " + expense.getDescription() + " for:");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            this.members.setText("...");
            Glide.with(this.picture.getContext()).load(expense.getUrlPhoto()).into(this.picture);

            this.members.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, EventMenuPopUp.class);
                    intent.putExtra("expenseID", expense.getExpenseID());
                    intent.putExtra("eventID", expense.getEventID());
                    context.startActivity(intent);
                    return false;
                }
            });
        }
    }



    public EventMenuAdapter(Context context, String eventID) {
        this.expenses = new ArrayList<Expense>();
        this.context = context;
        this.eventID = eventID;
        getEventStatus();
        getAllExpensesFromFB();
    }

    private void getEventStatus() {
        database.getReference().child("Events").child(eventID).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("active")) {
                    active = true;
                }
                else {
                    active = false;
                }
                ((Activity)context).findViewById(R.id.newExpense).setEnabled(active);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllExpensesFromFB() {

        database.getReference().child("Events").child(eventID).child("Expenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = userSnapshot.getValue(Expense.class);
                    expenses.add(expense);
                }
                notifyDataSetChanged(); //updates the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_info, parent, false);
        EventMenuAdapter.MyViewHolder vh = new EventMenuAdapter.MyViewHolder(newView, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EventMenuAdapter.MyViewHolder)holder).bindData(expenses.get(position));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
