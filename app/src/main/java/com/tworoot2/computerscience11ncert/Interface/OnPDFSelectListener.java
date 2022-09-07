package com.tworoot2.computerscience11ncert.Interface;

import android.content.Context;

import java.io.File;

public interface OnPDFSelectListener {
    void onPDFSelected(File file);
    void onDelete(File file, int position);
    void inExternalApp(File file, Context context);

}
