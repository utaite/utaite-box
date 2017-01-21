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
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.SearchMusic;
import com.yuyu.utaitebox.rest.Source;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment1 extends RxFragment {

    private final String TAG = SearchFragment1.class.getSimpleName();

    private Context context;
    private ArrayList<MainVO> vo;
    private MainAdapter mainAdapter;

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
                    requestRetrofitSearch1(result);
                })
                .onNegative((dialog, which) -> dialog.cancel())
                .show();
    }

    public void requestRetrofitSearch1(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi1.class)
                .searchApi1(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            search_recyclerview1.setHasFixedSize(true);
                            search_recyclerview1.setLayoutManager(llm);
                            vo = new ArrayList<>();
                            mainAdapter = new MainAdapter(context, ((MainActivity) context).getFragmentManager(), vo);
                            for (SearchMusic e : response.getMusic()) {
                                requestRetrofitSearch2(Integer.parseInt(e.get_source_id()), 1);
                            }
                            search_recyclerview1.setAdapter(mainAdapter);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitSearch2(int index, int repeat) {
        RestUtils.getRetrofit()
                .create(RestUtils.SearchApi2.class)
                .searchApi2(index, repeat)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            for (Source song : response) {
                                vo.add(new MainVO(song.getCover(), song.getArtist_cover(), song.getSong_original(), song.getArtist_en(),
                                        song.get_sid(), song.get_aid()));
                            }
                            mainAdapter.notifyDataSetChanged();
                            requestRetrofitSearch2(index, repeat + 1);
                        },
                        e -> Log.e(TAG, e.toString()));
    }

}
