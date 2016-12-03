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
import com.yuyu.utaitebox.retrofit.Music;
import com.yuyu.utaitebox.retrofit.Repo;
import com.yuyu.utaitebox.view.MainAdapter;
import com.yuyu.utaitebox.view.MainData;
import com.yuyu.utaitebox.view.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    private View view;
    private Context context;
    private RequestManager glide;
    private String tag = "MainFragment";
    private boolean loading;

    @BindView(R.id.main_recyclerview)
    RecyclerView main_recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        glide = Glide.with(context);
        requestRetrofit("chart", MainActivity.today);
        return view;
    }

    // today의 값이 없으면 today의 값을, 있으면 차트의 값을 받아옴
    public void requestRetrofit(String what, int index) {
        Task task = new Task(context, 0);
        task.onPreExecute();
        Call<Repo> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MainActivity.UtaiteBoxGetRepo.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if (MainActivity.today == 688882 && loading) {
                    loading = false;
                    MainActivity.today = Integer.parseInt(response.body().getNavigation().getPageCount());
                    requestRetrofit("chart", MainActivity.today);
                } else {
                    main_recyclerview.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    main_recyclerview.setLayoutManager(llm);
                    ArrayList<MainData> mainData = new ArrayList<>();
                    ArrayList<Music> music = response.body().getMusic();
                    for (Music e : music) {
                        mainData.add(new MainData(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                e.get_sid(), e.get_aid()));
                    }
                    MainAdapter mainAdapter = new MainAdapter(mainData, context, glide, getFragmentManager());
                    main_recyclerview.setAdapter(mainAdapter);
                }
                task.onPostExecute(null);
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e(tag, String.valueOf(t));
            }
        });
    }

    @Override
    public void onStart() {
        loading = true;
        super.onStart();
    }
}