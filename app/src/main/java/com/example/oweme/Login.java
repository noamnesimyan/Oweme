package com.example.oweme;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Login extends AppCompatActivity {

    public ProgressBar mProgress;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgress = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.userName);
        mPasswordField = findViewById(R.id.password);
        remember = findViewById(R.id.remember);

        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");
        if(checkBox.equals("true")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();
                }
                else if (!buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(Login.this, "UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClick(View view)
    {
        int id = view.getId();
        switch (id) {
            case R.id.signIn_btn:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.register_btn:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
        }
    }

    private void signIn(String email, String password) {

        showProgress();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(Login.this, "Authentication completed.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            moveToMainActivity(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();

                        }

                        hideProgress();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    private void moveToMainActivity(FirebaseUser currentUser){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
