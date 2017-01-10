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

import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.utils.MainVO;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MusicListFragment extends Fragment {

    private static final String TAG = MusicListFragment.class.getSimpleName();

    private final int PAGE = 5;

    private Context context;
    private ArrayList<MainVO> vo;
    private MainAdapter mainAdapter;

    private int count;
    private boolean loading = true;

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

        musiclist_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        musiclist_recyclerview.setLayoutManager(llm);
        vo = new ArrayList<>();

        requestRetrofit(getString(R.string.rest_songlist), count);
        mainAdapter = new MainAdapter(context, getFragmentManager(), vo);
        musiclist_recyclerview.setAdapter(mainAdapter);
        musiclist_next.setVisibility(View.GONE);
        musiclist_prev.setText(getString(R.string.musiclist_txt1));
        fragment_musiclist.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (fragment_musiclist.getScrollY() >= musiclist_recyclerview.getHeight() - fragment_musiclist.getHeight() && loading) {
                loading = false;
                ++count;
                if ((count - 1) % PAGE != 0) {
                    musiclist_next.setVisibility((count - 1) % PAGE == 4 ? View.VISIBLE : View.INVISIBLE);
                    requestRetrofit(getString(R.string.rest_songlist), count);
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
    public void musiclist_prev() {
        if (count > PAGE) {
            loading = false;
            count = count / 5 * 5 - 4;
            vo.clear();
            mainAdapter.notifyDataSetChanged();
            requestRetrofit(getString(R.string.rest_songlist), count);
            if (count == 1) {
                musiclist_prev.setText(getString(R.string.musiclist_txt1));
            }
        }
    }

    @OnClick(R.id.musiclist_next)
    public void musiclist_next() {
        if ((count - 1) % PAGE == 0) {
            musiclist_next.setVisibility(View.GONE);
            vo.clear();
            mainAdapter.notifyDataSetChanged();
            requestRetrofit(getString(R.string.rest_songlist), count);
            musiclist_prev.setText(getString(R.string.musiclist_txt2));
        }
    }

    public void requestRetrofit(String what, int index) {
        Task task = new Task(context, 0);
        task.onPreExecute();
        RestUtils.getRetrofit()
                .create(RestUtils.ArraySongApi.class)
                .arraySongApi(what, index)
                .subscribe(new Subscriber<ArrayList<Song>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        ((MainActivity) context).getToast().setTextShow(getString(R.string.rest_error));
                        task.onPostExecute(null);
                    }

                    @Override
                    public void onNext(ArrayList<Song> songs) {
                        for (Song e : songs) {
                            vo.add(new MainVO(e.getCover(), e.getArtist_cover(), e.getSong_original(), e.getArtist_en(),
                                    e.get_sid(), e.get_aid()));
                        }
                        mainAdapter.notifyDataSetChanged();
                        task.onPostExecute(null);
                        loading = true;
                    }
                });
    }

}
