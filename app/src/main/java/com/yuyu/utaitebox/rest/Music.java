package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Music {

    @SerializedName("key")
    private String key;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("_source_id")
    private String _source_id;

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

    @SerializedName("related")
    private ArrayList<Related> related;

}
