package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Upload {

    @SerializedName("cover")
    private String cover;

    @SerializedName("number")
    private String number;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
