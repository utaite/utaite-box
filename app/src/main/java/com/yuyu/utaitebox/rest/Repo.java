package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Repo {

    @SerializedName("song")
    private Song song;

    @SerializedName("comment")
    private ArrayList<Comment> comment;

    @SerializedName("ribbon")
    private String ribbon;

    @SerializedName("sidebar")
    private Service sidebar;

    @SerializedName("tags")
    private ArrayList<Tags> tags;

    @SerializedName("member")
    private Member member;

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("recent")
    private Recent recent;

    @SerializedName("upload")
    private Upload upload;

    @SerializedName("playlist")
    private Playlist playlist;

    @SerializedName("listen")
    private String listen;

    @SerializedName("navigation")
    private Navigation navigation;

    @SerializedName("music")
    private ArrayList<Music> music;

    @SerializedName("artist")
    private ArrayList<Artist> artist;

    @SerializedName("status")
    private String status;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("data")
    private Data_ data;

}
