package com.mihneacristian.traveljournal.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mihneacristian.traveljournal.Activities.EditTripActivity;
import com.mihneacristian.traveljournal.Activities.ViewTripActivity;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;
import com.mihneacristian.traveljournal.ViewHolder.TripViewHolder;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TripsFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Trip, TripViewHolder> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        setLayoutManager();
        setAdapter();
    }

    private void setLayoutManager() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);
    }

    private void setAdapter() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("trips");

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(databaseReference, Trip.class).build();

        adapter = new FirebaseRecyclerAdapter<Trip, TripViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final TripViewHolder holder, int position, @NonNull final Trip model) {
                holder.myTripNameTextView.setText(model.getTripName());
                holder.destinationTextView.setText(model.getTripDestination());
                holder.tripTypeTextView.setText(model.getTripType());
                holder.tripPriceTextView.setText(String.valueOf(model.getTripPrice()) + " €");
                holder.tripRatingTextView.setText((String.valueOf(model.getTripRating())) + "★");
                Glide.with(getActivity()).load(model.getPhotoURL()).into(holder.tripImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewTripActivity.class);

                        intent.putExtra("tripId", model.getTripId());
                        intent.putExtra("tripName", model.getTripName());
                        intent.putExtra("tripDestination", model.getTripDestination());
                        intent.putExtra("tripType", model.getTripType());
                        intent.putExtra("tripPrice", model.getTripPrice());
                        intent.putExtra("startDate", model.getStartDate());
                        intent.putExtra("endDate", model.getEndDate());
                        intent.putExtra("rating", model.getTripRating());
                        intent.putExtra("photo", model.getPhotoURL());

                        intent.putExtra("fav", model.isFavorite());

                        startActivity(intent);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(getContext(), EditTripActivity.class);


                        intent.putExtra("tripId", model.getTripId());
                        intent.putExtra("tripName", model.getTripName());
                        intent.putExtra("tripDestination", model.getTripDestination());
                        intent.putExtra("tripType", model.getTripType());
                        intent.putExtra("tripPrice", model.getTripPrice());
                        intent.putExtra("startDate", model.getStartDate());
                        intent.putExtra("endDate", model.getEndDate());
                        intent.putExtra("rating", model.getTripRating());
                        intent.putExtra("photo", model.getPhotoURL());

                        startActivity(intent);
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public TripViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trips, parent, false);
                return new TripViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}