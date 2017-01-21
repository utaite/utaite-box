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
import com.yuyu.utaitebox.rest.Music;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.SearchMusic;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment1 extends RxFragment {

    private final String TAG = SearchFragment1.class.getSimpleName();

    private Context context;

    @BindView(R.id.search_recyclerview1)
    RecyclerView search_recyclerview1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search1, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @OnClick(R.id.search_txt1)
    public void onTextView1Click() {
        new MaterialDialog.Builder(context)
                .input(null, null, (dialog, input) -> {
                })
                .content(getString(R.string.search_dialog))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive((dialog, which) -> {
                    String result = dialog.getInputEditText().getText().toString().trim();
                    requestRetrofit(result);
                })
                .onNegative((dialog, which) -> dialog.cancel())
                .show();
    }

    public void requestRetrofit(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi.class)
                .searchApi(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            search_recyclerview1.setHasFixedSize(true);
                            search_recyclerview1.setLayoutManager(llm);
                            for (SearchMusic e : response.getMusic()) {
                                requestRetrofitSong(Integer.parseInt(e.get_source_id()));
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitSong(int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(getString(R.string.rest_song), index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(repo -> {
                            ArrayList<MainVO> vo = new ArrayList<>();
                            for (Music e : repo.getMusic()) {
                                vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                        e.get_sid(), e.get_aid()));
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

}
