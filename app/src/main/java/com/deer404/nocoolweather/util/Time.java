package com.deer404.nocoolweather.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Deer404 on 2018/12/20
 */
public class Time {
    public static long converTime(String time){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String phonetime  = formatter.format(curDate);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatter.parse(time);
            d2 = formatter.parse(phonetime);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    return -5;
    }
}
