package com.apricot.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apricot.coolweather.model.City;
import com.apricot.coolweather.model.County;
import com.apricot.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apricot on 2016/4/17.
 */
public class CoolWeatherDB {
    public static final String DB_NAME="cool_weather";
    public static final int VERSION=1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB==null){
            coolWeatherDB=new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province){
        if(province!=null){
            db.execSQL("insert into Province(province_name) values('"+province.getProvinceName()+"')");
        }
    }

    public List<Province> loadProvince(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToNext()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city){
        if(city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("province_name",city.getProvinceName());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(String provinceName){
        List<City> list=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_name=?",new String[]{String.valueOf(provinceName)},null,null,null);
        if(cursor.moveToNext()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceName(provinceName);
                list.add(city);

            }while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCounty(County county){
        if(county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_name",county.getCityName());
            db.insert("County",null,values);
        }
    }

    public List<County> loadCounties(String cityName){
        List<County> list=new ArrayList<County>();
        Cursor cursor=db.query("County",null,"city_name=?",new String[]{String.valueOf(cityName)},null,null,null);
        if(cursor.moveToNext()){
            do{
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityName(cityName);
                list.add(county);
            }while (cursor.moveToNext());
        }
        return list;
    }

}
