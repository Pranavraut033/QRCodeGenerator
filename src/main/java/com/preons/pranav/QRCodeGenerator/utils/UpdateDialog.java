package com.preons.pranav.QRCodeGenerator.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created on 11-11-2017 at 22:33 by Pranav Raut.
 * For QRCodeProtection
 */

public class UpdateDialog extends Activity {
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(c.DSP, 0);
        String s = getIntent().getStringExtra("type");
        if (s != null)
            if (s.equals("update")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(sharedPreferences.getString("title", ""));
                dialog.setMessage(sharedPreferences.getString("body", ""));
                dialog.setNeutralButton("later", (dialog1, which) -> {
                    finish();
                    //TODO ADD to shared preference
                }).setPositiveButton("update", (dialog1, which) -> {
                    String appPackageName = getPackageName();
                    finish();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                });
                dialog.show();
            }
    }
}
