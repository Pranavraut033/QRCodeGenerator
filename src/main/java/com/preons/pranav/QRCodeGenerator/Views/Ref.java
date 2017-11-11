package com.preons.pranav.QRCodeGenerator.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.preons.pranav.QRCodeGenerator.R;

import pranav.utilities.Utilities;

import static android.view.Gravity.CENTER;

/**
 * Created on 29-08-2017 at 12:03 by Pranav Raut.
 * For QRCodeProtection
 */

public class Ref extends LinearLayout {

    public Ref(Context context) {
        super(context);
    }

    public void init(int[] ints, String[] properties, String[] values) {
        removeAllViews();
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setGravity(CENTER);
        Utilities.Resources res = new Utilities.Resources(getContext());
        int p = (int) res.getPx(4);
        setPadding(p, p, p, p);
        TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{R.attr.selectableItemBackground});
        setBackgroundResource(typedArray.getResourceId(0, 0));

        typedArray.recycle();
        Sub[] subs = new Sub[ints.length];
        for (int i = 0; i < ints.length; i++) {
            subs[i] = new Sub(getContext(), ints[i], properties[i], values[i]);
            addView(subs[i]);
        }
    }

    public Ref(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
