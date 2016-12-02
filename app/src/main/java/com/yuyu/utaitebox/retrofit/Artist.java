package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    public String get_aid() {
        return _aid;
    }

    public void set_aid(String _aid) {
        this._aid = _aid;
    }

    public String getAritst_en() {
        return aritst_en;
    }

    public void setAritst_en(String aritst_en) {
        this.aritst_en = aritst_en;
    }

    public String getArtist_jp() {
        return artist_jp;
    }

    public void setArtist_jp(String artist_jp) {
        this.artist_jp = artist_jp;
    }

    public String getArtist_kr() {
        return artist_kr;
    }

    public void setArtist_kr(String artist_kr) {
        this.artist_kr = artist_kr;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String getArtist_comment() {
        return artist_comment;
    }

    public void setArtist_comment(String artist_comment) {
        this.artist_comment = artist_comment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getArtist_description() {
        return artist_description;
    }

    public void setArtist_description(String artist_description) {
        this.artist_description = artist_description;
    }

    public String getArtist_cover() {
        return artist_cover;
    }

    public void setArtist_cover(String artist_cover) {
        this.artist_cover = artist_cover;
    }

    public String getArtist_background() {
        return artist_background;
    }

    public void setArtist_background(String artist_background) {
        this.artist_background = artist_background;
    }

    public String getLastedit() {
        return lastedit;
    }

    public void setLastedit(String lastedit) {
        this.lastedit = lastedit;
    }

    public String getArtist_played() {
        return artist_played;
    }

    public void setArtist_played(String artist_played) {
        this.artist_played = artist_played;
    }

    public ArrayList<Related> getRelated() {
        return related;
    }

    public void setRelated(ArrayList<Related> related) {
        this.related = related;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Comment> getHearter() {
        return hearter;
    }

    public void setHearter(ArrayList<Comment> hearter) {
        this.hearter = hearter;
    }

}
