package com.deer404.nocoolweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.deer404.nocoolweather.gson.FindLocation;
import com.deer404.nocoolweather.gson.Findbasic;
import com.deer404.nocoolweather.util.HttpUtil;
import com.deer404.nocoolweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements AMapLocationListener {
    public AMapLocationClient mLocationClient ;
    public AMapLocationClientOption mLocationOption = null;
    private String ccid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> permissionList = new ArrayList<>();
        // 申请权限
        // =============================================
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }else {
            requestLocation();
        }
        // =============================================
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        Intent intent = new Intent(this,WeatherActivity.class);
        /*intent.putExtra("weather_id",ccid);*/
        startActivity(intent);
/*        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }*/
    private void requestLocation(){
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            String district = aMapLocation.getDistrict();
            StringBuilder builder = new StringBuilder();
            for (String lo : district.split("区")){
                    builder.append(lo);
            }
            String location = builder.toString();
            findLocation(location);

        }else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }

    /*
     * 权限申请
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    private void findLocation(String location){
        StringBuilder locationid = new StringBuilder();
        HttpUtil.sendfindlocation(location, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responses = response.body().string();
                FindLocation findLocation = Utility.handleLocationResponse(responses);
                if (findLocation.status.equals("ok")){
                    for (Findbasic basic: findLocation.basicList){
                        locationid.append(basic.locationid);
                    }
                    String cid = locationid.toString();
                    ccid = cid;
                }else {
                    Log.d("Deer404Log","出错");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
}
