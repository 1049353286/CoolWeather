package com.apricot.coolweather.util;

import com.apricot.coolweather.db.CoolWeatherDB;
import com.apricot.coolweather.model.City;
import com.apricot.coolweather.model.County;
import com.apricot.coolweather.model.Province;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Apricot on 2016/4/18.
 */
public class ParseUtil {
    public static void parseXMLWithDOM(final CoolWeatherDB coolWeatherDB,final InputStream in,final ParseCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
//                    DocumentBuilder builder=factory.newDocumentBuilder();
//                    Document document=builder.parse(in,"UTF-8");
//                    Element root=document.getDocumentElement();
//                    NodeList items=root.getElementsByTagName("Province");
//                    for(int i=0;i<items.getLength();i++){
//                        Province province=new Province();
//                        Element item= (Element) items.item(i);
//                        province.setProvinceName(item.getAttribute("name"));
//                        coolWeatherDB.saveProvince(province);
//                        NodeList cityItems=item.getChildNodes();
//                        for(int j=0;j<cityItems.getLength();j++){
//                            City city=new City();
//                            Element cityItem= (Element) cityItems.item(j);
//                            city.setCityName(cityItem.getAttribute("name"));
//                            city.setProvinceName(province.getProvinceName());
//                            coolWeatherDB.saveCity(city);
//                            NodeList countItems=cityItem.getChildNodes();
//                            for(int k=0;k<countItems.getLength();k++){
//                                County county=new County();
//                                Element countyItem= (Element) countItems.item(k);
//                                county.setCityName(countyItem.getAttribute("name"));
//                                county.setCountyCode(countyItem.getAttribute("code"));
//                                county.setCityName(city.getCityName());
//                                coolWeatherDB.saveCounty(county);
//                            }
//                        }
//                    }
//                    if(listener!=null){
//                        listener.onFinish();
//                    }
//                } catch (Exception e) {
//                    listener.onError(e);
//                }
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(in, "UTF-8");

                    org.w3c.dom.Element root = document.getDocumentElement();
                    NodeList items = root.getElementsByTagName("Province");
                    //遍历所有节点
                    for (int i = 0; i < items.getLength(); i++) {


                        if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Province province = new Province();
                            Element item = (Element) items.item(i);
                            String provinceName = item.getAttribute("name");
                            province.setProvinceName(provinceName);
                            coolWeatherDB.saveProvince(province);
                            NodeList cityItems = item.getChildNodes();
                            for (int y = 0; y < cityItems.getLength(); y++) {
                                if (cityItems.item(y).getNodeType() == Node.ELEMENT_NODE) {
                                    City city = new City();
                                    Element cityItem = (Element) cityItems.item(y);
                                    String cityName = cityItem.getAttribute("name");
                                    city.setCityName(cityItem.getAttribute("name"));
                                    city.setProvinceName(provinceName);
                                    coolWeatherDB.saveCity(city);
                                    NodeList countyItems = cityItem.getChildNodes();
                                    for (int z = 0; z < countyItems.getLength(); z++) {
                                        if (countyItems.item(z).getNodeType() == Node.ELEMENT_NODE) {
                                            County county = new County();
                                            Element countyItem = (Element) countyItems.item(z);
                                            county.setCountyName(countyItem.getAttribute("name"));
                                            county.setCountyCode(countyItem.getAttribute("code"));
                                            county.setCityName(cityName);
                                            coolWeatherDB.saveCounty(county);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    listener.onFinish();
                }catch (Exception e){
                    listener.onError(e);
                }
            }
        }).start();
    }
}
