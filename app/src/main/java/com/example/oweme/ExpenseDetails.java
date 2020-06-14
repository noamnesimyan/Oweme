package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> selectedUsersIDs;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String eventID;
    private ImageView imgPhoto;
    private TextView eventDate;
    private TextView description;
    private TextView bill;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private MyDataBase myLocalDB;
    public ProgressBar progressBar;
    private String expenseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        selectedUsersIDs = getIntent().getStringArrayListExtra("SelectedUsers");
        mAdapter = new ExpenseDetailsAdapter(selectedUsersIDs); // specify an adapter
        recyclerView.setAdapter(mAdapter);
        mAuth = FirebaseAuth.getInstance();
        imgPhoto = findViewById(R.id.photo);
        eventID = getIntent().getStringExtra("EventID");
        description = findViewById(R.id.description);
        bill = findViewById(R.id.bill);
        myLocalDB = MyDataBase.getInstance(ExpenseDetails.this);
        progressBar = findViewById(R.id.progressBar);

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
            case R.id.createExpenseBTN:
                if (((ExpenseDetailsAdapter) mAdapter).getUsersIPaidFor().size() > 0) {
                    uploadImageIntoFB();
                } else {
                    Toast.makeText(ExpenseDetails.this, "You have to pay for at least 1 person \n(it can be you either)", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void takeAPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgPhoto.setImageBitmap(imageBitmap);
            imgPhoto.setTag(imageBitmap);
        }
    }


   private void uploadImageIntoFB() {
        showProgress();

        Bitmap bitmap = (Bitmap) imgPhoto.getTag();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        expenseID = database.getReference().child("Events").child(this.eventID).child("Expenses").push().getKey();
        final StorageReference storageRef = storage.getReference().child("ExpensesPhotos").child(eventID).child(expenseID+ ".jpeg");

        UploadTask uploadTask = storageRef.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUri = task.getResult().toString();
                    Expense newExpense = new Expense();
                    newExpense.setUrlPhoto(downloadUri);
                    addExpenseToDBs(newExpense, ExpenseDetails.this);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void addExpenseToDBs(final Expense newExpense, final Context context) {

        String usersIPaidFor = ((ExpenseDetailsAdapter) mAdapter).getUsersIPaidFor().toString();
        usersIPaidFor = usersIPaidFor.substring(1, usersIPaidFor.length() - 1); //removes '[' and ']' from ArrayList.toString
        usersIPaidFor = usersIPaidFor.replaceAll(" ", ""); // removes the spaces from ArrayList.toString

        newExpense.setDescription(description.getText().toString());
        newExpense.setAmount(Double.parseDouble(bill.getText().toString()));
        newExpense.setOwner(mAuth.getCurrentUser().getUid());
        newExpense.setMembers(usersIPaidFor);
        newExpense.setCreatedDate(System.currentTimeMillis());
        newExpense.setExpenseID(expenseID);
        newExpense.setEventID(this.eventID);
        database.getReference().child("Events").child(this.eventID).child("Expenses").child(expenseID).setValue(newExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                myLocalDB.addNewExpense(newExpense); // add expense to local DB

                String[] members = newExpense.getMembers().split(",");
                double bill = newExpense.getAmount() / members.length;


                for(int i = 0; i < members.length; i++) {

                    if(!members[i].equals(mAuth.getCurrentUser().getUid())) {

                        Debt debt = myLocalDB.getDebtByUid(members[i], true);
                        if(debt == null) {
                            Debt newDebt = new Debt(members[i], bill, true);
                            myLocalDB.updateDebt(newDebt);
                        }
                        else {
                            debt.setAmount(debt.getAmount() + bill);
                            myLocalDB.updateDebt(debt);
                        }
                    }
                }

                hideProgress();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }); //add expense to FireBase


    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
