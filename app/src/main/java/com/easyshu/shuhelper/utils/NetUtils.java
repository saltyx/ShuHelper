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

    private final static String PATH_FOR_VALIDATE = "/User/GetValidateCode";
    private final static String PATH_FOR_LOGIN = "/";
    private final static String HOST_FOR_OTHER_INFO = "cj.shu.edu.cn";

    private final static Map<String ,String> cookieMap = new HashMap<>();

    private String tag = getClass().getName();
    private static NetUtils _NET_UTILS;
    private OkHttpClient client;

    private Map<String, List<Cookie>> cookie = new HashMap<>();

    static {
        cookieMap.put(PATH_FOR_LOGIN,PATH_FOR_VALIDATE);
    }

    private NetUtils() {
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                        //以urlPath为key，判断cookie最新情况
                        //cookie只存储以下站点
                        Log.d(tag, url.url().getPath());
                        Log.d(tag, cookies.toString());
                        cookie.put(url.url().getPath(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        Log.d(tag, "$"+url.url().getPath());
                        Log.d(tag, "$"+url.host());
                        String host = url.host();//host
                        String path = url.url().getPath();//path

                        List<Cookie> cookies = null;
                        if (cookieMap.get(path) != null || host.contains(HOST_FOR_OTHER_INFO)) {
                            cookies = cookie.get( cookieMap.get(PATH_FOR_LOGIN) );
                        }
                        if (cookies != null) Log.d(tag, "$"+cookies.toString());
                        return cookies == null ? new ArrayList<Cookie>() : cookies;
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
