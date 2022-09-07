package com.tworoot2.computerscience11ncert.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tworoot2.computerscience11ncert.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView textView1;
    public LinearLayout container;
    public TextView delete;
    public TextView openPDF;


    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.textView1);
        container = itemView.findViewById(R.id.container);
        delete = itemView.findViewById(R.id.delete);
        openPDF = itemView.findViewById(R.id.openPDF);

    }
}
