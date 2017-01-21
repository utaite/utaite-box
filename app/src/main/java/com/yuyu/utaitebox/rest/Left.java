package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Left {

    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("_source_id")
    private String _source_id;

    @SerializedName("_mid")
    private String _mid;

    @SerializedName("artist_en")
    private String artist_en;

    @SerializedName("song_original")
    private String song_original;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("avatar")
    private String avatar;

}
