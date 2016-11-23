package com.easyshu.shuhelper.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by shiyan on 2016/11/20.
 */


public class NetUtils {

    private String tag = getClass().getName();
    private static NetUtils _NET_UTILS;
    private OkHttpClient client;

    private Map<String, List<Cookie>> cookie = new HashMap<>();
    private NetUtils() {
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        String host = url.host();
                        List<Cookie> temp = cookie.get(host) == null || cookie.get(host).size() == 0 ?
                                cookies : cookie.get(host);
                        cookie.put(host, temp);
                        Log.d(tag, host);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookie.get(url.host()) == null ? new ArrayList<Cookie>() : cookie.get(url.host());
                    }
                }).connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES).build();


    }

    public static NetUtils getInstance() {
        if (_NET_UTILS == null){
            synchronized (NetUtils.class){
                if (_NET_UTILS == null){
                    _NET_UTILS = new NetUtils();
                }
            }
        }

        return _NET_UTILS;
    }

    public void newCall(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    public boolean isCookieIsClear() {
        return cookie.size() == 0 ;
    }

    public void clearCookie() {
        if (!isCookieIsClear()){
            cookie.clear();
        }
    }
}
