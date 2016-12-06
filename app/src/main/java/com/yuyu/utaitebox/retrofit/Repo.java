package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    private ArrayList<Artist> aritst;

    @SerializedName("status")
    private String status;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("data")
    private Data data;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public String getRibbon() {
        return ribbon;
    }

    public void setRibbon(String ribbon) {
        this.ribbon = ribbon;
    }

    public Service getSidebar() {
        return sidebar;
    }

    public void setSidebar(Service sidebar) {
        this.sidebar = sidebar;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Recent getRecent() {
        return recent;
    }

    public void setRecent(Recent recent) {
        this.recent = recent;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public ArrayList<Music> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<Music> music) {
        this.music = music;
    }

    public ArrayList<Artist> getAritst() {
        return aritst;
    }

    public void setAritst(ArrayList<Artist> aritst) {
        this.aritst = aritst;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
