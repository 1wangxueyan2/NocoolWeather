package com.deer404.nocoolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.deer404.nocoolweather.db.City;
import com.deer404.nocoolweather.db.County;
import com.deer404.nocoolweather.db.Province;
import com.deer404.nocoolweather.gson.Weather;
import com.deer404.nocoolweather.util.HttpUtil;
import com.deer404.nocoolweather.util.Utility;



import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Deer404 on 2018/12/7
 */
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList; //存储的是个对象
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //加载布局
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { //当活动创建完成后
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position); //把集合里 对应下标的数据存储到对象里
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() { //返回键的监听事件
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){ //如果是在县级页面，返回时候查询出所属市的数据
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){ //如果是在市级页面，返回时候查询出所属省的数据
                    queryProvinces();
                }
            }
        });
        queryProvinces(); //加载省级数据
    }
    /*
     * 查询全国所有的省，优先从数据库查询，如果没有查询到就从服务器上查询
     */
    private void queryProvinces(){
        titleText.setText("中国"); //设置标题
        backButton.setVisibility(View.GONE); //隐藏返回键
        provinceList = DataSupport.findAll(Province.class); //把数据库的数据存储到list集合
        if (provinceList.size()>0){ //如果数据库有数据
            dataList.clear(); //清空数据集
            for (Province province : provinceList){ //遍历
                dataList.add(province.getProvinceName()); //将数据库查询到的放到数据集里
            }
            adapter.notifyDataSetChanged(); //通知适配器更新
            listView.setSelection(0); //定位到第一行
            currentLevel = LEVEL_PROVINCE; //省级标签
        }else {
            String address = "http://guolin.tech/api/china/";
            queryFromServer(address,"province");
        }

    }
    /*
     * 查询选中省的所有的市，优先从数据库查询，如果没有查询到就从服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() >0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged(); //主要用于告诉适配器数据发送了变化，需要更新listview的显示
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }
    /*
     * 查询选中市的所有的县，优先从数据库查询，如果没有查询到就从服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0 ){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode +"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address,final String type){ //服务器查询
        showProgressDialog(); //弹出窗口
        HttpUtil.sendOkHttpRequest(address, new Callback() {  //向服务器发送请求
            @Override
            public void onResponse(Call call, Response response) throws IOException { //响应的数据会回调到这
                String responseText =response.body().string(); //存储获得的数据
                boolean result = false; //判断是否获得了数据，下面几个if判断，如果成功就会返回true并把数据存储到数据库
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){ //如果从服务器查询到了数据
                    getActivity().runOnUiThread(new Runnable() { //回到主线程
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /*
     * 显示进度对话框*/
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
