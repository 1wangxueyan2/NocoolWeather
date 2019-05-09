package com.deer404.nocoolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Deer404 on 2018/12/14
 */
public class Weather {
    public String status;
    public Basic basic;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
/*    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;*/
}
