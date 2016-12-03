package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Utaite {

    @SerializedName("artist")
    private Artist aritst;

    @SerializedName("comment")
    private ArrayList<Comment> comment;

    @SerializedName("heart")
    private String heart;

    @SerializedName("hearter")
    private ArrayList<Comment> hearter;

    public Artist getAritst() {
        return aritst;
    }

    public void setAritst(Artist aritst) {
        this.aritst = aritst;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public ArrayList<Comment> getHearter() {
        return hearter;
    }

    public void setHearter(ArrayList<Comment> hearter) {
        this.hearter = hearter;
    }
}