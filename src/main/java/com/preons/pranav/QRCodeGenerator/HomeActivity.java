package com.preons.pranav.QRCodeGenerator;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.preons.pranav.QRCodeGenerator.Views.DividerItemDecor;
import com.preons.pranav.QRCodeGenerator.Views.ItemViewHolderAdapter;
import com.preons.pranav.QRCodeGenerator.utils.DB;
import com.preons.pranav.QRCodeGenerator.utils.InitNav;
import com.preons.pranav.QRCodeGenerator.utils.Item;
import com.preons.pranav.QRCodeGenerator.utils.Utils;

import java.util.ArrayList;

import pranav.utilities.Animations;
import pranav.utilities.Buffer;
import pranav.utilities.Task;
import pranav.utilities.Utilities;
import pranav.views.FloatingMenu.FMGroupHelper;
import pranav.views.FloatingMenu.FloatingMenu;

import static com.preons.pranav.QRCodeGenerator.utils.Utils.initRec;
import static com.preons.pranav.QRCodeGenerator.utils.c.DCI;
import static com.preons.pranav.QRCodeGenerator.utils.c.DI;
import static com.preons.pranav.QRCodeGenerator.utils.c.setFilter;
import static pranav.utilities.Animations.ANIMATION_TIME;
import static pranav.utilities.Animations.AnimatingParameter.ANIMATE_WIDTH;
import static pranav.utilities.Utilities.Resources.getColoredDrawable;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean multiOn = false;
    private int iniTop, i = -1, j = 0x90000000, k = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 0xCCFFFFFF : 0x90000000,
            primaryColor, primaryColorDark;
    private float lastH = 0, f = 0, H;
    Buffer<Float> floatBuffer = new Buffer<Float>() {
        @Override
        public void execute(Float f) {
            System.out.println(f);
            lastH = new FloatEvaluator().evaluate(f, 0, H);
            i = (Integer) new ArgbEvaluator()
                    .evaluate(f, -1, primaryColor);
            j = (Integer) new ArgbEvaluator()
                    .evaluate(f, 0x90000000, -1);
            k = (Integer) new ArgbEvaluator()
                    .evaluate(f, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 0xCCFFFFFF : 0x90000000, primaryColorDark);
        }
    }.setDelaySpan(25);
    private Drawable search;
    private ArrayList<Item> restore = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> data = new ArrayList<>();
    private Context c;
    private Menu menu;
    private Snackbar snackbar;
    private Toolbar toolbar;
    private SearchView searchView;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private ValueAnimator valueAnimator = ObjectAnimator.ofObject(new FloatEvaluator());
    private Utilities.Resources res;
    private InitNav nav;
    private DB.I handler;
    private FloatingMenu floatingMenu;
    private Animations.AnimateStatusBar statusBar;
    Task<Float> apply = f -> {
        if (f == 1) {
            HomeActivity.this.recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            return;
        } else
            HomeActivity.this.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        appBarLayout.setBackgroundColor(i);
        toolbar.setTitleTextColor(j);
        if (f > .65f)
            statusBar.clearLightStatusBar();
        else
            statusBar.setLightStatusBar();
        setFilter(j, search, nav.getDrawable());
        nav.setBackground(k);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setElevation(lastH);
            statusBar.setColor(k);
        }
    }, tools = f -> {
        if (f == 0) {
            if (this.f != 1f) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Utilities.setLightStatusBar(getWindow());
                HomeActivity.this.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        } else if (f == 1) {
            HomeActivity.this.recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Utilities.clearLightStatusBar(getWindow());
        }
        int i = (Integer) new ArgbEvaluator().evaluate(f, this.i, 0xFF424242),
                j = (Integer) new ArgbEvaluator().evaluate(f, this.j, -1),
                k = (Integer) new ArgbEvaluator().evaluate(f, this.k, 0xFF212121);
        toolbar.setTitleTextColor(j);
        setFilter(j, search, nav.getDrawable());
        nav.setBackground(k);
        appBarLayout.setBackgroundColor(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setElevation(new FloatEvaluator().evaluate(f, lastH, H));
            statusBar.setColor(k);
        }
    };
    private Animations.AnimatingParameter parameter;
    private ItemViewHolderAdapter adapter = new ItemViewHolderAdapter(null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepare();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contextual_actionbar, menu);
        this.menu = menu;
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        search = searchMenuItem.getIcon();
        searchView = (SearchView) searchMenuItem.getActionView();
        ((ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn))
                .setColorFilter(0x90000000, PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            searchView.setBackground(getColoredDrawable(res.getPx(5),
                    -1, (int) res.getPx(10)));
        else searchView.setBackgroundDrawable(getColoredDrawable(res.getDimen(R.dimen.pad10dp),
                -1, (int) res.getDimen(R.dimen.pad10dp)));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return search(query);
            }

            @SuppressWarnings("PointlessBooleanExpression")
            @Override
            public boolean onQueryTextChange(String newText) {
                return search(newText) || true;
            }

            boolean search(String s) {
                if (data.isEmpty()) return true;
                items.clear();
                items.addAll(s.isEmpty() ? data : Utils.search(data, s));
                refresh();
                return false;
            }
        });
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                adapter.setMultiEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setMultiEnabled(true);
                floatBuffer.addTasks(apply);
                valueAnimator.setObjectValues(1f, 0f);
                valueAnimator.start();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                RelativeLayout layout = findViewById(R.id.ref_layout);
                snackbar = Snackbar.make(layout, adapter.getSelectionCount() + " Items Deleted",
                        Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", this);
                View v = snackbar.getView();
                v.setBackgroundColor(-1);
                System.out.println(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) v.setElevation(res.getPx(8));
                ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(0x90000000);
                floatingMenu.liftFor(res.getDimen(R.dimen.pad36dp), 2800);
                v.setVisibility(View.INVISIBLE);
                snackbar.show();
                v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        v.removeOnLayoutChangeListener(this);
                        v.setTranslationY(v.getHeight());
                        v.setVisibility(View.VISIBLE);
                        v.animate().translationY(0).setDuration(ANIMATION_TIME).setInterpolator(DI);
                    }
                });
                delete();
                return true;
            case R.id.search:
                searchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        searchView.removeOnLayoutChangeListener(this);
                        Point loc = new Point((int) (right - left - res.getPx(28)), (bottom - top) / 2);
                        int r = (int) Math.hypot(loc.x, loc.y);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Animator animation = ViewAnimationUtils.createCircularReveal(searchView, loc.x, loc.y, 0, r);
                            animation.setDuration((long) (1.3f * ANIMATION_TIME));
                            animation.setInterpolator(DCI);
                            animation.start();
                        }
                        floatBuffer.removeTasks(apply);
                        valueAnimator.setObjectValues(0f, 1f);
                        valueAnimator.start();
                    }
                });
                return true;
            case R.id.toggle_selection:
                if (adapter.getSelectionCount() < data.size()) {
                    adapter.selectAll(true);
                    item.setIcon(res.getDrawable(R.drawable.ic_select_none));
                    item.setTitle(R.string.select_none);
                } else {
                    adapter.selectAll(false);
                    item.setIcon(res.getDrawable(R.drawable.ic_select_all));
                    item.setTitle(R.string.select_all);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.back_key:
                c();
                break;
            case android.support.design.R.id.snackbar_action:
                items.clear();
                handler.removeAll();
                for (int i = 0; i < restore.size(); i++)
                    handler.insertItem(restore.get(i));
                items.addAll(restore);
                refresh();
                snackbar.dismiss();
                floatingMenu.lift(0);
                break;
        }
    }

    private void init() {
        res = new Utilities.Resources(c = this);
        H = (int) res.getDimen(R.dimen.action_bar_elevation);
        primaryColor = res.getColor(R.color.colorPrimary);
        primaryColorDark = res.getColor(R.color.colorPrimaryDark);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        statusBar = new Animations.AnimateStatusBar(getWindow());

        recyclerView = findViewById(R.id.historyItems);
        appBarLayout = findViewById(R.id.app_bar_layout);
        floatingMenu = findViewById(R.id.menu);

        SharedPreferences sharedPreferences = getSharedPreferences(com.preons.pranav.QRCodeGenerator.utils.c.DSP, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        nav = new InitNav(c, toolbar, sharedPreferences);

        ImageView ref_iv = findViewById(R.id.back_key);
        parameter = new Animations.AnimatingParameter(ref_iv,
                1, (int) res.getDimen(R.dimen.pad56dp)).setMode(ANIMATE_WIDTH);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            float mul = 5;
            ValueAnimator animator = ObjectAnimator.ofObject(new FloatEvaluator());

            {
                animator.addUpdateListener(animation -> floatBuffer.runTasks((Float) animation.getAnimatedValue()));
                animator.setInterpolator(DCI);
                System.out.println();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View v = recyclerView.getChildAt(0);
                if (v == null) {
                    if (f != 0) ps(0);
                } else if (!v.getContentDescription().equals("0")) {
                    if (f != 1) ps((int) (mul * H - 1));
                } else ps(iniTop - v.getTop());
            }

            synchronized void ps(int y) {
                if (y > mul * H) {
                    if (f == 1) return;
                    animator.setObjectValues(f, 1);
                    animator.start();
                    f = 1;
                } else if (y < 0) {
                    if (f == 0) return;
                    animator.setObjectValues(f, 0);
                    animator.start();
                    f = 0;
                } else {
                    f = y / (mul * H);
                    floatBuffer.add(f);
                }
            }
        });

        int i = (int) res.getDimen(R.dimen.activity_vertical_margin);

        initRec(new DividerItemDecor(this).setPadding(i, i), recyclerView);
        ref_iv.setOnClickListener(this);

        floatingMenu.setInterpolator(new AccelerateDecelerateInterpolator());
        floatingMenu.build(new FMGroupHelper(
                floatingMenu.getDetails().setAnimatedStatusBar(false).setListeners(v -> startActivity(new Intent(c, ChoiceActivity.class)),
                        v -> startActivity(new Intent(c, ScannerActivity.class))), null).setDividerVisible(false));

        adapter.setClick(new ItemViewHolderAdapter.OnClick() {

            @Override
            public void onClick(int position) {
                startActivity(new Intent(HomeActivity.this, PreviewActivity.class));
                //TODO: Link next activity
            }

            @Override
            public void onMultiSelect(int count, boolean multiOn) {
                if (multiOn != HomeActivity.this.multiOn) {
                    HomeActivity.this.multiOn = multiOn;
                    menu.findItem(R.id.delete).setVisible(multiOn);
                    if (multiOn) a();
                    else b();
                }
                //MenuItem i = menu.findItem(R.id.toggle_selection);
                //i.setIcon(res.getDrawable(count == data.size() ?
                //        R.drawable.ic_select_none : R.drawable.ic_select_all));
                //i.setTitle(count == data.size() ? R.string.select_none : R.string.select_all);
                nav.setIndicatorEnable(!multiOn);
                if (multiOn) toolbar.setTitle(count + " Item(s) Selected");
            }
        });

        floatBuffer.addTasks(apply);
        valueAnimator.addUpdateListener(animation -> tools.execute((float) animation.getAnimatedValue()));
        valueAnimator.setDuration(ANIMATION_TIME);
        valueAnimator.setInterpolator(DCI);
        iniTop = recyclerView.getTop();
        setFilter(0x90000000, nav.getDrawable());
        statusBar.setColor(0xCCFFFFFF);
        statusBar.setLightStatusBar();
        recyclerView.setAdapter(adapter);
    }

    private void a() {
        floatBuffer.removeTasks(apply);
        //menu.findItem(R.id.toggle_selection).setIcon(res.getDrawable(R.drawable.ic_select_all));
        for (int i : new int[]{R.id.search,})//R.id.toggle_selection})
            menu.findItem(i).setVisible(i != R.id.search);
        valueAnimator.setObjectValues(0f, 1f);
        valueAnimator.start();
        parameter.animate(true);
    }

    private void b() {
        floatBuffer.addTasks(apply);
        for (int i : new int[]{R.id.search,})//R.id.toggle_selection})
            menu.findItem(i).setVisible(i == R.id.search);
        valueAnimator.setObjectValues(1f, 0f);
        valueAnimator.start();
        parameter.animate(false);
        toolbar.setTitle("Home");
    }

    private void c() {
        nav.setIndicatorEnable(true);
        adapter.reset();
        multiOn = false;
        menu.findItem(R.id.delete).setVisible(false);
        b();
        refresh();
    }

    private void prepare() {
        handler = new DB.I(c);
        items.clear();
        data.clear();
        items.addAll(handler.getEverything());
        data.addAll(items);
        adapter.setItems(items);
        refresh();
    }

    private void delete() {
        ArrayList<Integer> checkItems = adapter.getCheckedItemIndices();
        ArrayList<Item> temp = new ArrayList<>();

        for (int i : checkItems) {
            Item it = items.get(i);
            temp.add(it);
            handler.deleteItem(handler.getID(it.getIndex()));
            //TODO: delete Image
        }
        restore.clear();
        restore.addAll(items);
        items.removeAll(temp);
        data.clear();
        data.addAll(items);
        c();
    }

    private void refresh() {
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}