package com.yuyu.utaitebox.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.chain.Chained;
import com.yuyu.utaitebox.chain.ChainedToast;
import com.yuyu.utaitebox.rest.RestUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class RegisterActivity extends RxAppCompatActivity {

    private final String TAG = RegisterActivity.class.getSimpleName();

    private Context context;
    private ChainedToast toast;

    @BindView(R.id.register_email_edit)
    AutoCompleteTextView register_email_edit;
    @BindView(R.id.register_id_edit)
    AutoCompleteTextView register_id_edit;
    @BindView(R.id.register_pw_edit)
    EditText register_pw_edit;
    @BindView(R.id.register_bg)
    ImageView register_bg;
    @BindView(R.id.register_register_btn)
    Button register_register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        context = this;
        toast = new ChainedToast(this).makeTextTo(this, "", Toast.LENGTH_SHORT);
        register_bg.setImageResource(R.drawable.register_bg);
        register_bg.setAlpha(125);
        Chained.setAlpha(150, register_register_btn);
        setTitle(getString(R.string._login_register_btn));
    }

    @OnClick(R.id.register_register_btn)
    public void onRegisterButtonClick(View view) {
        registerCheck();
    }

    public void registerCheck() {
        if (!networkCheck()) {
            toast.setTextShow(getString(R.string.login_network_err));

        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            Observable.create((Observable.OnSubscribe<EditText>) subscriber -> {
                subscriber.onNext(register_email_edit);
                subscriber.onNext(register_id_edit);
                subscriber.onNext(register_pw_edit);
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
                                if (arrayList.size() == 3) {
                                    if (!emailCheck()) {
                                        toast.setTextShow(getString(R.string.register_email_err));
                                    } else {
                                        loginPrepare(arrayList.get(0), arrayList.get(1), arrayList.get(2));
                                    }
                                }
                            });
        }
    }

    public boolean networkCheck() {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public boolean emailCheck() {
        return getText(register_email_edit).contains("@") && getText(register_email_edit).contains(".");
    }

    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void loginPrepare(String email, String id, String pw) {
        RestUtils.getRetrofit()
                .create(RestUtils.Register.class)
                .register(email, id, pw)
                .subscribe(o -> {
                            Log.e(TAG, o.toString());
                            if (o.isStatus()) {
                                toast.setTextShow(getString(R.string.register_success));
                                finish();
                            } else {
                                if (Integer.parseInt(o.getError().getCode()) == 2) {
                                    toast.setTextShow(getString(R.string.register_err_2));
                                } else if (Integer.parseInt(o.getError().getCode()) == 3) {
                                    toast.setTextShow(getString(R.string.register_err_3));
                                } else if (Integer.parseInt(o.getError().getCode()) == 4) {
                                    toast.setTextShow(getString(R.string.register_err_4));
                                }
                            }
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            toast.setTextShow(getString(R.string.login_network_err));
                        });
    }

}
