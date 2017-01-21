package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SearchArtist {

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("artist_en")
    private String artist_en;

    @SerializedName("artist_cover")
    private String artist_cover;

}
