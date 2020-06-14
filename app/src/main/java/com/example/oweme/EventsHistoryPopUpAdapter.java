package com.example.oweme;

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
import java.util.List;

public class EventsHistoryPopUpAdapter extends RecyclerView.Adapter {

    private ArrayList<Expense> expenses;
    private MyDataBase myLocalDB;
    private List<Debt> debts;
    Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView debtDetails;
        private ImageView picture;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.debtDetails = itemView.findViewById(R.id.debtDetails);
            this.picture = itemView.findViewById(R.id.picture);
        }

        public void bindData(final Debt debt) {

            if(debt.getUserID().equals(mAuth.getCurrentUser().getUid())) {
                this.debtDetails.setText("So far, you wasted " + String.format("%.2f", -1*debt.getAmount()) + " NIS!");
            }
            else {
                if(debt.getAmount() > 0) {
                    this.debtDetails.setText(debt.getNickName() + " owe you " + String.format("%.2f", debt.getAmount()) + " NIS!");
                }
                else {
                    this.debtDetails.setText("You owe " + debt.getNickName()  + " " + String.format("%.2f", -debt.getAmount()) + " NIS!");
                }
            }

            Glide.with(this.picture.getContext()).load(debt.getUrlPhoto()).into(picture);
        }
    }

    public EventsHistoryPopUpAdapter(String eventID, Context context) {
        this.expenses = new ArrayList<Expense>();
        this.context = context;
        myLocalDB = MyDataBase.getInstance(context);
        getEventExpenses(eventID);

    }

    private void getDebtsDetails() {
        for (int i = 0; i < debts.size(); i++) {
            final int finalI = i;
            database.getReference().child("Users").child(this.debts.get(i).getUserID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    debts.get(finalI).setNickName(dataSnapshot.child("nickName").getValue(String.class));
                    debts.get(finalI).setUrlPhoto(dataSnapshot.child("urlPhoto").getValue(String.class));
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void createDebtsFromExpenses(ArrayList<Expense> expenses) {
        MyDataBase myLocalDB = MyDataBase.getInstance(context);
        myLocalDB.deleteAllDebts(false);
        for(int i = 0; i < expenses.size(); i++) {
            Util.addDebt(myLocalDB, expenses.get(i), false);
        }
        this.debts = myLocalDB.getAllDebts(false);
    }

    private void getEventExpenses(String eventID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Events").child(eventID).child("Expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    expenses.add(userSnapshot.getValue(Expense.class));
                }
                createDebtsFromExpenses(expenses);
                getDebtsDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_popup, parent, false);
        EventsHistoryPopUpAdapter.MyViewHolder vh = new EventsHistoryPopUpAdapter.MyViewHolder(newView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EventsHistoryPopUpAdapter.MyViewHolder)holder).bindData(debts.get(position));
    }

    @Override
    public int getItemCount() {
        return debts.size();
    }
}
