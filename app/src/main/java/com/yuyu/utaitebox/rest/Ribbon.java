package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ribbon {

    @SerializedName("cover")
    private String cover;

    @SerializedName("number")
    private String number;

    @SerializedName("id")
    private String id;

    @SerializedName("_mid")
    private String _mid;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("date")
    private String date;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("description")
    private String description;

    @SerializedName("websites")
    private String websites;

    @SerializedName("lastfm")
    private String lastfm;

    @SerializedName("skype")
    private String skype;

    @SerializedName("twitter")
    private String twitter;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("lastedit")
    private String lastedit;

}
