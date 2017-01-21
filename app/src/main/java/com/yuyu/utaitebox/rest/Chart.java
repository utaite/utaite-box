package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Chart {

    @SerializedName("navigation")
    private Navigation navigation;

    @SerializedName("music")
    private ArrayList<Music> music;

    @SerializedName("artist")
    private ArrayList<Artist> artist;

    @SerializedName("active")
    private ArrayList<Active> active;


}
