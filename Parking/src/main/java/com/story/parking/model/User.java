package com.story.parking.model;

import android.util.Log;

import com.story.parking.model.abstracts.BaseModel;

import org.json.JSONObject;

/**
 * Created by Story on 2017-06-08.
 */
public class User extends BaseModel {
    private String mEmail;
    private String mPassword;
    private String mName;
    private String mPhoneNum;
    private String mCarNum;
    private UsingInfo mUsingInfo;

    public static User InitFromJson(JSONObject json) {
        try {
            User user = new User();

            JSONObject userObj = json.getJSONObject("user");

            user.mEmail = userObj.getString("email");
            user.mPassword = userObj.getString("password");
            user.mName = userObj.getString("name");
            user.mPhoneNum = userObj.getString("phonenum");
            user.mCarNum = userObj.getString("carnum");

            user.mUsingInfo = UsingInfo.InitFromJson(userObj);

            return user;
        } catch (Exception e) {
            // error handling
            Log.d("User", e.getMessage());
        }

        return null;
    }

    public String getEmail() { return mEmail; }
    public String getPassword() { return mPassword; }
    public String getName() { return mName; }
    public String getPhoneNum() { return mPhoneNum; }
    public String getCarNum() {
        return mCarNum;
    }
    public UsingInfo getUsingInfo() { return mUsingInfo; }
}
