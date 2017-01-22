package com.yuyu.utaitebox.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.chain.ChainedToast;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.service.MusicService;
import com.yuyu.utaitebox.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;


public class MusicActivity extends RxAppCompatActivity {

    private final String TAG = MusicActivity.class.getSimpleName();

    private Context context;
    private ChainedToast toast;

    private int sid, position;
    private String key, cover, title, utaite;
    private boolean isDestroy, isShow;

    @BindView(R.id.music_cover)
    ImageView music_cover;
    @BindView(R.id.music_time_current)
    TextView music_time_current;
    @BindView(R.id.music_time_total)
    TextView music_time_total;
    @BindView(R.id.music_seek_bar)
    SeekBar music_seek_bar;
    @BindView(R.id.music_title)
    TextView music_title;
    @BindView(R.id.music_utaite)
    TextView music_utaite;
    @BindView(R.id.music_stop)
    ImageView music_stop;
    @BindView(R.id.music_progress)
    ProgressBar music_progress;
    @BindView(R.id.music_shuffle)
    ImageView music_shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        context = this;
        toast = new ChainedToast(this).makeTextTo(this, "", Toast.LENGTH_SHORT);
        setTitle(getString(R.string.music));

        sid = getIntent().getIntExtra(getString(R.string.music_sid), -1);
        key = getIntent().getStringExtra(getString(R.string.music_key));
        cover = getIntent().getStringExtra(getString(R.string.music_cover));
        title = getIntent().getStringExtra(getString(R.string.music_title));
        utaite = getIntent().getStringExtra(getString(R.string.music_utaite));
        isDestroy = false;
        isShow = true;
        MusicService.isLoading = false;
        music_progress.setVisibility(View.VISIBLE);
        music_shuffle.setColorFilter(ContextCompat.getColor(context, MusicService.isShuffle ? R.color.colorAccent : android.R.color.black));

        int size = MainActivity.playlists.size();
        for (int i = 0; i < size; i++) {
            if (MainActivity.playlists.get(i).get_sid() == sid) {
                MusicService.positions.clear();
                position = i;
                break;
            }
        }

        if (getString(R.string.service_noti).equals(getIntent().getAction())) {
            initialize();
        } else {
            prepare();
        }
    }

    public void prepare() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (MusicService.mediaPlayer != null) {
                        while (!MusicService.mediaPlayer.isPlaying()) {
                            Thread.sleep(10);
                        }
                        runOnUiThread(() -> initialize());
                    } else {
                        while (MusicService.mediaPlayer == null) {
                            Thread.sleep(10);
                        }
                        while (!MusicService.mediaPlayer.isPlaying()) {
                            Thread.sleep(10);
                        }
                        runOnUiThread(() -> initialize());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }.start();
    }

    @OnClick(R.id.music_stop)
    public void onMusicStopButtonClick() {
        if (MusicService.isLoading) {
            if (MusicService.mediaPlayer.getDuration() > Constant.DELAY_TIME) {
                if (MusicService.mediaPlayer.isPlaying()) {
                    MusicService.mediaPlayer.pause();
                    music_stop.setImageResource(R.drawable.music_play);
                } else {
                    MusicService.mediaPlayer.start();
                    music_stop.setImageResource(R.drawable.music_stop);
                }
            }
        }
    }

    @OnClick(R.id.music_list_btn)
    public void onMusicListButtonClick() {
        if (MusicService.isLoading) {
            if (MusicService.mediaPlayer.getDuration() > Constant.DELAY_TIME) {
                Intent intent = new Intent(context, MainActivity.class)
                        .setAction(getString(R.string.service_list))
                        .setFlags(FLAG_ACTIVITY_NO_HISTORY)
                        .putExtra(getString(R.string.music_sid), sid)
                        .putExtra(getString(R.string.music_key), key)
                        .putExtra(getString(R.string.music_cover), cover)
                        .putExtra(getString(R.string.music_title), title)
                        .putExtra(getString(R.string.music_utaite), utaite);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.music_shuffle)
    public void onMusicShuffleButtonClick() {
        MusicService.isShuffle = !MusicService.isShuffle;
        getSharedPreferences(getString(R.string.service_shuffle), MODE_PRIVATE).
                edit().putBoolean(getString(R.string.service_shuffle), MusicService.isShuffle).apply();
        toast.setTextShow(getString(MusicService.isShuffle ? R.string.service_shuffle_on : R.string.service_shuffle_off));
        music_shuffle.setColorFilter(ContextCompat.getColor(context, MusicService.isShuffle ? R.color.colorAccent : android.R.color.black));
    }

    @OnClick(R.id.music_prev)
    public void onMusicPrevButtonClick() {
        if (MusicService.isLoading) {
            mediaPlayerDestroy();
            initValue(true);

            Intent service = new Intent(context, MusicService.class);
            context.stopService(service);
            context.startService(putIntent(service));
            prepare();
        }
    }

    @OnClick(R.id.music_next)
    public void onMusicNextButtonClick() {
        if (MusicService.isLoading) {
            mediaPlayerDestroy();
            initValue(false);

            Intent service = new Intent(context, MusicService.class);
            context.stopService(service);
            context.startService(putIntent(service));
            prepare();
        }
    }

    public void onMusicNext() {
        mediaPlayerDestroy();
        initValue(true);

        Intent service = new Intent(this, MusicService.class);
        stopService(service);
        startService(putIntent(service));

        if (isShow) {
            prepare();
        }
    }

    public void initValue(boolean isPrev) {
        isDestroy = true;
        int size = MainActivity.playlists.size();

        if (!MusicService.isShuffle) {
            for (int i = 0; i < size; i++) {
                if (MainActivity.playlists.get(i).get_sid() == sid) {

                    sid = MainActivity.playlists.get(isPrev ? (i != 0 ? i - 1 : size - 1) : (i < size - 1 ? i + 1 : 0)).get_sid();
                    key = MainActivity.playlists.get(isPrev ? (i != 0 ? i - 1 : size - 1) : (i < size - 1 ? i + 1 : 0)).getKey();
                    cover = MainActivity.playlists.get(isPrev ? (i != 0 ? i - 1 : size - 1) : (i < size - 1 ? i + 1 : 0)).getCover();
                    title = MainActivity.playlists.get(isPrev ? (i != 0 ? i - 1 : size - 1) : (i < size - 1 ? i + 1 : 0)).getSong_original();
                    utaite = MainActivity.playlists.get(isPrev ? (i != 0 ? i - 1 : size - 1) : (i < size - 1 ? i + 1 : 0)).getArtist_en();
                    break;
                }
            }

        } else {
            if (!isPrev) {
                MusicService.positions.add(position);
                position = (int) (Math.random() * size);

            } else {
                int psize = MusicService.positions.size();
                if (psize > 0) {
                    position = MusicService.positions.get(psize - 1);
                    MusicService.positions.remove(psize - 1);
                }
            }

            sid = MainActivity.playlists.get(position).get_sid();
            key = MainActivity.playlists.get(position).getKey();
            cover = MainActivity.playlists.get(position).getCover();
            title = MainActivity.playlists.get(position).getSong_original();
            utaite = MainActivity.playlists.get(position).getArtist_en();
        }

    }

    public Intent putIntent(Intent intent) {
        return intent.setFlags(FLAG_ACTIVITY_NO_HISTORY)
                .putExtra(getString(R.string.music_sid), sid)
                .putExtra(getString(R.string.music_key), key)
                .putExtra(getString(R.string.music_cover), cover)
                .putExtra(getString(R.string.music_title), title)
                .putExtra(getString(R.string.music_utaite), utaite);
    }

    public void initialize() {
        MusicService.isLoading = true;
        music_progress.setVisibility(View.GONE);
        music_stop.setImageResource(R.drawable.music_stop);
        music_seek_bar.setMax(MusicService.mediaPlayer.getDuration());
        music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
        music_title.setText(title);
        music_utaite.setText("- " + utaite);
        setTime(music_time_total, music_seek_bar.getMax());
        setTime(music_time_current, MusicService.mediaPlayer.getCurrentPosition());
        MusicService.mediaPlayer.setOnCompletionListener(mp -> onMusicNext());

        Glide.with(context).load(RestUtils.BASE + (cover == null ? getString(R.string.rest_images_cover) : getString(R.string.rest_cover) + cover))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(music_cover);

        musicThread();
        music_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (MusicService.mediaPlayer != null) {
                    if (fromUser) {
                        MusicService.mediaPlayer.seekTo(progress);
                        music_seek_bar.setProgress(progress);
                    }
                    setTime(music_time_current, MusicService.mediaPlayer.getCurrentPosition());
                }
            }
        });
    }

    public void setTime(TextView textView, int time) {
        int min = time / 60000;
        int sec = time % 60000;
        textView.setText(min + ":" + ((sec / 1000 < 10) ? "0" : "") + (sec) / 1000);
    }

    public void musicThread() {
        new Thread() {
            @Override
            public void run() {
                while (!isDestroy) {
                    while (isShow) {
                        if (MusicService.isLoading) {
                            try {
                                music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                finishAffinity();
                            }
                        } else {
                            isDestroy = true;
                        }
                    }
                }
            }
        }.start();
    }

    public void mediaPlayerDestroy() {
        if (MusicService.mediaPlayer != null) {
            MusicService.mediaPlayer.stop();
            MusicService.mediaPlayer.release();
            MusicService.mediaPlayer = null;
        }
        music_progress.setVisibility(View.VISIBLE);
        music_cover.setImageResource(android.R.color.transparent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        isShow = false;
    }
}
