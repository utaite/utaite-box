package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Right;
import com.yuyu.utaitebox.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TimelineFragment2 extends RxFragment {

    private final String TAG = TimelineFragment2.class.getSimpleName();

    private Context context;

    @BindView(R.id.timeline_view2)
    LinearLayout timeline_view2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline2, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        requestRetrofit(getString(R.string.rest_timeline));
        return view;
    }

    public void requestRetrofit(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.TimelineApi.class)
                .timelineApi(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            initialize(response.getRight());
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void initialize(ArrayList<Right> right) {
        int nickInt = 1, contInt = 100, dateInt = 10000;
        RequestManager glide = Glide.with(context);
        int size = right.size();

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
                String avatar = right.get(i).getAvatar();
                glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + (avatar == null ?
                        getString(R.string.rest_profile) : avatar))
                        .bitmapTransform(new CropCircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv[i]);
                int position = i;
                iv[i].setOnClickListener(v -> onProfileClick(right, position));

                rRelativeImg = new RelativeLayout(context);
                RelativeLayout.LayoutParams pImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                rRelativeImg.setLayoutParams(pImg);
                rRelativeImg.setPadding(10, 10, 0, 10);
                rRelativeImg.addView(iv[i]);
                rAbsolute.addView(rRelativeImg);

                nick[i] = new TextView(context);
                String nickname = right.get(i).getNickname();
                nick[i].setText(nickname.length() <= Constant.LONG_STRING ? nickname : nickname.substring(0, Constant.LONG_STRING) + "...");
                nick[i].setTextColor(Color.BLACK);
                nick[i].setTextSize(20);
                nick[i].setOnClickListener(v -> onProfileClick(right, position));

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
                cont[i].setText(right.get(i).getContent());
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
                String dateStr = right.get(i).getDate();
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
            timeline_view2.addView(rAbsolute);
        }
    }

    public void onProfileClick(ArrayList<Right> right, int position) {
        Fragment fragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(context.getString(R.string.rest_mid), Integer.parseInt(right.get(position).get_mid()));
        fragment.setArguments(bundle);
        ((MainActivity) context).getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(null)
                .commit();
    }

}
