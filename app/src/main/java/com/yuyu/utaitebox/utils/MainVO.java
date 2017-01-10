package com.yuyu.utaitebox.utils;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MainVO {

    private String bg, img, title, utaite, _sid, _aid;

    public MainVO() {
    }

    public MainVO(String bg, String img, String title, String utaite, String _sid, String _aid) {
        this.bg = bg;
        this.img = img;
        this.title = title;
        this.utaite = utaite;
        this._sid = _sid;
        this._aid = _aid;
    }

}