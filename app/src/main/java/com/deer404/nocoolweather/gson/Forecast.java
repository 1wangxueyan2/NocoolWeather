package com.deer404.nocoolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deer404 on 2018/12/28
 */
public class Forecast {
    @SerializedName("tmp_max")
    public String tmpmax;
    @SerializedName("tmp_min")
    public String tmpmin;
    @SerializedName("cond_txt_d")
    public String cond;
    @SerializedName("date")
    public String date;
}
