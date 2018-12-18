package com.deer404.nocoolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deer404 on 2018/12/14
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
