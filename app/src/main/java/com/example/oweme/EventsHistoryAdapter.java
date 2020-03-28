package com.example.oweme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView eventName;
        private TextView date;
        private EventsHistoryAdapter myAdapter;

        public MyViewHolder(final View item, EventsHistoryAdapter myAdapter) {

            super(item);
            eventName = item.findViewById(R.id.eventName);
            date = item.findViewById(R.id.date);
            this.myAdapter = myAdapter;

        }

        public void bindData(final Event event) {
            this.eventName.setText(event.getEventName());
            this.date.setText(convertDate(event.getCreatedDate()));
        }

        private String convertDate(long seconds) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateTime = new Date(seconds);
            return dateFormat.format(dateTime);

        }
    }

    public EventsHistoryAdapter(Context context) {

        this.events = new ArrayList<Event>();
        this.eventsIDs = new String();
        this.context = context;
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
        EventsHistoryAdapter.MyViewHolder vh = new EventsHistoryAdapter.MyViewHolder(newView, this);
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