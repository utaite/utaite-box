package com.yuyu.utaitebox.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.yuyu.utaitebox.R;

public class Task extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog dialog;

    public Task(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    public void onPreExecute() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getString(R.string.task_1));
        dialog.show();
        super.onPreExecute();
    }

    @Override
    public Void doInBackground(Void... arg0) {
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        dialog.dismiss();
        super.onPostExecute(result);
    }
}