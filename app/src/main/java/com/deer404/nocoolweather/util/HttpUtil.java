package com.deer404.nocoolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public  static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    public  static void sendfindlocation(String location,okhttp3.Callback callback){
        String address = "https://search.heweather.net/find?";
        String key ="b9305ee0694f49e9bf5c897a4571c969";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address+"location="+location+"&key="+key+"&group=cn").build();
        client.newCall(request).enqueue(callback);
    }
}
