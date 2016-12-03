package com.yuyu.utaitebox.retrofit;

import com.google.gson.annotations.SerializedName;

public class Recent {

    @SerializedName("ribbon")
    private Ribbon ribbon;

    @SerializedName("listen")
    private Listen listen;

    public Ribbon getRibbon() {
        return ribbon;
    }

    public void setRibbon(Ribbon ribbon) {
        this.ribbon = ribbon;
    }

    public Listen getListen() {
        return listen;
    }

    public void setListen(Listen listen) {
        this.listen = listen;
    }

}
