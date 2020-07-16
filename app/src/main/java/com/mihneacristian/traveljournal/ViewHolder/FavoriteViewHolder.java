package com.mihneacristian.traveljournal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mihneacristian.traveljournal.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    public TextView favoriteTripNameTextView;
    public TextView favoriteDestinationTextView;
    public TextView favoriteTripTypeTextView;
    public TextView favoriteTripPriceTextView;
    public TextView favoriteTripRatingTextView;
    public CircleImageView favoriteTripImage;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);

        favoriteTripNameTextView = (TextView) itemView.findViewById(R.id.favoriteTripNameTextView);
        favoriteDestinationTextView = (TextView) itemView.findViewById(R.id.favoriteDestinationTextView);
        favoriteTripTypeTextView = (TextView) itemView.findViewById(R.id.favoriteTripTypeTextView);
        favoriteTripPriceTextView = (TextView) itemView.findViewById(R.id.favoriteTripPriceTextView);
        favoriteTripRatingTextView = (TextView) itemView.findViewById(R.id.favoriteTripRatingTextView);
        favoriteTripImage = (CircleImageView) itemView.findViewById(R.id.favoriteTripImage);
    }
}
