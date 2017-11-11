package com.preons.pranav.QRCodeGenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.zxing.Result;
import com.preons.pranav.QRCodeGenerator.utils.StringHandler;
import com.preons.pranav.QRCodeGenerator.utils.setImage;

import java.io.File;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pranav.utilities.Animations;
import pranav.utilities.FirebaseTasks;
import pranav.views.L1;
import pranav.utilities.Utilities;

import static com.preons.pranav.QRCodeGenerator.utils.StringHandler.URL;
import static com.preons.pranav.QRCodeGenerator.utils.StringHandler.makeToast;
import static com.preons.pranav.QRCodeGenerator.utils.StringHandler.setClipboard;
import static com.preons.pranav.QRCodeGenerator.utils.c.PERMISSION_CAMERA;
import static pranav.utilities.Utilities.Colors.changeAlpha;
import static pranav.utilities.Utilities.hasPermissions;

@SuppressWarnings("deprecation")
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private AlertDialog dialog1;
    private StringHandler handler;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!hasPermissions(this, android.Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    PERMISSION_CAMERA);

        setContentView(R.layout.scanner);
        mScannerView = findViewById(R.id.scn_view);
        mScannerView.setAutoFocus(true);
        mScannerView.setOnClickListener(v -> mScannerView.toggleFlash());
        Utilities.Resources res = new Utilities.Resources(this);
        mScannerView.setLaserEnabled(false);
        mScannerView.setBorderColor(Color.WHITE);
        mScannerView.setBorderCornerRadius((int) res.getDimen(R.dimen.pad3dp));
        mScannerView.setMaskColor(0x50212121);
        mScannerView.setBorderLineLength((int) res.getDimen(R.dimen.pad48dp));
        f();
    }

    public void f() {
        final TextView textView = findViewById(R.id.text);
        textView.setVisibility(hasPermissions(this, android.Manifest.permission.CAMERA) ? View.VISIBLE : View.GONE);
        final int i = textView.getCurrentTextColor();
        final Animations.AnimatingColor color = new Animations.AnimatingColor();
        color.setColorChangeListener(textView::setTextColor)
                .addListener((L1.EndAnimation) animation -> color.start(i, changeAlpha(i, .3f)));
        color.setDuration(750);
        color.start(changeAlpha(i, .3f), i);
        color.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasPermissions(this, android.Manifest.permission.CAMERA))
            recreate();
        else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @SuppressLint("InflateParams")
    @Override
    public void handleResult(Result rawResult) {
        final ZXingScannerView.ResultHandler resultHandler = this;
        final String[] inputString = {rawResult.getText()};
        handler = new StringHandler(inputString[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Spanned[] s = new Spanned[1];
        if (handler.isPassword()) {
            final boolean std = handler.containSTD(handler.isPassword());
            final boolean url = handler.containsURL();
            builder.setTitle("Information is password Protected");
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog_view, null);
            final TextInputEditText textInputEditText = view.findViewById(R.id.inputPassword_d);
            final TextInputLayout textInputLayout = view.findViewById(R.id.inputPasswordLayout);
            final TextView textView = view.findViewById(R.id.result);
            final Button button = view.findViewById(R.id.submit);
            Button button1 = view.findViewById(R.id.dDismiss);
            final Button button2 = view.findViewById(R.id.copy);
            builder.setView(null);
            builder.setView(view);
            button.setOnClickListener(v -> {
                String s1 = textInputEditText.getText().toString();
                if (s1.isEmpty()) {
                    makeToast(getApplicationContext(), textView);
                    return;
                }
                boolean correctPass = handler.isCorrect(s1);
                if (correctPass) {
                    handler.replacePassword();
                    handler.interchange(handler.getCurrentString(), StringHandler.FINAL);
                    handler.decrypt();
                    if (std) handler.replaceSTD();
                    if (url) handler.replaceURL();
                    if (std) {
                        handler.getAllInfo(StringHandler.FINAL);
                        //TODO: ADD OPTION
                        inputString[0] = handler.getAllForText();
                        handler.interchange(inputString[0], StringHandler.FINAL);
                    }
                    s[0] = Html.fromHtml(handler.getFinalText());
                    textView.setText(s[0]);
                    if (url) download(view, R.id.profile, R.id.progress_up);
                    textInputLayout.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), StringHandler.wrongPass, Toast.LENGTH_LONG).show();
                    dialog1.dismiss();
                }
            });
            button2.setOnClickListener(v -> copy(s[0]));
            button1.setOnClickListener(v -> dialog1.dismiss());
        } else {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Scan Result");
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.without_pass_dv, null);
            builder.setView(null);
            builder.setView(view);
            TextView textView = view.findViewById(R.id.info);
            textView.setText(s[0]);
            if (handler.containSTD(handler.isPassword())) {
                handler.replaceSTD();
                handler.getAllInfo(StringHandler.INITIAL);
                //TODO: ADD OPTION
                inputString[0] = handler.getAllForText();
            }
            s[0] = Html.fromHtml(inputString[0]);
            builder.setNegativeButton("Copy", (dialog, which) -> copy(s[0]))
                    .setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss());

            boolean url = handler.containsURL();
            if (url) {
                handler.replaceURL();
                download(view, R.id.profilea, R.id.progress_upa);
            }
            textView.setText(s[0]);
        }
        dialog1 = builder.create();
        dialog1.show();
        dialog1.setOnDismissListener(dialog -> mScannerView.resumeCameraPreview(resultHandler));
    }

    private void download(View view, int... i) {
        final ImageView imageView = view.findViewById(i[0]);
        final setImage image = new setImage(this);
        Toast.makeText(getApplicationContext(), "Downloading", Toast.LENGTH_SHORT).show();
        final FirebaseTasks tasks = new FirebaseTasks();
        if (handler.getFilePath() != null) {
            final ProgressBar progressBar = view.findViewById(i[1]);
            progressBar.setVisibility(View.VISIBLE);
            tasks.DownloadFileFromFireBase(handler.getFilePath().replace(URL, ""));
            tasks.setDownloadListener(new FirebaseTasks.DownloadListener() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Downloaded successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressBar.setOnClickListener(v -> tasks.DownloadFileFromFireBase(handler.getFilePath()));
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(int progress) {
                    Log.v("progress", String.valueOf(progress));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        progressBar.setProgress(progress, true);
                    else
                        progressBar.setProgress(progress);
                }

                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task, File file) {
                    Bitmap bm = image.decodeSampledBitmap(file.getPath(), .5f);
                    imageView.setImageBitmap(bm);
                    imageView.animate().alpha(1f);
                }
            });
        }
    }

    private void copy(Spanned s) {
        Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
        setClipboard(getApplicationContext(), s.toString().
                replace("<br>", "\n").replace("</b>", "").replace("<b>", ""));
        dialog1.dismiss();
    }
}