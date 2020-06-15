package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventMenu extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDate;
    private ArrayList<String> selectedUsersIDS;
    private String eventID;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();



    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_menu);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        eventID = getIntent().getStringExtra("EventID");
        mAdapter = new EventMenuAdapter(this, eventID); // specify an adapter
        recyclerView.setAdapter(mAdapter);
        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.date);
        Intent intent = getIntent();

        setNameAndDate(intent);
    }

    private void setNameAndDate(Intent intent) {

        eventName.setText(intent.getStringExtra("EventName")); //modifier the event name

        if(intent.getStringExtra("EventDate") == null) {
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());  //modifier the event date
            eventDate.setText("Creation Date: " + date);
        }
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateTime = new Date(Long.parseLong(getIntent().getStringExtra("EventDate")));
            eventDate.setText("Creation Date: " + dateFormat.format(dateTime));

        }

    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.newExpense:
                moveToExpenseDetails();
                break;
            case R.id.back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.lockEvent:
                openDialog();
                break;

        }
    }

    private void openDialog() {
        Dialog dialog = new Dialog(this.eventID, this);
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    private void moveToExpenseDetails() {
        Intent i = new Intent(this, ExpenseDetails.class);
        selectedUsersIDS = getIntent().getStringArrayListExtra("SelectedUsers");
        i.putStringArrayListExtra("SelectedUsers", selectedUsersIDS);
        i.putExtra("EventID", this.eventID);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
            recyclerView.setLayoutManager(layoutManager);
            eventID = getIntent().getStringExtra("EventID");
            mAdapter = new EventMenuAdapter(this, eventID); // specify an adapter
            recyclerView.setAdapter(mAdapter);

            Toast.makeText(EventMenu.this, "Expense completed", Toast.LENGTH_LONG).show();
        }
    }



}
