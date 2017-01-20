package com.yuyu.utaitebox.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.chain.Chained;
import com.yuyu.utaitebox.rest.Comment;
import com.yuyu.utaitebox.rest.RestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserInfoFragment extends RxFragment {

    private final String TAG = UserInfoFragment.class.getSimpleName();

    private Context context;
    private RequestManager glide;

    @BindView(R.id.userinfo_bg1)
    ImageView userinfo_bg1;
    @BindView(R.id.userinfo_img)
    ImageView userinfo_img;
    @BindView(R.id.userinfo_id)
    TextView userinfo_id;
    @BindView(R.id.userinfo_text1)
    TextView userinfo_text1;
    @BindView(R.id.userinfo_recyclerview1)
    RecyclerView userinfo_recyclerview1;
    @BindView(R.id.userinfo_text2)
    TextView userinfo_text2;
    @BindView(R.id.userinfo_recyclerview2)
    RecyclerView userinfo_recyclerview2;
    @BindView(R.id.userinfo_text3)
    TextView userinfo_text3;
    @BindView(R.id.userinfo_recyclerview3)
    RecyclerView userinfo_recyclerview3;
    @BindView(R.id.userinfo_text4)
    TextView userinfo_text4;
    @BindView(R.id.userinfo_recyclerview4)
    RecyclerView userinfo_recyclerview4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        glide = Glide.with(context);
        requestRetrofit(getString(R.string.rest_member), getArguments().getInt(getString(R.string.rest_mid)));
        Chained.setVisibilityMany(View.GONE, userinfo_recyclerview1, userinfo_recyclerview2, userinfo_recyclerview3, userinfo_recyclerview4);
        Chained.setTextMany(new TextView[]{userinfo_text1, userinfo_text2, userinfo_text3, userinfo_text4},
                new String[]{getString(R.string.user_txt1), getString(R.string.user_txt2), getString(R.string.user_txt3), getString(R.string.user_txt4)});
        return view;
    }

    @OnClick(R.id.userinfo_text1)
    public void onTextView1Click() {

    }

    @OnClick(R.id.userinfo_text2)
    public void onTextView2Click() {

    }

    @OnClick(R.id.userinfo_text3)
    public void onTextView3Click() {

    }

    @OnClick(R.id.userinfo_text4)
    public void onTextView4Click() {

    }

    public void requestRetrofit(String what, int index) {
        ((MainActivity) context).getTask().onPostExecute(null);
        ((MainActivity) context).getTask().onPreExecute();

        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            userinfo_id.setText(response.getMember().getUsername());

                            glide.load(RestUtils.BASE + (response.getProfile().getAvatar() == null ?
                                    getString(R.string.rest_profile_image) + getString(R.string.rest_profile) : getString(R.string.rest_profile_image) + response.getProfile().getAvatar()))
                                    .bitmapTransform(new CropCircleTransformation(context))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(userinfo_img);

                            if (response.getProfile().getCover() == null) {
                                userinfo_bg1.setImageResource(android.R.color.transparent);
                                userinfo_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
                            } else {
                                glide.load(RestUtils.BASE + getString(R.string.rest_profile_image) + response.getProfile().getCover())
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(userinfo_bg1);
                            }
                            ((MainActivity) context).getTask().onPostExecute(null);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                            ((MainActivity) context).getTask().onPostExecute(null);
                        });
    }

}
