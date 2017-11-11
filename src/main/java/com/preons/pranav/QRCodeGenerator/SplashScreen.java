package com.preons.pranav.QRCodeGenerator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static pranav.utilities.Animations.USER_FRIENDLY_DELAY;
import static pranav.utilities.Animations.animateAlpha;

public class SplashScreen extends AppCompatActivity {
    static final String TAG = "Preons";

    private static final int UI_ANIMATION_DELAY = 300;
    private static boolean AUTO_HIDE = true;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private View mControlsView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            animateAlpha(mControlsView);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 150);
    }

    private void toggle() {
        if (mVisible) hide();
        else show();
    }

    private void hide() {
        mVisible = false;
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mVisible = true;
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        animateAlpha(mControlsView);
        if (AUTO_HIDE) {
            mHideHandler.removeCallbacks(mHidePart2Runnable);
            mHideHandler.postDelayed(mHidePart2Runnable, USER_FRIENDLY_DELAY);
        }
    }

    public void open(View view1) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}