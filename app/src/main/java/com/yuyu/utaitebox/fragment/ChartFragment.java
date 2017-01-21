package com.yuyu.utaitebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

public class ChartFragment extends RxFragment {

    private final String TAG = ChartFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        initialize(view);
        return view;
    }

    public void initialize(View view) {
        Log.e(TAG, "초기화");
        TabLayout chart_tap = (TabLayout) view.findViewById(R.id.chart_tap) ;
        TextView chart_txt1 = (TextView) view.findViewById(R.id.chart_txt1);
        ViewPager chart_view_pager = (ViewPager) view.findViewById(R.id.chart_view_pager);

        int[] imgs = {R.drawable.tab_music, R.drawable.tab_utaite, R.drawable.tab_user};

        ArrayList<TabVO> arrayList = new ChainedArrayList().addMany(
                new TabVO(new ChartFragment1(), getString(R.string.tab_title1), getString(R.string.tab_desc1)),
                new TabVO(new ChartFragment2(), getString(R.string.tab_title2), getString(R.string.tab_desc2)),
                new TabVO(new ChartFragment3(), getString(R.string.tab_title3), getString(R.string.tab_desc3)));

        TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), arrayList);
        chart_view_pager.setAdapter(tabAdapter);
        chart_tap.setupWithViewPager(chart_view_pager);
        chart_txt1.setText("▼  " + tabAdapter.getPageDesc(0));
        for (int i = 0; i < chart_tap.getTabCount(); i++) {
            chart_tap.getTabAt(i).setIcon(imgs[i]);
        }

        chart_tap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
