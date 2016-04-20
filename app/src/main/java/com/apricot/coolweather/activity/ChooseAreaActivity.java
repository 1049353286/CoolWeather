package com.apricot.coolweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apricot.coolweather.R;
import com.apricot.coolweather.db.CoolWeatherDB;
import com.apricot.coolweather.model.City;
import com.apricot.coolweather.model.County;
import com.apricot.coolweather.model.Province;
import com.apricot.coolweather.util.ParseCallbackListener;
import com.apricot.coolweather.util.ParseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apricot on 2016/4/18.
 */
public class ChooseAreaActivity extends AppCompatActivity{
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> datalist=new ArrayList<String>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    private boolean isFromWeatherActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected",false)&&!isFromWeatherActivity){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        getSupportActionBar().hide();
        setContentView(R.layout.choose_area);
        listView= (ListView) findViewById(R.id.list_view);
        titleText= (TextView) findViewById(R.id.title_text);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        coolWeatherDB=CoolWeatherDB.getInstance(this);

        Log.d("ChooseWeather","getInstance");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    String countyCode=countyList.get(position).getCountyCode();
                    Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                    intent.putExtra("county_code",countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList=coolWeatherDB.loadProvince();
        if(provinceList.size()>0){
            datalist.clear();
            for(Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel=LEVEL_PROVINCE;
        }else{
            try {
                showProgressDialog();
                InputStream in = getAssets().open("relation.xml");
                ParseUtil.parseXMLWithDOM(coolWeatherDB, in, new ParseCallbackListener() {
                    @Override
                    public void onFinish() {
                        closeProgressDialog();
                        queryProvinces();
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChooseAreaActivity.this,"ParseXMLError",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void queryCities(){
        cityList=coolWeatherDB.loadCities(selectedProvince.getProvinceName());
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
//            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties(){
        countyList=coolWeatherDB.loadCounties(selectedCity.getCityName());
        if(countyList.size()>0){
            datalist.clear();
            for(County county:countyList){
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel=LEVEL_COUNTY;
        }else{
//            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

//    private void queryFromServer(final String code,final String type){
//        String address;
//        if(!TextUtils.isEmpty(code)){
//            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
//        }else{
//            address="http://www.weather.com.cn/data/list3/city.xml";
//        }
//        showProgressDialog();
//        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                boolean result=false;
//                if("province".equals(type)){
//                    result= Utility.handleProvincesResponse(coolWeatherDB,response);
//                }else if("city".equals(type)){
//                    result=Utility.handleCitiesResponse(coolWeatherDB,response,selectedProvince.getId());
//                }else if("county".equals(type)){
//                    result=Utility.handleCountiesResponse(coolWeatherDB,response,selectedCity.getId());
//                }
//                if(result){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            closeProgressDialog();
//                            if("province".equals(type)){
//                                queryProvinces();
//                            }else if("city".equals(type)){
//                                queryCities();
//                            }else if("county".equals(type)){
//                                queryCounties();
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLevel==LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel==LEVEL_CITY){
            queryProvinces();
        }else{
            if(isFromWeatherActivity){
                Intent intent=new Intent(this,WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

}
