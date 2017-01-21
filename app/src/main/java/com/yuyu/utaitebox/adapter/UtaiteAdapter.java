package com.yuyu.utaitebox.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.fragment.UserInfoFragment;
import com.yuyu.utaitebox.fragment.UtaiteInfoFragment;
import com.yuyu.utaitebox.rest.Active;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.rest.Utaite;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UtaiteAdapter extends RecyclerView.Adapter<UtaiteAdapter.ViewHolder> {

    private final String TAG = UtaiteAdapter.class.getSimpleName();

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Utaite> vo;

    public UtaiteAdapter(Context context, FragmentManager fragmentManager, ArrayList<Utaite> vo) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.vo = vo;
    }

    @Override
    public UtaiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RequestManager glide = Glide.with(context);

        holder.nav_id.setText(vo.get(position).getAritst().getAritst_en());

        String cover = vo.get(position).getAritst().getArtist_cover();
        glide.load(RestUtils.BASE + (cover == null ?
                context.getString(R.string.rest_images_artist) : context.getString(R.string.rest_artist_image) + vo.get(position).getAritst().getArtist_cover()))
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.nav_img);
        holder.nav_img.setOnClickListener(v -> {
            Fragment fragment = new UtaiteInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(context.getString(R.string.rest_aid), Integer.parseInt(vo.get(position).getAritst().get_aid()));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        if (vo.get(position).getAritst().getArtist_background() == null) {
            holder.nav_bg1.setImageResource(android.R.color.transparent);
            holder.nav_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
        } else {
            glide.load(RestUtils.BASE + context.getString(R.string.rest_artist_cover) + vo.get(position).getAritst().getArtist_background())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.nav_bg1);
        }
    }

    @Override
    public int getItemCount() {
        return vo.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nav_bg1, nav_img;
        TextView nav_id;

        ViewHolder(View view) {
            super(view);
            nav_bg1 = (ImageView) view.findViewById(R.id.nav_bg1);
            nav_img = (ImageView) view.findViewById(R.id.nav_img);
            nav_id = (TextView) view.findViewById(R.id.nav_id);
        }
    }

}