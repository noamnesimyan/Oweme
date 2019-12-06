package com.example.oweme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewEvent extends AppCompatActivity {

    private TextView eventName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        eventName = (TextView)findViewById(R.id.eventName);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);  // use a linear layout manager
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddUsersAdapter(this); // specify an adapter
        recyclerView.setAdapter(mAdapter);

         /*eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                flag = !eventName.getText().toString().equals("");
                if(flag)
                {
                    (findViewById(R.id.createNewEvent)).setEnabled(true);
                }
            }
        }); */
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.addBTN:
                (findViewById(R.id.my_recycler_view)).setVisibility(View.VISIBLE);
                break;
            case R.id.createNewEvent:
                ((AddUsersAdapter)mAdapter).moveToNewEvent(eventName);
                break;

        }
    }


}
