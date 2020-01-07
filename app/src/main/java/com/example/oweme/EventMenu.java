package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventMenu extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDate;
    private ArrayList<String> selectedUsersUIDS;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_menu);
        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.date);
        eventID = getIntent().getStringExtra("eventID");
        setEventNameAndDate();
    }

    private void setEventNameAndDate()
    {
        //modifier the event name
        String finalEventName = getIntent().getStringExtra("EventName");
        eventName.setText(finalEventName);
        //modifier the event date
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        eventDate.setText("Creation Date: " + date);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.newExpense:
                moveToExpenseDetails();
        }
    }
    
    private void moveToExpenseDetails()
    {
        Intent i = new Intent(this, ExpenseDetails.class);
        selectedUsersUIDS = getIntent().getStringArrayListExtra("SelectedUsers");
        i.putStringArrayListExtra("SelectedUsers",selectedUsersUIDS);
        i.putExtra("eventID", this.eventID);
        startActivity(i);
    }

    private void startActivityForResult(Intent i) {
    }


}
