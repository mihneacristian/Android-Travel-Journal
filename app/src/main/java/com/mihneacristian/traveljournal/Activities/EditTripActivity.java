package com.mihneacristian.traveljournal.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mihneacristian.traveljournal.FirebaseDB.Trip;
import com.mihneacristian.traveljournal.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    Boolean fav;

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
    ImageView updateOpenGalleryImageView;
    ImageView updateOpenCameraImageView;

    private String pathToFile;
    private String photoURL;

    FirebaseDatabase firebaseDatabase;
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
        updateOpenGalleryImageView = findViewById(R.id.updateOpenGalleryImageView);
        updateOpenCameraImageView = findViewById(R.id.updateOpenCameraImageView);

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

        fav = getIntent().getExtras().getBoolean("fav");

        showDates();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseTripReference = FirebaseDatabase.getInstance().getReference("trips");
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference dbSelectedTrip = firebaseDatabase.getReference("trips").child(fav.toString());

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

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        updateOpenCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        updateOpenGalleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }

    public void showDates() {
        updateStartDateTextView.setText(tripStartDateString);
        updateEndDateTextView.setText(tripEndDateString);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(pathToFile);
                updateUploadedPhotoImageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Photo URL: " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "TravelJournal_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "Gallery URI:  " + imageFileName);
                updateUploadedPhotoImageView.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "Uploaded Photo URL: " + uri.toString());
                        photoURL = uri.toString();
                    }
                });
                Toast.makeText(EditTripActivity.this, "Photo updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditTripActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TravelJournal_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        pathToFile = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.mihneacristian.traveljournal.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
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

            Trip trip = new Trip(tripId, tripName, destination, radioButtonToStringTripType, priceOfTrip, tripRating, startDate, endDate, url, fav);

            databaseTripReference.child(tripId).setValue(trip);

            Toast.makeText(this, "Trip updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter Trip Name", Toast.LENGTH_SHORT).show();
        }
    }
}

