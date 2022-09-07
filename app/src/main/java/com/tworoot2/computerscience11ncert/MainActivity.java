package com.tworoot2.computerscience11ncert;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.downloader.PRDownloader;
import com.google.android.material.navigation.NavigationView;
import com.tworoot2.computerscience11ncert.Adapter.AdapterClass;
import com.tworoot2.computerscience11ncert.DownloadHandler.DownloadHandler;
import com.tworoot2.computerscience11ncert.Interface.OnPDFSelectListener;
import com.tworoot2.computerscience11ncert.Interface.PDFDownloadListner;
import com.tworoot2.computerscience11ncert.Model.ModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PDFDownloadListner, OnPDFSelectListener {

    JSONArray jsonArray;
    ArrayList<ModelClass> studyMaterialsModelArrayList;
    AdapterClass studyMaterialsAdapter;
    public static RecyclerView recV;
    ProgressDialog progressDialog;
    DownloadHandler downloadHandler;
    File file;
    File fileDestination;
    String MyPREFERENCES = "myPref";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Chapter Wise");

//        String info = getIntent().getStringExtra("page");

//        if (info.equals("d")) {
//
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//            alertDialog.setTitle("Downloaded successfully");
//            alertDialog.setMessage(getIntent().getStringExtra("sTitle") + " is downloaded successfully");
//            alertDialog.setIcon(R.drawable.done);
//            alertDialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent intent = new Intent(MainActivity.this, DownloadedFiles.class);
//                    startActivity(intent);
//                }
//            });
//            alertDialog.show();
//
//        }

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        downloadHandler = new DownloadHandler();
        file = new File(getExternalFilesDir(null) + "/" + getString(R.string.folderLocation));

        fileDestination = new File(Environment.getExternalStorageDirectory(), "/" + getString(R.string.folderLocation));

        if (!file.exists()) {
            file.mkdirs();
        }

//        Toast.makeText(MainActivity.this, ""+fileDestination, Toast.LENGTH_SHORT).show();

        PRDownloader.initialize(getApplicationContext());


        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recV = (RecyclerView) findViewById(R.id.recView);
        recV.setHasFixedSize(true);
        recV.setLayoutManager(layoutManager);

        studyMaterialsModelArrayList = new ArrayList<>();
        studyMaterialsAdapter = new AdapterClass(MainActivity.this, studyMaterialsModelArrayList, this, file, this);
        recV.setAdapter(studyMaterialsAdapter);
        fetchData1();

    }


    private void fetchData1() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://tworoot2.github.io/NCERT_Books_API/NCERT%20API/Class%2011th/CS.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {

                JSONArray studyM = null;

                try {
                    studyM = response.getJSONArray("studyMaterialCS");

                    editor.putString("json", studyM.toString());
                    editor.commit();

//                    studyMaterialsModelArrayList.clear();
//
//
//                    for (int i = 0; i < studyM.length(); i++) {
//
//
//                        JSONObject stateWise = studyM.getJSONObject(i);
//
//                        String Link = stateWise.getString("Link");
//                        String Title = stateWise.getString("Title");
//                        String chNo = stateWise.getString("chNo");
//
//
//                        ModelClass stateC = new ModelClass(Link, Title, chNo);
//
//                        studyMaterialsModelArrayList.add(stateC);
//                        studyMaterialsAdapter.notifyDataSetChanged();
//
//
//                        if (studyMaterialsAdapter.getItemCount() != 0) {
//                            progressDialog.cancel();
//                        }}

//                    Toast.makeText(MainActivity.this, sharedpreferences.getString("json", null), Toast.LENGTH_SHORT).show();


                    loadRecView();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadRecView();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(jsonObjectRequest);

    }

    private void loadRecView() {

        try {
            jsonArray = new JSONArray(sharedpreferences.getString("json", null));

        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "DidNot Parsed", Toast.LENGTH_SHORT).show();
        }



        try {
            studyMaterialsModelArrayList.clear();


            for (int i = 0; i < jsonArray.length(); i++) {


            JSONObject stateWise = jsonArray.getJSONObject(i);

            String Link = stateWise.getString("Link");
            String Title = stateWise.getString("Title");
            String chNo = stateWise.getString("chNo");


            ModelClass stateC = new ModelClass(Link, Title, chNo);

            studyMaterialsModelArrayList.add(stateC);
            studyMaterialsAdapter.notifyDataSetChanged();


            if (studyMaterialsAdapter.getItemCount() != 0) {
                progressDialog.cancel();
            }}
        } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        if (item.getItemId() == R.id.notice) {

            Toast.makeText(MainActivity.this, "Please read carefully", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    MainActivity.this).create();
            alertDialog.setTitle("Note that");
            alertDialog.setMessage(getString(R.string.note));
            alertDialog.setIcon(R.drawable.rating);
            alertDialog.setButton("Got it", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public int onDownload(String url, String title) {
        String time = String.valueOf(System.currentTimeMillis());
        String shortTime = time.substring(8, 12);
//        String fileName = title + " [" + "twoRoot2-" + shortTime + "]";
        String fileName = title;

//        downloadFile(url, fileName  + ".pdf");

        Toast.makeText(getApplicationContext(), "Downloading....", Toast.LENGTH_SHORT).show();
        int tof = downloadHandler.downloadFile(url, fileName + ".pdf", MainActivity.this, file);

        return tof;

    }

    @Override
    public void onPDFSelected(File file) {
        Intent intent = new Intent(MainActivity.this, PDFActivity.class);
        intent.putExtra("path", file.getAbsolutePath());
        intent.putExtra("flag", "n");
        startActivity(intent);
    }

    @Override
    public void onDelete(File file, int position) {

    }

    @Override
    public void inExternalApp(File file, Context context) {
        try {


            Intent target = new Intent(Intent.ACTION_VIEW);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "*/*");


                if (file.getName().endsWith(".pdf") || file.getName().endsWith(".pptx")) {
                    target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
                }
                if (file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")|| file.getName().endsWith(".jpg")) {
                    target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");
                }
                if (file.getName().endsWith(".docx") || file.getName().endsWith(".docs") || file.getName().endsWith(".txt")) {
                    target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "text/plain");
                }
                if (file.getName().endsWith(".sce")) {
                    target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "*/*");
                }


//                target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
//                target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "*/*");
//                target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");
//                Toast.makeText(context, "SDK N : "+FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), Toast.LENGTH_SHORT).show();

            } else {
                target.setDataAndType(Uri.fromFile(file), "*/*");
//                target.setDataAndType(Uri.fromFile(file), "image/*");
//                target.setDataAndType(Uri.fromFile(file), "application/pdf");
//                Toast.makeText(context, "SDK !N", Toast.LENGTH_SHORT).show();

            }

            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {

                Toast.makeText(context, "No apk found to open this file", Toast.LENGTH_SHORT).show();
                // Instruct the user to install a PDF reader here, or something
            }
        } catch (Exception e) {
            Log.d("ErrorGot", e.getMessage());
        }
    }

//    @Override
//    public void onBackPressed() {
//
//        if (getIntent().getStringExtra("page").equals("d")) {
//            overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
//            finishActivity(123);
//        }
//
//        super.onBackPressed();
//    }
}