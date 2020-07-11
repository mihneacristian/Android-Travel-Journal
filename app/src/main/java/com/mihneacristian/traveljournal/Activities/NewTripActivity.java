package com.mihneacristian.traveljournal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.mihneacristian.traveljournal.LogInActivity;
import com.mihneacristian.traveljournal.MainScreen;
import com.mihneacristian.traveljournal.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class NewTripActivity extends AppCompatActivity {

    ImageView returnToHomeImageView;
    ImageView openCameraImageView;
    ImageView openGalleryImageView;
    String pathToFile;
    private Uri photoUri;

    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int GALLERY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        openCameraImageView = findViewById(R.id.openCameraImageView);
        openGalleryImageView = findViewById(R.id.openGalleryImageView);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        openCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photoFile = null;
                try {
                    photoFile = createPhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoUri = FileProvider.getUriForFile(NewTripActivity.this, "com.mihneacristian.traveljournal.fileprovider", photoFile);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        openGalleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent selectIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                selectIntent.setType("image/*");

                Intent chooseImageIntent = Intent.createChooser(getIntent(), "Select image");
                chooseImageIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {selectIntent});

                startActivityForResult(chooseImageIntent, GALLERY_REQUEST_CODE);


            }
        });

    }

    private File createPhoto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "travelJournal_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        pathToFile = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri image = data.getData();
                String[] photoPath = {MediaStore.Images.Media.DATA};

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Cursor cursor = getContentResolver().query(image, photoPath, null, null);
                    cursor.moveToFirst();

                    int index = cursor.getColumnIndex(photoPath[0]);
                    pathToFile = cursor.getString(index);
                    cursor.close();
                }
            }
        }
    }


    public void onClickReturnToHome(View view) {
        returnToHomeImageView = (ImageView) findViewById(R.id.previousMenuImageView);
        returnToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
    }
}