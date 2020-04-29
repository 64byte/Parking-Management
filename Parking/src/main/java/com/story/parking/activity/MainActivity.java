package com.story.parking.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.story.parking.AppConfig;
import com.story.parking.R;
import com.story.parking.RequestManager;
import com.story.parking.activity.abstracts.BaseActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Story on 2017-06-08.
 */
public class MainActivity extends BaseActivity {
    private TextView mTextViewUserName;
    private TextView mTextViewUserEmail;
    private TextView mTextViewUserCarNum;

    private ImageButton mImageButtonEntranceCar = null;
    private ImageButton mImageButtonExitCar = null;
    private ImageButton mImageButtonSetting = null;
    private ImageButton mImageButtonExitApp = null;

    public MainActivity() {
        super();
        setContentViewResId(R.layout.activty_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main);
        setupViews();

        doRequestUser();
    }

    private void setupViews() {
        mTextViewUserEmail = (TextView)findViewById(R.id.textview_useremail);
        mTextViewUserName = (TextView)findViewById(R.id.textview_username);
        mTextViewUserCarNum = (TextView)findViewById(R.id.textview_usercarnum);
        mImageButtonEntranceCar = (ImageButton)findViewById(R.id.imagebutton_entrance_car);
        mImageButtonExitCar = (ImageButton)findViewById(R.id.imagebutton_exit_car);
        mImageButtonSetting = (ImageButton)findViewById(R.id.imagebutton_setting);

        mImageButtonEntranceCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntranceCarActivity.class);
                startActivity(intent);
            }
        });

        mImageButtonExitCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExitCarActivity.class);
                startActivity(intent);
            }
        });

        mImageButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doRequestUser() {
        RequestManager.getInstance().doRequestUser(MainActivity.this, AppConfig.getInstance().getEmail(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewUserEmail.setText(AppConfig.getInstance().getUser().getEmail());
                            mTextViewUserName.setText(AppConfig.getInstance().getUser().getName());
                            mTextViewUserCarNum.setText(AppConfig.getInstance().getUser().getCarNum());
                        }
                    });
                }

                return false;
            }
        });
    }

    public static void Open(BaseActivity baseActivity) {
        baseActivity.startActivity(new Intent(baseActivity, MainActivity.class));
    }
}
