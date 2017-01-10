package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Related {

    @SerializedName("key")
    private String key;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("artist_en")
    private String artist_en;

    @SerializedName("song_original")
    private String song_original;

    @SerializedName("info_date")
    private String info_date;

    @SerializedName("cover")
    private String cover;

    @SerializedName("ribbon")
    private String ribbon;

    @SerializedName("played")
    private String played;

    @SerializedName("artist_cover")
    private String artist_cover;

}
