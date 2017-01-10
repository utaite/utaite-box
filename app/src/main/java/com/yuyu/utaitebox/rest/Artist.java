package com.yuyu.utaitebox.rest;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Artist {

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("artist_en")
    private String aritst_en;

    @SerializedName("artist_jp")
    private String artist_jp;

    @SerializedName("artist_kr")
    private String artist_kr;

    @SerializedName("heart")
    private String heart;

    @SerializedName("artist_comment")
    private String artist_comment;

    @SerializedName("gender")
    private String gender;

    @SerializedName("artist_description")
    private String artist_description;

    @SerializedName("artist_cover")
    private String artist_cover;

    @SerializedName("artist_background")
    private String artist_background;

    @SerializedName("lastedit")
    private String lastedit;

    @SerializedName("artist_played")
    private String artist_played;

    @SerializedName("related")
    private ArrayList<Related> related;

    @SerializedName("comment")
    private String comment;

    @SerializedName("hearter")
    private ArrayList<Comment> hearter;

}
