package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.chain.Chained;
import com.yuyu.utaitebox.rest.Comment;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.rest.Utaite;
import com.yuyu.utaitebox.utils.Constant;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UtaiteInfoFragment extends RxFragment {

    private static final String TAG = UtaiteInfoFragment.class.getSimpleName();

    private Context context;
    private RequestManager glide;
    private Utaite utaite;

    private ArrayList<MainVO> vo;
    private MainAdapter mainAdapter;

    private boolean ribbonCheck, text1Check, img1Check, text2Check, img2Check, text3Check, img3Check;
    private String str1Check;
    private int count;

    @BindView(R.id.utaiteinfo_bg1)
    ImageView utaiteinfo_bg1;
    @BindView(R.id.utaiteinfo_id)
    TextView utaiteinfo_id;
    @BindView(R.id.utaiteinfo_img)
    ImageView utaiteinfo_img;
    @BindView(R.id.utaiteinfo_text1)
    TextView utaiteinfo_text1;
    @BindView(R.id.utaiteinfo_text1_src)
    ImageView utaiteinfo_text1_src;
    @BindView(R.id.utaiteinfo_ribbon_img)
    LinearLayout utaiteinfo_ribbon_img;
    @BindView(R.id.utaiteinfo_text2)
    TextView utaiteinfo_text2;
    @BindView(R.id.utaiteinfo_timeline)
    LinearLayout utaiteinfo_timeline;
    @BindView(R.id.utaiteinfo_avatar)
    ImageView utaiteinfo_avatar;
    @BindView(R.id.utaiteinfo_text3)
    TextView utaiteinfo_text3;
    @BindView(R.id.utaiteinfo_recyclerview)
    RecyclerView utaiteinfo_recyclerview;
    @BindView(R.id.utaiteinfo_timeline_edittext)
    EditText utaiteinfo_timeline_edittext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utaiteinfo, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        glide = Glide.with(context);
        initialize();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(utaiteinfo_timeline_edittext.getWindowToken(), 0);
    }

    public void initialize() {
        vo = new ArrayList<>();
        utaiteinfo_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        utaiteinfo_recyclerview.setLayoutManager(llm);

        mainAdapter = new MainAdapter(context, vo);
        utaiteinfo_recyclerview.setAdapter(mainAdapter);
        Chained.setVisibilityMany(View.GONE, utaiteinfo_text1_src, utaiteinfo_ribbon_img, utaiteinfo_timeline, utaiteinfo_recyclerview);
        requestRetrofit(getString(R.string.rest_artist), getArguments().getInt(getString(R.string.rest_aid)));
        ribbonCheck = text1Check = img1Check = text2Check = img2Check = text3Check = img3Check = false;
    }

    @OnClick(R.id.utaiteinfo_text1)
    public void onTextView1Click() {
        if (!text1Check && !img1Check) {
            ArrayList<Comment> comment = utaite.getHearter();
            int size = comment.size();
            if (size != 0) {
                ImageView iv[] = new ImageView[size];
                TextView tv[] = new TextView[size];
                LinearLayout rAbsolute, rHorizontal = null;
                RelativeLayout rRelative;

                for (int i = 0; i < size; i++) {
                    if (i % 4 == 0) {
                        rHorizontal = new LinearLayout(context);
                        rHorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        rHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        rHorizontal.setHorizontalGravity(Gravity.CENTER);
                        utaiteinfo_ribbon_img.addView(rHorizontal);
                    }

                    iv[i] = new ImageView(context);
                    iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = comment.get(i).getAvatar();
                    glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ? getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(iv[i]);

                    int position = i;
                    iv[i].setOnClickListener(v -> onProfileClick(comment.get(position).get_mid()));

                    tv[i] = new TextView(context);
                    String nickname = comment.get(i).getNickname();
                    tv[i].setText(nickname.length() <= Constant.LONG_STRING ? nickname : nickname.substring(0, Constant.LONG_STRING) + "...");
                    tv[i].setTextColor(Color.BLACK);

                    rAbsolute = new LinearLayout(context);
                    rAbsolute.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rAbsolute.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    param1.setMargins(23, 10, 23, 10);
                    rAbsolute.addView(iv[i], param1);
                    rAbsolute.setId(i + 1);

                    rRelative = new RelativeLayout(context);
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.addRule(RelativeLayout.BELOW, rAbsolute.getId());
                    rRelative.setLayoutParams(p);
                    rRelative.addView(tv[i]);
                    rAbsolute.addView(rRelative);
                    rAbsolute.setGravity(Gravity.CENTER);
                    rAbsolute.setPadding(10, 0, 10, 0);
                    rHorizontal.addView(rAbsolute);
                }
            } else {
                TextView tv = new TextView(context);
                tv.setText(getString(R.string.not_ribbon));
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setGravity(Gravity.CENTER);
                utaiteinfo_ribbon_img.addView(tv);
            }
            img1Check = true;
        }
        utaiteinfo_ribbon_img.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        utaiteinfo_text1_src.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        utaiteinfo_text1.setText((!text1Check ? "▲" : "▼") + str1Check);
        text1Check = !text1Check;
    }

    @OnClick(R.id.utaiteinfo_text2)
    public void onTextView2Click() {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(utaiteinfo_timeline_edittext.getWindowToken(), 0);
        int nickInt = 1, contInt = 100, dateInt = 10000;
        if (!text2Check && !img2Check) {
            glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (MainActivity.TEMP_AVATAR == null ?
                    getString(R.string.rest_profile) : MainActivity.TEMP_AVATAR))
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(utaiteinfo_avatar);

            ArrayList<Comment> comment = utaite.getComment();
            int size = comment.size();
            if (size != 0) {
                LinearLayout rAbsolute;
                rAbsolute = new LinearLayout(context);
                rAbsolute.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout rRelativeNick, rRelativeCont, rRelativeImg, rRelativeDate;
                ImageView iv[] = new ImageView[size];
                TextView nick[] = new TextView[size];
                TextView cont[] = new TextView[size];
                TextView date[] = new TextView[size];
                for (int i = 0; i < size; i++) {
                    iv[i] = new ImageView(context);
                    iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = comment.get(i).getAvatar();
                    glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ?
                            getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(iv[i]);
                    int position = i;
                    iv[i].setOnClickListener(v -> onProfileClick(comment.get(position).get_mid()));

                    rRelativeImg = new RelativeLayout(context);
                    RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    rRelativeImg.setLayoutParams(pImg);
                    rRelativeImg.setPadding(10, 10, 0, 10);
                    rRelativeImg.addView(iv[i]);
                    rAbsolute.addView(rRelativeImg);

                    nick[i] = new TextView(context);
                    String nickname = comment.get(i).getNickname();
                    nick[i].setText(nickname.length() <= Constant.LONG_STRING ? nickname : nickname.substring(0, Constant.LONG_STRING) + "...");
                    nick[i].setTextColor(Color.BLACK);
                    nick[i].setTextSize(20);

                    rRelativeNick = new RelativeLayout(context);
                    rRelativeNick.setId(i + nickInt);
                    RelativeLayout.LayoutParams pNick = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    pNick.setMargins(250, 0, 0, 0);
                    rRelativeNick.setLayoutParams(pNick);
                    rRelativeNick.addView(nick[i]);
                    rRelativeNick.setPadding(0, 10, 10, 0);
                    rRelativeImg.addView(rRelativeNick);

                    cont[i] = new TextView(context);
                    cont[i].setText(comment.get(i).getContent());
                    cont[i].setTextColor(Color.BLACK);
                    cont[i].setTextSize(12);
                    rRelativeCont = new RelativeLayout(context);
                    RelativeLayout.LayoutParams pCont = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    pCont.setMargins(250, 0, 0, 0);
                    pCont.addRule(RelativeLayout.BELOW, rRelativeNick.getId());
                    rRelativeCont.setId(i + contInt);
                    rRelativeCont.setPadding(0, 0, 10, 10);
                    rRelativeCont.setLayoutParams(pCont);
                    rRelativeCont.addView(cont[i]);
                    rRelativeImg.addView(rRelativeCont);

                    date[i] = new TextView(context);
                    String dateStr = comment.get(i).getDate();
                    date[i].setText(dateStr.substring(0, dateStr.indexOf("T")));
                    date[i].setTextColor(Color.BLACK);
                    date[i].setTextSize(10);

                    rRelativeDate = new RelativeLayout(context);
                    rRelativeDate.setId(i + dateInt);
                    RelativeLayout.LayoutParams pDate = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    pDate.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    pDate.setMargins(0, 0, 20, 0);
                    rRelativeDate.setLayoutParams(pDate);
                    rRelativeDate.addView(date[i]);
                    rRelativeImg.addView(rRelativeDate);
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setStroke(3, Color.BLACK);
                    rRelativeImg.setBackground(drawable);
                }
                utaiteinfo_timeline.addView(rAbsolute);
            }
            img2Check = true;
        }
        utaiteinfo_timeline.setVisibility(!text2Check ? View.VISIBLE : View.GONE);
        utaiteinfo_text2.setText(getString(R.string.utaiteinfo_txt2, !text2Check ? "▲" : "▼"));
        text2Check = !text2Check;
    }

    @OnClick(R.id.utaiteinfo_text3)
    public void onTextView3Click() {
        if (!text3Check && !img3Check) {
            requestRetrofitList(getArguments().getInt(getString(R.string.rest_aid)), count = 1);
            img3Check = true;
        }
        utaiteinfo_recyclerview.setVisibility(!text3Check ? View.VISIBLE : View.GONE);
        utaiteinfo_text3.setText(getString(R.string.utaiteinfo_txt3, !text3Check ? "▲" : "▼"));
        text3Check = !text3Check;
    }

    @OnClick(R.id.utaiteinfo_text1_src)
    public void onUtaiteRibbonClick() {
        if (MainActivity.MID == Constant.GUEST) {
            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_guest_err));
        } else {
            RestUtils.getRetrofit()
                    .create(RestUtils.UtaiteRibbon.class)
                    .utaiteRibbon(MainActivity.TOKEN, getArguments().getInt(getString(R.string.rest_aid)))
                    .compose(bindToLifecycle())
                    .distinct()
                    .subscribe(o -> {
                                ((MainActivity) context).getToast().setTextShow(getString(ribbonCheck ? R.string.utaiteinfo_not_ribbon : R.string.utaiteinfo_ribbon));
                                getFragmentManager().beginTransaction()
                                        .detach(this)
                                        .attach(this)
                                        .commit();
                            },
                            e -> {
                                ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                                Log.e(TAG, e.toString());
                            });
        }
    }

    @OnClick(R.id.utaiteinfo_timeline_button)
    public void onUtaiteTimelineClick() {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(utaiteinfo_timeline_edittext.getWindowToken(), 0);
        if (MainActivity.MID == Constant.GUEST) {
            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_guest_err));
            utaiteinfo_timeline_edittext.getText().clear();
        } else {
            RestUtils.getRetrofit()
                    .create(RestUtils.UtaiteTimeline.class)
                    .utaiteTimeline(MainActivity.TOKEN, getArguments().getInt(getString(R.string.rest_aid)),
                            utaiteinfo_timeline_edittext.getText().toString().trim())
                    .compose(bindToLifecycle())
                    .distinct()
                    .subscribe(o -> {
                                utaiteinfo_timeline_edittext.getText().clear();
                                getFragmentManager().beginTransaction()
                                        .detach(this)
                                        .attach(this)
                                        .commit();
                            },
                            e -> Log.e(TAG, e.toString()));
        }
    }

    public void requestRetrofit(String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.UtaiteApi.class)
                .utaiteApi(what, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            utaite = response;
                            utaiteinfo_id.setText(utaite.getAritst().getAritst_en());

                            String cover = utaite.getAritst().getArtist_cover();
                            glide.load(RestUtils.BASE + (cover == null ?
                                    getString(R.string.rest_images_artist) : getString(R.string.rest_artist_image) + utaite.getAritst().getArtist_cover()))
                                    .bitmapTransform(new CropCircleTransformation(context))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(utaiteinfo_img);

                            if (utaite.getAritst().getArtist_background() == null) {
                                utaiteinfo_bg1.setImageResource(android.R.color.transparent);
                                utaiteinfo_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
                            } else {
                                glide.load(RestUtils.BASE + getString(R.string.rest_artist_cover) + utaite.getAritst().getArtist_background())
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(utaiteinfo_bg1);
                            }

                            for (Comment e : utaite.getHearter()) {
                                if (!ribbonCheck) {
                                    ribbonCheck = MainActivity.MID == Integer.parseInt(e.get_mid());
                                }
                            }
                            utaiteinfo_text1_src.setBackground(getResources().getDrawable(ribbonCheck ? R.drawable.circle_yellow : R.drawable.circle_black));
                            str1Check = ribbonCheck ? getString(R.string.utaiteinfo_txt1_1) : getString(R.string.utaiteinfo_txt1_2);
                            Chained.setTextMany(new TextView[]{utaiteinfo_text1, utaiteinfo_text2, utaiteinfo_text3},
                                    new String[]{"▼" + str1Check, getString(R.string.utaiteinfo_txt2, "▼"), getString(R.string.utaiteinfo_txt3, "▼")});
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitList(int aid, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.ArraySongApis.class)
                .arraySongApis(aid, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(songs -> {
                    for (Song e : songs) {
                        vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                e.get_sid(), e.get_aid()));
                    }
                    mainAdapter.notifyDataSetChanged();
                    requestRetrofitList(getArguments().getInt(getString(R.string.rest_aid)), ++count);
                }, e -> mainAdapter.notifyDataSetChanged());
    }

    public void onProfileClick(String mid) {
        Fragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(context.getString(R.string.rest_mid), Integer.parseInt(mid));
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(null)
                .commit();
    }

}