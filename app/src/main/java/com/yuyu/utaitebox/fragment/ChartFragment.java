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

public class ChartFragment extends RxFragment {

    private final String TAG = ChartFragment.class.getSimpleName();

    private int[] imgs = {R.drawable.tab_music, R.drawable.tab_utaite, R.drawable.tab_user};

    @BindView(R.id.chart_tab)
    TabLayout chart_tab;
    @BindView(R.id.chart_txt1)
    TextView chart_txt1;
    @BindView(R.id.chart_view_pager)
    ViewPager chart_view_pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    public void initialize() {
        ArrayList<TabVO> arrayList = new ChainedArrayList().addMany(
                new TabVO(new ChartFragment1(), getString(R.string.tab_title1), getString(R.string.tab_desc1)),
                new TabVO(new ChartFragment2(), getString(R.string.tab_title2), getString(R.string.tab_desc2)),
                new TabVO(new ChartFragment3(), getString(R.string.tab_title3), getString(R.string.tab_desc3)));

        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), arrayList);
        chart_view_pager.setAdapter(tabAdapter);
        chart_tab.setupWithViewPager(chart_view_pager);
        for (int i = 0; i < chart_tab.getTabCount(); i++) {
            chart_tab.getTabAt(i).setIcon(imgs[i]);
        }
        chart_txt1.setText("▼  " + tabAdapter.getPageDesc(0));

        chart_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chart_txt1.setText("▼  " + tabAdapter.getPageDesc(tab.getPosition()));
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
