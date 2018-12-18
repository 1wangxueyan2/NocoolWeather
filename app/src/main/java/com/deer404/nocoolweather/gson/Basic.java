package com.deer404.nocoolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deer404 on 2018/12/14
 */
public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherid;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
