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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.retrofit.Song;
import com.yuyu.utaitebox.view.MainAdapter;
import com.yuyu.utaitebox.view.MainData;

public class MusicListFragment extends Fragment {

    private interface UtaiteBoxGET {
        @GET("/api/{what}/{index}")
        Call<ArrayList<Song>> listRepos(@Path("what") String what,
                                        @Path("index") int index);
    }

    private View view;
    private Context context;
    private RequestManager glideManager;
    private ArrayList<MainData> mainDataSet;
    private MainAdapter mainAdapter;
    private boolean loading = true;
    private int count;
    private final int PAGE = 5;

    @BindView(R.id.musiclist_recyclerview)
    RecyclerView musiclist_recyclerview;
    @BindView(R.id.fragment_musiclist)
    ScrollView fragment_musiclist;
    @BindView(R.id.musiclist_next)
    TextView musiclist_next;
    @BindView(R.id.musiclist_prev)
    TextView musiclist_prev;

    public MusicListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_musiclist, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();
        glideManager = Glide.with(context);
        musiclist_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        musiclist_recyclerview.setLayoutManager(llm);
        mainDataSet = new ArrayList<>();
        count = 1;
        requestRetrofit("songlist", count);
        mainAdapter = new MainAdapter(mainDataSet, context, glideManager, getFragmentManager());
        musiclist_recyclerview.setAdapter(mainAdapter);
        final String prev1 = "▼  가장 최근에 업로드 된 노래에요.";
        final String prev2 = "▲  이전 페이지로";
        musiclist_next.setVisibility(View.GONE);
        musiclist_prev.setText(prev1);
        fragment_musiclist.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (fragment_musiclist.getScrollY() >= musiclist_recyclerview.getHeight() - fragment_musiclist.getHeight() && loading) {
                loading = false;
                ++count;
                if ((count - 1) % PAGE != 0) {
                    if ((count - 1) % PAGE == 4) {
                        musiclist_next.setVisibility(View.VISIBLE);
                    }
                    requestRetrofit("songlist", count);
                } else if ((count - 1) % PAGE == 0) {
                    musiclist_next.setOnClickListener(v1 -> {
                        musiclist_next.setVisibility(View.GONE);
                        mainDataSet.clear();
                        mainAdapter.notifyDataSetChanged();
                        requestRetrofit("songlist", count);
                        musiclist_prev.setText(prev2);
                    });
                }
            }
        });
        musiclist_prev.setOnClickListener(v12 -> {
            if (count > PAGE) {
                loading = false;
                count = count / 5 * 5 - 4;
                mainDataSet.clear();
                mainAdapter.notifyDataSetChanged();
                requestRetrofit("songlist", count);
                if (count == 1) {
                    musiclist_prev.setText(prev1);
                }
            }
        });

        return view;
    }

    public void requestRetrofit(String what, int index) {
        final CheckTypesTask task = new CheckTypesTask();
        task.onPreExecute();
        Call<ArrayList<Song>> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UtaiteBoxGET.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                ArrayList<Song> song = response.body();
                for (Song e : song) {
                    mainDataSet.add(new MainData(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                            e.get_sid(), e.get_aid()));
                }
                mainAdapter.notifyDataSetChanged();
                task.onPostExecute(null);
                loading = true;
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.e("list Problem", String.valueOf(t));
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
