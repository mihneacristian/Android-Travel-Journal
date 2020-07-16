package com.mihneacristian.traveljournal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ViewTripActivity extends AppCompatActivity {

    private String tripId;
    private String destinationNameString;
    private String tripTypeString;
    private String toolbarTitleString;
    private String tripPriceString;
    private String tripStartDateString;
    private String tripEndDateString;
    private String tripImageString;
    private float ratingTrip;
    private int priceInteger;

    private Toolbar toolbarTitle;
    private TextView destinationNameTextView;
    private TextView viewTripTypeTextView;
    private TextView viewTripPriceTextView;
    private TextView viewTripActualStartDateTextView;
    private TextView viewTripActualEndDateTextView;
    private ImageView tripImageView;
    private RatingBar tripRatingViewTrip;

    private TextView locationTemperature;
    private TextView weatherDescription;
    private ImageView descriptionImageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FloatingActionButton fabAddToFavorites;
    FloatingActionButton fabShareItem;

    Boolean fav;

    private static final String API_KEY = "e89024468ba6624bb1ca47053e1aa3e2";

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_view_trip);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        toolbarTitle = findViewById(R.id.tripNameToolbarText);
        destinationNameTextView = findViewById(R.id.destinationName);
        viewTripTypeTextView = findViewById(R.id.viewTripTypeTextView);
        viewTripPriceTextView = findViewById(R.id.viewTripPriceTextView);
        viewTripActualStartDateTextView = findViewById(R.id.viewTripActualStartDateTextView);
        viewTripActualEndDateTextView = findViewById(R.id.viewTripActualEndDateTextView);
        tripImageView = findViewById(R.id.tripImageView);
        tripRatingViewTrip = findViewById(R.id.tripRatingViewTrip);

        locationTemperature = findViewById(R.id.locationTemperature);
        weatherDescription = findViewById(R.id.weatherDescription);
        descriptionImageView = findViewById(R.id.descriptionImageView);

        tripId = getIntent().getExtras().get("tripId").toString();
        destinationNameString = getIntent().getExtras().get("tripDestination").toString();
        toolbarTitleString = getIntent().getExtras().get("tripName").toString();
        tripTypeString = getIntent().getExtras().get("tripType").toString();
        tripPriceString = getIntent().getExtras().get("tripPrice").toString();
        tripStartDateString = getIntent().getExtras().get("startDate").toString().trim();
        tripEndDateString = getIntent().getExtras().get("endDate").toString();
        tripImageString = getIntent().getExtras().get("photo").toString();
        ratingTrip = getIntent().getExtras().getFloat("rating");

        toolbarTitle.setTitle(toolbarTitleString.toString().trim());
        destinationNameTextView.setText(destinationNameString.toString().trim());
        viewTripTypeTextView.setText(tripTypeString.toString().trim());
        viewTripPriceTextView.setText(tripPriceString.toString().trim() + " €");
        viewTripActualStartDateTextView.setText(tripStartDateString);
        viewTripActualEndDateTextView.setText(tripEndDateString.toString().trim());
        tripRatingViewTrip.setRating(ratingTrip);

        fabAddToFavorites = findViewById(R.id.fabAddToFavorites);
        fabShareItem = findViewById(R.id.fabShareItem);

        Glide.with(getApplicationContext()).load(tripImageString).into(tripImageView);

        priceInteger = Integer.parseInt(tripPriceString);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("trips");

        fav = getIntent().getExtras().getBoolean("fav");

        showTemperature();

        printKeyHash();

        final ShareDialog shareDialog = new ShareDialog(this);

        final DatabaseReference dbSelectedTrip = firebaseDatabase.getReference("trips").child(fav.toString());

        if (dbSelectedTrip.toString().contains("true")) {
            fabAddToFavorites.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else if (dbSelectedTrip.toString().contains("false")) {
            fabAddToFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24_blue);
        }


        fabAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddOrRemoveFromFavorites(tripId);
            }
        });

        FloatingActionButton fabDeleteTrip = findViewById(R.id.fabDeleteTrip);
        fabDeleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTrip(tripId);
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivityForResult(intent, 1);
            }
        });

        fabShareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("TO ADD URL"))

                        //TODO

                        .build();

                shareDialog.show(content);
            }
        });
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.mihneacristian.traveljournal", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void onClickAddOrRemoveFromFavorites(String tripId) {

        final DatabaseReference dbSelectedTrip = firebaseDatabase.getReference("trips").child(fav.toString());

        Log.d("Is trip favorite", dbSelectedTrip.toString());

        if (dbSelectedTrip.toString().contains("true")) {
            fabAddToFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24_blue);
            removeFromFavorites(tripId);
        } else {
            fabAddToFavorites.setImageResource(R.drawable.ic_baseline_favorite_24);
            addToFavorites(tripId);
        }
    }

    public void addToFavorites(String tripId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("trips").child(tripId);
        boolean isFavorite = true;
        Trip favTrip = new Trip(tripId, toolbarTitleString, destinationNameString, tripTypeString, priceInteger, ratingTrip, tripStartDateString, tripEndDateString, tripImageString, isFavorite);
        databaseReference.setValue(favTrip);
        Toast.makeText(getApplicationContext(), "Added to Favorites list", Toast.LENGTH_LONG).show();
    }

    public void removeFromFavorites(String tripId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("trips").child(tripId);
        boolean isFavorite = false;
        Trip favTrip = new Trip(tripId, toolbarTitleString, destinationNameString, tripTypeString, priceInteger, ratingTrip, tripStartDateString, tripEndDateString, tripImageString, isFavorite);
        databaseReference.setValue(favTrip);
        Toast.makeText(getApplicationContext(), "Removed from Favorites list", Toast.LENGTH_LONG).show();
    }

    public void deleteTrip(String tripId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("trips").child(tripId);
        databaseReference.removeValue();
        Toast.makeText(getApplicationContext(), "Trip deleted", Toast.LENGTH_LONG).show();
    }

    public void showTemperature() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + destinationNameString + "&appid=" + API_KEY + "&units=Metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    double temperatureDouble = jsonObject.getDouble("temp");
                    String description = object.getString("main");


                    double temperatureRounded = (int) Math.round(temperatureDouble);
                    int temperatureInt = (int) temperatureRounded;
                    String temp = String.valueOf(temperatureInt + "°C");

                    locationTemperature.setText(temp);

                    if (description.contains("Cloud")) {
                        descriptionImageView.setImageResource(R.drawable.ic_cloud);
                        weatherDescription.setText(description);
                    } else if (description.contains("Clear")) {
                        descriptionImageView.setImageResource(R.drawable.ic_sun);
                        weatherDescription.setText(description);
                    } else if (description.contains("Rain")) {
                        descriptionImageView.setImageResource(R.drawable.ic_rain);
                        weatherDescription.setText(description);
                    } else if (description.contains("Snow")) {
                        descriptionImageView.setImageResource(R.drawable.ic_snow);
                        weatherDescription.setText(description);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}