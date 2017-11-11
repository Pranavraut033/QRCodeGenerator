package com.preons.pranav.QRCodeGenerator;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.preons.pranav.QRCodeGenerator.Views.STDInformation;
import com.preons.pranav.QRCodeGenerator.utils.setImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pranav.utilities.FirebaseTasks;

import static com.preons.pranav.QRCodeGenerator.utils.CFileHelper.tempImageFile;
import static com.preons.pranav.QRCodeGenerator.utils.StringHandler.P;
import static com.preons.pranav.QRCodeGenerator.utils.StringHandler.URL;
import static com.preons.pranav.QRCodeGenerator.utils.c.IMAGE;

/**
 * Created on 10-03-17 at 22:56 by Pranav Raut.
 * For QRCodeProtection
 */

public class ExtraActivity extends AppCompatActivity {
    public static final String INFOS = "DATA";
    public static final String EXTRA_CONTACT = "view";
    Button done;
    private STDInformation info;
    private String Final = "";
    private Button button;
    private String filePath = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_activity);
        done = findViewById(R.id.done);
        info = findViewById(R.id.stdInfo);
        info.setListener(done::setEnabled);
        done.setOnClickListener(v -> {
            Final = info.getInformation() + filePath;
            setResult(RESULT_OK,
                    new Intent().putExtra(INFOS, Final));
            supportFinishAfterTransition();
        });
        new Handler().postDelayed(() -> info.setTranslationY(-done.getHeight() / 2), 50);
        button = findViewById(R.id.getImage);
        button.setOnClickListener(v -> pickImage());


    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        info.setAlpha(0);
        info.animate().alpha(1).setStartDelay(50).translationY(0)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (info.getTranslationY() != 0)
                            info.setTranslationY(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Uploading", Toast.LENGTH_SHORT).show();

                    final FirebaseTasks tasks = new FirebaseTasks();
                    InputStream inputStream = null;
                    try {
                        inputStream = getApplicationContext()
                                .getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final ImageView imageView = findViewById(R.id.profile);

                    final File file = tempImageFile(inputStream);
                    //new CFileHelper(this).scanMedia(file);
                    tasks.setUploadListener(new FirebaseTasks.UploadListener() {
                        ProgressBar progressBar = findViewById(R.id.progress_up);

                        {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getApplicationContext(), "Uploaded successful", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            imageView.animate().alpha(1f);
                            filePath = P + URL + tasks.getFilePath();
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setOnClickListener(v -> tasks.getUploadTask().resume());
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Tap To retry (" + e.getMessage() + ")", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onSuccess(@Nullable UploadTask.TaskSnapshot taskSnapshot) {
                        }

                        @Override
                        public void onProgress(Double percentage) {
                            Log.v("progress", String.valueOf(percentage));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                progressBar.setProgress((int) Math.round(percentage), true);
                            else
                                progressBar.setProgress((int) Math.round(percentage));

                        }
                    });
                    if (file != null) {
                        tasks.uploadFile(file);
                        if (file.exists()) {
                            setImage image = new setImage(this);
                            imageView.setImageBitmap(image.decodeSampledBitmap(file.getPath(), .35f));
                            button.setText(file.getPath());
                        }
                    }
                }
                break;
        }
    }


    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE);
    }
}
