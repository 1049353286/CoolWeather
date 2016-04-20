package com.apricot.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apricot.coolweather.R;
import com.apricot.coolweather.util.HttpCallbackListener;
import com.apricot.coolweather.util.HttpUtil;
import com.apricot.coolweather.util.Utility;

/**
 * Created by Apricot on 2016/4/20.
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button swithCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.weather_layout);
        weatherInfoLayout= (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText= (TextView) findViewById(R.id.city_name);
        publishText= (TextView) findViewById(R.id.publish_text);
        weatherDespText= (TextView) findViewById(R.id.weather_desp);
        temp1Text= (TextView) findViewById(R.id.temp1);
        temp2Text= (TextView) findViewById(R.id.temp2);
        currentDateText= (TextView) findViewById(R.id.current_date);
        swithCity= (Button) findViewById(R.id.swith_city);
        refreshWeather= (Button) findViewById(R.id.refresh_weather);
        swithCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryFromServer(countyCode);
        }else{
            showWeather();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swith_city:
                Intent intent=new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
                String countyCode=prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(countyCode)){
                    queryFromServer(countyCode);
                }
                break;
            default:
                break;
        }
    }

    private void queryFromServer(final String countyCode){
        String address="http://www.weather.com.cn/data/cityinfo/"+countyCode+".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("cityName",""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(prefs.getString("publish_time", ""));
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
