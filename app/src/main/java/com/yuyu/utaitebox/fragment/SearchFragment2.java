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
import com.yuyu.utaitebox.adapter.ActiveAdapter;
import com.yuyu.utaitebox.adapter.UtaiteAdapter;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.SearchArtist;
import com.yuyu.utaitebox.rest.Utaite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment2 extends RxFragment {

    private final String TAG = SearchFragment2.class.getSimpleName();

    private Context context;
    private ArrayList<Utaite> vo;
    private UtaiteAdapter utaiteAdapter;

    @BindView(R.id.search_recyclerview2)
    RecyclerView search_recyclerview2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search2, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @OnClick(R.id.search_txt2)
    public void onTextView2Click() {
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
                            search_recyclerview2.setHasFixedSize(true);
                            search_recyclerview2.setLayoutManager(llm);
                            vo = new ArrayList<>();
                            utaiteAdapter = new UtaiteAdapter(context, ((MainActivity) context).getFragmentManager(), vo);
                            for (SearchArtist e : response.getArtist()) {
                                requestRetrofitSearch2(Integer.parseInt(e.get_aid()));
                            }
                            search_recyclerview2.setAdapter(utaiteAdapter);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

    public void requestRetrofitSearch2(int index) {
        RestUtils.getRetrofit()
                .create(RestUtils.UtaiteApi.class)
                .utaiteApi(getString(R.string.rest_artist), index)
                .compose(bindToLifecycle())
                .distinct()
                .subscribe(response -> {
                            vo.add(response);
                            utaiteAdapter.notifyDataSetChanged();
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        });
    }

}
