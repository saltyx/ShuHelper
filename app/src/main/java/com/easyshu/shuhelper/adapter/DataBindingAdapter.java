package com.easyshu.shuhelper.adapter;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.easyshu.shuhelper.utils.NetUtils;
import com.easyshu.shuhelper.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shiyan on 2016/11/21.
 */

public class DataBindingAdapter {

    @BindingAdapter({"image"})
    public static void imageLoader(final ImageView imageView, final String url){

        Utils.refreshImage(imageView,url);

    }

    @BindingAdapter({"newImage"})
    public static void newImage(final ImageView view,final String url){
        Request request = new Request.Builder()
                .url(url).build();
        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String data = response.body().string();
                    Pattern pattern = Pattern.compile("<url>(.+?)<\\/url>");
                    Matcher matcher = pattern.matcher(data);
                    if (matcher.find()){
                        String imageUrl = "http://cn.bing.com" + matcher.group(1);
                        Utils.refreshImage(view, imageUrl);
                    }

                }
            }
        });
    }
}
