package com.yuyu.utaitebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.adapter.UtaiteAdapter;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.SearchArtist;
import com.yuyu.utaitebox.rest.SearchMusic;
import com.yuyu.utaitebox.rest.Source;
import com.yuyu.utaitebox.rest.Utaite;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends RxFragment {

    private final String TAG = SearchFragment.class.getSimpleName();


    private Context context;
    private ArrayList<MainVO> mainList;
    private MainAdapter mainAdapter;
    private ArrayList<Utaite> utaiteList;
    private UtaiteAdapter utaiteAdapter;

    @BindView(R.id.search_recyclerview)
    RecyclerView search_recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @OnClick(R.id.search_music_btn)
    public void onMusicSearchButtonClick() {
        getFragmentManager().beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

        new MaterialDialog.Builder(context)
                .input(null, null, (dialog, input) -> {
                })
                .content(getString(R.string.search_dialog))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive((dialog, which) -> {
                    String result = dialog.getInputEditText().getText().toString().trim();
                    requestRetrofitMusic1(result);
                })
                .onNegative((dialog, which) -> dialog.cancel())
                .show();
    }

    public void requestRetrofitMusic1(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi1.class)
                .searchApi1(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            search_recyclerview.setHasFixedSize(true);
                            search_recyclerview.setLayoutManager(llm);
                            mainList = new ArrayList<>();
                            mainAdapter = new MainAdapter(context, mainList);
                            for (SearchMusic e : response.getMusic()) {
                                requestRetrofitMusic2(Integer.parseInt(e.get_source_id()), 1);
                            }
                            search_recyclerview.setAdapter(mainAdapter);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitMusic2(int index, int repeat) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi2.class)
                .searchApi2(index, repeat)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            for (Source song : response) {
                                mainList.add(new MainVO(song.getCover(), song.getArtist_cover(), song.getSong_original(), song.getArtist_en(),
                                        song.get_sid(), song.get_aid()));
                            }
                            mainAdapter.notifyDataSetChanged();
                            requestRetrofitMusic2(index, repeat + 1);
                        },
                        e -> Log.e(TAG, e.toString()));
    }

    @OnClick(R.id.search_utaite_btn)
    public void onUtaiteSearchButtonClick() {
        getFragmentManager().beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

        new MaterialDialog.Builder(context)
                .input(null, null, (dialog, input) -> {
                })
                .content(getString(R.string.search_dialog))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive((dialog, which) -> {
                    String result = dialog.getInputEditText().getText().toString().trim();
                    requestRetrofitUtaite1(result);
                })
                .onNegative((dialog, which) -> dialog.cancel())
                .show();
    }

    public void requestRetrofitUtaite1(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi1.class)
                .searchApi1(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            search_recyclerview.setHasFixedSize(true);
                            search_recyclerview.setLayoutManager(llm);
                            utaiteList = new ArrayList<>();
                            utaiteAdapter = new UtaiteAdapter(context, utaiteList);
                            for (SearchArtist e : response.getArtist()) {
                                requestRetrofitUtaite2(Integer.parseInt(e.get_aid()));
                            }
                            search_recyclerview.setAdapter(utaiteAdapter);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitUtaite2(int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.UtaiteApi.class)
                .utaiteApi(getString(R.string.rest_artist), index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            utaiteList.add(response);
                            utaiteAdapter.notifyDataSetChanged();
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

}
