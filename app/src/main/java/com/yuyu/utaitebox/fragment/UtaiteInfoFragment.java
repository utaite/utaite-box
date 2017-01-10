package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.chain.Chained;
import com.yuyu.utaitebox.rest.Comment;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Utaite;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Subscriber;

public class UtaiteInfoFragment extends Fragment {

    private static final String TAG = UtaiteInfoFragment.class.getSimpleName();

    private Context context;
    private RequestManager glide;
    private Utaite utaite;
    private boolean ribbonCheck, text1Check, img1Check, text2Check, img2Check;
    private String str1Check;

    @BindView(R.id.utaiteInfo_bg1)
    ImageView utaiteInfo_bg1;
    @BindView(R.id.utaiteInfo_id)
    TextView utaiteInfo_id;
    @BindView(R.id.utaiteInfo_img)
    ImageView utaiteInfo_img;
    @BindView(R.id.utaiteInfo_text1)
    TextView utaiteInfo_text1;
    @BindView(R.id.utaiteInfo_text1_src)
    ImageView utaiteInfo_text1_src;
    @BindView(R.id.utaiteInfo_ribbon_img)
    LinearLayout utaiteInfo_ribbon_img;
    @BindView(R.id.utaiteInfo_text2)
    TextView utaiteInfo_text2;
    @BindView(R.id.utaiteInfo_timeline)
    LinearLayout utaiteInfo_timeline;
    @BindView(R.id.utaiteInfo_avatar)
    ImageView utaiteInfo_avatar;
    @BindView(R.id.utaiteInfo_text3)
    TextView utaiteInfo_text3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utaiteinfo, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        glide = Glide.with(context);
        Chained.setVisibilityMany(View.GONE, utaiteInfo_text1_src, utaiteInfo_ribbon_img, utaiteInfo_timeline);
        requestRetrofit(getString(R.string.rest_artist), getArguments().getInt(getString(R.string.rest_aid)));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ribbonCheck = text1Check = img1Check = text2Check = img2Check = false;
    }

    @OnClick(R.id.utaiteInfo_text1)
    public void utaiteInfo_text1() {
        if (!text1Check && !img1Check) {
            ArrayList<Comment> comment = utaite.getHearter();
            int temp = comment.size();
            if (temp != 0) {
                ImageView iv[] = new ImageView[temp];
                TextView tv[] = new TextView[temp];
                LinearLayout rAbsolute, rHorizontal = null;
                RelativeLayout rRelative;

                for (int i = 0; i < temp; i++) {
                    if (i % 4 == 0) {
                        rHorizontal = new LinearLayout(context);
                        rHorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        rHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        utaiteInfo_ribbon_img.addView(rHorizontal);
                    }

                    iv[i] = new ImageView(context);
                    iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = comment.get(i).getAvatar();
                    glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ? getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(iv[i]);

                    tv[i] = new TextView(context);
                    String nickname = comment.get(i).getNickname();
                    tv[i].setText(nickname.length() <= 10 ? nickname : nickname.substring(0, 10) + "...");
                    tv[i].setTextColor(Color.BLACK);

                    rAbsolute = new LinearLayout(context);
                    rAbsolute.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rAbsolute.setOrientation(LinearLayout.VERTICAL);
                    final LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    rHorizontal.addView(rAbsolute);
                }
            } else {
                TextView tv = new TextView(context);
                tv.setText(getString(R.string.not_ribbon));
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setGravity(Gravity.CENTER);
                utaiteInfo_ribbon_img.addView(tv);
            }
            img1Check = true;
        }
        utaiteInfo_ribbon_img.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        utaiteInfo_text1_src.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        utaiteInfo_text1.setText((!text1Check ? "▲" : "▼") + str1Check);
        text1Check = !text1Check;
    }

    @OnClick(R.id.utaiteInfo_text2)
    public void utaiteInfo_text2() {
        int nickInt = 1, contInt = 100, dateInt = 10000;
        if (!text2Check && !img2Check) {
            glide.load(RestUtils.BASE + (MainActivity.TEMP_COVER == null ?
                    getString(R.string.rest_profile_cover) + getString(R.string.rest_profile) : getString(R.string.rest_profile_image) + MainActivity.TEMP_COVER))
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(300, 300)
                    .into(utaiteInfo_avatar);

            ArrayList<Comment> comment = utaite.getComment();
            int temp = comment.size();
            if (temp != 0) {
                LinearLayout rAbsolute;
                rAbsolute = new LinearLayout(context);
                rAbsolute.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout rRelativeNick, rRelativeCont, rRelativeImg, rRelativeDate;
                ImageView iv[] = new ImageView[temp];
                TextView nick[] = new TextView[temp];
                TextView cont[] = new TextView[temp];
                TextView date[] = new TextView[temp];
                for (int i = 0; i < temp; i++) {
                    iv[i] = new ImageView(context);
                    iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = comment.get(i).getAvatar();
                    glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ?
                            getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(iv[i]);

                    rRelativeImg = new RelativeLayout(context);
                    RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    rRelativeImg.setLayoutParams(pImg);
                    rRelativeImg.setPadding(10, 10, 0, 10);
                    rRelativeImg.addView(iv[i]);
                    rAbsolute.addView(rRelativeImg);

                    nick[i] = new TextView(context);
                    String nickname = comment.get(i).getNickname();
                    nick[i].setText(nickname.length() <= 10 ? nickname : nickname.substring(0, 10) + "...");
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
                utaiteInfo_timeline.addView(rAbsolute);
            }
            img2Check = true;
        }
        utaiteInfo_timeline.setVisibility(!text2Check ? View.VISIBLE : View.GONE);
        utaiteInfo_text2.setText(getString(R.string.utaiteinfo_txt2, !text2Check ? "▲" : "▼"));
        text2Check = !text2Check;
    }

    public void requestRetrofit(String what, int index) {
        Task task = new Task(context, 1);
        task.onPreExecute();


        RestUtils.getRetrofit()
                .create(RestUtils.UtaiteApi.class)
                .utaiteApi(what, index)
                .subscribe(new Subscriber<Utaite>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        task.onPostExecute(null);
                    }

                    @Override
                    public void onNext(Utaite response) {
                        utaite = response;
                        task.onPostExecute(null);
                        utaiteInfo_id.setText(utaite.getAritst().getAritst_en());

                        String cover = utaite.getAritst().getArtist_cover();
                        glide.load(RestUtils.BASE + (cover == null ?
                                getString(R.string.rest_images_artist) : getString(R.string.rest_artist_image) + utaite.getAritst().getArtist_cover()))
                                .bitmapTransform(new CropCircleTransformation(context))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(utaiteInfo_img);

                        if (utaite.getAritst().getArtist_background() == null) {
                            utaiteInfo_bg1.setImageResource(android.R.color.transparent);
                            utaiteInfo_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
                        } else {
                            glide.load(RestUtils.BASE + getString(R.string.rest_artist_cover) + utaite.getAritst().getArtist_background())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(utaiteInfo_bg1);
                        }

                        for (Comment e : utaite.getHearter()) {
                            if (!ribbonCheck) {
                                ribbonCheck = MainActivity.MID == Integer.parseInt(e.get_mid());
                            }
                        }
                        utaiteInfo_text1_src.setBackground(getResources().getDrawable(ribbonCheck ? R.drawable.circle_yellow : R.drawable.circle_black));
                        str1Check = ribbonCheck ? getString(R.string.utaiteinfo_txt1_1) : getString(R.string.utaiteinfo_txt1_2);
                        Chained.setTextMany(new TextView[]{utaiteInfo_text1, utaiteInfo_text2, utaiteInfo_text3},
                                new String[]{"▼" + str1Check, getString(R.string.utaiteinfo_txt2, "▼"), getString(R.string.utaiteinfo_txt3, "▼")});
                    }
                });
    }
}