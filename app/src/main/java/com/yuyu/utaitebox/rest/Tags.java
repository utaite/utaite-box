package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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

}
