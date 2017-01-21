package com.yuyu.utaitebox.rest;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Timeline {

    @SerializedName("left")
    private ArrayList<Left> left;

    @SerializedName("right")
    private ArrayList<Right> right;

    @SerializedName("today")
    private Today today;

}
