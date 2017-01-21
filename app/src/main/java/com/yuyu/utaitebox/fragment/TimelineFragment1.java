package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.rest.Left;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TimelineFragment1 extends RxFragment {

    private final String TAG = TimelineFragment1.class.getSimpleName();

    private Context context;

    @BindView(R.id.timeline_view1)
    LinearLayout timeline_view1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline1, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        requestRetrofit(getString(R.string.rest_timeline));
        return view;
    }

    public void requestRetrofit(String what) {
        ((MainActivity) context).getTask().onPostExecute(null);
        ((MainActivity) context).getTask().onPreExecute();

        RestUtils.getRetrofit()
                .create(RestUtils.TimelineApi.class)
                .timelineApi(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            initialize(response.getLeft());
                            ((MainActivity) context).getTask().onPostExecute(null);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                            ((MainActivity) context).getTask().onPostExecute(null);
                        });
    }

    public void initialize(ArrayList<Left> left) {
        RequestManager glide = Glide.with(context);
        int nickInt = 1, titleInt = 100, dateInt = 1000000;
        int size = left.size();

        if (size != 0) {
            LinearLayout rAbsolute;
            rAbsolute = new LinearLayout(context);
            rAbsolute.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout rRelativeNick, rRelativeCont, rRelativeImg, rRelativeDate;
            ImageView iv[] = new ImageView[size];
            TextView nick[] = new TextView[size];
            TextView title[] = new TextView[size];
            TextView date[] = new TextView[size];

            for (int i = 0; i < size; i++) {
                iv[i] = new ImageView(context);
                iv[i].setScaleType(ImageView.ScaleType.FIT_XY);
                String avatar = left.get(i).getAvatar();
                glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ? getString(R.string.rest_profile) : avatar))
                        .bitmapTransform(new CropCircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv[i]);
                int position = i;
                iv[i].setOnClickListener(v -> {
                    Fragment fragment = new UserInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(context.getString(R.string.rest_mid), Integer.parseInt(left.get(position).get_mid()));
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_main, fragment)
                            .addToBackStack(null)
                            .commit();
                });

                rRelativeImg = new RelativeLayout(context);
                RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                rRelativeImg.setLayoutParams(pImg);
                rRelativeImg.setPadding(10, 10, 0, 10);
                rRelativeImg.addView(iv[i]);
                rAbsolute.addView(rRelativeImg);

                nick[i] = new TextView(context);
                String nickname = left.get(i).getNickname() == null ? getString(R.string.rest_guest) : left.get(i).getNickname();
                nick[i].setText(nickname.length() <= Constant.LONG_STRING ? nickname : nickname.substring(0, Constant.LONG_STRING) + "...");
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

                title[i] = new TextView(context);
                title[i].setText(left.get(i).getSong_original() + " - " + left.get(i).getArtist_en());
                title[i].setTextColor(Color.BLACK);
                title[i].setTextSize(12);

                rRelativeCont = new RelativeLayout(context);
                RelativeLayout.LayoutParams pCont = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                pCont.setMargins(250, 0, 0, 0);
                pCont.addRule(RelativeLayout.BELOW, rRelativeNick.getId());
                rRelativeCont.setId(i + titleInt);
                rRelativeCont.setPadding(0, 0, 10, 10);
                rRelativeCont.setLayoutParams(pCont);
                rRelativeCont.addView(title[i]);
                rRelativeImg.addView(rRelativeCont);

                date[i] = new TextView(context);
                String temp1 = left.get(i).getDate();
                String temp2 = temp1.substring(temp1.indexOf("T") + 1);
                String temp3 = temp2.substring(0, temp2.length() - 5);
                int temp4 = Integer.parseInt(temp3.substring(0, 2)) + 9;
                int temp5 = temp4 > 24 ? temp4 - 24 : temp4;
                String temp6 = temp5 < 10 ? "0" + temp5 : String.valueOf(temp5);
                String temp7 = temp6 + temp3.substring(2);
                date[i].setText(temp7);
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
            timeline_view1.addView(rAbsolute);
        }
    }

}
