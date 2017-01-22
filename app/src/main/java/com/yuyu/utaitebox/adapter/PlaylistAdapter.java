package com.yuyu.utaitebox.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.activity.MusicActivity;
import com.yuyu.utaitebox.fragment.MusicInfoFragment;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.service.MusicService;
import com.yuyu.utaitebox.utils.PlaylistVO;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private final String TAG = PlaylistAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<PlaylistVO> vo;

    public PlaylistAdapter(Context context, ArrayList<PlaylistVO> vo) {
        this.context = context;
        this.vo = vo;
    }

    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(vo.get(position).getSong_original());
        holder.utaite.setText("- " + vo.get(position).getArtist_en());

        holder.cardView.setOnClickListener(v -> onMusicClick(position));
        holder.cardView.setOnLongClickListener(v -> {
            onMusicLongClick(position);
            return false;
        });
    }

    public void onMusicClick(int position) {
        if (MusicService.mediaPlayer != null) {
            MusicService.mediaPlayer.stop();
            MusicService.mediaPlayer.release();
        }
        Intent service = new Intent(context, MusicService.class);
        context.stopService(service);
        context.startService(setIntent(service, position));

        Intent intent = new Intent(context, MusicActivity.class);
        context.startActivity(setIntent(intent, position));
    }

    public Intent setIntent(Intent intent, int position) {
        intent.putExtra(context.getString(R.string.music_sid), vo.get(position).get_sid());
        intent.putExtra(context.getString(R.string.music_key), vo.get(position).getKey());
        intent.putExtra(context.getString(R.string.music_cover), vo.get(position).getCover());
        intent.putExtra(context.getString(R.string.music_title), vo.get(position).getSong_original());
        intent.putExtra(context.getString(R.string.music_utaite), vo.get(position).getArtist_en());
        return intent;
    }

    public void onMusicLongClick(int position) {
        String items[] = new String[]{context.getString(R.string.playlist_option1),
                context.getString(R.string.playlist_option2), context.getString(R.string.playlist_option3)};
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(context.getString(R.string.playlist_option));
        ab.setSingleChoiceItems(items, -1, (dialog, which) -> {
            dialog.dismiss();
            onOptionProcess(which, position);
        });
        ab.show();
    }

    public void onOptionProcess(int which, int position) {
        if (which == 0) {
            onMusicInfo(position);
        } else if (which == 1) {
            onSelectMusicDelete(position);
        } else if (which == 2) {
            onAllMusicDelete();
        }
    }

    public void onMusicInfo(int position) {
        Fragment fragment = new MusicInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(context.getString(R.string.rest_sid), vo.get(position).get_sid());
        fragment.setArguments(bundle);
        ((MainActivity) context).getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onSelectMusicDelete(int position) {
        vo.remove(position);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (PlaylistVO v : vo) {
            arrayList.add(v.get_sid());
        }
        RestUtils.getRetrofit()
                .create(RestUtils.PlaylistUpdate.class)
                .playlistUpdate(MainActivity.TOKEN, arrayList)
                .subscribe(o -> notifyDataSetChanged(),
                        e -> Log.e(TAG, e.toString()));
    }

    public void onAllMusicDelete() {
        RestUtils.getRetrofit()
                .create(RestUtils.PlaylistDelete.class)
                .playlistDelete(MainActivity.TOKEN, "")
                .subscribe(o -> {
                            vo.clear();
                            notifyDataSetChanged();
                        },
                        e -> Log.e(TAG, e.toString()));
    }

    @Override
    public int getItemCount() {
        return vo.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, utaite;

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.playlist_view);
            title = (TextView) view.findViewById(R.id.playlist_title);
            utaite = (TextView) view.findViewById(R.id.playlist_utaite);
        }
    }

}