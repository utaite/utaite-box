package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
