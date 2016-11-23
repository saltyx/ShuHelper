package com.easyshu.shuhelper.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.adapter.CourseItemAdapter;
import com.easyshu.shuhelper.databinding.ActivityMainBinding;
import com.easyshu.shuhelper.handler.PreLoginHandler;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.Student;
import com.easyshu.shuhelper.utils.FormatUtils;
import com.easyshu.shuhelper.utils.NetUtils;
import com.easyshu.shuhelper.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final private static String COURSE_TAG = "COURSE_TAG";
    final private static String STUDENT_TAG = "STUDENT_TAG";

    private String tag = getClass().getName();
    private Student student;
    private ArrayList<Course> courses = new ArrayList<>();

    @BindView(R.id.course) protected RecyclerView mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState != null){
            student = (Student) savedInstanceState.getSerializable(STUDENT_TAG);
            courses = (ArrayList<Course>) savedInstanceState.getSerializable(COURSE_TAG);
        }else {
            student = new Student();
        }

        binding.setVariable(BR.student,student);
        binding.setVariable(BR.login,new PreLoginHandler(this,student));

        NetUtils.getInstance();
        ButterKnife.bind(this);
        mCourse.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COURSE_TAG,courses);
        outState.putSerializable(STUDENT_TAG, student);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getSerializableExtra(PreLoginHandler.RETURN_DATA) != null){
            student.setName(((Student)intent.getSerializableExtra(
                    PreLoginHandler.RETURN_DATA)).getName());
        }
        refreshCourse();
    }

    private void refreshCourse(){

        Request request = new Request.Builder()
                .url("http://cj.shu.edu.cn/StudentPortal/ScoreQuery").build();

        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String data = response.body().string();

                    Document doc = Jsoup.parse(data);
                    Elements elements = doc.select("#AcademicTermID");
                    if (elements.size() != 0 ){
                        Element e = elements.get(0);
                        for (int i= e.children().size() -1;i>=0 ; --i) {
                            Element x = e.child(i);
                            Log.d(tag,x.text());
                            courses.add(FormatUtils.formatCourse(x));
                        }
                    }
                    mHandler.sendEmptyMessage(1);
                }
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1: //onFails
                    Snackbar.make(mCourse,"IOException",Snackbar.LENGTH_SHORT).show();
                    break;
                case 0:
                    Snackbar.make(mCourse,"Response Failed",Snackbar.LENGTH_SHORT).show();
                    break;
                case 1:
                    mCourse.setAdapter(new CourseItemAdapter(getApplication(),courses));
                    break;
            }
        }
    };
}
