package com.apricot.coolweather.util;

/**
 * Created by Apricot on 2016/4/18.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
