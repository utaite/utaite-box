package com.yuyu.utaitebox.rest;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Login {

    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private Data_ data;

    @SerializedName("error")
    private Error_ error;

}
