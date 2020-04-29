/**
 * Created by Story on 2017-06-07.
 */

package com.story.parking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.UrlQuerySanitizer;
import android.os.Debug;
import android.util.Log;

import com.story.parking.activity.ExitCarActivity;
import com.story.parking.activity.abstracts.BaseActivity;
import com.story.parking.model.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestManager {
    private volatile static RequestManager sInstance = null;
    private final OkHttpClient mClient = new OkHttpClient();
    private String mSession = null;

    public static RequestManager getInstance() {
        if (sInstance == null) {
            synchronized (RequestManager.class) {
                if (sInstance == null) {
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    public void doLogin(BaseActivity baseActivity, String email, String password, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doLogin", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("email", email);
        formBodyBuilder.add("password", password);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "user/login", new OnJSON() {
            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        ;
                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRequestUser(BaseActivity baseActivity, String email, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("email", email);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "users/email", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        AppConfig appConfig = AppConfig.getInstance();
                        Log.i("doRequestUSer", "it's req");

                        User user = User.InitFromJson(json);
                        if (user != null)
                            appConfig.setUser(user);

                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRequestCar(BaseActivity baseActivity, String carnum, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("carnum", carnum);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "car", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        Log.i("doRequestCar", "it's req");
                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRequestEntranceCar(final BaseActivity baseActivity, String carNum, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("carnum", carNum);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "cars", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        Log.i("entrancecar", " >>>>>>>>>>>> " + json.getString("entrancetime"));
                        final long Entrancetime = json.getLong("entrancetime");

                        baseActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
                                builder.setMessage("주차가 완료되면 버튼을 눌러주세요.");
                                builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RequestManager.getInstance().doRequestUpdateUserInfo(baseActivity, null, null, null, Entrancetime, 0, new RequestManager.OnJSON() {
                                            @Override
                                            public boolean onJSON(JSONObject json) {
                                                try {
                                                    if (json != null) {

                                                    }
                                                } catch (Exception e) {

                                                }

                                                return false;
                                            }
                                        });
                                    }
                                });

                                builder.show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRequestExitCar(final BaseActivity baseActivity, String carnum, String checkoutkey, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("carnum", carnum);
        formBodyBuilder.add("checkoutKey", checkoutkey);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "cars/exit", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        AppConfig.getInstance().setCheckoutKey("");

                        baseActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
                                builder.setMessage("출차가 완료되었습니다.");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        baseActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                baseActivity.onBackPressed();
                                            }
                                        });
                                    }
                                });
                                builder.create().show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRquestCalcPayment(final BaseActivity baseActivity, String carnum, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestCalcPayment", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("carnum", carnum);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "cars/usingpayment", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);

    }

    public void doRequestPay(final BaseActivity baseActivity, String cardnum, String exp, String cvv, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("cardnum", cardnum);
        formBodyBuilder.add("expiration", exp);
        formBodyBuilder.add("cvv", cvv);

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "payment", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {
                        AppConfig.getInstance().setCheckoutKey(json.getString("checkoutKey"));
                        Log.i("onJSON", ">>>>>>>>>>>>>>> " + AppConfig.getInstance().getCheckoutKey());

                        baseActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
                                builder.setMessage("결제가 완료되었습니다.");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
/*                                        doRequestExitCar(baseActivity, AppConfig.getInstance().getUser().getCarNum(), AppConfig.getInstance().getCheckoutKey(), new RequestManager.OnJSON() {
                                                    @Override
                                                    public boolean onJSON(JSONObject json) {
                                                        if (json != null) {
                                                            baseActivity.onBackPressed();
                                                        }

                                                        return false;
                                                    }
                                                }); */
//                                        baseActivity.onBackPressed();
                                        ExitCarActivity.Open(baseActivity);
                                        baseActivity.finish();
                                    }
                                });
                                builder.create().show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    public void doRequestUpdateUserInfo(final BaseActivity baseActivity, String name, String password, String phonenum, long entrancetime, long exittime, final OnJSON onJSON) {
        if (baseActivity == null || onJSON == null) {
            Log.d("doRequestUser", "baseActivity or onJSON is null");
            return;
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("email", AppConfig.getInstance().getEmail());

        if (name != null)
            formBodyBuilder.add("name", name);

        if (password != null)
            formBodyBuilder.add("password", password);

        if (phonenum != null)
            formBodyBuilder.add("phonenum", phonenum);

        formBodyBuilder.add("usinginfo.entrancetime", entrancetime + "");
        formBodyBuilder.add("usinginfo.exittime", exittime + "");

        requestHttp(baseActivity, AppConfig.API_BASE_URL + "users/update", new OnJSON() {

            @Override
            public boolean onJSON(JSONObject json) {
                if (json != null) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                        onJSON.onJSON(null);
                    }
                }

                onJSON.onJSON(json);
                return (json != null);
            }
        }, formBodyBuilder, null);
    }

    private void requestHttp(final BaseActivity baseActivity, String url, final OnJSON onJSON, FormBody.Builder formBodyBuilder, Hashtable<String, String> headers) {
        if (baseActivity == null || onJSON == null) {
            Log.d("requestHttp", "baseActivity or onJSON is null");
            return;
        }

        if (formBodyBuilder == null) {
            formBodyBuilder = new FormBody.Builder();
        }

        formBodyBuilder.add("apiKey", AppConfig.API_KEY);

        AppConfig appConfig = AppConfig.getInstance();

        Request.Builder builder = new Request.Builder().url(url).post(formBodyBuilder.build());

        if (headers != null) {
            Enumeration e = headers.keys();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String val = headers.get(key);
                builder.header(key, val);
            }
        }

        Request request = builder.build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                // Error
                onJSON.onJSON(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String res = response.body().string();

                try {
                    Log.d("requestHttp", "res data =" + res);
                    final JSONObject json = new JSONObject(res);

                    if (json.has("error") && json.getBoolean("error")) {
                        baseActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
                                    builder.setMessage(json.has("errormsg") ? json.getString("errormsg") : "알 수 없는 오류");
                                    builder.setPositiveButton("확인", null);
                                    builder.create().show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        onJSON.onJSON(null);
                    } else {
                        onJSON.onJSON(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onJSON.onJSON(null);
                }
            }
        });
    }

    public interface OnJSON {
        boolean onJSON(JSONObject json);
    }
}
