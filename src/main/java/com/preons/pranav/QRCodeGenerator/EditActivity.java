package com.preons.pranav.QRCodeGenerator;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.BarcodeFormat;
import com.lib.Contents;
import com.lib.Encoder;
import com.preons.pranav.QRCodeGenerator.Views.Tools;
import com.preons.pranav.QRCodeGenerator.code.Code;
import com.preons.pranav.QRCodeGenerator.code.Data;
import com.preons.pranav.QRCodeGenerator.utils.Choice;
import com.preons.pranav.QRCodeGenerator.utils.DB;
import com.preons.pranav.QRCodeGenerator.utils.Item;
import com.preons.pranav.QRCodeGenerator.utils.StringHandler;
import com.preons.pranav.QRCodeGenerator.utils.c;
import pranav.views.TextField.TextField;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import pranav.utilities.Animations;
import pranav.utilities.Utilities;

import static com.preons.pranav.QRCodeGenerator.ExtraActivity.INFOS;
import static com.preons.pranav.QRCodeGenerator.utils.Choice.getChoice;
import static com.preons.pranav.QRCodeGenerator.utils.c.DATE_FORMAT2;
import static com.preons.pranav.QRCodeGenerator.utils.c.DI;
import static com.preons.pranav.QRCodeGenerator.utils.c.PERMISSION_CAMERA;
import static com.preons.pranav.QRCodeGenerator.utils.c.e.BUFFER_SIZE;
import static com.preons.pranav.QRCodeGenerator.utils.c.e.vINFO;
import static com.preons.pranav.QRCodeGenerator.utils.c.e.vPASS;
import static com.preons.pranav.QRCodeGenerator.utils.c.e.vTOOLS;
import static pranav.utilities.Animations.ANIMATION_TIME;
import static pranav.utilities.Animations.animateAlpha;
import static pranav.utilities.Utilities.hasPermissions;

public class EditActivity extends AppCompatActivity {

    private boolean collapsed = true, b;
    private int back = -1, fore = 0xff000000, type;
    private String pass;

    private Button button, std;
    private Menu menu;
    private Toolbar toolbar;
    private ToggleButton lock;
    private TextField info, passT, desp;

    @Nullable
    private ValueAnimator animator;
    private Tools tools;
    private Code<TextInputEditText> code;

    private Drawable d;
    private Activity a;
    private Bundle tlBun = new Bundle();
    private StringHandler stringHandler;
    private ViewGroup parent;
    private Data data;
    private AlertDialog.Builder builder;
    Utilities.Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        res = new Utilities.Resources(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        initProperties();
        checkpPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        code.setTarget(info.getTextField());
        code.set(code.getType());

        toolbar.setTitle(BarcodeFormat.values()[type = code.getType()].name());
        info.setLimit(code.getLimit());
    }

    private void checkpPermission() {
        String[] PERMISSIONS = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
            PERMISSIONS = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(this, PERMISSIONS))
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CAMERA);
    }

    private void initProperties() {
        //todo animate
        lock.setOnCheckedChangeListener((buttonView, isChecked) -> passT.setEnabled(b = isChecked));

        std.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ExtraActivity.class);
            intent.putExtra(ExtraActivity.EXTRA_CONTACT, true);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(a, std, "std");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                startActivityForResult(intent, c.e.INFO, options.toBundle());
            else
                startActivityForResult(intent, c.e.INFO);
        });

        button.setOnClickListener(v -> generate(stringHandler.encryptString(info.getContentText(),
                pass = (lock.isChecked() ? passT.getContentText() : ""))));
        builder = new AlertDialog.Builder(this);
        // TODO: 29-08-2017 change icon
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Cancel create code?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("OK", (dialog, which) -> finish());

        builder.create();
    }

    private void init() {
        a = this;
        stringHandler = new StringHandler(null);
        Choice<TextInputEditText> choice = getChoice(getIntent());
        code = choice.getProperty();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        info = findViewById(R.id.textInfo);
        passT = findViewById(R.id.inputPassword);
        desp = findViewById(R.id.description);
        lock = findViewById(R.id.toggle_protect);
        std = findViewById(R.id.standard);
        button = findViewById(R.id.generate);

        parent = findViewById(R.id.ref_layout);

        if (code.isAlphanumeric() && choice.getSubType() != -1) {
            LinearLayout area = findViewById(R.id.area);
            area.setVisibility(View.VISIBLE);
            info.setVisibility(View.GONE);
            data = new Data(this, choice, parent, area);
            data.setE(() -> data.getDualView().openView());
        } else findViewById(R.id.ref).setVisibility(View.GONE);
        if (code.isAlphanumeric()) findViewById(R.id.pass_container).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(vINFO, info.onSaveInstanceState());
        outState.putBundle(vPASS, passT.onSaveInstanceState());
        outState.putBundle(vTOOLS, tools.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.tlBun = savedInstanceState.getBundle(vTOOLS);
        info.onRestoreInstanceState(savedInstanceState.getBundle(vINFO));
        passT.onRestoreInstanceState(savedInstanceState.getBundle(vPASS));
    }

    private void generate(String s) {
        int width = res.getDeviceWidth();
        int height = res.getDeviceHeight();
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        Encoder encoder = new Encoder(s,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.values()[type].name(),
                smallerDimension);
        encoder.setBackground(back).setForeground(fore);

        try {
            Bitmap bitmap = encoder.encodeAsBitmap();
            ImageView myImage = findViewById(R.id.qr_preview);
            DB.I itemDBHandler = new DB.I(this);
            // TODO: 12-08-2017 update this
            Item item = new Item(0, s, desp.getText(), type, s, 0,
                    Long.parseLong(DATE_FORMAT2.format(new Date())), b, pass);
            itemDBHandler.insertItem(item);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "/QRCode/" + item.getImg_ref() + ".jpg");
            int i = 1;
            while (file.exists())
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "/QRCode/" + item.getImg_ref() + " (" + i++ + ").jpg");
            File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QRCode");
            if (!file1.exists())
                file1.mkdir();
            writeExternalToCache(bitmap, file);
            final File finalFile = file;
            myImage.setOnClickListener(v -> {
                if (finalFile.isFile()) {
                    MediaScannerConnection.scanFile(EditActivity.this, new String[]{finalFile.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "image/*");
                            startActivity(intent);
                        }
                    });
                }
            });
            myImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case c.e.INFO:
                //TODO std info
                if (resultCode == RESULT_OK) info.setText(data.getStringExtra(INFOS));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_button, menu);
        tools = (Tools) menu.findItem(R.id.color).getActionView();
        tools.setColorListener((fore, back) -> {
            EditActivity.this.fore = fore;
            EditActivity.this.back = back;
        });
        if (tlBun != null) tools.onRestoreInstanceState(tlBun);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collapse:
                if (animator == null || !animator.isRunning()) {
                    View v = findViewById(R.id.collapse_layout);
                    v.measure(-1, -2);
                    animateAlpha(tools, collapsed ? 0 : 1, ANIMATION_TIME, DI, collapsed ? 0 : ANIMATION_TIME / 2);
                    animateAlpha(v, collapsed ? Float.MIN_VALUE : 1, ANIMATION_TIME, DI, collapsed ? 0 : ANIMATION_TIME / 2);
                    d = menu.findItem(R.id.collapse).getIcon();
                    new Animations.AnimatingParameter(v, 0, v.getMeasuredHeight())
                            .setDuration(ANIMATION_TIME)
                            .setDelay(collapsed ? ANIMATION_TIME / 2 : 0)
                            .animate(collapsed = !collapsed);
                    animator = ObjectAnimator.ofFloat(0f, 90f)
                            .setDuration(ANIMATION_TIME);
                    animator.setInterpolator(DI);
                    if (!collapsed)
                        animator.setFloatValues(0f, -90f);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float angle = (float) animation.getAnimatedValue();
                            if (angle == 0f) d = menu.findItem(R.id.collapse).getIcon();
                            final Drawable[] arD = {d};
                            menu.findItem(R.id.collapse).setIcon(new LayerDrawable(arD) {
                                @Override
                                public void draw(final Canvas canvas) {
                                    canvas.save();
                                    canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
                                    super.draw(canvas);
                                    canvas.restore();
                                }
                            });
                        }
                    });
                    animator.start();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void writeExternalToCache(Bitmap bitmap, File file) {
        if (bitmap != null && file != null) {
            try {
                if (file.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(file);
                    final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    fos.close();
                }
                Snackbar snackbar = Snackbar.make(parent, "Image Stored as " + file.getPath(), Snackbar.LENGTH_INDEFINITE);
                ((TextView) snackbar.getView()
                        .findViewById(android.support.design.R.id.snackbar_text))
                        .setTextColor(Color.WHITE);
                snackbar.show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (data!=null&&!data.isDone()) {
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}