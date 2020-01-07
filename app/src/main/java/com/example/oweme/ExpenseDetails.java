package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> selectedUsersUIDS;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String eventID;
    private ImageView photo;
    private TextView eventDate;

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
        photo = findViewById(R.id.photo);
        eventID = getIntent().getStringExtra("eventID");
        eventDate = findViewById(R.id.date);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        eventDate.setText("Creation Date: " + date);

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
            photo.setImageBitmap(imageBitmap);
        }
    }



}
