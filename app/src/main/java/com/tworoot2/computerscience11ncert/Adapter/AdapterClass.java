package com.tworoot2.computerscience11ncert.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.PRDownloader;
import com.downloader.Status;
import com.tworoot2.computerscience11ncert.BuildConfig;
import com.tworoot2.computerscience11ncert.DownloadHandler.DownloadHandler;
import com.tworoot2.computerscience11ncert.Interface.DownloadCompletedListener;
import com.tworoot2.computerscience11ncert.Interface.OnPDFSelectListener;
import com.tworoot2.computerscience11ncert.Interface.PDFDownloadListner;
import com.tworoot2.computerscience11ncert.Model.ModelClass;
import com.tworoot2.computerscience11ncert.PDFActivity;
import com.tworoot2.computerscience11ncert.R;
import com.tworoot2.computerscience11ncert.ViewHolder.ViewHolderClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<ViewHolderClass> {

    Context context;
    private ArrayList<ModelClass> arrayList;
    private List<File> pdfFile;
    PDFDownloadListner listener;
    private OnPDFSelectListener listeners;
    File folderLocations;


    public AdapterClass(Context context, ArrayList<ModelClass> arrayList, PDFDownloadListner listener, File folderLocations, OnPDFSelectListener listeners) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.folderLocations = folderLocations;
        this.listeners = listeners;

    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_materials, parent, false);
        return new ViewHolderClass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        ModelClass currentItem = arrayList.get(holder.getAdapterPosition());

//        File fileDestination = new File(Environment.getExternalStorageDirectory(), "/" + R.string.folderLocation);

        if (!folderLocations.exists()) {
            folderLocations.mkdirs();
        }


        File file = new File(folderLocations + "/" + String.valueOf("Ch-" + currentItem.getChNo()) + ". " + currentItem.getTitle() + ".pdf");

        if (file.exists()) {
            holder.downloadBtn.setVisibility(View.GONE);
        }


        holder.subjectName.setText(currentItem.getTitle());
        holder.subjectHead.setText(currentItem.getChNo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file.exists()) {
                    listeners.onPDFSelected(file);

                } else {

                    Intent intent = new Intent(holder.itemView.getContext(), PDFActivity.class);
                    intent.putExtra("link", currentItem.getLink());
                    intent.putExtra("flag", "y");
                    intent.putExtra("title", currentItem.getTitle());
                    intent.putExtra("chNo", currentItem.getChNo());
                    v.getContext().startActivity(intent);
                }
//                Toast.makeText(context, file + "", Toast.LENGTH_SHORT).show();

            }
        });

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(holder.downloadBtn.getContext(), "Downloading....", Toast.LENGTH_SHORT).show();
                int tof = listener.onDownload(currentItem.getLink(),
                        String.valueOf("Ch-" + currentItem.getChNo()) + ". " + currentItem.getTitle());


//                Toast.makeText(context, status + "", Toast.LENGTH_SHORT).show();

                if (file.exists()) {
                    holder.downloadBtn.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }
}
