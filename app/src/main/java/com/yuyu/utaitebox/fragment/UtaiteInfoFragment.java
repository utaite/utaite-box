package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import com.yuyu.utaitebox.retrofit.Comment;
import com.yuyu.utaitebox.retrofit.Utaite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UtaiteInfoFragment extends Fragment {

    private View view;
    private Context context;
    private RequestManager glideManager;
    private Utaite utaite;
    private boolean ribbonCheck, text1Check, img1Check, text2Check, img2Check;
    private String str1Check;

    @BindView(R.id.utaiteinfo_bg1)
    ImageView utaiteinfo_bg1;
    @BindView(R.id.utaiteinfo_id)
    TextView utaiteinfo_id;
    @BindView(R.id.utaiteinfo_img)
    ImageView utaiteinfo_img;
    @BindView(R.id.utaiteinfo_text1)
    TextView utaiteinfo_text1;
    @BindView(R.id.utaiteinfo_text1src)
    ImageView utaiteinfo_text1src;
    @BindView(R.id.utaiteinfo_ribbonimg)
    LinearLayout utaiteinfo_ribbonimg;
    @BindView(R.id.utaiteinfo_text2)
    TextView utaiteinfo_text2;
    @BindView(R.id.utaiteinfo_timeline)
    LinearLayout utaiteinfo_timeline;
    @BindView(R.id.utaiteinfo_avatar)
    ImageView utaiteinfo_avatar;

    public UtaiteInfoFragment(Context context, RequestManager glideManager, Utaite utaite) {
        this.context = context;
        this.glideManager = glideManager;
        this.utaite = utaite;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_utaiteinfo, container, false);
        ButterKnife.bind(this, view);
        utaiteinfo_id.setText(utaite.getAritst().getAritst_en());
        if (utaite.getAritst().getArtist_cover() != null) {
            Glide.with(this).load(MainActivity.BASE + "/res/artist/image/" + utaite.getAritst().getArtist_cover())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(utaiteinfo_img);
        } else {
            Glide.with(this).load(MainActivity.BASE + "/images/artist.jpg")
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(utaiteinfo_img);
        }
        if (utaite.getAritst().getArtist_background() != null) {
            glideManager.load(MainActivity.BASE + "/res/artist/cover/" + utaite.getAritst().getArtist_background())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(utaiteinfo_bg1);
        } else {
            utaiteinfo_bg1.setImageResource(android.R.color.transparent);
            utaiteinfo_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
        }
        for (Comment e : utaite.getHearter()) {
            if (MainActivity._mid == Integer.parseInt(e.get_mid())) {
                ribbonCheck = true;
                break;
            } else {
                ribbonCheck = false;
            }
        }
        if (ribbonCheck) {
            utaiteinfo_text1src.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
            str1Check = "  우타이테에 좋아요를 눌러주셨어요.";
        } else {
            utaiteinfo_text1src.setBackground(getResources().getDrawable(R.drawable.circle_black));
            str1Check = "  우타이테에 좋아요를 눌러보세요.";
        }
        utaiteinfo_text1.setText("▼" + str1Check);
        utaiteinfo_text1src.setVisibility(View.GONE);
        utaiteinfo_ribbonimg.setVisibility(View.GONE);
        final LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.setMargins(23, 10, 23, 10);
        utaiteinfo_text1.setOnClickListener(view1 -> {
            if (!text1Check) {
                if (!img1Check) {
                    ArrayList<Comment> ribbon1 = utaite.getHearter();
                    int temp = ribbon1.size();
                    if (temp != 0) {
                        ImageView img[] = new ImageView[temp];
                        TextView iv[] = new TextView[temp];
                        LinearLayout rHorizontal = null, rAbsolute;
                        RelativeLayout rRelative;
                        for (int i = 0; i < temp; i++) {
                            if (i % 4 == 0) {
                                rHorizontal = new LinearLayout(context);
                                rHorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                rHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                                utaiteinfo_ribbonimg.addView(rHorizontal);
                            }
                            img[i] = new ImageView(context);
                            img[i].setScaleType(ImageView.ScaleType.FIT_XY);
                            String avatar = ribbon1.get(i).getAvatar();
                            if (avatar != null) {
                                glideManager.load(MainActivity.BASE + "/res/profile/image/" + avatar)
                                        .bitmapTransform(new CropCircleTransformation(context))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img[i]);
                            } else {
                                glideManager.load(MainActivity.BASE + "/res/profile/image/" + MainActivity.PROFILE)
                                        .bitmapTransform(new CropCircleTransformation(context))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img[i]);
                            }
                            iv[i] = new TextView(context);
                            if(ribbon1.get(i).getNickname().length() <= 10) {
                                iv[i].setText(ribbon1.get(i).getNickname());
                            } else {
                                iv[i].setText(ribbon1.get(i).getNickname().substring(0, 10) + "...");
                            }
                            iv[i].setTextColor(Color.BLACK);
                            rAbsolute = new LinearLayout(context);
                            rAbsolute.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            rAbsolute.setOrientation(LinearLayout.VERTICAL);
                            rAbsolute.addView(img[i], param1);
                            rAbsolute.setId(i + 1);
                            rRelative = new RelativeLayout(context);
                            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            p.addRule(RelativeLayout.BELOW, rAbsolute.getId());
                            rRelative.setLayoutParams(p);
                            rRelative.addView(iv[i]);
                            rAbsolute.addView(rRelative);
                            rAbsolute.setGravity(Gravity.CENTER);
                            rHorizontal.addView(rAbsolute);
                        }
                    } else {
                        TextView iv = new TextView(context);
                        iv.setText("아직 아무도 좋아요를 누르지 않았어요.\n" +
                                "첫 좋아요를 눌러보세요!");
                        iv.setTextColor(Color.BLACK);
                        iv.setTextSize(20);
                        iv.setGravity(Gravity.CENTER);
                        utaiteinfo_ribbonimg.addView(iv);
                    }
                    img1Check = true;
                }
                utaiteinfo_ribbonimg.setVisibility(View.VISIBLE);
                utaiteinfo_text1src.setVisibility(View.VISIBLE);
                utaiteinfo_text1.setText("▲" + str1Check);
            } else {
                utaiteinfo_ribbonimg.setVisibility(View.GONE);
                utaiteinfo_text1src.setVisibility(View.GONE);
                utaiteinfo_text1.setText("▼" + str1Check);
            }
            text1Check = !text1Check;
        });
        utaiteinfo_timeline.setVisibility(View.GONE);
        final String txt2 = "  어떤 느낌의 우타이테인가요?";
        final int nickInt = 1, contInt = 100, dateInt = 10000;
        utaiteinfo_text2.setText("▼" + txt2);
        utaiteinfo_text2.setOnClickListener(view12 -> {
            if (!text2Check) {
                if (!img2Check) {
                    if (MainActivity.PROFILE != null) {
                        glideManager.load(MainActivity.BASE + "/res/profile/image/" + MainActivity.tempCover)
                                .bitmapTransform(new CropCircleTransformation(context))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .override(300, 300)
                                .into(utaiteinfo_avatar);
                    } else {
                        glideManager.load(MainActivity.BASE + "/res/profile/cover/" + MainActivity.PROFILE)
                                .bitmapTransform(new CropCircleTransformation(context))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .override(300, 300)
                                .into(utaiteinfo_avatar);
                    }
                    ArrayList<Comment> comment = utaite.getComment();
                    int temp = comment.size();
                    if (temp != 0) {
                        LinearLayout rAbsolute;
                        rAbsolute = new LinearLayout(context);
                        rAbsolute.setOrientation(LinearLayout.VERTICAL);

                        RelativeLayout rRelativeNick, rRelativeCont, rRelativeImg, rRelativeDate;
                        ImageView img[] = new ImageView[temp];
                        TextView nick[] = new TextView[temp];
                        TextView cont[] = new TextView[temp];
                        TextView date[] = new TextView[temp];
                        for (int i = 0; i < temp; i++) {
                            img[i] = new ImageView(context);
                            img[i].setScaleType(ImageView.ScaleType.FIT_XY);
                            String avatar = comment.get(i).getAvatar();
                            if (avatar != null) {
                                glideManager.load(MainActivity.BASE + "/res/profile/image/" + avatar)
                                        .bitmapTransform(new CropCircleTransformation(context))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img[i]);
                            } else {
                                glideManager.load(MainActivity.BASE + "/res/profile/image/" + MainActivity.PROFILE)
                                        .bitmapTransform(new CropCircleTransformation(context))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(img[i]);
                            }
                            rRelativeImg = new RelativeLayout(context);
                            RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            rRelativeImg.setLayoutParams(pImg);
                            rRelativeImg.setPadding(10, 10, 0, 10);
                            rRelativeImg.addView(img[i]);
                            rAbsolute.addView(rRelativeImg);

                            nick[i] = new TextView(context);
                            if(comment.get(i).getNickname().length() <= 10) {
                                nick[i].setText(comment.get(i).getNickname());
                            } else {
                                nick[i].setText(comment.get(i).getNickname().substring(0, 10) + "...");
                            }
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
                            rRelativeImg.setBackgroundDrawable(drawable);
                        }
                        utaiteinfo_timeline.addView(rAbsolute);
                    }
                    img2Check = true;
                }
                utaiteinfo_timeline.setVisibility(View.VISIBLE);
                utaiteinfo_text2.setText("▲" + txt2);
            } else {
                utaiteinfo_timeline.setVisibility(View.GONE);
                utaiteinfo_text2.setText("▼" + txt2);
            }
            text2Check = !text2Check;
        });

        return view;
    }

    @Override
    public void onStart() {
        ribbonCheck = false;
        text1Check = false;
        img1Check = false;
        text2Check = false;
        img2Check = false;
        super.onStart();
    }

}