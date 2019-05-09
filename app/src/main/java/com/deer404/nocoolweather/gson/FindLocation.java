package com.deer404.nocoolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Deer404 on 2018/12/28
 */
public class FindLocation {
    @SerializedName("basic")
    public List<Findbasic> basicList;
    @SerializedName("status")
    public String status;
}
