package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ExpenseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> selectedUsersUIDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        selectedUsersUIDS = getIntent().getStringArrayListExtra("SelectedUsers");
        mAdapter = new ExpenseAdapter(selectedUsersUIDS); // specify an adapter
        recyclerView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imageButton:
                //open camera here
                break;
        }
    }


}
