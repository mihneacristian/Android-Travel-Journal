package com.mihneacristian.traveljournal.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;
import com.mihneacristian.traveljournal.ViewHolder.TripViewHolder;

public class RecyclerViewActivity extends AppCompatActivity {
/*
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Trip, TripViewHolder> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("trips");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showTrips();
    }

    private void showTrips() {
        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(databaseReference, Trip.class).build();

        adapter = new FirebaseRecyclerAdapter<Trip, TripViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TripViewHolder holder, int position, @NonNull Trip model) {
                holder.myTripNameTextView.setText(model.getTripName());
                holder.destinationTextView.setText(model.getTripDestination());
                holder.tripTypeTextView.setText(model.getTripType());
            }

            @NonNull
            @Override
            public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trips, parent, false);
                return new TripViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }*/
}