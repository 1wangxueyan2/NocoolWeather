package com.deer404.nocoolweather.gson;

/**
 * Created by Deer404 on 2018/12/14
 */
public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
