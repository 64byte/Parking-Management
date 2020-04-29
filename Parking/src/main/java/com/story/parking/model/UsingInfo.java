package com.story.parking.model;

import android.util.Log;

import com.story.parking.model.abstracts.BaseModel;

import org.json.JSONObject;

/**
 * Created by Story on 2017-06-10.
 */
public class UsingInfo extends BaseModel {
    private long mEntranceTime;
    private long mExitTime;
    private long mTotalTime;
    private long mTotalAmount;

    public static UsingInfo InitFromJson(JSONObject json) {
        try {
            UsingInfo usinginfo = new UsingInfo();

            JSONObject usingObj = json.getJSONObject("usinginfo");

            usinginfo.mEntranceTime = usingObj.getLong("entrancetime");
            usinginfo.mExitTime = usingObj.getLong("exittime");
//            usinginfo.mTotalTime = usingObj.getLong("totaltime");
//            usinginfo.mTotalAmount = usingObj.getLong("totalamount");

            return usinginfo;
        } catch (Exception e) {
            Log.i("UsingInfo", "nulllllllllllllllllllllllllllllll");
        }

        return null;
    }

    public long getEntranceTime() { return mEntranceTime; }
    public long getExitTime() { return mExitTime;}
}
