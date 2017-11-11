package com.preons.pranav.QRCodeGenerator.utils;

import android.animation.TimeInterpolator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.preons.pranav.QRCodeGenerator.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class c {
    public static final String TAG = "preons";
    public static final int PERMISSION_CAMERA = 1;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyhhmm", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("mmhhyyMMdd", Locale.ENGLISH);
    public static final int IMAGE = 0x53d5;
    // TODO: 15-08-2017 fix interpolator of all activity
    public static final TimeInterpolator DCI = new AccelerateDecelerateInterpolator();
    public static final Interpolator DI = new OvershootInterpolator(1);
    public static final String DSP = "default";

    //EDIT ACTIVITY
    public final static class e {
        public static final int INFO = 0x53d4;
        public static final int BUFFER_SIZE = 1024;
        public static final String vINFO = "text";
        public static final String vPASS = "pass";
        public final static String vTOOLS = "tools";
    }

    public static class i {
        public static final int[] C_IDS = new int[]{
                R.id.name, R.id.num0,// R.id.num1, R.id.num2,
                R.id.url, R.id.email, R.id.address, R.id.note};

        public static final int[] L_IDS = new int[]{
                R.id.lat, R.id.lon, R.id.alt, R.id.address};

        public static final int[] E_IDS = new int[]{
                R.id.email, R.id.sub, R.id.msg};

        public static final int[] S_IDS = new int[]{
                R.id.num, R.id.msg};
    }

    public static class src {
        public static final int[] C_IDS = new int[]{
                R.drawable.ic_person, R.drawable.ic_call,
              //  R.drawable.ic_call, R.drawable.ic_call,
                R.drawable.ic_web, R.drawable.ic_email,
                R.drawable.ic_address, R.drawable.ic_note
        };

        public static final int[] L_IDS = new int[]{
                R.drawable.ic_lat, R.drawable.ic_lon,
                R.drawable.ic_alt, R.drawable.ic_address};

        public static final int[] E_IDS = new int[]{
                R.drawable.ic_email, R.drawable.ic_subject,
                R.drawable.ic_msg};

        public static final int[] S_IDS = new int[]{
                R.drawable.ic_call, R.drawable.ic_msg};
    }

    public synchronized static void setFilter(int i, Drawable... drawables) {
        for (Drawable drawable : drawables)
            drawable.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }



}
