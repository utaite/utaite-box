package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SearchTag {

    @SerializedName("_tid")
    private String _tid;

    @SerializedName("tagname")
    private String tagname;

}
