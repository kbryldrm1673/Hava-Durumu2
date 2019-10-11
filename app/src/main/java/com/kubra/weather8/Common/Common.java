package com.kubra.weather8.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID="2947fa68e467f4f05fea8bed28838749";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt) {
        Date date=new java.sql.Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String  formatted=sdf.format(date);
        return formatted;

    }

    public static String convertUnixToHour(long dt) {
        Date date=new java.sql.Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String  formatted=sdf.format(date);
        return formatted;
    }
}