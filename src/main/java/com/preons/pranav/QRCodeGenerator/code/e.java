package com.preons.pranav.QRCodeGenerator.code;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.preons.pranav.QRCodeGenerator.R;

import pranav.utilities.Animations;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.preons.pranav.QRCodeGenerator.ChoiceActivity.TAG;
import static com.preons.pranav.QRCodeGenerator.utils.c.i.C_IDS;
import static pranav.utilities.Animations.AnimatingParameter.ANIMATE_HEIGHT;

/**
 * Created on 29-08-2017 at 23:32 by Pranav Raut.
 * For QRCodeProtection
 */

class e {

    public static class c {
        private int i = 0;

        public c(Context context) {
            final Activity a = ((Activity) context);
            final ImageView imageView = a.findViewById(R.id.add);
            imageView.setOnClickListener(v -> {
                if (i++ < 2) {
                    Log.i(TAG, "onClick: " + a.findViewById(C_IDS[i + 1]));
                    View view = a.findViewById(C_IDS[i + 1]);
                    view.setVisibility(View.VISIBLE);
                    view.measure(MATCH_PARENT, WRAP_CONTENT);
                    new Animations.AnimatingParameter(view, 1, v.getMeasuredHeight()).setMode(ANIMATE_HEIGHT).animate();
                } else
                    imageView.setVisibility(View.GONE);

            });
        }
    }

}
