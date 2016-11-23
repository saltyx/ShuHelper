package com.easyshu.shuhelper.handler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.ui.CourseDetailActivity;
import com.easyshu.shuhelper.utils.NetUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by shiyan on 2016/11/21.
 */

public class CourseItemHandler {

    public static String COURSE_PARAM = "COURSE_PARAM";

    private String tag = getClass().getName();
    private Context mContext;

    public CourseItemHandler(Context context) {
        this.mContext = context;
    }

    /*cardview点击事件*/
    public void onItemClick(View view, final Course param){

        CardView me = (CardView) view;
        me.setCardBackgroundColor(Color.parseColor("#CFD8DC"));

        Intent intent = new Intent(mContext, CourseDetailActivity.class);
        intent.putExtra(COURSE_PARAM, param);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

}
