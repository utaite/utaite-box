package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Recent {

    @SerializedName("ribbon")
    private Ribbon ribbon;

    @SerializedName("listen")
    private Listen listen;

}
