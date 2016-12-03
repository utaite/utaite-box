package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String get_sid() {
        return _sid;
    }

    public void set_sid(String _sid) {
        this._sid = _sid;
    }

    public String get_aid() {
        return _aid;
    }

    public void set_aid(String _aid) {
        this._aid = _aid;
    }

    public String getArtist_en() {
        return artist_en;
    }

    public void setArtist_en(String artist_en) {
        this.artist_en = artist_en;
    }

    public String getSong_original() {
        return song_original;
    }

    public void setSong_original(String song_original) {
        this.song_original = song_original;
    }

    public String getInfo_date() {
        return info_date;
    }

    public void setInfo_date(String info_date) {
        this.info_date = info_date;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRibbon() {
        return ribbon;
    }

    public void setRibbon(String ribbon) {
        this.ribbon = ribbon;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getArtist_cover() {
        return artist_cover;
    }

    public void setArtist_cover(String artist_cover) {
        this.artist_cover = artist_cover;
    }

}
