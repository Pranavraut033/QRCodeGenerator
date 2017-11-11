package com.preons.pranav.QRCodeGenerator.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.preons.pranav.QRCodeGenerator.R;

/**
 * Created on 29-08-2017 at 11:52 by Pranav Raut.
 * For QRCodeProtection
 */

public class Sub extends LinearLayout {

    private TextView tProperty;
    private TextView tValue;
    private ImageView iIcon;

    public Sub(Context context, int id, String title, String value) {
        super(context);
        init(id, title, value);
    }

    public Sub(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(int id, String property, String value) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.sub, this, true);
        tProperty = v.findViewById(R.id.property);
        tValue = v.findViewById(R.id.value);
        iIcon = v.findViewById(R.id.icon);
        setTitle(property);
        setValue(value);
        setIcon(id);
    }

    public void setTitle(String title) {
        tProperty.setText((title + ": ").replace(" *", ""));
    }

    public void setValue(String value) {
        tValue.setText(value);
    }

    public void setIcon(int icon) {
        iIcon.setImageResource(icon);
    }

}
