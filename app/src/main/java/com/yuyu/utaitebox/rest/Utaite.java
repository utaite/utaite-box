package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Utaite {

    @SerializedName("artist")
    private Artist aritst;

    @SerializedName("comment")
    private ArrayList<Comment> comment;

    @SerializedName("heart")
    private String heart;

    @SerializedName("hearter")
    private ArrayList<Comment> hearter;

}