package com.easyshu.shuhelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shiyan on 2016/11/20.
 */

public class Utils {

    public static String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis());
    }

    public static void refreshImage(final ImageView imageView, String url){
        final Request request = new Request.Builder()
                .url(url)
                .build();

        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap img = BitmapFactory.decodeStream(inputStream);
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(img);
                        }
                    });

                }
            }
        });
    }
}
