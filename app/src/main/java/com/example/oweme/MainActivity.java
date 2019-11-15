package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.createEventBTN:
                moveToNewEventActivity();
                break;

            case R.id.historyBTN:
                break;

            case R.id.myAccountBTN:
                break;
        }
    }

    private void moveToNewEventActivity(){
        Intent i = new Intent(this, NewEvent.class);
        startActivity(i);
    }


}
