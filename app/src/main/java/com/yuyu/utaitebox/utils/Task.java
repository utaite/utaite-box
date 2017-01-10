package com.yuyu.utaitebox.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.yuyu.utaitebox.R;

public class Task extends AsyncTask<Void, Void, Void> {

    private String msg;
    private ProgressDialog asyncDialog;

    public Task(Context context, int type) {
        asyncDialog = new ProgressDialog(context);
        switch (type) {
            case 0:
                msg = context.getString(R.string.task_0);
                break;
            case 1:
                msg = context.getString(R.string.task_1);
                break;
            default:
                break;
        }
    }


    @Override
    public void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage(msg);
        asyncDialog.show();
        asyncDialog.setCancelable(false);
        asyncDialog.setCanceledOnTouchOutside(false);
        super.onPreExecute();
    }

    @Override
    public Void doInBackground(Void... arg0) {
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        asyncDialog.dismiss();
        super.onPostExecute(result);
    }
}