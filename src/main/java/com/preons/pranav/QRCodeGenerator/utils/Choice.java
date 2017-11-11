package com.preons.pranav.QRCodeGenerator.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.preons.pranav.QRCodeGenerator.ExtraActivity;
import com.preons.pranav.QRCodeGenerator.code.Code;

import org.jetbrains.annotations.Contract;

import pranav.utilities.Utilities;


/**
 * Created on 25-06-2017 at 14:51 by Pranav Raut.
 * For QRCodeProtection
 */

public final class Choice<T extends EditText> {

    private static final String TYPE = "type";
    private static final String SUB_TYPE = "subtype";
    private static final String DATA = "data";
    private final Drawable drawable;
    private final String title, dis;
    private int type = -1;
    private Integer subType = -1;
    private Context c;
    private float padding;
    private Code<T> property;

    private Choice(Context context, @DrawableRes int drawable, @StringRes int title, @StringRes int dis, int type) {
        Utilities.Resources res = new Utilities.Resources(context);
        this.drawable = res.getDrawable(drawable);
        this.dis = dis == -1 ? null : res.getString(dis);
        this.title = res.getString(title);
        this.type = type;
        this.subType = -1;
    }

    private Choice(int type, int subType) {
        this.type = type;
        dis = title = null;
        drawable = null;
        this.subType = subType;
        property = new Code<>(type);
    }

    public Choice(Context c) {
        title = dis = null;
        subType = -1;
        type = -1;
        drawable = null;
        this.c = c;
    }

    public static Intent getIntent(Context context, Class<?> aClass, Choice choice) {
        Intent bundle = new Intent(context, aClass);
        bundle.putExtra(TYPE, choice.getType());
        bundle.putExtra(SUB_TYPE, choice.getSubType());
        return bundle;
    }

    @Contract("_ -> !null")
    public static <T extends EditText> Choice<T> getChoice(@NonNull Intent intent) {
        return new Choice<>(intent.getIntExtra(TYPE, -1), intent.getIntExtra(SUB_TYPE, -1));
    }

    @Contract(pure = true)
    public int getType() {
        return type;
    }

    @Contract("_, _, _, _ -> !null")
    public Choice g(int drawable, @StringRes int title, @StringRes int dis, int type) {
        return new Choice(c, drawable, title, dis, type);
    }

    public Choice g2(@DrawableRes int drawable, @StringRes int title, int type, int subtype) {
        return g(drawable, title, -1, type).setSubType(subtype);
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }

    public String getDis() {
        return dis;
    }

    @Contract(pure = true)
    public int getSubType() {
        return subType;
    }

    private Choice setSubType(int subType) {
        this.subType = subType;
        return this;
    }

    public float getPadding() {
        return padding;
    }

    public Choice setPadding(float padding) {
        this.padding = padding;
        return this;
    }

    public Code<T> getProperty() {
        return property;
    }

    public Choice initProperty() {
        property = new Code<>(type);
        return this;
    }
}
