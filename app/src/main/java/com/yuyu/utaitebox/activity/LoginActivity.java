package com.yuyu.utaitebox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.retrofit.Repo;
import com.yuyu.utaitebox.view.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    public interface UtaiteBoxPostLogin {
        @FormUrlEncoded
        @POST("/api/login")
        Call<Repo> login(@Field("id") String id,
                         @Field("password") String password);
    }

    @BindView(R.id.id_edit)
    AutoCompleteTextView id_edit;
    @BindView(R.id.pw_edit)
    EditText pw_edit;
    @BindView(R.id.check_btn)
    AppCompatCheckBox check_btn;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Toast toast;
    private Context context;
    private String id, pw;
    private long currentTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        id = getSharedPreferences("login", MODE_PRIVATE).getString("id", null);
        pw = getSharedPreferences("login", MODE_PRIVATE).getString("pw", null);
        if (id != null && pw != null) {
            id_edit.setText(id);
            pw_edit.setText(pw);
            check_btn.setChecked(true);
            loginMethod();
        }
        check_btn.setOnCheckedChangeListener((compoundButton, b) -> {
            getSharedPreferences("login", MODE_PRIVATE).edit().putString("id", check_btn.isChecked() ? id : null).apply();
            getSharedPreferences("login", MODE_PRIVATE).edit().putString("pw", check_btn.isChecked() ? pw : null).apply();
        });
        id_edit.setOnEditorActionListener((v, actionId, event) -> {
            pw_edit.requestFocus();
            return true;
        });
        pw_edit.setOnEditorActionListener((v, actionId, event) -> {
            loginMethod();
            return true;
        });
    }

    @OnClick({R.id.login_btn, R.id.check_txt})
    public void onClickMethod(View view) {
        int vid = view.getId();
        if (vid == R.id.login_btn) {
            loginMethod();
        } else if (vid == R.id.check_txt) {
            check_btn.setChecked(!check_btn.isChecked());
        }
    }

    @Override
    public void onBackPressed() {
        if (currentTime + 2000 < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            toast.setText(getString(R.string.onBackPressed));
            toast.show();
        } else {
            super.onBackPressed();
        }
    }

    public void keyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(id_edit.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pw_edit.getWindowToken(), 0);
    }

    public void loginMethod() {
        id_edit.setError(null);
        pw_edit.setError(null);
        id = id_edit.getText().toString();
        pw = pw_edit.getText().toString();
        if (TextUtils.isEmpty(id)) {
            id_edit.setError(getString(R.string.error_field_required));
            id_edit.requestFocus();
        } else if (TextUtils.isEmpty(pw)) {
            pw_edit.setError(getString(R.string.error_field_required));
            pw_edit.requestFocus();
        } else {
            keyboardDown();
            Task task = new Task(this, 1);
            task.onPreExecute();
            Call<Repo> repos = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UtaiteBoxPostLogin.class)
                    .login(id, pw);
            repos.enqueue(new Callback<Repo>() {
                @Override
                public void onResponse(Call<Repo> call, Response<Repo> response) {
                    task.onPostExecute(null);
                    Repo repo = response.body();
                    if (!Boolean.valueOf(repo.getStatus())) {
                        toast.setText(getString(R.string.error_login));
                        toast.show();
                    } else {
                        MainActivity._mid = Integer.parseInt(repo.getData().get_mid());
                        MainActivity.token = repo.getData().getToken();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Repo> call, Throwable t) {
                    Log.e(TAG, String.valueOf(t));
                }
            });
        }
    }
}