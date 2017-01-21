package com.yuyu.utaitebox.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;

import butterknife.ButterKnife;

public class ChartFragment3 extends RxFragment {

    private final String TAG = ChartFragment3.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart3, container, false);
        Log.e(TAG, "초기화");
        return view;
    }

}
