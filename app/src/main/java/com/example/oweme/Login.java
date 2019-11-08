package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view)
    {
        int id = view.getId();
        switch (id) {
            case R.id.signIn_btn:
                Toast.makeText(this, "noam the king",Toast.LENGTH_LONG).show();
                break;
            case R.id.register_btn:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
        }
    }
}
