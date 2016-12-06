package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Service {

    @SerializedName("other")
    private ArrayList<Other> other;

    @SerializedName("ribbon")
    private ArrayList<Ribbon> ribbon;

    public ArrayList<Other> getOther() {
        return other;
    }

    public void setOther(ArrayList<Other> other) {
        this.other = other;
    }

    public ArrayList<Ribbon> getRibbon() {
        return ribbon;
    }

    public void setRibbon(ArrayList<Ribbon> ribbon) {
        this.ribbon = ribbon;
    }

}
