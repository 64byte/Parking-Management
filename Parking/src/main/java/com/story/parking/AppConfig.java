package com.story.parking;

import com.story.parking.model.User;

/**
 * Created by Story on 2017-06-07.
 */
public class AppConfig {
    public static final String BASE_URL = "http://172.17.18.230:8080";
    public static final String API_BASE_URL = BASE_URL + "/api/";
    public static final String API_KEY = "4cb873a9f2f3b850997957d56d9fd308";

    private static AppConfig sInstance = null;

    private String mEmail = null;
    private User mUser = null;
    private String mCheckoutKey = null;

    public static AppConfig getInstance() {
        if (sInstance == null) {
            synchronized (AppConfig.class) {
                if (sInstance == null) {
                    sInstance = new AppConfig();
                }
            }
        }
        return sInstance;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public String getCheckoutKey() { return mCheckoutKey; }

    public void setCheckoutKey(String checkoutkey) {
        this.mCheckoutKey = checkoutkey;
    }
}
