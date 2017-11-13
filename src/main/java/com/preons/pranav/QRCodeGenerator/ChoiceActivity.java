package com.preons.pranav.QRCodeGenerator;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mancj.slideup.SlideUp;
import com.preons.pranav.QRCodeGenerator.Views.ChoiceViewHolderAdapter;
import com.preons.pranav.QRCodeGenerator.Views.DividerItemDecor;
import com.preons.pranav.QRCodeGenerator.code.Code;
import com.preons.pranav.QRCodeGenerator.utils.Choice;
import com.preons.pranav.QRCodeGenerator.utils.InitNav;

import java.util.ArrayList;

import pranav.utilities.Utilities;
import pranav.views.Background;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.preons.pranav.QRCodeGenerator.utils.Utils.initRec;
import static com.preons.pranav.QRCodeGenerator.utils.c.DI;
import static pranav.utilities.Animations.ANIMATION_TIME;
import static pranav.utilities.Utilities.isFinite;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "preons";
    private Choice c;
    private int i = 0, H;
    private float lH = 0f;
    private Context context;
    private ArrayList<Choice> choices = new ArrayList<>();
    private ChoiceViewHolderAdapter holderAdapter = new ChoiceViewHolderAdapter(choices);
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private Utilities.Resources res;
    private RecyclerView recyclerView2;
    private SlideUp slideUp;
    private Background background;
    private ArrayList<Choice> subTypes = new ArrayList<>();
    private int iniT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.c);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        init();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            float mul = 3;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getChildAt(0).getContentDescription().equals("0")) {
                    int y = iniT - recyclerView.getChildAt(0).getTop();
                    if (y < 0) y = 0;
                    Log.d(TAG, "onScrolled: " + y);
                    recyclerView.setOverScrollMode(y > 360 ? View.OVER_SCROLL_IF_CONTENT_SCROLLS : View.OVER_SCROLL_NEVER);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        if (y <= mul * H) appBarLayout.setElevation(lH = y / mul);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && appBarLayout.getElevation() != H)
                    appBarLayout.setElevation(H);
            }
        });

        holderAdapter.setOnClick(this::onClick);

        background.setClickListener(this)
                .setState(GONE)
                .setKeys((percent, a, b) -> {
                    if (percent > 0) {
                        slideUp.hide();
                        return true;
                    }
                    return super.onKeyDown(a, b);
                });

        recyclerView.setAdapter(holderAdapter);
    }

    private void onClick(int position) {
        Choice choice = choices.get(position);
        boolean toShow = false;
        int i = choice.getType();

        subTypes.clear();
        if (new Code(i).isAlphanumeric()) {
            if(i == Code.Types.QR_Code.ordinal()){
                subTypes.add(c.g2(R.drawable.ic_qr_code,R.string.std_info,i,-2));
            }
            toShow = true;
            subTypes.add(c.g2(R.drawable.ic_text_24dp, R.string.text, i, -1));
            subTypes.add(c.g2(R.drawable.ic_contacts_24dp, R.string.contact, i, 1));
            subTypes.add(c.g2(R.drawable.ic_sms_24dp, R.string.sms, i, 3));
            subTypes.add(c.g2(R.drawable.ic_location_24dp, R.string.location, i, 4));
            subTypes.add(c.g2(R.drawable.ic_mail_24dp, R.string.email2, i, 5));
            subTypes.add(c.g2(R.drawable.ic_wifi_24dp, R.string.wifi, i, 6));

        } else switch (i) {
            case 2:
                subTypes.add(c.g(R.drawable.ic_usd_3, R.string.code_39, R.string.code_39_dis, 2));
                subTypes.add(c.g(R.drawable.ic_usd_3, R.string.code_93, R.string.code_93_dis, 3));
                subTypes.add(c.g(R.drawable.ic_usd_3, R.string.code_128, R.string.code_128_dis, 4));
                toShow = true;
                break;

            case 6:
                subTypes.add(c.g(R.drawable.ic_ean_8, R.string.ean_8, R.string.ean_8_dis, 6));
                subTypes.add(c.g(R.drawable.ic_ean_8, R.string.ean_13, R.string.ean_13_dis, 7));
                toShow = true;
                break;

            case 14:
                subTypes.add(c.g(R.drawable.ic_upc_a, R.string.upc_a, R.string.upc_a_dis, 14));
                subTypes.add(c.g(R.drawable.ic_upc_a, R.string.upc_e, R.string.upc_e_dis, 14));
                toShow = true;
                break;
        }

        if (toShow) {
            ChoiceViewHolderAdapter secondary = new ChoiceViewHolderAdapter(subTypes, res.getDimen(R.dimen.pad12dp));
            secondary.toAnimate(true);
            secondary.setOnClick(position1 -> {
                Choice d = subTypes.get(position1);
                startActivity(Choice.getIntent(context, d.getSubType() == -2 ? ExtraActivity.class : EditActivity.class, d));
            });
            recyclerView2.setAdapter(secondary);
            slideUp.show();
        } else startActivity(Choice.getIntent(context, EditActivity.class, choice));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void prepare() {
        choices.add(c.g(R.drawable.ic_aztec, R.string.aztec, R.string.aztec_dis, 0));
        choices.add(c.g(R.drawable.ic_codabar, R.string.codabar, R.string.codobar_dis, 1));
        choices.add(c.g(R.drawable.ic_usd_3, R.string.code, R.string.extra_info, 2));
        choices.add(c.g(R.drawable.ic_data_matrix, R.string.data_matrix, R.string.data_matrix_dis, 5));
        choices.add(c.g(R.drawable.ic_ean_8, R.string.ean, R.string.extra_info, 6));
        choices.add(c.g(R.drawable.ic_itf, R.string.itf, R.string.itf_dis, 8));
        choices.add(c.g(R.drawable.ic_pdf_417, R.string.pdf417, R.string.pdf417_dis, 10));
        choices.add(c.g(R.drawable.ic_qr_code, R.string.qr_code, R.string.qr_code_dis, 11));
        choices.add(c.g(R.drawable.ic_upc_a, R.string.upc, R.string.extra_info, 14));

//        choices.add(c.g(R.drawable.action_mode_grey, R.string.maxicode, R.string.maxicode_dis, 9));
//        choices.add(c.g(R.drawable.action_mode_grey, R.string.rss, R.string.rss_dis, 12));
//        choices.add(c.g(R.drawable.action_mode_grey, R.string.rss_expanded, R.string.rss_expanded_dis, 13));
//        choices.add(c.g(R.drawable.action_mode_grey, R.string.extension, R.string.extension_dis, 16));
    }

    private void init() {
        context = this;

        c = new Choice(context);

        res = new Utilities.Resources(context);
        H = (int) res.getDimen(R.dimen.action_bar_elevation);

        appBarLayout = findViewById(R.id.app_bar_layout);
        background = ((Background) findViewById(R.id.back)).setState(GONE);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView2 = findViewById(R.id.subtype);

        slideUp = new SlideUp.Builder(recyclerView2)
                .withStartState(SlideUp.State.HIDDEN)
                .withGesturesEnabled(true)
                .withInterpolator(DI)
                .withTouchableArea(getResources().getDisplayMetrics().heightPixels
                        / getResources().getDisplayMetrics().density)
                .withListeners(new SlideUp.Listener.Events() {
                    boolean run = true;
                    float l = 0f;
                    @Override
                    public void onSlide(float percent) {
                        // TODO: 25-08-2017 Fix Background Issue
                        if (!(run = (!isFinite(percent) || l == percent || l - percent == 100))) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                appBarLayout.setElevation(lH * percent / 100);
                            background.setPercent(100 - percent);
                        }
                        l = percent;
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (run) {
                            background.animateTo(l = (visibility == VISIBLE ? 100 : 0));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                appBarLayout.animate().z((1 - l / 100) * lH)
                                        .setInterpolator(DI).setDuration(ANIMATION_TIME);
                        }
                        if (l != 0 && l != 100) {
                            background.animateTo(0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                appBarLayout.animate().z(lH * l / 100)
                                        .setInterpolator(DI).setDuration(ANIMATION_TIME);
                        }
                        if (visibility == GONE) {
                            recyclerView2.removeAllViews();
                            subTypes.clear();
                        }

                    }
                }).withLoggingEnabled(false).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView2.setElevation(res.getDimen(R.dimen.pad10dp));

        (new InitNav(this)).setIndicatorEnable(false);

        initRec(new DividerItemDecor(this).setPadding(res.getDimen(R.dimen.pad74dp),
                res.getDimen(R.dimen.activity_horizontal_margin)), recyclerView2, recyclerView);

        prepare();
        iniT = recyclerView.getTop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case Background.BACK_ID:
                slideUp.hide();
                break;
        }
    }
}