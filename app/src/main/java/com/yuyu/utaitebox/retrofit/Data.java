package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

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

    public String get_mid() {
        return _mid;
    }

    public void set_mid(String _mid) {
        this._mid = _mid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
