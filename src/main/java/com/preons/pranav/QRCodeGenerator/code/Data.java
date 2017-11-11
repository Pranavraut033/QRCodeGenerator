package com.preons.pranav.QRCodeGenerator.code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.Views.Ref;
import com.preons.pranav.QRCodeGenerator.utils.Choice;
import com.preons.pranav.QRCodeGenerator.utils.DB;
import com.preons.pranav.QRCodeGenerator.utils.c.i;
import com.preons.pranav.QRCodeGenerator.utils.c.src;
import pranav.views.TextField.TextField;

import pranav.utilities.DataBaseHelper;
import pranav.views.DualView;
import pranav.views.L1;
import pranav.views.Background;

import static com.preons.pranav.QRCodeGenerator.utils.c.DI;

/**
 * Created on 02-07-2017 at 20:31 by Pranav Raut.
 * For QRCodeProtection
 */

public class Data {

    private final ViewGroup group;
    private final LinearLayout container;
    private final Context context;
    private final Choice choice;
    @Nullable
    e e;
    private TextField[] fields;
    private DataBaseHelper helper;
    private DualView dualView;
    private Background background;
    private String[] properties;
    private String[] values;
    private boolean done;
    private Ref ref;

    public Data(Context context, Choice choice, ViewGroup group, LinearLayout container) {
        this.context = context;
        this.choice = choice;
        this.group = group;
        this.container = container;
        init();
    }

    @SuppressWarnings("ResourceType")
    @SuppressLint("InflateParams")
    private void init() {
        switch (choice.getSubType()) {
            case 1:
                helper = new DB.EC(context);
                intiField(R.layout.ec, i.C_IDS, src.C_IDS);
                //new e.c(context);
                break;
            case 3:
                helper = new DB.ES(context);
                intiField(R.layout.es, i.S_IDS, src.S_IDS);
                break;
            case 4:
                helper = new DB.EL(context);
                intiField(R.layout.el, i.L_IDS, src.L_IDS);
                break;
            case 5:
                helper = new DB.EE(context);
                intiField(R.layout.ee, i.E_IDS, src.E_IDS);
                break;
            case 6:
                helper = new DB.EW(context);
                //intiField(R.layout.ew, i.W_IDS);
                break;
        }
    }

    private void intiField(@LayoutRes int id, final int[] ints, final int[] ints1) {
        final LinearLayout view = group.findViewById(R.id.container);
        container.setVisibility(View.VISIBLE);
        ref = new Ref(context);
        View layout = LayoutInflater.from(context).inflate(id, null);

        fields = new TextField[ints.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = layout.findViewById(ints[i]);
            /*fields[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus)
                        background.requestFocus();
                }
            });
            fields[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        v.clearFocus();
                        return background.requestFocus();
                    }
                    return false;
                }
            });*/
        }
        layout.findViewById(R.id.done).setOnClickListener(v -> dualView.dismiss());
        values = new String[fields.length];
        properties = new String[fields.length];
        get();
        ref.init(ints1, properties, values);

        dualView = new DualView(group, R.id.area, R.id.card_con);
        container.addView(ref);
        background = group.findViewById(R.id.ref);
        background.setState(View.GONE);
        background.setKeys((percent, a, b) -> {
            if (dualView.isOpen()) {
                dualView.dismiss();
                done = true;
                return true;
            } else
                return false;
        }).setClickListener(v -> dualView.dismiss());
        dualView.addListener(new DualView.State.Events() {

            @Override
            public void onClose() {
                done = true;
                get();
                ref.init(ints1, properties, values);
            }

            @Override
            public void onOpen() {
                done = false;
            }
        });
        view.addView(layout);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                dualView.addListener((L1.StartAnimation) animation -> background.toggle());
                dualView.setInterpolator(DI);
                new Handler().postDelayed(() -> {
                    if (e != null) e.initDone();
                }, 200);
            }
        });
    }

    public DualView getDualView() {
        return dualView;
    }

    public void setE(@Nullable Data.e e) {
        this.e = e;
    }

    private void get() {
        for (int i = 0; i < fields.length; i++) {
            properties[i] = fields[i].getHint();
            values[i] = fields[i].getContentText();
        }
    }

    public boolean isDone() {
        return done;
    }

    public interface e {
        void initDone();
    }
}
