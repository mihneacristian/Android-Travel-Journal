package com.mihneacristian.traveljournal.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mihneacristian.traveljournal.Activities.EditTripActivity;
import com.mihneacristian.traveljournal.Activities.ViewTripActivity;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;
import com.mihneacristian.traveljournal.ViewHolder.FavoriteViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Trip, FavoriteViewHolder> adapter;
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

        adapter = new FirebaseRecyclerAdapter<Trip, FavoriteViewHolder>(firebaseRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final FavoriteViewHolder holder, int position, @NonNull final Trip model) {

                if (model.isFavorite() == true) {

                    holder.favoriteTripNameTextView.setText(model.getTripName());
                    holder.favoriteDestinationTextView.setText(model.getTripDestination());
                    holder.favoriteTripTypeTextView.setText(model.getTripType());
                    holder.favoriteTripPriceTextView.setText(String.valueOf(model.getTripPrice()) + " €");
                    holder.favoriteTripRatingTextView.setText((String.valueOf(model.getTripRating())) + "★");

                    Glide.with(getActivity()).load(model.getPhotoURL()).into(holder.favoriteTripImage);

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
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                }

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
                public FavoriteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorites, parent, false);
                    return new FavoriteViewHolder(view);
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
