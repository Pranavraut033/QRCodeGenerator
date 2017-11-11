package com.preons.pranav.QRCodeGenerator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.preons.pranav.QRCodeGenerator.ChoiceActivity;
import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.ScannerActivity;

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
        this(context, toolbar, null);
    }

    public InitNav(final Context context, @Nullable Toolbar toolbar, @Nullable SharedPreferences sharedPreferences) {
        this.context = context;
        drawer = ((Activity) context).findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(
                (Activity) context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        if (sharedPreferences != null && sharedPreferences.getBoolean("drawer", true)) {
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
                // TODO: 24-08-2017 share apk
                break;
            case R.id.rate:
                // TODO: 24-08-2017 link to play store
                break;
        }

        return false;
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
