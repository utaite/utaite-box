package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.yuyu.utaitebox.rest.Other;
import com.yuyu.utaitebox.rest.Repo;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Ribbon;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.utils.MainVO;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;

public class MusicInfoFragment extends Fragment {

    private static final String TAG = MusicInfoFragment.class.getSimpleName();

    private Repo repo;
    private Task task;
    private Context context;
    private RequestManager requestManager;
    private MediaPlayer mediaPlayer;

    private ArrayList<MainVO> vo;
    private String str2Check;
    private boolean ribbonCheck, text1Check, img1Check, text2Check, img2Check, text3Check, img3Check;

    @BindView(R.id.musicInfo_recyclerview)
    RecyclerView musicInfo_recyclerview;
    @BindView(R.id.musicInfo_cover)
    ImageView musicInfo_cover;
    @BindView(R.id.musicInfo_utaite)
    TextView musicInfo_utaite;
    @BindView(R.id.musicInfo_song)
    TextView musicInfo_song;
    @BindView(R.id.musicInfo_songkr)
    TextView musicInfo_songkr;
    @BindView(R.id.musicInfo_ribbon)
    TextView musicInfo_ribbon;
    @BindView(R.id.musicInfo_addlist)
    TextView musicInfo_addlist;
    @BindView(R.id.musicInfo_ribbon_src)
    ImageView musicInfo_ribbon_src;
    @BindView(R.id.musicInfo_text2_src)
    ImageView musicInfo_text2_src;
    @BindView(R.id.musicInfo_ribbon_right)
    TextView musicInfo_ribbon_right;
    @BindView(R.id.musicInfo_played_right)
    TextView musicInfo_played_right;
    @BindView(R.id.musicInfo_comment_right)
    TextView musicInfo_comment_right;
    @BindView(R.id.musicInfo_status)
    RelativeLayout musicInfo_status;
    @BindView(R.id.musicInfo_text1)
    TextView musicInfo_text1;
    @BindView(R.id.musicInfo_text2)
    TextView musicInfo_text2;
    @BindView(R.id.musicInfo_ribbon_img)
    LinearLayout musicInfo_ribbon_img;
    @BindView(R.id.musicInfo_timeline)
    LinearLayout musicInfo_timeline;
    @BindView(R.id.musicInfo_avatar)
    ImageView musicInfo_avatar;
    @BindView(R.id.musicInfo_text3)
    TextView musicInfo_text3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicinfo, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        requestManager = Glide.with(context);
        initialize();
        return view;
    }

    public void initialize() {
        task = new Task(context, 1);
        task.onPreExecute();
        Chained.setVisibilityMany(View.GONE, musicInfo_text2_src, musicInfo_text2_src, musicInfo_ribbon_img, musicInfo_timeline, musicInfo_status);
        requestRetrofit(getString(R.string.rest_song), getArguments().getInt(getString(R.string.rest_sid)));
    }

    @Override
    public void onStart() {
        ribbonCheck = text1Check = img1Check = text2Check = img2Check = text3Check = img3Check = false;
        super.onStart();
    }

    @OnClick(R.id.musicInfo_text1)
    public void musicInfo_text1() {
        if (!text1Check && !img1Check) {
            for (Other e : repo.getSidebar().getOther()) {
                requestRetrofit(getString(R.string.rest_song), Integer.parseInt(e.get_sid()));
            }
            img1Check = true;
        }
        musicInfo_recyclerview.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        musicInfo_text1.setText(getString(R.string.musicinfo_txt1, !text1Check ? "▲" : "▼"));
        text1Check = !text1Check;
    }

    @OnClick(R.id.musicInfo_text2)
    public void musicInfo_text2() {
        if (!text2Check && !img2Check) {
            ArrayList<Ribbon> ribbon = repo.getSidebar().getRibbon();
            int size = ribbon.size();
            if (size != 0) {
                ImageView iv[] = new ImageView[size];
                TextView tv[] = new TextView[size];
                LinearLayout rAbsolute, rHorizontal = null;
                RelativeLayout rRelative;

                for (int i = 0; i < size; i++) {
                    if (i % 4 == 0) {
                        rHorizontal = new LinearLayout(context);
                        rHorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        rHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        musicInfo_ribbon_img.addView(rHorizontal);
                    }

                    iv[i] = new ImageView(context);
                    iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = ribbon.get(i).getAvatar();
                    requestManager.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ? getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(iv[i]);

                    tv[i] = new TextView(context);
                    String nickname = ribbon.get(i).getNickname();
                    tv[i].setText(nickname.length() <= 10 ? nickname : nickname.substring(0, 10) + "...");
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
                    rHorizontal.addView(rAbsolute);
                }
            } else {

                TextView tv = new TextView(context);
                tv.setText(R.string.not_ribbon);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setGravity(Gravity.CENTER);
                musicInfo_ribbon_img.addView(tv);
            }
            img2Check = true;
        }
        musicInfo_ribbon_img.setVisibility(!text2Check ? View.VISIBLE : View.GONE);
        musicInfo_text2_src.setVisibility(!text2Check ? View.VISIBLE : View.GONE);
        musicInfo_text2.setText((!text2Check ? "▲" : "▼") + str2Check);
        text2Check = !text2Check;
    }

    @OnClick(R.id.musicInfo_text3)
    public void musicInfo_text3() {
        int nickInt = 1, contInt = 1001, dateInt = 2001;
        if (!text3Check && !img3Check) {
            requestManager.load(RestUtils.BASE + (MainActivity.TEMP_COVER == null ? getString(R.string.rest_profile_cover) + getString(R.string.rest_profile) : getString(R.string.rest_profile_image) + MainActivity.TEMP_COVER))
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(300, 300)
                    .into(musicInfo_avatar);
            ArrayList<Comment> comment = repo.getComment();
            int size = comment.size();
            if (size != 0) {
                LinearLayout rAbsolute;
                rAbsolute = new LinearLayout(context);
                rAbsolute.setOrientation(LinearLayout.VERTICAL);
                RelativeLayout rRelativeNick, rRelativeCont, rRelativeImg, rRelativeDate;
                ImageView img[] = new ImageView[size];
                TextView nick[] = new TextView[size];
                TextView cont[] = new TextView[size];
                TextView date[] = new TextView[size];

                for (int i = 0; i < size; i++) {
                    img[i] = new ImageView(context);
                    img[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    String avatar = comment.get(i).getAvatar();
                    requestManager.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ? getString(R.string.rest_profile) : avatar))
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img[i]);

                    rRelativeImg = new RelativeLayout(context);
                    RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    rRelativeImg.setLayoutParams(pImg);
                    rRelativeImg.setPadding(10, 10, 0, 10);
                    rRelativeImg.addView(img[i]);
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
                    rRelativeNick.setPadding(0, 10, 10, 0);
                    rRelativeNick.addView(nick[i]);
                    rRelativeImg.addView(rRelativeNick);

                    cont[i] = new TextView(context);
                    cont[i].setText(!comment.get(i).getContent().equals("") ? comment.get(i).getContent() : comment.get(i).getType().equals("1") ? "/*   Upload music   */" : "/*   Upload cover image   */");
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
                musicInfo_timeline.addView(rAbsolute);
            }
            img3Check = true;
        }
        musicInfo_timeline.setVisibility(!text3Check ? View.VISIBLE : View.GONE);
        musicInfo_text3.setText(getString(R.string.musicinfo_txt3, !text3Check ? "▲" : "▼"));
        text3Check = !text3Check;
    }

    @OnClick(R.id.musicInfo_play)
    public void musicInfo_play() {
        mediaPlayer = new MediaPlayer();
        task.onPreExecute();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(RestUtils.BASE + getString(R.string.rest_stream) + repo.getSong().getKey());
        } catch (Exception ignored) {
        }
        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            task.onPostExecute(null);
        });
        mediaPlayer.prepareAsync();
    }

    public void requestRetrofit(String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .subscribe(new Subscriber<Repo>() {
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
                    public void onNext(Repo response) {
                        task.onPostExecute(null);
                        if (repo == null) {
                            repo = response;
                            String cover = repo.getSong().getCover();
                            requestManager.load(RestUtils.BASE + (cover == null ? getString(R.string.rest_images_cover) : getString(R.string.rest_cover) + cover))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(musicInfo_cover);

                            for (Ribbon e : repo.getSidebar().getRibbon()) {
                                if (!ribbonCheck) {
                                    ribbonCheck = MainActivity.MID == Integer.parseInt(e.get_mid());
                                }
                            }
                            musicInfo_ribbon_src.setBackground(getResources().getDrawable(ribbonCheck ? R.drawable.circle_yellow : R.drawable.circle_black));
                            musicInfo_text2_src.setBackground(getResources().getDrawable(ribbonCheck ? R.drawable.circle_yellow : R.drawable.circle_black));
                            str2Check = getString(ribbonCheck ? R.string.musicinfo_txt2_1 : R.string.musicinfo_txt2_2);

                            vo = new ArrayList<>();
                            String played = repo.getSong().getPlayed();
                            if (Integer.parseInt(played) >= 1000) {
                                played = String.valueOf(Integer.parseInt(played) / 1000) + "." + String.valueOf(Integer.parseInt(played) % 1000).substring(0, 1) + "K+";
                            }
                            musicInfo_played_right.setText(played);
                            musicInfo_status.setVisibility(View.VISIBLE);
                            musicInfo_recyclerview.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            musicInfo_recyclerview.setLayoutManager(llm);

                            Chained.setTextMany(new TextView[]{musicInfo_text1, musicInfo_text2, musicInfo_text3, musicInfo_utaite, musicInfo_song, musicInfo_songkr, musicInfo_ribbon_right, musicInfo_comment_right},
                                    new String[]{getString(R.string.musicinfo_txt1, "▼"), "▼" + str2Check, getString(R.string.musicinfo_txt3, "▼"), repo.getSong().getArtist_en(), repo.getSong().getSong_original(), repo.getSong().getSong_kr(), repo.getSong().getRibbon(), repo.getSong().getComment()});
                            Chained.setPaintFlagsMany(musicInfo_ribbon_right, musicInfo_songkr, musicInfo_ribbon, musicInfo_addlist, musicInfo_utaite, musicInfo_song, musicInfo_played_right, musicInfo_comment_right);
                        } else {
                            Song song = response.getSong();
                            vo.add(new MainVO(song.getCover(), song.getArtist_cover(), song.getSong_original(), song.getArtist_en(),
                                    song.get_sid(), song.get_aid()));
                            musicInfo_recyclerview.setAdapter(new MainAdapter(context, getFragmentManager(), vo));
                        }
                    }
                });
    }

}