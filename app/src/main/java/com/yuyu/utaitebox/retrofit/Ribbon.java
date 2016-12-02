package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Ribbon {

    @SerializedName("cover")
    private String cover;

    @SerializedName("number")
    private String number;

    @SerializedName("id")
    private String id;

    @SerializedName("_mid")
    private String _mid;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("date")
    private String date;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("description")
    private String description;

    @SerializedName("websites")
    private String websites;

    @SerializedName("lastfm")
    private String lastfm;

    @SerializedName("skype")
    private String skype;

    @SerializedName("twitter")
    private String twitter;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("lastedit")
    private String lastedit;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLastedit() {
        return lastedit;
    }

    public void setLastedit(String lastedit) {
        this.lastedit = lastedit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_mid() {
        return _mid;
    }

    public void set_mid(String _mid) {
        this._mid = _mid;
    }

    public String get_sid() {
        return _sid;
    }

    public void set_sid(String _sid) {
        this._sid = _sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public String getLastfm() {
        return lastfm;
    }

    public void setLastfm(String lastfm) {
        this.lastfm = lastfm;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
