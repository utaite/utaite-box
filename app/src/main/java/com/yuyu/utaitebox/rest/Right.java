package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Right {

    @SerializedName("id")
    private String id;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_mid")
    private String _mid;

    @SerializedName("type")
    private String type;

    @SerializedName("content")
    private String content;

    @SerializedName("ip")
    private String ip;

    @SerializedName("date")
    private String date;

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("artist_en")
    private String artist_en;

    @SerializedName("song_original")
    private String song_original;

    @SerializedName("played")
    private String played;

    @SerializedName("ribbon")
    private String ribbon;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("avatar")
    private String avatar;

}
