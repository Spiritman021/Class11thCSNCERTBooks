package com.tworoot2.computerscience11ncert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.tworoot2.computerscience11ncert.DownloadHandler.DownloadHandler;
import com.tworoot2.computerscience11ncert.Interface.PDFDownloadListner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PDFActivity extends AppCompatActivity {

    PDFView pdfView;
    String filePath = null;
    String fileLink = null;
    String flag = null;
    String title = null;
    String chNo = null;
    ProgressDialog progressDialog;
    DownloadHandler downloadHandler;
    File file;
    String time;
    String shortTime;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        pdfView = (PDFView) findViewById(R.id.pdfView);
        filePath = getIntent().getStringExtra("path");
        fileLink = getIntent().getStringExtra("link");
        flag = getIntent().getStringExtra("flag");
        title = getIntent().getStringExtra("title");
        chNo = getIntent().getStringExtra("chNo");

        downloadHandler = new DownloadHandler();
        file = new File(getExternalFilesDir(null) + "/" + getString(R.string.folderLocation));
        time = String.valueOf(System.currentTimeMillis());
        shortTime = time.substring(8,12);
        fileName = "Ch-" + chNo + ". " + title;




        if (flag.equals("y")) {
            progressDialog();
            new RetrivePDFfromUrl().execute(fileLink);
            setTitle(title);

        }

        if (flag.equals("n")) {
            File file = new File(filePath);
            Uri path = Uri.fromFile(file);
            pdfView.fromUri(path).scrollHandle(new DefaultScrollHandle(PDFActivity.this)).load();
        }


    }

    private void progressDialog() {
        progressDialog = new ProgressDialog(PDFActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        progressDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        if (item.getItemId() == R.id.download) {

            Toast.makeText(getApplicationContext(), "Downloading....", Toast.LENGTH_SHORT).show();
            downloadHandler.downloadFile(fileLink,fileName+".pdf",PDFActivity.this,file);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (flag.equals("y")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_download, menu);
        }
        return true;
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Integer, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressDialog.dismiss();
                }
            }).scrollHandle(new DefaultScrollHandle(PDFActivity.this)).load();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }
    }

}