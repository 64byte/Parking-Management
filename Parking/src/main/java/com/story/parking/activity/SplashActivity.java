package com.story.parking.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.story.parking.R;
import com.story.parking.activity.abstracts.BaseActivity;

public class SplashActivity extends BaseActivity {
    private static final long TIMEOUT_SPLASH = 2000L;

    public SplashActivity() {
        setContentViewResId(R.layout.activity_splash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.Open(SplashActivity.this);
                finish();
            }
        }, TIMEOUT_SPLASH);
    }
}
