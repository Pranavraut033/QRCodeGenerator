package com.preons.pranav.QRCodeGenerator.code;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.preons.pranav.QRCodeGenerator.R;

/**
 * Created on 02-07-2017 at 20:20 by Pranav Raut.
 * For QRCodeProtection
 */

@SuppressWarnings("unused")
public final class Code<E extends EditText> {

    /**
     * Might be available in later Version
     */
    @SuppressWarnings("WeakerAccess")
    public static final int NOT_AVAILABLE = 0;

    public enum Types {
        Aztec,
        Codabar,
        USD_3,
        CODE_93,
        CODE_128,
        Data_Matrix,
        EAN_8,
        EAN_13,
        ITF,
        Maxicode,
        PDF_417,
        QR_Code,
        RSS,
        RSS_Expanded,
        UPC_A,
        UPC_E,
        Extension
    }
    private final int type;
    private int limit;
    private int min = 0;
    private String allowedSpecial;
    private boolean isAlphanumeric = false;
    private boolean isAllCap = false;
    private boolean isEven = false;
    private
    @Nullable
    E target;

    public Code(int type) {
        this.type = type;
        set(type);
    }

    public void set(int type) {
        setAlphanumeric(false);
        switch (type) {
            case 0:
                limit = 3832;
                setAlphanumeric(true);
                break;
            case 1:
                limit = 70;
                break;
            case 2:
                setAllCap(true);
                allowedSpecial = "-.$/+% ";
                limit = 43;
                break;
            case 3:
                setAllCap(true);
                allowedSpecial = "-.$/+% ";
                limit = 47;
                break;
            case 4:
                limit = 60;
                break;
            case 5:
                setAlphanumeric(true);
                limit = min = 2335;
                break;
            case 6:
                limit = min = 8;
                break;
            case 7:
                limit = min = 13;
                break;
            case 8:
                setEven(true);
                limit = 80;
                break;
            case 10:
                setAlphanumeric(true);
                limit = 1850;
                break;
            case 11:
                setAlphanumeric(true);
                limit = 4296;
                break;
            case 14:
                min = 11;
                limit = 12;
                break;
            case 15:
                limit = min = 8;
                break;
            case 9:
            case 12:
            case 13:
            case 16:
            default:
                limit = NOT_AVAILABLE;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) setDrawable();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setDrawable() {
        if (target != null) {
            switch (type) {
                case 0:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_aztec, 0, 0, 0);
                    break;
                case 1:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_codabar, 0, 0, 0);
                    break;
                case 2:
                case 3:
                case 4:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_usd_3, 0, 0, 0);
                    break;
                case 5:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_data_matrix, 0, 0, 0);
                    break;
                case 6:
                case 7:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_ean_8, 0, 0, 0);
                    break;
                case 8:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_itf, 0, 0, 0);
                    break;
                case 10:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pdf_417, 0, 0, 0);
                    break;
                case 11:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_qr_code, 0, 0, 0);
                    break;
                case 14:
                case 15:
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_upc_a, 0, 0, 0);
                    break;
            }
        }
    }

    public int getType() {
        return type;
    }

    public boolean isAlphanumeric() {
        return isAlphanumeric;
    }

    private void setAlphanumeric(boolean isAlphanumeric) {
        this.isAlphanumeric = isAlphanumeric;
        if ((target != null))
            target.setInputType(isAlphanumeric ?
                    InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_NUMBER);
    }

    public boolean isAllCap() {
        return isAllCap;
    }

    private void setAllCap(boolean allCap) {
        isAllCap = allCap;
        if (target != null)
            target.setInputType(allCap ?
                    target.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS :
                    target.getInputType() | ~InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    }

    public String getAllowedSpecial() {
        return allowedSpecial;
    }

    public int getLimit() {
        return limit;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isEven() {
        return isEven;
    }

    private void setEven(boolean even) {
        isEven = even;
    }

    public void mode(int mode) {
        set(mode);
    }

    @SuppressWarnings("unchecked")
    public Code<E> setTarget(@Nullable View target) {
        this.target = null;
        if (target != null && target instanceof EditText) this.target = (E) target;
        return this;
    }

}