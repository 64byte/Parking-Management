package com.story.parking.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.story.parking.R;
import com.story.parking.activity.abstracts.BaseActivity;

public class SettingActivity extends BaseActivity {

    public SettingActivity() {
        setContentViewResId(R.layout.activity_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
    }
}
