package com.example.oweme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseListener extends Service {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private String[] myEvents;
    private MyDataBase myLocalDB;

    public FirebaseListener() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Toast.makeText(this, "service starting", Toast.LENGTH_LONG).show();
        myLocalDB = MyDataBase.getInstance(this);
        refreshMyEvents();
        return super.onStartCommand(intent, flags, startId);
    }

    private void refreshMyEvents()
    {
        database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String events = (String)dataSnapshot.getValue();
                String[] newEvents = events.split(", "); //we assume that we only add events and it will be the last one in the array
                if (myEvents == null) {
                    for (int i = 0; i < newEvents.length; i++)
                        listenToEventExpenses(newEvents[i]);
                }
                else  if (newEvents.length > myEvents.length)
                    listenToEventExpenses(newEvents[newEvents.length-1]);

                myEvents = newEvents;
                //send notification if new event was added
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void listenToEventExpenses(String eventID)
    {
        database.getReference().child("Events").child(eventID).child("Expenses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Expense newExpense = dataSnapshot.getValue(Expense.class);

                if (!myLocalDB.exist(newExpense)) {
                    myLocalDB.addNewExpense(newExpense);

                    Depth newDepth = myLocalDB.getDepthByUid(newExpense.getOwner());

                    String[] members = newExpense.getMembers().split(", "); //fix all this spaces
                    double bill = newExpense.getAmount() / members.length;

                        if (newDepth == null) {
                            newDepth = new Depth(newExpense.getOwner(), -bill);
                            myLocalDB.updateDepth(newDepth);
                        }
                        else {
                            newDepth.setAmount(newDepth.getAmount() - bill);
                            myLocalDB.updateDepth(newDepth);
                        }
                        //send notification
                }
                else {
                    Toast.makeText(FirebaseListener.this, "Already added", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
