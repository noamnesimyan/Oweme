package com.example.oweme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class EventsHistoryAdapter extends RecyclerView.Adapter {

    private ArrayList<Event> events;
    private String eventsIDs;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;
    private Intent intent;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView eventName;
        private TextView date;
        Context context;
        private Intent intent;
        private ArrayList<String> uIDs; // ArrayList of all the user IDs in the event


        public MyViewHolder(final View item, Context context, Intent intent) {

            super(item);
            eventName = item.findViewById(R.id.eventName);
            date = item.findViewById(R.id.date);
            this.context = context;
            this.uIDs = new ArrayList<String>();
            this.intent = intent;
        }

        public void bindData(final Event event) {
            this.eventName.setText(event.getEventName());
            this.date.setText(convertDate(event.getCreatedDate()));
            this.uIDs = arrayToArrayList(event.getMembers().split(","));

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, EventMenu.class);
                    intent.putExtra("EventID", event.getEVentID());
                    intent.putExtra("EventName", event.getEventName());
                    intent.putExtra("EventDate", event.getCreatedDate());
                    intent.putStringArrayListExtra("SelectedUsers", uIDs);
                    context.startActivity(intent);
                    return false;
                }
            });

            if(this.intent != null) {
                itemView.setOnClickListener(new DoubleClickListener() {
                    @Override
                    public void onDoubleClick() {
                        String eventID = event.getEVentID();
                        Intent intent = new Intent(context, EventsHistoryPopUp.class);
                        intent.putExtra("eventID", eventID);
                        context.startActivity(intent);
                    }
                });
            }


        }

        private String convertDate(long seconds) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateTime = new Date(seconds);
            return dateFormat.format(dateTime);

        }

        private ArrayList<String> arrayToArrayList(String[] array) {
            ArrayList<String> arrayList = new ArrayList<String>();
            for(String element : array) {
                arrayList.add(element);
            }
            return arrayList;
        }
    }

    public EventsHistoryAdapter(Context context, Intent intent) {

        this.events = new ArrayList<Event>();
        this.eventsIDs = new String();
        this.context = context;
        this.intent = intent;
        getAllEventsFromFB();
    }

    private void getAllEventsFromFB() {
        database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsIDs = dataSnapshot.getValue().toString();
                getEventFromEventID(eventsIDs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEventFromEventID(String eventsIDs) {
        String[] evIDs = eventsIDs.split(",");
        for(int i = 0; i < evIDs.length; i++) {
            database.getReference().child("Events").child(evIDs[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    events.add(dataSnapshot.getValue(Event.class));
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_info, parent, false);
        EventsHistoryAdapter.MyViewHolder vh = new EventsHistoryAdapter.MyViewHolder(newView, context, intent);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EventsHistoryAdapter.MyViewHolder)holder).bindData(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}