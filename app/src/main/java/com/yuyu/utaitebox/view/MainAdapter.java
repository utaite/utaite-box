package com.yuyu.utaitebox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.fragment.MusicInfoFragment;
import com.yuyu.utaitebox.fragment.UtaiteInfoFragment;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainData> mDataset;
    private Context context;
    private RequestManager glide;
    private FragmentManager fragmentManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bg, img;
        public TextView title, utaite;

        public ViewHolder(View view) {
            super(view);
            bg = (ImageView) view.findViewById(R.id.bg);
            img = (ImageView) view.findViewById(R.id.img);
            title = (TextView) view.findViewById(R.id.title);
            utaite = (TextView) view.findViewById(R.id.utaite);
        }
    }

    public MainAdapter(ArrayList<MainData> mDataset, Context context, RequestManager glide, FragmentManager fragmentManager) {
        this.mDataset = mDataset;
        this.context = context;
        this.glide = glide;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        glide.load((mDataset.get(position).getBg() == null) ? MainActivity.BASE + "/images/cover.jpg" : MainActivity.BASE + "/res/cover/" + mDataset.get(position).getBg())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.bg);
        holder.bg.setOnClickListener(v -> {
            Fragment fragment = new MusicInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("sid", Integer.parseInt(mDataset.get(position).get_sid()));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        });
        glide.load(MainActivity.BASE + (mDataset.get(position).getImg() == null ? "/images/artist.jpg" : "/res/artist/image/" + mDataset.get(position).getImg()))
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.img);
        holder.img.setOnClickListener(v -> {
            Fragment fragment = new UtaiteInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("aid", Integer.parseInt(mDataset.get(position).get_aid()));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        });
        holder.title.setText(mDataset.get(position).getTitle());
        holder.title.bringToFront();
        holder.utaite.setText(mDataset.get(position).getUtaite());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}