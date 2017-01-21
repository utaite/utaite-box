package com.yuyu.utaitebox.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.chain.Chained;
import com.yuyu.utaitebox.chain.ChainedArrayList;
import com.yuyu.utaitebox.rest.Playlist;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserInfoFragment extends RxFragment {

    private final String TAG = UserInfoFragment.class.getSimpleName();

    private Context context;
    private RequestManager glide;

    private boolean text1Check, img1Check, text2Check, img2Check, text3Check, img3Check, text4Check, img4Check;
    private ArrayList<Integer> counts;

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
                new String[]{getString(R.string.user_txt1, "▼", 0), getString(R.string.user_txt2, "▼", 0), getString(R.string.user_txt3, "▼", 0), getString(R.string.user_txt4, "▼", 0)});
        text1Check = img1Check = text2Check = img2Check = text3Check = img3Check = text4Check = img4Check = false;
        return view;
    }

    @OnClick(R.id.userinfo_text1)
    public void onTextView1Click() {
        if (!text1Check && !img1Check) {
            requestRetrofitMember(userinfo_recyclerview1, getArguments().getInt(getString(R.string.rest_mid)), getString(R.string.rest_ribbon));
            img1Check = true;
        }
        userinfo_recyclerview1.setVisibility(!text1Check ? View.VISIBLE : View.GONE);
        userinfo_text1.setText(getString(R.string.user_txt1, !text1Check ? "▲" : "▼", counts.get(0)));
        text1Check = !text1Check;
    }

    @OnClick(R.id.userinfo_text2)
    public void onTextView2Click() {
        if (!text2Check && !img2Check) {
            requestRetrofitMember(userinfo_recyclerview2, getArguments().getInt(getString(R.string.rest_mid)), getString(R.string.rest_listen));
            img2Check = true;
        }
        userinfo_recyclerview2.setVisibility(!text2Check ? View.VISIBLE : View.GONE);
        userinfo_text2.setText(getString(R.string.user_txt2, !text2Check ? "▲" : "▼", counts.get(1)));
        text2Check = !text2Check;
    }

    @OnClick(R.id.userinfo_text3)
    public void onTextView3Click() {
        if (!text3Check && !img3Check) {
            requestRetrofitMember(userinfo_recyclerview3, getArguments().getInt(getString(R.string.rest_mid)), getString(R.string.rest_upload));
            img3Check = true;
        }
        userinfo_recyclerview3.setVisibility(!text3Check ? View.VISIBLE : View.GONE);
        userinfo_text3.setText(getString(R.string.user_txt3, !text3Check ? "▲" : "▼", counts.get(2)));
        text3Check = !text3Check;
    }

    @OnClick(R.id.userinfo_text4)
    public void onTextView4Click() {
        if (!text4Check && !img4Check) {
            requestRetrofitMember(userinfo_recyclerview4, getArguments().getInt(getString(R.string.rest_mid)), getString(R.string.rest_playlist));
            img4Check = true;
        }
        userinfo_recyclerview4.setVisibility(!text4Check ? View.VISIBLE : View.GONE);
        userinfo_text4.setText(getString(R.string.user_txt4, !text4Check ? "▲" : "▼", counts.get(3)));
        text4Check = !text4Check;
    }

    public void requestRetrofit(String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            int playlist = 0;
                            if (response.getPlaylist().toString().equals("0.0")) {
                                playlist = 0;
                            } else {
                                String temp1 = response.getPlaylist().toString();
                                String temp2 = temp1.substring(temp1.lastIndexOf("=") + 1, temp1.lastIndexOf("."));
                                playlist = Integer.parseInt(temp2);
                            }

                            counts = new ChainedArrayList().addMany(Integer.parseInt(response.getRecent().getRibbon().getNumber()),
                                    Integer.parseInt(response.getListen()), Integer.parseInt(response.getUpload().getNumber()),
                                    playlist);
                            Chained.setTextMany(new TextView[]{userinfo_text1, userinfo_text2, userinfo_text3, userinfo_text4},
                                    new String[]{getString(R.string.user_txt1, "▼", counts.get(0)),
                                            getString(R.string.user_txt2, "▼", counts.get(1)),
                                            getString(R.string.user_txt3, "▼", counts.get(2)),
                                            getString(R.string.user_txt4, "▼", counts.get(3))});

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
                                glide.load(RestUtils.BASE + getString(R.string.rest_profile_cover) + response.getProfile().getCover())
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(userinfo_bg1);
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitMember(RecyclerView view, int index, String category) {
        RestUtils.getRetrofit()
                .create(RestUtils.MemberApi.class)
                .memberApi(index, category)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            view.setLayoutManager(llm);
                            if (!response.toString().equals("[]")) {
                                ArrayList<MainVO> vo = new ArrayList<>();
                                for (Song e : response) {
                                    vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                            e.get_sid(), e.get_aid()));
                                }
                                MainAdapter mainAdapter = new MainAdapter(context, ((MainActivity) context).getFragmentManager(), vo);
                                view.setAdapter(mainAdapter);
                                mainAdapter.notifyDataSetChanged();
                            }
                        },
                        e -> Log.e(TAG, e.toString()));
    }

}
