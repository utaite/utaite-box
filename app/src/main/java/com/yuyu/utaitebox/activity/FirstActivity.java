package com.yuyu.utaitebox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.sdsmdg.tastytoast.TastyToast;
import com.yuyu.utaitebox.R;
import com.yuyu.utaitebox.view.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class FirstActivity extends AhoyOnboarderActivity {

    private final String FIRST = "FIRST", START = "START";
    private final int TITLE_TEXT_SIZE = 10, DESC_TEXT_SIZE = 7;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ^ View.SYSTEM_UI_FLAG_FULLSCREEN ^ View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        startCheck(getSharedPreferences(FIRST, MODE_PRIVATE).getBoolean(START, false));
    }

    // Finish 버튼을 눌렀다면 튜토리얼을 완료한 상태로 저장
    @Override
    public void onFinishButtonPressed() {
        getSharedPreferences(FIRST, MODE_PRIVATE).edit().putBoolean(START, true).apply();
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (Constant.CURRENT_TIME + Constant.BACK_TIME < System.currentTimeMillis()) {
            Constant.CURRENT_TIME = System.currentTimeMillis();
            TastyToast.makeText(context, getString(R.string.onBackPressed), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        } else {
            super.onBackPressed();
        }
    }

    // 이미 튜토리얼을 거친 유저인지 START로 확인 후 분기에 맞게 실행
    public void startCheck(boolean start) {
        // 튜토리얼을 거치지 않았다면 초기 화면(사용법)을 보여줌
        // 튜토리얼로 보여줄 View 객체 생성 및 값 설정
        if (!start) {
            List<AhoyOnboarderCard> pages = new ArrayList<>(
                    Arrays.asList(new AhoyOnboarderCard(getString(R.string.first_1_1), getString(R.string.first_1_2)),
                            new AhoyOnboarderCard(getString(R.string.first_2_1), getString(R.string.first_2_2))));

            Observable.from(pages)
                    .doOnUnsubscribe(() -> {
                        setFont(Typeface.createFromAsset(getAssets(), Constant.FONT));
                        setFinishButtonTitle(getString(R.string.first_btn));
                        showNavigationControls(true);
                        setGradientBackground();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            setFinishButtonDrawableStyle(ContextCompat.getDrawable(context, R.drawable.rounded_button));
                        }
                        setOnboardPages(pages);
                    })
                    .subscribe(page -> {
                        page.setBackgroundColor(R.color.black_transparent);
                        page.setTitleColor(R.color.white);
                        page.setDescriptionColor(R.color.grey_200);
                        page.setTitleTextSize(dpToPixels(TITLE_TEXT_SIZE, context));
                        page.setDescriptionTextSize(dpToPixels(DESC_TEXT_SIZE, context));
                    });

            // 튜토리얼을 이미 거쳤다면 바로 로그인 액티비티로 이동
        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }

}
