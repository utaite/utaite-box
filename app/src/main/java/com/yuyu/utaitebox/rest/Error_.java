package com.yuyu.utaitebox.rest;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Error_ {

    @SerializedName("code")
    private String code;

    @SerializedName("string")
    private String string;

}
