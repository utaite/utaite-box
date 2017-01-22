package com.yuyu.utaitebox.rest;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Playlist_ {

    @SerializedName("status")
    private boolean status;

    @SerializedName("array")
    private ArrayList<Integer> array;

}
