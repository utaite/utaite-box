package com.yuyu.utaitebox.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.yuyu.utaitebox.rest.Active;
import com.yuyu.utaitebox.rest.RestUtils;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder> {

    private final String TAG = ActiveAdapter.class.getSimpleName();

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Active> vo;

    public ActiveAdapter(Context context, FragmentManager fragmentManager, ArrayList<Active> vo) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.vo = vo;
    }

    @Override
    public ActiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RequestManager glide = Glide.with(context);

        String avatar = vo.get(position).getAvatar();
        String cover = vo.get(position).getCover();
        String url = RestUtils.BASE + context.getString(R.string.rest_profile_image);

        holder.nav_id.setText(vo.get(position).getNickname());
        holder.nav_id.setGravity(Gravity.CENTER);
        holder.nav_point.setText(context.getString(R.string.rest_point) + " " + vo.get(position).getPoint());
        holder.nav_point.setGravity(Gravity.CENTER);

        glide.load(url + (avatar == null ? context.getString(R.string.rest_profile) : avatar))
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.nav_img);
        holder.nav_img.setOnClickListener(v -> {
            Fragment fragment = new UserInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(context.getString(R.string.rest_mid), Integer.parseInt(vo.get(position).get_mid()));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        if (cover == null) {
            holder.nav_bg1.setImageResource(android.R.color.transparent);
            holder.nav_bg1.setBackgroundColor(Color.rgb(204, 204, 204));
        } else {
            glide.load(RestUtils.BASE + context.getString(R.string.rest_profile_cover) + cover)
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
        TextView nav_id, nav_point;

        ViewHolder(View view) {
            super(view);
            nav_bg1 = (ImageView) view.findViewById(R.id.nav_bg1);
            nav_img = (ImageView) view.findViewById(R.id.nav_img);
            nav_id = (TextView) view.findViewById(R.id.nav_id);
            nav_point = (TextView) view.findViewById(R.id.nav_point);
        }
    }

}