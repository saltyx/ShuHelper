package com.easyshu.shuhelper.handler;

import android.content.Context;
import android.content.Intent;

import com.easyshu.shuhelper.model.Student;
import com.easyshu.shuhelper.ui.LoginActivity;

/**
 * Created by shiyan on 2016/11/21.
 */

public class PreLoginHandler {

    public static String RETURN_DATA = "RETURN_DATA";

    private Context mContext;
    private Student student;

    public PreLoginHandler(Context mContext, Student student) {
        this.mContext = mContext;
        this.student = student;
    }

    /*跳转界面*/
    public void onPreLoginClick(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(RETURN_DATA,student);
        mContext.startActivity(intent);
    }
}
