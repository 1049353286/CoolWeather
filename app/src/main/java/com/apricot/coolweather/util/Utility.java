package com.apricot.coolweather.util;

import android.text.TextUtils;

import com.apricot.coolweather.db.CoolWeatherDB;
import com.apricot.coolweather.model.City;
import com.apricot.coolweather.model.County;
import com.apricot.coolweather.model.Province;

/**
 * Created by Apricot on 2016/4/18.
 */
public class Utility {
//    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
//        if(!TextUtils.isEmpty(response)){
//            String[] allProvinces=response.split(",");
//            if(allProvinces!=null&&allProvinces.length>0){
//                for(String p:allProvinces){
//                    String[] array=p.split("\\|");
//                    Province province=new Province();
//                    province.setProvinceCode(array[0]);
//                    province.setProvinceName(array[1]);
//                    coolWeatherDB.saveProvince(province);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
//        if(!TextUtils.isEmpty(response)){
//            String[] allCities=response.split(",");
//            if(allCities!=null&&allCities.length>0){
//                for(String c:allCities){
//                    String[] array=c.split("\\|");
//                    City city=new City();
//                    city.setCityCode(array[0]);
//                    city.setCityName(array[1]);
//                    city.setProvinceId(provinceId);
//                    coolWeatherDB.saveCity(city);
//                    return true;
//                }
//            }
//
//        }
//        return false;
//    }
//
//    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
//        if(!TextUtils.isEmpty(response)){
//            String[] allCounties=response.split(",");
//            if(allCounties!=null&&allCounties.length>0){
//                for(String c:allCounties){
//                    String[] array=c.split("\\|");
//                    County county=new County();
//                    county.setCountyCode(array[0]);
//                    county.setCountyName(array[1]);
//                    county.setCityId(cityId);
//                    coolWeatherDB.saveCounty(county);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}