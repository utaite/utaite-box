package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.experimental.Accessors;

@lombok.Data
@Accessors(chain = true)
public class Data {


    @SerializedName("_mid")
    private String _mid;

    @SerializedName("username")
    private String username;

    @SerializedName("token")
    private String token;

    @SerializedName("cover")
    private String cover;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

}
