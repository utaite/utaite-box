package com.yuyu.utaitebox.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.chain.ChainedArrayList;
import com.yuyu.utaitebox.chain.ChainedToast;
import com.yuyu.utaitebox.fragment.MainFragment;
import com.yuyu.utaitebox.fragment.MusicListFragment;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.utils.Constant;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends RxAppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    public static int MID = 53;
    public static int TODAY = Constant.TODAY_DEFAULT;
    public static String TEMP_AVATAR;

    private Task task;
    private Context context;
    private ChainedToast toast;
    private RequestManager requestManager;

    private int index;
    private ArrayList<Integer> items;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.nav_view)
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        task = new Task(context);
        requestManager = Glide.with(context);
        toast = new ChainedToast(context).makeTextTo(this, "", Toast.LENGTH_SHORT);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(item -> {
            getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_main, getFragment(item.getItemId()))
                    .commit();
            setTitle(item.getTitle());
            drawer_layout.closeDrawer(GravityCompat.START);
            return true;
        });
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav_view.getMenu().getItem(index).setChecked(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nav_view.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);

        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else if (Constant.CURRENT_TIME + Constant.BACK_TIME < System.currentTimeMillis()) {
            Constant.CURRENT_TIME = System.currentTimeMillis();
            toast.setTextShow(getString(R.string.onBackPressed));

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iid = item.getItemId();
        if (iid == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment getFragment(int iid) {
        index = items.indexOf(iid);
        Fragment fragment = null;

        if (iid == R.id.nav_home) {
            fragment = new MainFragment();
        } else if (iid == R.id.nav_music) {
            fragment = new MusicListFragment();
        } else if (iid == R.id.nav_chart) {
        } else if (iid == R.id.nav_upload) {
        } else if (iid == R.id.nav_timeline) {
        }
        return fragment;
    }

    public void initialize() {
        int size = nav_view.getMenu().size();
        items = (ArrayList<Integer>) new ChainedArrayList()
                .addMenu(nav_view.getMenu(), 0, size);
        setTitle(getString(R.string.nav_home));
        requestRetrofit(getString(R.string.rest_member), MID);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new MainFragment())
                .commit();
    }

    public void requestRetrofit(String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .compose(bindToLifecycle())
                .subscribe(repo -> {
                            TextView nav_id = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_id);
                            ImageView nav_bg1 = (ImageView) nav_view.getHeaderView(0).findViewById(R.id.nav_bg1);
                            ImageView nav_img = (ImageView) nav_view.getHeaderView(0).findViewById(R.id.nav_img);

                            String avatar = TEMP_AVATAR = repo.getProfile().getAvatar();
                            String cover = repo.getProfile().getCover();
                            String url = RestUtils.BASE + getString(R.string.rest_profile_image);

                            nav_id.setText(repo.getMember().getUsername());
                            requestManager.load(url + (avatar == null ? getString(R.string.rest_profile) : avatar))
                                    .bitmapTransform(new CropCircleTransformation(context))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(nav_img);
                            if (cover == null) {
                                nav_bg1.setImageResource(android.R.color.transparent);
                                nav_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
                            } else {
                                requestManager.load(RestUtils.BASE + getString(R.string.rest_profile_cover) + cover)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(nav_bg1);
                            }
                        },
                        e -> Log.e(TAG, e.toString()));
    }

    public ChainedToast getToast() {
        return toast;
    }

    public Task getTask() {
        return task;
    }

}
