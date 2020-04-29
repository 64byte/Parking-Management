package com.story.parking.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.story.parking.AppConfig;
import com.story.parking.R;
import com.story.parking.RequestManager;
import com.story.parking.activity.abstracts.BaseActivity;

import org.json.JSONObject;

import java.util.Date;

public class ExitCarActivity extends BaseActivity {

    private TextView mTextViewExitName;
    private TextView mTextViewEntranceTime;
    private TextView mTextViewInfoMsg;
    private Button mButtonTest;

    public ExitCarActivity() {
        setContentViewResId(R.layout.activity_exit_car);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        mTextViewExitName = (TextView)findViewById(R.id.textview_exitname);
        mTextViewEntranceTime = (TextView)findViewById(R.id.textview_exitentrancetime);
        mTextViewInfoMsg = (TextView)findViewById(R.id.textview_infomsg);
        mButtonTest = (Button)findViewById(R.id.button_test);

        mTextViewExitName.setText(AppConfig.getInstance().getUser().getName());

        RequestManager.getInstance().doRequestUser(ExitCarActivity.this, AppConfig.getInstance().getEmail(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    ExitCarActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (AppConfig.getInstance().getUser().getUsingInfo() != null) {
                                if (AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime() > 0) {
                                    Date date = new Date(AppConfig.getInstance().getUser().getUsingInfo().getEntranceTime());
                                    mTextViewEntranceTime.setText(date.toString());
                                } else {
                                    mTextViewEntranceTime.setText("");
                                }
                            }
                        }
                    });

                    return true;
                }

                return false;
            }
        });

        RequestManager.getInstance().doRquestCalcPayment(ExitCarActivity.this, AppConfig.getInstance().getUser().getCarNum(), new RequestManager.OnJSON() {
                    @Override
                    public boolean onJSON(JSONObject json) {
                        try {
                            if (json != null) {
                                final String totalpay = json.getString("totalpayment");
                                ExitCarActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTextViewInfoMsg.setText("총 요금은 " + totalpay + "원 입니다. 출차하시려면 결제를 눌러주세요.");
                                    }
                                });
                            }
                        } catch (Exception e) {

                        }

                        return false;
                    }
                }
        );

         mButtonTest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 RequestManager.getInstance().doRequestCar(ExitCarActivity.this, AppConfig.getInstance().getUser().getCarNum(), new RequestManager.OnJSON() {
                     @Override
                     public boolean onJSON(JSONObject json) {

                         if (json != null) {


                                        PaymentActivity.Open(ExitCarActivity.this);
                                        finish();
                                    }

                                    return false;
                                }
                            });
                    }
                });

        if (AppConfig.getInstance().getCheckoutKey() != null && AppConfig.getInstance().getCheckoutKey().length() > 0) {
            doRequestExitCar();
        }
    }

    private void doRequestExitCar() {
        RequestManager.getInstance().doRequestExitCar(ExitCarActivity.this, AppConfig.getInstance().getUser().getCarNum(), AppConfig.getInstance().getCheckoutKey(), new RequestManager.OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    RequestManager.getInstance().doRequestUpdateUserInfo(ExitCarActivity.this, null, null, null, 0, 0, new RequestManager.OnJSON() {
                        @Override
                        public boolean onJSON(JSONObject json) {
                            return false;
                        }
                    });
                }

                return false;
            }
        });
    }

    public static void Open(BaseActivity baseActivity) {
        baseActivity.startActivity(new Intent(baseActivity, ExitCarActivity.class));
    }
}
