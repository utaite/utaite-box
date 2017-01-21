package com.yuyu.utaitebox.rest;


import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    public interface Register {
        @FormUrlEncoded
        @POST("api/register")
        Observable<com.yuyu.utaitebox.rest.Login> register(@Field("email") String email,
                                                           @Field("username") String username,
                                                           @Field("password") String password);
    }

    public interface Login {
        @FormUrlEncoded
        @POST("api/login")
        Observable<com.yuyu.utaitebox.rest.Login> login(@Field("id") String id,
                                                        @Field("password") String pw);
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

    public interface SearchApi1 {
        @GET("api/search/all/{what}")
        Observable<Search> searchApi1(@Path("what") String what);
    }

    public interface SearchApi2 {
        @GET("api/source/{index}/list/{repeat}")
        Observable<ArrayList<Source>> searchApi2(@Path("index") int index,
                                                 @Path("repeat") int repeat);
    }

    public interface MusicRibbon {
        @GET("api/song/ribbon/{index}")
        Observable<Void> musicRibbon(@Header("Authorization") String authorization,
                                     @Path("index") int index);
    }

    public interface UtaiteRibbon {
        @GET("api/artist/heart/{index}")
        Observable<Void> utaiteRibbon(@Header("Authorization") String authorization,
                                      @Path("index") int index);
    }

    public interface MusicTimeline {
        @FormUrlEncoded
        @POST("api/song/comment")
        Observable<Void> musicTimeline(@Header("Authorization") String authorization,
                                       @Field("_sid") int _sid,
                                       @Field("comment") String comment);
    }

    public interface UtaiteTimeline {
        @FormUrlEncoded
        @POST("api/artist/comment")
        Observable<Void> utaiteTimeline(@Header("Authorization") String authorization,
                                        @Field("_aid") int _aid,
                                        @Field("comment") String comment);
    }

}