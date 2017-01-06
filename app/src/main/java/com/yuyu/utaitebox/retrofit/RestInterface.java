package com.yuyu.utaitebox.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// Restful 통신에 필요한 데이터, 객체, 인터페이스 등의 자원을 모으기 위해 구현한 클래스
public class RestInterface {

    public static String BASE = "http://192.168.1.15/CLearn/";

    private static Retrofit retrofit;

    // Retrofit 객체 생성 후 싱글톤으로 return
    public static void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new RxThreadCallAdapter(Schedulers.io(), AndroidSchedulers.mainThread()))
                .build();
    }

    public static Retrofit getRestClient() {
        return retrofit;
    }

    public interface UtaiteBoxPostLogin {
        @FormUrlEncoded
        @POST("/api/{what}")
        Observable<Repo> login(
                @Path("what") String what,
                @Field("id") String id,
                @Field("password") String password);
    }

}
