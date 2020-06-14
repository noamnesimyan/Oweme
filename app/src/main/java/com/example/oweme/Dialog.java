package com.example.oweme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.FirebaseDatabase;

public class Dialog extends AppCompatDialogFragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String eventID;
    Context context;

    public Dialog(String eventID, Context context) {
        this.eventID = eventID;
        this.context = context;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Be Careful!").setMessage("Are you sure you want to lock the event? \nYou won't be able to add expenses in this event anymore!")
                .setPositiveButton("Im Sure!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference().child("Events").child(eventID).child("status").setValue("locked");
                        ((Activity)context).findViewById(R.id.newExpense).setEnabled(false);
                    }
                }).setNegativeButton("NO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Action canceled", Toast.LENGTH_LONG).show();
            }
        });


        return builder.create();

    }
}
