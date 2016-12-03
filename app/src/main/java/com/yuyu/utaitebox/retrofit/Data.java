package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

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
