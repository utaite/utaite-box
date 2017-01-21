package com.yuyu.utaitebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment2 extends RxFragment {

    private final String TAG = SearchFragment2.class.getSimpleName();

    private Context context;

    @BindView(R.id.search_recyclerview2)
    RecyclerView search_recyclerview2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search2, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

}
