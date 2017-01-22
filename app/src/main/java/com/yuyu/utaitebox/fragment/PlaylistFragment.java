package com.yuyu.utaitebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.components.RxFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.adapter.MainAdapter;
import com.yuyu.utaitebox.adapter.PlaylistAdapter;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Song;
import com.yuyu.utaitebox.utils.MainVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaylistFragment extends RxFragment {

    private static final String TAG = PlaylistFragment.class.getSimpleName();

    private Context context;

    @BindView(R.id.playlist_recyclerview)
    RecyclerView playlist_recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
        return view;
    }

    public void initialize() {
        playlist_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        playlist_recyclerview.setLayoutManager(llm);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(context, ((MainActivity) context).getPlaylists());
        playlist_recyclerview.setAdapter(playlistAdapter);
    }

}
