package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUp extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final String TAG = "appName";
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNickNameField;
    public ProgressBar mProgress;
    private ImageView imgPhoto;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mProgress = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.userName);
        mNickNameField = findViewById(R.id.nickName);
        mPasswordField = findViewById(R.id.password);
        imgPhoto = findViewById(R.id.imgUser);
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                flag = !mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("");
                if (flag)
                    findViewById(R.id.createUser).setEnabled(true);
            }
        });
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                flag = !mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("");
                if (flag)
                    findViewById(R.id.createUser).setEnabled(true);
            }
        });
        mNickNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                flag = !mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("") && !mNickNameField.getText().toString().equals("");
                if (flag)
                    findViewById(R.id.createUser).setEnabled(true);
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnGalery:
                pickImage();
                break;
            case R.id.createUser:
                findViewById(R.id.someText).setVisibility(View.VISIBLE);
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = findViewById(R.id.imgUser);
                imageView.setImageBitmap(bitmap);
                imageView.setTag(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "registration committed", Toast.LENGTH_LONG).show();
                            uploadImageIntoFB(user);
                           // addNewUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void uploadImageIntoFB(final FirebaseUser user){

        showProgress();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = (Bitmap) imgPhoto.getTag();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReference().child("UsersPhotos").child(user.getUid() + ".jpeg");

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
                    Uri downloadUri = task.getResult();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mNickNameField.getText().toString())
                            .setPhotoUri(downloadUri)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            addNewUser(user);
                        }
                    });

                } else {
                        // Handle failures
                        // ...
                }
            }
        });
    }
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.INVISIBLE);
    }


    private void addNewUser(final FirebaseUser user) {

        User newUser = new User(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
        database.getReference().child("Users").child(user.getUid()).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                 moveToMainActivity(user);
                                 hideProgress();
                            }
                        }
                    })
                .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgress();
                        }
                    });
    }
    private void moveToMainActivity(FirebaseUser currentUser){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
