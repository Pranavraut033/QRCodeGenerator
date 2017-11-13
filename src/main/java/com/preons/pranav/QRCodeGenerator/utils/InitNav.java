package com.preons.pranav.QRCodeGenerator.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.preons.pranav.QRCodeGenerator.BuildConfig;
import com.preons.pranav.QRCodeGenerator.ChoiceActivity;
import com.preons.pranav.QRCodeGenerator.DonationsActivity;
import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.ScannerActivity;

import java.io.File;
import java.io.IOException;

import static com.preons.pranav.QRCodeGenerator.HomeActivity.REQUEST_INVITE;
import static com.preons.pranav.QRCodeGenerator.utils.CFileHelper.copyFile;
import static com.preons.pranav.QRCodeGenerator.utils.c.TAG;

/**
 * Created on 24-08-2017 at 19:40 by Pranav Raut.
 * For QRCodeProtection
 */

public class InitNav implements NavigationView.OnNavigationItemSelectedListener {

    private final Context context;
    private final ActionBarDrawerToggle toggle;
    private final DrawerLayout drawer;

    public InitNav(Context context) {
        this(context, null);
    }

    public InitNav(Context context, @Nullable Toolbar toolbar) {
        this(context, toolbar, null, true);
    }

    public InitNav(final Context context, @Nullable Toolbar toolbar, @Nullable SharedPreferences sharedPreferences, boolean toShow) {
        this.context = context;
        drawer = ((Activity) context).findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(
                (Activity) context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        if (sharedPreferences != null && sharedPreferences.getBoolean("drawer", true)) {
            if (toShow)
                drawer.openDrawer(Gravity.START, true);
            sharedPreferences.edit().putBoolean("drawer", false).apply();
        }

        toggle.syncState();
        NavigationView navigationView = ((Activity) context).findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        if (sharedPreferences != null) sharedPreferences.edit().apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create:
                context.startActivity(new Intent(context, ChoiceActivity.class));
                break;
            case R.id.scan:
                context.startActivity(new Intent(context, ScannerActivity.class));
                break;
            case R.id.share:
                shareApplication();
                break;
            case R.id.rate:
                String appPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.support:
                context.startActivity(new Intent(context, DonationsActivity.class));
                break;
        }

        return false;
    }

    private void shareApplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Send");
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.select_send, null, false);
        builder.setView(v);
        AlertDialog alertDialog = builder.create();
        v.findViewById(R.id.link).setOnClickListener(v1 -> {
            Intent intent = new AppInviteInvitation.IntentBuilder(context.getString(R.string.invitation_title))
                    .setMessage(context.getString(R.string.invitation_message))
                    //.setDeepLink(Uri.parse(context.getString(R.string.invitation_deep_link)))
                    //.setCustomImage(Uri.parse(context.getString(R.string.invitation_custom_image)))
                    .build();
            ((Activity) context).startActivityForResult(intent, REQUEST_INVITE);
        });
        v.findViewById(R.id.apk).setOnClickListener(v1 -> {
            alertDialog.dismiss();
            File originalApk = new File(context.getApplicationContext().getApplicationInfo().sourceDir);

            Intent intent = new Intent(Intent.ACTION_SEND);

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.setType("*/*");
            //intent.setType("application/vnd.android.package-archive");

            try {
                File tempFile = new File(context.getExternalCacheDir() + "/temp");

                if (!tempFile.isDirectory() && !tempFile.mkdirs()) return;

                tempFile = new File(tempFile.getPath() + "/" + context.getString(R.string.app_name).replace(" ", "_") + ".apk");

                if (!tempFile.exists() && !tempFile.createNewFile()) return;

                copyFile(originalApk, tempFile);
                System.out.println("File copied.");
                //Open share dialog'
                Log.d(TAG, "shareApplication: " + BuildConfig.APPLICATION_ID);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider", tempFile));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, "Share app via"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alertDialog.show();
    }

    public void setIndicatorEnable(boolean b) {
        toggle.setDrawerIndicatorEnabled(b);
    }

    public Drawable getDrawable() {
        return toggle.getDrawerArrowDrawable();
    }

    public void setBackground(int color) {
        drawer.setBackgroundColor(color);
    }
}
