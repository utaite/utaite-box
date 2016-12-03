package com.yuyu.utaitebox.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
    private RequestManager glideManager;

    public MainFragment() {
    }

    @BindView(R.id.main_recyclerview)
    RecyclerView main_recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();
        glideManager = Glide.with(context);

        if (MainActivity.today == -1) {
            final Call<Repo> repos = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MainActivity.UtaiteBoxGET.class)
                    .listRepos("chart", 688888);
            repos.enqueue(new Callback<Repo>() {
                @Override
                public void onResponse(Call<Repo> call, Response<Repo> response) {
                    MainActivity.today = Integer.parseInt(response.body().getNavigation().getPageCount());
                    requestRetrofit("chart", MainActivity.today);
                }

                @Override
                public void onFailure(Call<Repo> call, Throwable t) {
                    Log.e("chart Problem", String.valueOf(t));
                }
            });
        } else {
            requestRetrofit("chart", MainActivity.today);
        }
        return view;
    }

    public void requestRetrofit(String what, int index) {
        final CheckTypesTask task = new CheckTypesTask();
        task.onPreExecute();
        Call<Repo> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MainActivity.UtaiteBoxGET.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                main_recyclerview.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                main_recyclerview.setLayoutManager(llm);
                ArrayList<MainData> mainDataSet = new ArrayList<>();
                ArrayList<Music> music = response.body().getMusic();
                for (Music e : music) {
                    mainDataSet.add(new MainData(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                            e.get_sid(), e.get_aid()));
                }
                MainAdapter mainAdapter = new MainAdapter(mainDataSet, context, glideManager, getFragmentManager());
                main_recyclerview.setAdapter(mainAdapter);
                task.onPostExecute(null);
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e("chart Problem", String.valueOf(t));
            }
        });
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("리스트를 불러오는 중입니다 ...");
            asyncDialog.show();
            asyncDialog.setCancelable(false);
            asyncDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

}