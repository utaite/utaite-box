package com.yuyu.utaitebox.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.rest.Music;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartFragment1 extends RxFragment {

    private final String TAG = ChartFragment1.class.getSimpleName();

    private Context context;

    @BindView(R.id.chart_recyclerview1)
    RecyclerView chart_recyclerview1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart1, container, false);
        Log.e(TAG, "초기화");
        ButterKnife.bind(this, view);
        context = getActivity();
        requestRetrofit(getString(R.string.rest_chart));
        return view;
    }

    public void requestRetrofit(String what) {
        RestUtils.getRetrofit()
                .create(RestUtils.ChartApi.class)
                .chartApi(what)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            chart_recyclerview1.setLayoutManager(llm);
                            ArrayList<MainVO> vo = new ArrayList<>();
                            for (Music e : response.getMusic()) {
                                vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                        e.get_sid(), e.get_aid()));
                            }
                            MainAdapter mainAdapter = new MainAdapter(context, getFragmentManager(), vo);
                            chart_recyclerview1.setAdapter(mainAdapter);
                            mainAdapter.notifyDataSetChanged();
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

}