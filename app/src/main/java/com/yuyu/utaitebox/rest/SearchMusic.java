package com.yuyu.utaitebox.rest;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchMusic {

    @SerializedName("_source_id")
    private String _source_id;

    @SerializedName("song_original")
    private String song_original;

}
