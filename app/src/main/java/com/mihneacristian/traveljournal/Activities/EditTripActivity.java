package com.mihneacristian.traveljournal.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTripActivity extends AppCompatActivity {

    private String tripId;
    private String tripName;
    private String tripDestination;
    private String radioButtonCheckedOption;
    private String tripStartDateString;
    private String tripEndDateString;
    private String tripImageString;
    private String showPriceEUR;
    private float tripRating;

    private ImageView goToPreviousMenuImageView;
    private TextView updateTripNameEditText;
    private TextView updateDestinationEditText;
    private TextView updateStartDateTextView;
    private TextView updateEndDateTextView;
    private TextView updateShowPriceEUR;
    private RadioGroup updateTripTypeRadioGroup;
    private SeekBar updatePriceSeekBar;
    private RatingBar updateRatingBarTrip;
    private RadioButton updateRadioButtonCityBreak;
    private RadioButton updateRadioButtonSeaSide;
    private RadioButton updateRadioButtonMountain;
    private Button updateStartDateButton;
    private Button updateEndDateButton;
    private CircleImageView updateUploadedPhotoImageView;

    private String pathToFile;
    private String photoURL;

    private StorageReference storageReference;
    private DatabaseReference databaseTripReference;

    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 102;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        goToPreviousMenuImageView = findViewById(R.id.goToPreviousMenuImageView);
        updateTripNameEditText = findViewById(R.id.updateTripNameEditText);
        updateDestinationEditText = findViewById(R.id.updateDestinationEditText);
        updateStartDateTextView = findViewById(R.id.updateStartDateTextView);
        updateEndDateTextView = findViewById(R.id.updateEndDateTextView);
        updateTripTypeRadioGroup = findViewById(R.id.updateTripTypeRadioGroup);
        updatePriceSeekBar = findViewById(R.id.updatePriceSeekBar);
        updateRatingBarTrip = findViewById(R.id.updateRatingBarTrip);
        updateShowPriceEUR = findViewById(R.id.updateShowPriceEUR);
        updateStartDateButton = findViewById(R.id.updateStartDateButton);
        updateEndDateButton = findViewById(R.id.updateEndDateButton);
        updateUploadedPhotoImageView = findViewById(R.id.updateUploadedPhotoImageView);

        tripId = getIntent().getExtras().get("tripId").toString();
        tripName = getIntent().getExtras().get("tripName").toString();
        tripDestination = getIntent().getExtras().get("tripDestination").toString();
        photoURL = getIntent().getExtras().get("photo").toString();
        tripRating = getIntent().getExtras().getFloat("rating");
        tripStartDateString = getIntent().getExtras().get("startDate").toString().trim();
        tripEndDateString = getIntent().getExtras().get("endDate").toString();
        showPriceEUR = getIntent().getExtras().get("tripPrice").toString();

        radioButtonCheckedOption = getIntent().getExtras().get("tripType").toString();
        updateTripNameEditText.setText(tripName.toString().trim());
        updateDestinationEditText.setText(tripDestination.toString().trim());
        updateRatingBarTrip.setRating(tripRating);


        updateShowPriceEUR.setText(showPriceEUR);

        Glide.with(getApplicationContext()).load(photoURL).into(updateUploadedPhotoImageView);

        showDates();

        if (radioButtonCheckedOption.contains("City")) {
            updateTripTypeRadioGroup.check(R.id.updateRadioButtonCityBreak);
        } else if (radioButtonCheckedOption.contains("Sea")) {
            updateTripTypeRadioGroup.check(R.id.updateRadioButtonSeaSide);
        } else {
            updateTripTypeRadioGroup.check(R.id.updateRadioButtonMountain);
        }


        goToPreviousMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });

        int priceInteger = Integer.parseInt(showPriceEUR);
        updatePriceSeekBar.setProgress(priceInteger);

        updatePriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateShowPriceEUR.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void showDates() {
        updateStartDateTextView.setText(tripStartDateString);
        updateEndDateTextView.setText(tripEndDateString);
    }

    public void updateTripOnClick(View view) {
        String tripName = updateTripNameEditText.getText().toString().trim();
        String destination = updateDestinationEditText.getText().toString().trim();

        int selectedIdRadioGroup = updateTripTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButtonSelectedOption = (RadioButton) findViewById(selectedIdRadioGroup);
        String radioButtonToStringTripType = (String) radioButtonSelectedOption.getText();

        int priceOfTrip = updatePriceSeekBar.getProgress();
        float tripRating = updateRatingBarTrip.getRating();

        String startDate = updateStartDateTextView.getText().toString().trim();
        String endDate = updateEndDateTextView.getText().toString().trim();

        String url = photoURL;

        if (!TextUtils.isEmpty(tripName)) {
            String id = databaseTripReference.push().getKey();

            Trip trip = new Trip(id, tripName, destination, radioButtonToStringTripType, priceOfTrip, tripRating, startDate, endDate, url, false);

            databaseTripReference.child(id).setValue(trip);

            Toast.makeText(this, "Trip added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter Trip Name", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPickEndDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateEndDateTextView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();

    }

    public void onClickPickStartDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateStartDateTextView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}

