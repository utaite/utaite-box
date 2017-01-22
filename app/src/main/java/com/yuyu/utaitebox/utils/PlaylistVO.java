package com.yuyu.utaitebox.utils;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class PlaylistVO {

    @SerializedName("_sid")
    private int _sid;

    @SerializedName("_aid")
    private int _aid;

    @SerializedName("ribbon")
    private int ribbon;

    @SerializedName("song_original")
    private String song_original;

    @SerializedName("artist_en")
    private String artist_en;

    @SerializedName("key")
    private String key;

    @SerializedName("cover")
    private String cover;

}
