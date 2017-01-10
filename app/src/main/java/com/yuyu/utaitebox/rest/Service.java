package com.yuyu.utaitebox.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Service {

    @SerializedName("other")
    private ArrayList<Other> other;

    @SerializedName("ribbon")
    private ArrayList<Ribbon> ribbon;

}
