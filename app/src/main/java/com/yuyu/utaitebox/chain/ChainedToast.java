package com.yuyu.utaitebox.chain;

import android.content.Context;
import android.widget.Toast;

public class ChainedToast extends Toast {

    private Toast toast;

    public ChainedToast(Context context) {
        super(context);
    }

    public void setTextShow(CharSequence s) {
        toast.setText(s);
        toast.show();
    }

    public ChainedToast makeTextTo(Context context, CharSequence text, int duration) {
        toast = Toast.makeText(context, text, duration);
        return this;
    }

}