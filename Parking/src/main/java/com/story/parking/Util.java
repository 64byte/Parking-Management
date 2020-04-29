package com.story.parking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Story on 2017-06-10.
 */
public class Util {
    public static String convertToCurrentTimeZone(String dat) {
        String converted_date = "";
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = utcFormat.parse(dat);

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTFormat.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));

            converted_date =  currentTFormat.format(date);
        }catch (Exception e){ e.printStackTrace();}

        return converted_date;
    }


    //get the current time zone

    public static String getCurrentTimeZone(){
        TimeZone tz = Calendar.getInstance().getTimeZone();
        System.out.println(tz.getDisplayName());
        return tz.getID();
    }
}
