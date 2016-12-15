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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.retrofit.Song;
import com.yuyu.utaitebox.view.MainAdapter;
import com.yuyu.utaitebox.view.MainData;
import com.yuyu.utaitebox.view.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MusicListFragment extends Fragment {

    public interface UtaiteBoxGetArrSong {
        @GET("/api/{what}/{index}")
        Call<ArrayList<Song>> listRepos(@Path("what") String what,
                                        @Path("index") int index);
    }

    private static final String TAG = MusicListFragment.class.getSimpleName();

    private Context context;
    private ArrayList<MainData> mainDataSet;
    private MainAdapter mainAdapter;
    private final int PAGE = 5;
    private boolean loading = true;
    private int count;

    @BindView(R.id.musiclist_recyclerview)
    RecyclerView musiclist_recyclerview;
    @BindView(R.id.fragment_musiclist)
    ScrollView fragment_musiclist;
    @BindView(R.id.musiclist_next)
    TextView musiclist_next;
    @BindView(R.id.musiclist_prev)
    TextView musiclist_prev;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musiclist, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        RequestManager glide = Glide.with(context);
        musiclist_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        musiclist_recyclerview.setLayoutManager(llm);
        mainDataSet = new ArrayList<>();
        requestRetrofit("songlist", count);
        mainAdapter = new MainAdapter(mainDataSet, context, glide, getFragmentManager());
        musiclist_recyclerview.setAdapter(mainAdapter);
        musiclist_next.setVisibility(View.GONE);
        musiclist_prev.setText(getString(R.string.musiclist_txt1));
        fragment_musiclist.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (fragment_musiclist.getScrollY() >= musiclist_recyclerview.getHeight() - fragment_musiclist.getHeight() && loading) {
                loading = false;
                ++count;
                if ((count - 1) % PAGE != 0) {
                    musiclist_next.setVisibility((count - 1) % PAGE == 4 ? View.VISIBLE : View.INVISIBLE);
                    requestRetrofit("songlist", count);
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        count = 1;
    }

    @OnClick(R.id.musiclist_prev)
    public void listPrev() {
        if (count > PAGE) {
            loading = false;
            count = count / 5 * 5 - 4;
            mainDataSet.clear();
            mainAdapter.notifyDataSetChanged();
            requestRetrofit("songlist", count);
            if (count == 1) {
                musiclist_prev.setText(getString(R.string.musiclist_txt1));
            }
        }
    }

    @OnClick(R.id.musiclist_next)
    public void listNext() {
        if ((count - 1) % PAGE == 0) {
            musiclist_next.setVisibility(View.GONE);
            mainDataSet.clear();
            mainAdapter.notifyDataSetChanged();
            requestRetrofit("songlist", count);
            musiclist_prev.setText(getString(R.string.musiclist_txt2));
        }
    }

    public void requestRetrofit(String what, int index) {
        Task task = new Task(context, 0);
        task.onPreExecute();
        Call<ArrayList<Song>> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UtaiteBoxGetArrSong.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                for (Song e : response.body()) {
                    mainDataSet.add(new MainData(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                            e.get_sid(), e.get_aid()));
                }
                mainAdapter.notifyDataSetChanged();
                task.onPostExecute(null);
                loading = true;
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.e(TAG, String.valueOf(t));
            }
        });
    }

}
