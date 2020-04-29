package com.story.parking.activity.abstracts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.story.parking.R;

import butterknife.ButterKnife;

/**
 * Created by Story on 2017-06-07.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    private Toolbar mToolbar;

    protected Context mContext;
    protected ActionBar mActionBar;

    private int mContentViewResId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(getContentViewResId());
//        ButterKnife.bind(this);
    }

    protected void setupActionBar() {
        if (mToolbar == null) return;

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    public int getContentViewResId() {
        return mContentViewResId;
    }

    public void setContentViewResId(int mContentViewResId) {
        this.mContentViewResId = mContentViewResId;
    }
}
