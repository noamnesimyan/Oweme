package com.example.oweme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseListener extends Service {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private String[] myEvents;

    public FirebaseListener() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Toast.makeText(this, "service starting", Toast.LENGTH_LONG).show();
        refreshMyEvents();
        return super.onStartCommand(intent, flags, startId);
    }

    private void refreshMyEvents()
    {
        database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String events = (String)dataSnapshot.getValue();
                myEvents = events.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listenToEventExpenses()
    {
        // database.getReference().child("Users")
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
