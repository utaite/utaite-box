package com.yuyu.utaitebox.utils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import lombok.Data;
import lombok.experimental.Accessors;

@Parcel
@Data
@Accessors(chain = true)
public class MusicParcel {

    int sid;
    String key, cover, musicTitle, musicUtaite;

    @ParcelConstructor
    public MusicParcel(int sid, String key, String cover, String musicTitle, String musicUtaite) {
        this.sid = sid;
        this.key = key;
        this.cover = cover;
        this.musicTitle = musicTitle;
        this.musicUtaite = musicUtaite;
    }

}