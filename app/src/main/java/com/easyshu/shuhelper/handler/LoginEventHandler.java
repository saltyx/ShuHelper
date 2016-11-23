package com.easyshu.shuhelper.handler;

import android.content.Context;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;
import com.easyshu.shuhelper.ui.MainActivity;
import com.easyshu.shuhelper.utils.FormatUtils;
import com.easyshu.shuhelper.utils.NetUtils;
import com.easyshu.shuhelper.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shiyan on 2016/11/20.
 */

public class LoginEventHandler {

    private String tag = getClass().getName();

    private Context mContext;
    private ProgressDialog dialog;

    final private View root;
    final private LoginParam param;

    private Student student;

    public LoginEventHandler(Context mContext,View root ,ProgressDialog dialog,
                             LoginParam param,Student student) {
        this.dialog = dialog;
        this.mContext = mContext;
        this.root = root;
        this.param = param;
        this.student = student;
    }

    public void onLoginClick(){

        String postData = String.format("url=&txtUserNo=%s&txtPassword=%s&txtValidateCode=%s",
                param.getStudentID(),param.getPassword(),param.getValidateCode());
        Log.d(tag, postData);
        okhttp3.Request request = new Request.Builder()
                .url("http://cj.shu.edu.cn")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData))
                .build();
        dialog.show();
        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(tag, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String result = response.body().string();

                    Document doc = Jsoup.parse(result);

                    Log.d(tag, doc.title());
                    if (!doc.title().contentEquals("登录")){
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        Elements e = doc.select("#leftmenu_Accordion > div:nth-child(2) > div:nth-child(2)");
                        if (e!= null && e.size() > 0){
                            Log.d(tag, e.get(0).text());
                            student.setName(FormatUtils.formatName(e.get(0).text()));
                            intent.putExtra(PreLoginHandler.RETURN_DATA, student);
                        }
                        mContext.startActivity(intent);
                    }else{
                        dialog.dismiss();
                        Elements elements = doc.select("#divLoginAlert");
                        if (elements != null && elements.size() > 0){
                            TSnackbar.make(root,elements.get(0).text(),TSnackbar.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Log.d(tag,"response failed");
                }
            }
        });
    }

    public void refreshImage(View view, String url){
        Utils.refreshImage((ImageView) view, url);
    }
}
