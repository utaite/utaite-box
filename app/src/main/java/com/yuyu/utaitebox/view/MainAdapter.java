package com.yuyu.utaitebox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuyu.utaitebox.activity.MainActivity;
import com.yuyu.utaitebox.fragment.MusicInfoFragment;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.fragment.UtaiteInfoFragment;
import com.yuyu.utaitebox.retrofit.Artist;
import com.yuyu.utaitebox.retrofit.Repo;
import com.yuyu.utaitebox.retrofit.Utaite;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private interface UtaiteBoxGET {
        @GET("/api/{what}/{index}")
        Call<Utaite> listRepos(@Path("what") String what,
                               @Path("index") int index);
    }

    private ArrayList<MainData> mDataset;
    private Context context;
    private RequestManager glideManager;
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

    public MainAdapter(ArrayList<MainData> mDataset, Context context, RequestManager glideManager, FragmentManager fragmentManager) {
        this.mDataset = mDataset;
        this.context = context;
        this.glideManager = glideManager;
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
        if (mDataset.get(position).getBg() != null) {
            glideManager.load(MainActivity.BASE + "/res/cover/" + mDataset.get(position).getBg())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.bg);
        } else {
            glideManager.load(MainActivity.BASE + "/images/cover.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.bg);
        }
        holder.bg.setOnClickListener(v -> requestRetrofitSong("song", Integer.parseInt(mDataset.get(position).get_sid())));
        if (mDataset.get(position).getImg() != null) {
            glideManager.load(MainActivity.BASE + "/res/artist/image/" + mDataset.get(position).getImg())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.img);
        } else {
            glideManager.load(MainActivity.BASE + "/images/artist.jpg")
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.img);
        }
        holder.img.setOnClickListener(v -> requestRetrofitArtist("artist", Integer.parseInt(mDataset.get(position).get_aid())));
        holder.title.setText(mDataset.get(position).getTitle());
        holder.title.bringToFront();
        holder.utaite.setText(mDataset.get(position).getUtaite());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void requestRetrofitSong(String what, int index) {
        final CheckTypesTask task = new CheckTypesTask();
        task.onPreExecute();
        Call<Repo> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MainActivity.UtaiteBoxGET.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();
                Fragment fragment = new MusicInfoFragment(context, glideManager, repo);
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                task.onPostExecute(null);
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e("song Problem", String.valueOf(t));
            }
        });
    }

    public void requestRetrofitArtist(String what, int index) {
        final CheckTypesTask task = new CheckTypesTask();
        task.onPreExecute();
        Call<Utaite> repos = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UtaiteBoxGET.class)
                .listRepos(what, index);
        repos.enqueue(new Callback<Utaite>() {
            @Override
            public void onResponse(Call<Utaite> call, Response<Utaite> response) {
                Utaite utaite = response.body();
                Fragment fragment = new UtaiteInfoFragment(context, glideManager, utaite);
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                task.onPostExecute(null);
            }

            @Override
            public void onFailure(Call<Utaite> call, Throwable t) {
                Log.e("song Problem", String.valueOf(t));
            }
        });
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다 ...");
            asyncDialog.show();
            asyncDialog.setCancelable(false);
            asyncDialog.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

}