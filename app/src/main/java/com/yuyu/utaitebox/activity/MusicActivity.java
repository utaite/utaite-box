package com.yuyu.utaitebox.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.service.MusicService;
import com.yuyu.utaitebox.utils.Constant;
import com.yuyu.utaitebox.utils.MusicParcel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicActivity extends RxAppCompatActivity {

    private final String TAG = MusicActivity.class.getSimpleName();

    @InjectExtra
    MusicParcel musicParcel;

    private Context context;
    private Thread pThread;

    private int sid;
    private String key, cover, title, utaite;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        context = this;
        setTitle(getString(R.string.music));

        boolean isNoti = getString(R.string.service_noti).equals(getIntent().getAction());
        if (!isNoti) {
            Dart.inject(this);
        }
        sid = isNoti ? getIntent().getIntExtra(getString(R.string.music_sid), -1) : musicParcel.getSid();
        key = isNoti ? getIntent().getStringExtra(getString(R.string.music_key)) : musicParcel.getKey();
        cover = isNoti ? getIntent().getStringExtra(getString(R.string.music_cover)) : musicParcel.getCover();
        title = isNoti ? getIntent().getStringExtra(getString(R.string.music_title)) : musicParcel.getMusicTitle();
        utaite = isNoti ? getIntent().getStringExtra(getString(R.string.music_utaite)) : musicParcel.getMusicUtaite();

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                initialize();
            }
        }.sendEmptyMessageDelayed(0, isNoti ? 0 : Constant.DELAY_TIME);
    }

    public void initialize() {
        music_seek_bar.setMax(MusicService.mediaPlayer.getDuration());
        music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
        music_title.setText(title);
        music_utaite.setText(utaite);
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
        Runnable task = () -> {
            while (!pThread.isInterrupted()) {
                if (MusicService.mediaPlayer != null) {
                    Log.e(TAG, MusicService.mediaPlayer.getCurrentPosition() + "");
                    music_seek_bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    pThread.interrupt();
                }
            }
        };
        pThread = new Thread(task);
        pThread.start();
    }

}
