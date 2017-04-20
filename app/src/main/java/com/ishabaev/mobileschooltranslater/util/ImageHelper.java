package com.ishabaev.mobileschooltranslater.util;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

public class ImageHelper {

    public static void tintImage(@NonNull ImageView iv, int color) {
        iv.setColorFilter(ContextCompat.getColor(iv.getContext(), color));
    }

}
