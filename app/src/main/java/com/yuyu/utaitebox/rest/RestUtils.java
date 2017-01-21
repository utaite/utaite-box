package com.yuyu.utaitebox.rest;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestUtils {

    public static final String BASE = "http://utaitebox.com/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new RxThreadCallAdapter(Schedulers.io(), AndroidSchedulers.mainThread()))
                    .build();
        }

        return retrofit;
    }

    public interface DefaultApi {
        @GET("api/{what}/{index}")
        Observable<Repo> defaultApi(@Path("what") String what,
                                    @Path("index") int index);
    }

    public interface ArraySongApi {
        @GET("api/{what}/{index}")
        Observable<ArrayList<Song>> arraySongApi(@Path("what") String what,
                                                 @Path("index") int index);
    }

    public interface ArraySongApis {
        @GET("api/artist/{aid}/list/{index}")
        Observable<ArrayList<Song>> arraySongApis(@Path("aid") int aid,
                                                  @Path("index") int index);
    }

    public interface UtaiteApi {
        @GET("api/{what}/{index}")
        Observable<Utaite> utaiteApi(@Path("what") String what,
                                     @Path("index") int index);
    }

    public interface MemberApi {
        @GET("api/member/{index}/{category}")
        Observable<ArrayList<Song>> memberApi(@Path("index") int index,
                                              @Path("category") String category);
    }

    public interface ChartApi {
        @GET("api/{what}")
        Observable<Chart> chartApi(@Path("what") String what);
    }

    public interface TimelineApi {
        @GET("api/{what}")
        Observable<Timeline> timelineApi(@Path("what") String what);
    }

    public interface SearchApi {
        @GET("api/search/all/{what}")
        Observable<Search> searchApi(@Path("what") String what);
    }

}