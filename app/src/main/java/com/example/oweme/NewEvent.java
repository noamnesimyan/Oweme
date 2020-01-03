package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewEvent extends AppCompatActivity {

    private TextView eventName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
   // private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        eventName = (TextView)findViewById(R.id.eventName);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddUsersAdapter(this); // specify an adapter
        recyclerView.setAdapter(mAdapter);
      //  mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.addBTN:
                (findViewById(R.id.my_recycler_view)).setVisibility(View.VISIBLE);
                break;
            case R.id.createNewEvent:
                addEventToFireBase(database);
                ((AddUsersAdapter)mAdapter).moveToNewEvent(eventName);
                break;

        }
    }

    private void addEventToFireBase(final FirebaseDatabase database)
    {
        final String members = ((AddUsersAdapter)mAdapter).getMembers();
        String key = database.getReference().child("Events").push().getKey();
        final Event newEvent = new Event(key, this.eventName.getText().toString(),"active", members);
        database.getReference().child("Events").child(key).setValue(newEvent);

        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (members.contains(user.getUserID()))
                    {
                        // user.setEvents(user.getEvents() + newEvent.getEid());
                        database.getReference().child("Users").child(user.getUserID()).child("events").
                                setValue(user.getEvents().isEmpty()? newEvent.getEid() : user.getEvents()+", " + newEvent.getEid());
                    }
                }
                mAdapter.notifyDataSetChanged(); //updates the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"LAY!",Toast.LENGTH_LONG).show();
    }



}
