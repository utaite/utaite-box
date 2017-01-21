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

public class TimelineFragment extends RxFragment {

    private final String TAG = TimelineFragment.class.getSimpleName();

    private int[] imgs = {R.drawable.timeline_listen, R.drawable.timeline_comment};

    @BindView(R.id.timeline_tab)
    TabLayout timeline_tab;
    @BindView(R.id.timeline_txt1)
    TextView timeline_txt1;
    @BindView(R.id.timeline_view_pager)
    ViewPager timeline_view_pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    public void initialize() {
        ArrayList<TabVO> arrayList = new ChainedArrayList().addMany(
                new TabVO(new TimelineFragment1(), getString(R.string.timeline_title1), getString(R.string.timeline_desc1)),
                new TabVO(new TimelineFragment2(), getString(R.string.timeline_title2), getString(R.string.timeline_desc2)));

        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), arrayList);
        timeline_view_pager.setAdapter(tabAdapter);
        timeline_tab.setupWithViewPager(timeline_view_pager);
        timeline_txt1.setText("▼  " + tabAdapter.getPageDesc(0));
        for (int i = 0; i < timeline_tab.getTabCount(); i++) {
            timeline_tab.getTabAt(i).setIcon(imgs[i]);
        }

        timeline_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                timeline_txt1.setText("▼  " + tabAdapter.getPageDesc(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

}
