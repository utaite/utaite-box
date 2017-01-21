package com.yuyu.utaitebox.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.adapter.TabAdapter;
import com.yuyu.utaitebox.chain.ChainedArrayList;
import com.yuyu.utaitebox.utils.TabVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends RxFragment {

    private final String TAG = SearchFragment.class.getSimpleName();

    private int[] imgs = {R.drawable.tab_music, R.drawable.tab_utaite};

    @BindView(R.id.search_tab)
    TabLayout search_tab;
    @BindView(R.id.search_view_pager)
    ViewPager search_view_pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    public void initialize() {
        ArrayList<TabVO> arrayList = new ChainedArrayList().addMany(
                new TabVO(new SearchFragment1(), getString(R.string.search_title1), getString(R.string.search_desc1)),
                new TabVO(new SearchFragment2(), getString(R.string.search_title2), getString(R.string.search_desc2)));

        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), arrayList);
        search_view_pager.setAdapter(tabAdapter);
        search_tab.setupWithViewPager(search_view_pager);
        for (int i = 0; i < search_tab.getTabCount(); i++) {
            search_tab.getTabAt(i).setIcon(imgs[i]);
        }
    }

}
