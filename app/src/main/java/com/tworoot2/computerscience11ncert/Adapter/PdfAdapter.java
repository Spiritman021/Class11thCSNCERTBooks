package com.tworoot2.computerscience11ncert.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tworoot2.computerscience11ncert.Interface.DownloadCompletedListener;
import com.tworoot2.computerscience11ncert.Interface.OnPDFSelectListener;
import com.tworoot2.computerscience11ncert.R;
import com.tworoot2.computerscience11ncert.ViewHolder.PdfViewHolder;

import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter <PdfViewHolder> {

    private Context context;
    private List<File> pdfFile;
    private OnPDFSelectListener listener;

    public PdfAdapter(Context context, List<File> pdfFile, OnPDFSelectListener listener) {
        this.context = context;
        this.pdfFile = pdfFile;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_pdf_downloaded,parent,false);
        return new PdfViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder,int position) {

        holder.textView1.setText(pdfFile.get(holder.getAdapterPosition()).getName());
        holder.textView1.setSelected(true);


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPDFSelected(pdfFile.get(holder.getAdapterPosition()));
//                Toast.makeText(context, ""+pdfFile.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(pdfFile.get(holder.getAdapterPosition()),holder.getAdapterPosition());
            }
        });

        holder.openPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.inExternalApp(pdfFile.get(holder.getAdapterPosition()),holder.openPDF.getContext());
            }
        });

        int lastPosition = -1;

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.down_to_up
                        : R.anim.up_to_down);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


    }

    @Override
    public int getItemCount() {
        return pdfFile.size();
    }
}
