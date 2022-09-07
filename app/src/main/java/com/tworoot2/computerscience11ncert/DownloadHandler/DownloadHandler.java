package com.tworoot2.computerscience11ncert.DownloadHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.tworoot2.computerscience11ncert.DownloadedFiles;
import com.tworoot2.computerscience11ncert.Interface.DownloadCompletedListener;
import com.tworoot2.computerscience11ncert.MainActivity;
import com.tworoot2.computerscience11ncert.R;
import com.tworoot2.computerscience11ncert.ViewHolder.ViewHolderClass;

import java.io.File;

public class DownloadHandler {

    AlertDialog.Builder alertDialog;
    AlertDialog.Builder failed;
    ProgressDialog progressDialog;
    File fileDestination;
    DownloadCompletedListener downloadCompletedListener;
    static int[] tof = {0};


    public int downloadFile(String url, String fileName, Context context, File filePath) {


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading....");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Downloaded successfully");
        alertDialog.setMessage(fileName + " is downloaded successfully");
        alertDialog.setIcon(R.drawable.done);
        alertDialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, DownloadedFiles.class);
                context.startActivity(intent);
            }
        });

        failed = new AlertDialog.Builder(context);
        failed.setTitle("Downloading failed");
        failed.setMessage("Your file is not downloaded successfully");
        failed.setIcon(R.drawable.ic_baseline_error_24);

//        File file = new File(Environment.getExternalStorageDirectory(), "/Khan Sir App");
//        File file = new File(getExternalFilesDir(null) + "/" + "Khan Sir App");


        int downloadID = PRDownloader.download(url, filePath.getPath(), fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long per = progress.currentBytes * 100 / progress.totalBytes;
                        progressDialog.setMessage("Downloading : " + per + " %");


                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

//                        Intent i = new Intent(context,MainActivity.class).putExtra("page","d")
//                                .putExtra("sTitle",fileName);
//                        context.startActivity(i);
//                        ((Activity)context).finish();
//                        ((Activity) context).overridePendingTransition(R.anim.no_animation, R.anim.no_animation);

//                        ViewHolderClass.downloadBtn.setVisibility(View.GONE);

                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();


                        progressDialog.dismiss();
                        alertDialog.show();
                        tof[0] = 1;



                    }


                    @Override
                    public void onError(Error error) {


                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        failed.show();
                        progressDialog.dismiss();
                        tof[0] = 0;
                    }


                });
//        Toast.makeText(context, "Download completed " + tof[0], Toast.LENGTH_SHORT).show();

        return tof[0];


    }


}
