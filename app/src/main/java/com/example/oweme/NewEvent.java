package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class NewEvent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(); // specify an adapter
        recyclerView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.addBTN:
                (findViewById(R.id.my_recycler_view)).setVisibility(View.VISIBLE);
                break;
            case R.id.createNewEvent:
                moveToNewEvent();
                break;

        }
    }

    private void moveToNewEvent(FirebaseUser currentUser){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
