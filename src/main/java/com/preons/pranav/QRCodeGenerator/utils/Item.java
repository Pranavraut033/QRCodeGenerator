package com.preons.pranav.QRCodeGenerator.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.preons.pranav.QRCodeGenerator.code.Code;

import java.util.Comparator;

import static com.preons.pranav.QRCodeGenerator.utils.DB.I.getName;
import static pranav.utilities.Log.TAG;


/**
 * Created on 20-06-2017 at 11:46 by Pranav Raut.
 * For QRCodeProtection
 */

public class Item implements Comparator<Item> {
    final String title;
    final boolean isProtected;
    final long dateL;
    final int type;
    private final String typeS;
    private final String disp;
    private final String date;
    private final long index;
    private String img_ref;
    private int subtype;
    private String data;
    private int extra_ref;
    private String password;

    public Item(long index, String title, String disp, int type, String data,
                int extra_ref, long date, boolean isProtected, String password) {
        Log.d(TAG, "Item() called with: index = [" + index + "], title = [" + title + "], disp = [" + disp + "], type = [" + type + "], data = [" + data + "], extra_ref = [" + extra_ref + "], date = [" + date + "], isProtected = [" + isProtected + "], password = [" + password + "]");
        this.title = title;
        this.index = index;
        this.disp = disp;
        this.type = type;
        this.typeS = Code.Types.values()[type].name();
        dateL = date;
        this.date = getDate(date);
        this.isProtected = isProtected;
        this.data = data;
        this.extra_ref = extra_ref;
        this.password = password;
        this.img_ref = getName(this);
    }

    @NonNull
    private String getDate(long date) {
        StringBuilder builder = new StringBuilder();
        while (date > 999999) {
            builder.append(date % 100).append("/");
            date /= 100;
        }
        builder = new StringBuilder(builder.substring(0, builder.length() - 1));
        builder.append('\n');
        while (date > 9999) {
            builder.append(date % 100).append(":");
            date /= 100;
        }
        while (date > 99) {
            builder.append(date % 100).append(":");
            date /= 100;
        }
        int i = (int) (date % 100);
        if (i < 10)
            builder.append(0);
        builder.append(i);
        return builder.toString();
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDisp() {
        return disp;
    }

    public int getType() {
        return type;
    }

    public long getIndex() {
        return index;
    }

    public String getDate() {
        return date;
    }

    public String getTypeS() {
        return typeS;
    }

    public long getDateL() {
        return dateL;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public String getImg_ref() {
        return img_ref;
    }

    public int getSubtype() {
        return subtype;
    }

    public String getData() {
        return data;
    }

    int getExtra_ref() {
        return extra_ref;
    }

    String getPassword() {
        return password;
    }

    public String toSearchString() {
        return title + disp + typeS + password;
    }

    @Override
    public String toString() {
        return "[ " + index + " " + title + " " + disp + " " + typeS + " " + data + " " +
                date + " " + isProtected + " " + password + " ]";
    }

    @Override
    public int compare(Item o1, Item o2) {
        return o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
    }
}
