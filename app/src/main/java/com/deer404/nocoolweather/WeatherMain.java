package com.deer404.nocoolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class WeatherMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
        /*String a = getIntent().getStringExtra("weather_id");
        Log.d("Deer404",a);*/
    }
}
