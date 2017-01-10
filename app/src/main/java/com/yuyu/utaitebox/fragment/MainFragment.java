package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.rest.Music;
import com.yuyu.utaitebox.rest.Repo;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.utils.Constant;
import com.yuyu.utaitebox.utils.MainVO;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Context context;

    @BindView(R.id.main_recyclerview)
    RecyclerView main_recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestRetrofit(getString(R.string.rest_chart), MainActivity.TODAY);
    }

    public void requestRetrofit(String what, int index) {
        Task task = new Task(context, 0);
        task.onPreExecute();

        RestUtils.getRetrofit()
                .create(RestUtils.DefaultApi.class)
                .defaultApi(what, index)
                .subscribe(new Subscriber<Repo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Log.e(TAG, String.valueOf(e));
                        ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        task.onPostExecute(null);
                    }

                    @Override
                    public void onNext(Repo repo) {
                        task.onPostExecute(null);
                        if (MainActivity.TODAY == Constant.TODAY_DEFAULT) {
                            MainActivity.TODAY = Integer.parseInt(repo.getNavigation().getPageCount());
                            requestRetrofit(getString(R.string.rest_chart), MainActivity.TODAY);
                        } else {
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            main_recyclerview.setHasFixedSize(true);
                            main_recyclerview.setLayoutManager(llm);

                            ArrayList<MainVO> vo = new ArrayList<>();
                            for (Music e : repo.getMusic()) {
                                vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                        e.get_sid(), e.get_aid()));
                            }

                            MainAdapter mainAdapter = new MainAdapter(context, getFragmentManager(), vo);
                            main_recyclerview.setAdapter(mainAdapter);
                        }
                    }
                });
    }

}