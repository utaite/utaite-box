package com.yuyu.utaitebox.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.service.MusicService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicActivity extends RxAppCompatActivity {

    private final String TAG = MusicActivity.class.getSimpleName();

    private Context context;

    private int sid;
    private String key, cover, title, utaite;
    private boolean isDestroy;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        context = this;
        setTitle(getString(R.string.music));

        sid = getIntent().getIntExtra(getString(R.string.music_sid), -1);
        key = getIntent().getStringExtra(getString(R.string.music_key));
        cover = getIntent().getStringExtra(getString(R.string.music_cover));
        title = getIntent().getStringExtra(getString(R.string.music_title));
        utaite = getIntent().getStringExtra(getString(R.string.music_utaite));
        isDestroy = false;
        music_progress.setVisibility(View.VISIBLE);

        if (getString(R.string.service_noti).equals(getIntent().getAction())) {
            initialize();
        } else {
            new Thread() {
                @Override
                public void run() {
                    while (!MusicService.mediaPlayer.isPlaying()) {
                    }
                    runOnUiThread(() -> initialize());
                }
            }.start();
        }
    }

    @OnClick(R.id.music_stop)
    public void onMusicStopButtonClick() {
        if (MusicService.mediaPlayer.isPlaying()) {
            MusicService.mediaPlayer.pause();
            music_stop.setImageResource(R.drawable.music_play);
        } else {
            MusicService.mediaPlayer.start();
            music_stop.setImageResource(R.drawable.music_stop);
        }
    }

    @OnClick(R.id.music_list_btn)
    public void onMusisListButtonClick() {

    }

    public void initialize() {
        music_progress.setVisibility(View.GONE);
        music_stop.setImageResource(R.drawable.music_stop);
        music_seek_bar.setMax(MusicService.mediaPlayer.getDuration());
        music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
        music_title.setText(title);
        music_utaite.setText("- " + utaite);
        setTime(music_time_total, music_seek_bar.getMax());
        setTime(music_time_current, MusicService.mediaPlayer.getCurrentPosition());

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
                if (fromUser) {
                    MusicService.mediaPlayer.seekTo(progress);
                    music_seek_bar.setProgress(progress);
                }
                setTime(music_time_current, MusicService.mediaPlayer.getCurrentPosition());
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
                    if (MusicService.mediaPlayer != null) {
                        music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    } else {
                        isDestroy = true;
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
