package com.yuyu.utaitebox.fragment;

import android.content.Context;
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
import com.yuyu.utaitebox.adapter.ActiveAdapter;
import com.yuyu.utaitebox.rest.Active;
import com.yuyu.utaitebox.rest.RestUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartFragment3 extends RxFragment {

    private final String TAG = ChartFragment3.class.getSimpleName();

    private Context context;
    private ArrayList<Active> vo;

    @BindView(R.id.chart_recyclerview3)
    RecyclerView chart_recyclerview3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart3, container, false);
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
                            chart_recyclerview3.setLayoutManager(llm);
                            vo = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                vo.add(response.getActive().get(i));
                                requestRetrofitCover(i, context.getString(R.string.rest_member), Integer.parseInt(response.getActive().get(i).get_mid()));
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitCover(int position, String what, int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            String cover = response.getProfile().getCover();
                            vo.get(position).setCover(cover);
                            if (position == 4) {
                                ActiveAdapter activeAdapter = new ActiveAdapter(context, vo);
                                chart_recyclerview3.setAdapter(activeAdapter);
                                activeAdapter.notifyDataSetChanged();
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

}
