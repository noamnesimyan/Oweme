package com.example.oweme;

import android.content.Context;
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

import java.util.List;

public class AccountStatusAdapter extends RecyclerView.Adapter<AccountStatusAdapter.MyViewHolder> {

    private List<Debt> debts;
    private MyDataBase myLocalDB;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView debtDetails;
        private ImageView picture;
        MyDataBase myLocalDB;
        Context context;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.debtDetails = itemView.findViewById(R.id.debtDetails);
            this.picture = itemView.findViewById(R.id.picture);
            this.context = context;
            myLocalDB = MyDataBase.getInstance(context);
        }

        public void bindData(final Debt debt) {
            if(debt.getUserID().equals(mAuth.getCurrentUser().getUid())) {
                String details = ("You wasted " + debt.getAmount() + " NIS!");
                this.debtDetails.setText(details);
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, ((debt.getAmount() > 0 ) ?("Be sure s/he paid you your money!"):("Be sure you paid him/her their money!")), Toast.LENGTH_LONG).show();
                    myLocalDB.getDebtByUid(debt.getUserID(), true).setAmount(0);
                    myLocalDB.deleteDebt();
                    return false;
                }
            });
        }



    }

    public AccountStatusAdapter(Context context) {
        this.context = context;
        myLocalDB = MyDataBase.getInstance(context);
        this.debts = myLocalDB.getAllDebts(true);
        getDebtsDetails();
    }

    private void getDebtsDetails() {
        for (int i = 0; i < getItemCount(); i++) {
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

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.debt, parent, false);
        AccountStatusAdapter.MyViewHolder vh = new AccountStatusAdapter.MyViewHolder(newView, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ((AccountStatusAdapter.MyViewHolder)holder).bindData(debts.get(position));
    }

    @Override
    public int getItemCount() {
        return debts.size();
    }
}
