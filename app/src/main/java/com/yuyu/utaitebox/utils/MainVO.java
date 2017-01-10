package com.yuyu.utaitebox.utils;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MainVO {

    private final String bg, img, title, utaite, _sid, _aid;

}