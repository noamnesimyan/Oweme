package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isMyServiceRunning(FirebaseListener.class))
        {
            Intent intent = new Intent(this, FirebaseListener.class);
            startService(intent);
        }

    }
}
