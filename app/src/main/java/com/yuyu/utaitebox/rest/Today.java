package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Today {

    @SerializedName("listen")
    private String listen;

    @SerializedName("upload")
    private String upload;

    @SerializedName("comment")
    private String comment;

}
