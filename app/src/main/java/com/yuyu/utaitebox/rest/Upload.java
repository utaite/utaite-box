package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Upload {

    @SerializedName("cover")
    private String cover;

    @SerializedName("number")
    private String number;

}
