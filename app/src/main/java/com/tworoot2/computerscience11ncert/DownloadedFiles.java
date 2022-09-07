package com.tworoot2.computerscience11ncert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tworoot2.computerscience11ncert.Adapter.PdfAdapter;
import com.tworoot2.computerscience11ncert.Interface.OnPDFSelectListener;
import com.tworoot2.computerscience11ncert.Model.ModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DownloadedFiles extends AppCompatActivity implements OnPDFSelectListener {

    PdfAdapter pdfAdapter;
    List<File> pdfList;
    RecyclerView recyclerView;
    int spanCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_files);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Your Downloads");




        //

        File fileDestination = new File(getExternalFilesDir(null) + "/" + getString(R.string.folderLocation));

        if (!fileDestination.exists()) {
            fileDestination.mkdirs();
        }
//
//        Log.e("check_path", "" + fileDestination.getAbsolutePath());

//        Toast.makeText(DownloadedFiles.this, fileDestination + "", Toast.LENGTH_SHORT).show();

        runtimePermission();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runtimePermission() {
        Dexter.withContext(DownloadedFiles.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displayPDF();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "There was an error : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Dexter", "There was an error: " + error.toString());
            }
        }).check();
    }


    public ArrayList<File> findPDF(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findPDF(singleFile));
            } else {
                if (singleFile.getName().endsWith(".pdf")) {
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    public void displayPDF() {


        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        pdfList = new ArrayList<>();
//        pdfList.addAll(findPDF(Environment.getExternalStoragePublicDirectory("Khan Sir App")));
        pdfList.addAll(findPDF(getApplicationContext().getExternalFilesDir(getString(R.string.folderLocation))));
        pdfAdapter = new PdfAdapter(this, pdfList, this);
        pdfAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(pdfAdapter);


    }




    @Override
    public void onPDFSelected(File file) {

        Intent intent = new Intent(DownloadedFiles.this, PDFActivity.class);
        intent.putExtra("path", file.getAbsolutePath());
        intent.putExtra("flag", "n");
        startActivity(intent);

    }

    @Override
    public void onDelete(File file, int position) {

        File fearFiles = new File(file.getAbsolutePath());
        boolean deleted = fearFiles.delete();


        if (deleted) {
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Error - Please try again", Toast.LENGTH_SHORT).show();
        }

        displayPDF();

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

}