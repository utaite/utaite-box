package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Song {

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_source_id")
    private String _source_id;

    @SerializedName("key")
    private String key;

    @SerializedName("_aid")
    private String _aid;

    @SerializedName("played")
    private String played;

    @SerializedName("ribbon")
    private String ribbon;

    @SerializedName("comment")
    private String comment;

    @SerializedName("info_date")
    private String info_date;

    @SerializedName("nicovideo")
    private String nicovideo;

    @SerializedName("youtube")
    private String youtube;

    @SerializedName("description")
    private String description;

    @SerializedName("cover")
    private String cover;

    @SerializedName("lasteit")
    private String lasteit;

    @SerializedName("artist_en")
    private String artist_en;

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

    @SerializedName("song_original")
    private String song_original;

    @SerializedName("song_en")
    private String song_en;

    @SerializedName("song_jp")
    private String song_jp;

    @SerializedName("song_kr")
    private String song_kr;

}