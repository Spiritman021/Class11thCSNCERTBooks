package com.tworoot2.computerscience11ncert.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tworoot2.computerscience11ncert.R;

public class ViewHolderClass extends RecyclerView.ViewHolder {
    public TextView subjectName, subjectHead;
    public LinearLayout downloadBtn;


    public ViewHolderClass(View itemView) {
        super(itemView);
        subjectHead = (TextView) itemView.findViewById(R.id.subjectHead);
        subjectName = (TextView) itemView.findViewById(R.id.subjectName);
        downloadBtn = (LinearLayout) itemView.findViewById(R.id.downloadBtn);


    }
}
