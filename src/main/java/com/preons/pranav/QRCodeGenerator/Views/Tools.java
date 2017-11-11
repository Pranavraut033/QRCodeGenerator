package com.preons.pranav.QRCodeGenerator.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.preons.pranav.QRCodeGenerator.R;
import pranav.views.ColorDialog.ColorDialogBuilder;

import java.util.Arrays;

import pranav.utilities.Utilities;

import static pranav.utilities.Utilities.Resources.getColoredDrawable;


/**
 * Created on 22-07-2017 at 21:39 by Pranav Raut.
 * For QRCodeProtection
 */

public class Tools extends ActionMenuView {
    private static final String SUPER_INSTANCE = "instance";
    private static final String FORE = "fore";
    private static final String BACK = "back";
    private int foreColor = 0xff000000;
    private int backColor = -1;
    private Utilities.Resources res;
    private ImageButton imageView;
    private ImageButton imageView1;
    private Toast toast;
    private AlertDialog dialog;
    private Context c;
    private Integer[] ints = new Integer[]{
            -1, 0xff000000,
            0xFFb71c1c, 0xff880E4F, 0xff4A148C, 0xff311B92,
            0xff1A237E, 0xff0D47A1, 0xff01579B, 0xff006064,
            0xff004D40, 0xff1B5E20, 0xff33691E, 0xff827717,
            0xffF57F17, 0xffFF6F00, 0xffE65100, 0xffBF360C,
            0xff3E2723, 0xff212121, 0xff263238
    };
    private Integer[] ints1 = new Integer[]{
            0xff000000, -1,
            0xffef9a9a, 0xffF48FB1, 0xffCE93D8, 0xffB39DDB,
            0xff9FA8DA, 0xff90CAF9, 0xff81D4FA, 0xff80DEEA,
            0xff80CBC4, 0xffA5D6A7, 0xffC5E1A5, 0xffE6EE9C,
            0xffFFF59D, 0xffFFE082, 0xffFFCC80, 0xffFFAB91,
            0xffBCAAA4, 0xffEEEEEE, 0xffB0BEC5,
    };
    private Integer[] dummy = new Integer[ints.length];
    private boolean fc;
    @Nullable
    private l color;
    @Nullable
    private ColorDialogBuilder<AlertDialog> builder;

    public Tools(Context context) {
        super(context);
        this.c = context;
        inti();
    }

    public Tools(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        inti();
    }

    private void inti() {
        LayoutInflater.from(getContext()).inflate(R.layout.tool_bar_layout, this);
        imageView = findViewById(R.id.fore_color);
        imageView1 = findViewById(R.id.back_color);
        res = new Utilities.Resources(getContext());
        Arrays.fill(dummy, 0xffffffff);
        set();
    }

    private void set() {
        imageView.setLongClickable(true);
        final TextView textView = new TextView(getContext());
        textView.setText(R.string.fore_color);
        textView.setBackgroundResource(android.R.drawable.toast_frame);
        textView.setTextColor(0xffffffff);
        toast = new Toast(getContext());
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);

        imageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView.setText(R.string.fore_color);
                toast.setGravity(Gravity.TOP,
                        (int) v.getX(), (int) (v.getY() + 1.1f * v.getHeight()));
                toast.show();
                return true;
            }
        });

        imageView1.setLongClickable(true);
        imageView1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView.setText(R.string.back_color);
                toast.setGravity(Gravity.TOP, (int) v.getX(), (int) (v.getY() + 1.1f * v.getHeight()));
                toast.show();
                return true;
            }
        });

        builder = new ColorDialogBuilder<>(c);
        dialog = builder.addListeners(new ColorDialogBuilder.Listener.Tap() {
            @Override
            public void onTap(Integer color, int index) {
                if (fc) {
                    setForeColor(color);
                    //add premium feature
                    if (color == -1) setBackColor(0xff000000);
                    else if (color == 0xff000000) setBackColor(-1);
                    else if (getBackColor() == 0xff000000) setBackColor(-1);
                } else {
                    setBackColor(color);
                    //add premium feature
                    if (color == -1) setForeColor(0xff000000);
                    else if (color == 0xff000000) setForeColor(-1);
                    else if (getForeColor() == -1) setForeColor(0xff000000);
                }
                if (Tools.this.color != null)
                    Tools.this.color.onColorChange(getForeColor(), getBackColor());
            }
        }).setMode(ColorDialogBuilder.Mode.TAP).setType(ColorDialogBuilder.CIRCULAR)
                .setColumns(res.decideOR(5, 8))
                .setColors(dummy).setAnimateOnTap(true).create();

        dialog.dismiss();

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fc = true;
                builder.setColors(ints);
                dialog.show();
            }
        });
        imageView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fc = false;
                builder.setColors(ints1);
                dialog.show();
            }
        });
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(FORE, foreColor);
        bundle.putInt(BACK, backColor);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle.getParcelable(SUPER_INSTANCE));
        setBackColor(bundle.getInt(BACK, -1));
        setForeColor(bundle.getInt(FORE, 0xff000000));
        if (color != null) color.onColorChange(foreColor, backColor);
        if (builder != null) builder.setColumns(res.decideOR(5, 8));
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        imageView1.setImageDrawable(getColoredDrawable(res.getPx(res.decideOR(56, 48)), backColor));

    }

    public int getForeColor() {
        return foreColor;
    }

    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
        imageView.setImageDrawable(getColoredDrawable(res.getPx(res.decideOR(56, 48)), foreColor));
    }

    @Nullable
    public l getColor() {
        return color;
    }

    public void setColorListener(@Nullable l color) {
        this.color = color;
    }

    public interface l {
        void onColorChange(int fore, int back);
    }
}
