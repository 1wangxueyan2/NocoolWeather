package com.deer404.nocoolweather.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Deer404 on 2018/12/20
 */
public class Time {
    public static long converTime(String time){ //时间换算
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //设定时间的样式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String cDate = formatter.format(curDate); //如果不转为string,会有多出的毫秒差，我们需要这样
        Date serverDate = null; //接口获取的时间
        try {
            serverDate = formatter.parse(time);//接口获取的时间
            curDate = formatter.parse(cDate);
            long diff = serverDate.getTime() - curDate.getTime(); //时间差
            long days = diff / (1000 * 60 * 60 * 24); //除以 1天的毫秒量
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    return -4;
    }
}
