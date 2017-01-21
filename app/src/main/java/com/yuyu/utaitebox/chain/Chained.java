package com.yuyu.utaitebox.chain;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

public class Chained {

    public static void setVisibilityMany(int visibility, View... view) {
        for (View v : view) {
            v.setVisibility(visibility);
        }
    }

    public static void setTextMany(TextView[] view, String[] str) {
        for (int i = 0; i < view.length; i++) {
            view[i].setText(str[i]);
        }
    }

    public static void setPaintFlagsMany(TextView... view) {
        for (TextView v : view) {
            v.setPaintFlags(v.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }
    }

    public static void setAlpha(int alpha, View... view) {
        for(View v : view) {
            v.getBackground().setAlpha(alpha);
        }
    }

}