package com.mihneacristian.traveljournal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mihneacristian.traveljournal.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripViewHolder extends RecyclerView.ViewHolder {


    public TextView myTripNameTextView;
    public TextView destinationTextView;
    public TextView tripTypeTextView;
    public TextView tripPriceTextView;
    public TextView tripRatingTextView;
    public CircleImageView tripImage;


    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        
        myTripNameTextView = (TextView) itemView.findViewById(R.id.myTripNameTextView);
        destinationTextView = (TextView) itemView.findViewById(R.id.destinationTextView);
        tripTypeTextView = (TextView) itemView.findViewById(R.id.tripTypeTextView);
        tripPriceTextView = (TextView) itemView.findViewById(R.id.tripPriceTextView);
        tripRatingTextView = (TextView) itemView.findViewById(R.id.tripRatingTextView);
        tripImage = (CircleImageView) itemView.findViewById(R.id.tripImage);
    }

}
