package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Navigation {

    @SerializedName("currentPage")
    private String currentPage;

    @SerializedName("pageCount")
    private String pageCount;

    @SerializedName("data")
    private ArrayList<Data_> data;

}
