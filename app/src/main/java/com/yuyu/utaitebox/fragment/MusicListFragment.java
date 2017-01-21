package com.yuyu.utaitebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicListFragment extends RxFragment {

    private static final String TAG = MusicListFragment.class.getSimpleName();

    private Context context;
    private ArrayList<MainVO> vo;
    private MainAdapter mainAdapter;

    private int count = 1;

    @BindView(R.id.musiclist_recyclerview)
    RecyclerView musiclist_recyclerview;
    @BindView(R.id.musiclist_next)
    TextView musiclist_next;
    @BindView(R.id.musiclist_prev)
    TextView musiclist_prev;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musiclist, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
        return view;
    }

    public void initialize() {
        musiclist_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        musiclist_recyclerview.setLayoutManager(llm);
        vo = new ArrayList<>();

        requestRetrofit(getString(R.string.rest_songlist), count);
        mainAdapter = new MainAdapter(context, ((MainActivity) context).getFragmentManager(), vo);
        musiclist_recyclerview.setAdapter(mainAdapter);
        musiclist_prev.setText(getString(R.string.musiclist_txt1));
    }

    @OnClick(R.id.musiclist_search)
    public void onSearchButtonClick() {
        new MaterialDialog.Builder(context)
                .input(String.valueOf(count), null, (dialog, input) -> {
                })
                .content(getString(R.string.musiclist_search, count))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive((dialog, which) -> {
                    vo.clear();
                    mainAdapter.notifyDataSetChanged();
                    requestRetrofit(getString(R.string.rest_songlist),
                            count = Integer.parseInt(dialog.getInputEditText().getText().toString().trim()));
                    musiclist_prev.setText(getString(count == 1 ? R.string.musiclist_txt1 : R.string.musiclist_txt2));
                })
                .onNegative((dialog, which) -> dialog.cancel())
                .show();
    }

    @OnClick(R.id.musiclist_prev)
    public void onPrevButtonClick() {
        if (count > 1) {
            vo.clear();
            mainAdapter.notifyDataSetChanged();
            requestRetrofit(getString(R.string.rest_songlist), --count);
            musiclist_prev.setText(getString(count == 1 ? R.string.musiclist_txt1 : R.string.musiclist_txt2));
        }
    }

    @OnClick(R.id.musiclist_next)
    public void onNextButtonClick() {
        vo.clear();
        mainAdapter.notifyDataSetChanged();
        musiclist_prev.setText(R.string.musiclist_txt2);
        requestRetrofit(getString(R.string.rest_songlist), ++count);
    }

    public void requestRetrofit(String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.ArraySongApi.class)
                .arraySongApi(what, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(songs -> {
                    for (Song e : songs) {
                        vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                e.get_sid(), e.get_aid()));
                    }
                    mainAdapter.notifyDataSetChanged();
                }, e -> {
                    Log.e(TAG, e.toString());
                    ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_not_found));
                });
    }

}
