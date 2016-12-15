package com.yuyu.utaitebox.view;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

public class Custom {

    public void viewVisibilities(boolean visibility, View... view) {
        for (View e : view) {
            e.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    public void viewTexts(TextView[] view, String[] value) {
        for (int i = 0; i < view.length; i++) {
            view[i].setText(value[i]);
        }
    }

    public void viewPaints(TextView... view) {
        for (TextView e : view) {
            e.setPaintFlags(e.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }
    }

}
