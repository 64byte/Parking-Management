package com.story.parking.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.story.parking.AppConfig;
import com.story.parking.R;
import com.story.parking.RequestManager;
import com.story.parking.Util;
import com.story.parking.activity.abstracts.BaseActivity;
import com.story.parking.model.User;
import com.story.parking.model.UsingInfo;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EntranceCarActivity extends BaseActivity {
    private ProgressDialog mProgressDialog;
    private ProgressDialog pd;
    private boolean mRequestResult;

    private TextView mTextViewName;
    private TextView mTextViewCarNum;
    private TextView mTextViewEntranceTime;

    public EntranceCarActivity() {
        setContentViewResId(R.layout.activity_entrance_car);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        setupViews();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doRequestEntranceCar();
            }
        });
    }

    private void setupViews() {
        mTextViewName = (TextView)findViewById(R.id.textview_name);
        mTextViewCarNum = (TextView)findViewById(R.id.textview_carnum);
        mTextViewEntranceTime = (TextView)findViewById(R.id.textview_entrancetime);

        mTextViewName.setText(AppConfig.getInstance().getUser().getName());
        mTextViewCarNum.setText(AppConfig.getInstance().getUser().getCarNum());

        RequestManager.getInstance().doRequestUser(EntranceCarActivity.this, AppConfig.getInstance().getEmail(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    EntranceCarActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (AppConfig.getInstance().getUser() != null) {
                                if (AppConfig.getInstance().getUser().getUsingInfo() != null) {
                                    if (AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime() > 0) {
                                        Date date = new Date(AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime());
                                        mTextViewEntranceTime.setText(date.toString());
                                    } else {
                                        mTextViewEntranceTime.setText("");
                                    }
                                }
                            }
                        }
                    });
                }

                return false;
            }
        });
    }

    public void doRequestEntranceCar() {
        RequestManager.getInstance().doRequestEntranceCar(EntranceCarActivity.this, AppConfig.getInstance().getUser().getCarNum(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                try {
                    if (json != null) {
                        RequestManager.getInstance().doRequestUser(EntranceCarActivity.this, AppConfig.getInstance().getEmail(), new RequestManager.OnJSON() {
                            @Override
                            public boolean onJSON(JSONObject json) {
                                if (json != null) {
                                    EntranceCarActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (AppConfig.getInstance().getUser() != null) {
                                                if (AppConfig.getInstance().getUser().getUsingInfo() != null) {
                                                    if (AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime() > 0) {
                                                        Date date = new Date(AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime());
                                                        mTextViewEntranceTime.setText(date.toString());
                                                    } else {
                                                        mTextViewEntranceTime.setText("");
                                                    }
                                                }
                                            }
                                        }
                                    });

                                    return true;
                                }

                                return false;
                            }
                        });

                        return true;
                    }
                } catch (Exception e) {

                }

                return false;
            }
        });
    }
}
