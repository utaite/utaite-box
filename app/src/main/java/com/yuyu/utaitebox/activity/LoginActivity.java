package com.yuyu.utaitebox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.chain.ChainedToast;
import com.yuyu.utaitebox.rest.RestUtils;
import com.yuyu.utaitebox.utils.Constant;
import com.yuyu.utaitebox.utils.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class LoginActivity extends RxAppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private ChainedToast toast;

    @BindView(R.id.login_id_edit)
    AutoCompleteTextView login_id_edit;
    @BindView(R.id.login_pw_edit)
    EditText login_pw_edit;
    @BindView(R.id.login_save_btn)
    AppCompatCheckBox login_save_btn;
    @BindView(R.id.login_check_btn)
    AppCompatCheckBox login_check_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        toast = new ChainedToast(this).makeTextTo(this, "", Toast.LENGTH_SHORT);
        initialize();
    }

    @Override
    public void onBackPressed() {
        if (Constant.CURRENT_TIME + Constant.BACK_TIME < System.currentTimeMillis()) {
            Constant.CURRENT_TIME = System.currentTimeMillis();
            toast.setTextShow(getString(R.string.onBackPressed));

        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.login_login_btn)
    public void onLoginButtonClick(View view) {
        loginCheck();
    }

    @OnClick(R.id.login_guest_btn)
    public void onGuestButtonClick(View view) {
        if (!networkCheck()) {
            toast.setTextShow(getString(R.string.login_network_err));
        } else {
            loginPrepare(getString(R.string.login_guest), getString(R.string.login_guest), false);
        }
    }

    @OnClick({R.id.login_save_btn, R.id.login_save_txt})
    public void onSaveButtonClick(View view) {
        if (view.getId() == R.id.login_save_txt) {
            login_save_btn.setChecked(!login_save_btn.isChecked());
        }
        login_check_btn.setChecked(false);
    }

    @OnClick({R.id.login_check_btn, R.id.login_check_txt})
    public void onCheckButtonClick(View view) {
        if (view.getId() == R.id.login_check_txt) {
            login_check_btn.setChecked(!login_check_btn.isChecked());
        }
        login_save_btn.setChecked(false);
    }

    public void initialize() {
        String status = getSharedPreferences(getString(R.string.login_login), MODE_PRIVATE).getString(getString(R.string.login_status), null);

        Observable.just(status)
                .compose(bindToLifecycle())
                .filter(status1 -> status1 != null)
                .flatMap(status1 -> Observable.just(status1)
                        .compose(bindToLifecycle())
                        .groupBy(status2 -> status2.equals(getString(R.string.login_check))))
                .subscribe(group -> {
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.login_login), MODE_PRIVATE);
                    login_id_edit.setText(preferences.getString(getString(R.string.login_id), null));
                    login_pw_edit.setText(group.getKey() ? preferences.getString(getString(R.string.login_pw), null) : null);
                    login_check_btn.setChecked(group.getKey());
                    login_save_btn.setChecked(!group.getKey());
                    if (group.getKey()) {
                        loginCheck();
                    }
                });
    }

    public void loginCheck() {
        if (!networkCheck()) {
            toast.setTextShow(getString(R.string.login_network_err));

        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            Observable.create((Observable.OnSubscribe<EditText>) subscriber -> {
                subscriber.onNext(login_id_edit);
                subscriber.onNext(login_pw_edit);
                subscriber.onCompleted();
            }).compose(bindToLifecycle())
                    .filter(editText -> {
                        if (TextUtils.isEmpty(getText(editText))) {
                            editText.setError(getString(R.string.login_required_err));
                            editText.requestFocus();
                        }
                        return !TextUtils.isEmpty(getText(editText));
                    })
                    .map(this::getText)
                    .subscribe(arrayList::add,
                            e -> Log.e(TAG, e.toString()),
                            () -> {
                                if (arrayList.size() == 2) {
                                    loginPrepare(arrayList.get(0), arrayList.get(1), true);
                                }
                            });
        }
    }

    public boolean networkCheck() {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void loginPrepare(String id, String pw, boolean isGuest) {
        if(isGuest) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.login_login), MODE_PRIVATE);

            preferences.edit().putString(getString(R.string.login_status), login_check_btn.isChecked() ?
                    getString(R.string.login_check) : login_save_btn.isChecked() ?
                    getString(R.string.login_save) : null).apply();
            preferences.edit().putString(getString(R.string.login_id), login_check_btn.isChecked() || login_save_btn.isChecked() ? id : null).apply();
            preferences.edit().putString(getString(R.string.login_pw), login_check_btn.isChecked() ? pw : null).apply();
        }

        Task task = new Task(context);
        task.onPreExecute();
        RestUtils.getRetrofit()
                .create(RestUtils.Login.class)
                .login(id, pw)
                .subscribe(o -> {
                            task.onPostExecute(null);
                            if (o.isStatus()) {
                                MainActivity.MID = o.getData().get_mid();
                                MainActivity.TOKEN = o.getData().getToken();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                toast.setTextShow(getString(R.string.login_auth_err));
                            }
                        },
                        e -> {
                            task.onPostExecute(null);
                            Log.e(TAG, e.toString());
                            toast.setTextShow(getString(R.string.login_network_err));
                        });
    }

}
