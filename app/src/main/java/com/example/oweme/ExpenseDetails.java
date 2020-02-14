package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> selectedUsersIDS;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String eventID;
    private ImageView picture;
    private TextView eventDate;
    private TextView description;
    private TextView bill;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private MyDataBase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        selectedUsersIDS = getIntent().getStringArrayListExtra("SelectedUsers");
        mAdapter = new ExpenseDetailsAdapter(selectedUsersIDS); // specify an adapter
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setAdapter(mAdapter);
        picture = findViewById(R.id.photo);
        eventID = getIntent().getStringExtra("EventID");
        description = findViewById(R.id.description);
        bill = findViewById(R.id.bill);

        eventDate = findViewById(R.id.date);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        eventDate.setText("Creation Date: " + date);


        findViewById(R.id.createExpenseBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ExpenseDetailsAdapter)mAdapter).getUsersIPaidFor().size() > 0)
                {
                    addExpenseToDBs(database);
                }
                else
                {
                    Toast.makeText(ExpenseDetails.this, "You have to pay for at least 1 person \n(it can be you either)", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.takePhoto:
                takeAPhoto();
                break;
        }
    }

    private void takeAPhoto()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(imageBitmap);
        }
    }

    private void addExpenseToDBs(final FirebaseDatabase database) {

        String usersIPaidFor = ((ExpenseDetailsAdapter)mAdapter).getUsersIPaidFor().toString();
        usersIPaidFor = usersIPaidFor.substring(1, usersIPaidFor.length() - 1); //removes '[' and ']' from ArrayList.toString

        String expKey = database.getReference().child("Events").child(this.eventID).child("Expenses").push().getKey();
        Expense newExpense = new Expense(description.getText().toString(), Double.parseDouble(bill.getText().toString()), mAuth.getCurrentUser().getUid(), usersIPaidFor, expKey);
        newExpense.setEventID(this.eventID);
        database.getReference().child("Events").child(this.eventID).child("Expenses").child(expKey).setValue(newExpense); //add expense to FireBase

        MyDataBase.getInstance(ExpenseDetails.this).addNewExpense(newExpense); // add expense to local DB
       // MyDataBase.getInstance(ExpenseDetails.this).addNewDepth(); // להתחיל מכאן! להוסיף את החוב לDB הלוקאלי

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
