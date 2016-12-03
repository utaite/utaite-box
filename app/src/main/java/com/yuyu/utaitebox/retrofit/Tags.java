package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("_tid")
    private String _tid;

    @SerializedName("tagname")
    private String tagname;

    @SerializedName("id")
    private String id;

    @SerializedName("_sid")
    private String _sid;

    @SerializedName("_mid")
    private String _mid;

    @SerializedName("date")
    private String date;

    public String get_tid() {
        return _tid;
    }

    public void set_tid(String _tid) {
        this._tid = _tid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_sid() {
        return _sid;
    }

    public void set_sid(String _sid) {
        this._sid = _sid;
    }

    public String get_mid() {
        return _mid;
    }

    public void set_mid(String _mid) {
        this._mid = _mid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
