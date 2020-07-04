package com.mihneacristian.traveljournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView signOutImageView;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorites, R.id.nav_share, R.id.nav_about_us, R.id.nav_contact)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void sendEmailOnClick(MenuItem menuItem) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"popamihneacristian@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion or Issue");
        intent.setType("text/plain");
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(Intent.createChooser(intent, "Send Email using:"));
        } else {
            Toast.makeText(this, "You don't have any email apps to contact us", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignOut(View view) {
        signOutImageView = (ImageView) findViewById(R.id.sign_out_ImageView);
        signOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        TextView userEmailAddressTextView = headerView.findViewById(R.id.userEmailAddressTextView);
        ImageView userProfilePicture = headerView.findViewById(R.id.imageView);

        userNameTextView.setText(currentUser.getDisplayName());
        userEmailAddressTextView.setText(currentUser.getEmail());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(userProfilePicture);

    }
}