package com.mihneacristian.traveljournal.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.mihneacristian.traveljournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewTripActivity extends AppCompatActivity {

    private String destinationNameString;
    private String tripTypeString;
    private String toolbarTitleString;
    private String tripPriceString;
    private String tripStartDateString;
    private String tripEndDateString;
    private String tripImageString;
    private float ratingTrip;

    private Toolbar toolbarTitle;
    private TextView destinationNameTextView;
    private TextView viewTripTypeTextView;
    private TextView viewTripPriceTextView;
    private TextView viewTripActualStartDateTextView;
    private TextView viewTripActualEndDateTextView;
    private ImageView tripImageView;
    private RatingBar tripRatingViewTrip;

    private TextView locationTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        toolbarTitle = findViewById(R.id.tripNameToolbarText);
        destinationNameTextView = findViewById(R.id.destinationName);
        viewTripTypeTextView = findViewById(R.id.viewTripTypeTextView);
        viewTripPriceTextView = findViewById(R.id.viewTripPriceTextView);
        viewTripActualStartDateTextView = findViewById(R.id.viewTripActualStartDateTextView);
        viewTripActualEndDateTextView = findViewById(R.id.viewTripActualEndDateTextView);
        tripImageView = findViewById(R.id.tripImageView);
        tripRatingViewTrip = findViewById(R.id.tripRatingViewTrip);

        locationTemperature = findViewById(R.id.locationTemperature);

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
        Glide.with(getApplicationContext()).load(tripImageString).into(tripImageView);

        showTemperature();
    }

    public void showTemperature() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + destinationNameString + "&appid=e89024468ba6624bb1ca47053e1aa3e2&units=Metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);

                    double temperatureDouble = jsonObject.getDouble("temp");
                    double temperature = (int) Math.round(temperatureDouble);
                    int temperatureInt = (int) temperature;
                    String temp = String.valueOf(temperatureInt + "°C");

                    locationTemperature.setText(temp);

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