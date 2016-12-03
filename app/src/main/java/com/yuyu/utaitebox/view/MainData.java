package com.yuyu.utaitebox.view;

public class MainData {
    private String bg, img, title, utaite, _sid, _aid;

    public MainData(String bg, String img, String title, String utaite, String _sid, String _aid) {
        this.bg = bg;
        this.img = img;
        this.title = title;
        this.utaite = utaite;
        this._sid = _sid;
        this._aid = _aid;
    }

    public String getBg() {
        return bg;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getUtaite() {
        return utaite;
    }

    public String get_sid() {
        return _sid;
    }

    public String get_aid() {
        return _aid;
    }
}