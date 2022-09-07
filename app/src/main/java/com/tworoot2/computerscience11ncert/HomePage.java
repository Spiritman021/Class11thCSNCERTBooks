package com.tworoot2.computerscience11ncert;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tworoot2.computerscience11ncert.Model.ModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {

    LinearLayout studyMaterial, download, rate, report;
    public DrawerLayout drawerLayout;
    boolean doubleBackToExitPressedOnce = false;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView logoClicked;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Computer Science");

        myDrawerLayout();

        logoClicked = (ImageView) findViewById(R.id.logoClicked);
        logoClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this, "This App is Powered by twoRoot2", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        studyMaterial = (LinearLayout) findViewById(R.id.studyMaterial);

        studyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(HomePage.this, MainActivity.class).putExtra("page","y"));
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }
        });

        download = (LinearLayout) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DownloadedFiles.class);
                startActivity(intent);
            }
        });

        rate = (LinearLayout) findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this, "Rate your work by 5 star", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent);
            }
        });

        report = (LinearLayout) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailsubject = "Facing a problem in [ CS Class 11th NCERT ]";
                String emailbody =
                        "Enter your problem : " + " " + "\n" + "\n" + "\n" +
                                "MODEL: " + Build.MODEL + "\n" +
                                "ID: " + Build.ID + "\n" +
                                "type: " + Build.TYPE + "\n" +
                                "user: " + Build.USER + "\n" +
                                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                                "INCREMENTAL " + Build.VERSION.INCREMENTAL + "\n" +
                                "SDK  " + Build.VERSION.SDK + "\n" +
                                "BOARD: " + Build.BOARD + "\n" +
                                "BRAND " + Build.BRAND + "\n" +
                                "Version Code: " + Build.VERSION.RELEASE;

                Intent mail = new Intent(Intent.ACTION_SENDTO);
                mail.setAction(Intent.ACTION_VIEW);
                mail.setData(Uri.parse("mailto:notestroll@gmail.com" + "?subject="
                        + Uri.encode(emailsubject) + "&body=" + Uri.encode(emailbody)));
                mail.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                mail.putExtra(Intent.EXTRA_TEXT, emailbody);
                startActivity(mail);

            }
        });


        CheckForUpdate();
        notificationSender();

    }

    private void myDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        NavigationView navView = (NavigationView) findViewById(R.id.navigation);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.nav_home: {
                        Toast.makeText(HomePage.this, "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.nav_download: {
                        Toast.makeText(HomePage.this, "Your downloaded files", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DownloadedFiles.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.nav_privacy: {
                        Toast.makeText(HomePage.this, "Privacy policy", Toast.LENGTH_SHORT).show();
                        setPrivacy();
                        return true;
                    }
                    case R.id.nav_contact: {
                        Toast.makeText(HomePage.this, "Contact us", Toast.LENGTH_SHORT).show();

                        String emailsubject = "Hey! I am the user of [ CS Class 11th NCERT Application ]";
                        String emailbody = "Your views : ";

                        Intent mail = new Intent(Intent.ACTION_SENDTO);
                        mail.setAction(Intent.ACTION_VIEW);
                        mail.setData(Uri.parse("mailto:notestroll@gmail.com" + "?subject="
                                + Uri.encode(emailsubject) + "&body=" + Uri.encode(emailbody)));
                        mail.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                        mail.putExtra(Intent.EXTRA_TEXT, emailbody);
                        startActivity(mail);
                        return true;
                    }
//
                    case R.id.nav_rate: {
                        Toast.makeText(HomePage.this, "Rate your work by 5 star", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                        startActivity(intent);
                        return true;
                    }

                }
                return false;
            }
        });

//        navView.setItemIconTintList(null);


        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPrivacy() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                HomePage.this).create();
        alertDialog.setTitle("Privacy policy");
        alertDialog.setMessage(getString(R.string.p_message));
        alertDialog.setIcon(R.drawable.tworoot2);
        alertDialog.setButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void notificationSender() {

        FirebaseMessaging.getInstance().subscribeToTopic("twoRoot2")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });

    }


    private void CheckForUpdate() {
        try {
            String version = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("version").child("app").child("v2");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String versionName = (String) dataSnapshot.getValue();

                    if (!versionName.equals(version)) {


                        Dialog dialog = new Dialog(HomePage.this);
                        dialog.setContentView(R.layout.update_ui);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent1);
                            }
                        });
                        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Toast.makeText(HomePage.this, "Please update your app as soon as possible, you are loosing lots of thing without this update", Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}