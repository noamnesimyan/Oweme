package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AccountStatus extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_status);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AccountStatusAdapter(this); // specify an adapter
        recyclerView.setAdapter(mAdapter);


    }

    public void onClick(View view) {
        int id = view.getId();
        Intent i;
        switch (id) {
            case R.id.back:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.calcByEvent:
                i = new Intent(this, EventsHistory.class);
                i.putExtra("need double click?", true);
                startActivity(i);
                break;
            case R.id.paid:
                Toast.makeText(this,"Press a long click on the close debts", Toast.LENGTH_LONG).show();

        }
    }
}
