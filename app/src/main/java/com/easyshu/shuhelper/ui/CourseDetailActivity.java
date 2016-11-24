package com.easyshu.shuhelper.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.android.annotations.Nullable;
import com.androidadvance.topsnackbar.TSnackbar;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.adapter.CourseDetailAdapter;
import com.easyshu.shuhelper.data.DataSource;
import com.easyshu.shuhelper.data.source.DataRepo;
import com.easyshu.shuhelper.databinding.ActivityCourseDetailBinding;
import com.easyshu.shuhelper.handler.CourseItemHandler;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.CourseCreditPoint;
import com.easyshu.shuhelper.model.CourseDetail;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.utils.FormatUtils;
import com.easyshu.shuhelper.utils.NetUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetailActivity extends AppCompatActivity {

    private Course course;

    private DataRepo mDataPepo;

    private CourseDetailAdapter adapter;

    private CourseCreditPoint totalInfo = new CourseCreditPoint();

    private ArrayList<CourseDetail> courseInfo = new ArrayList<>();

    @BindView(R.id.main_content) protected RecyclerView mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCourseDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_course_detail);

        ButterKnife.bind(this);

        course = getIntent().getParcelableExtra(CourseItemHandler.COURSE_PARAM);
        totalInfo.setParam(course.getParam());
        binding.setCourse(course);
        binding.setTotalInfo(totalInfo);

        mainContent.setLayoutManager(new LinearLayoutManager(this));
        mDataPepo = DataRepo.getInstance(getApplicationContext());
        adapter = new CourseDetailAdapter(courseInfo);
        mainContent.setAdapter(adapter);

        refreshData(course.getParam());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataPepo.saveCourseCreditAndPoint(totalInfo);
        mDataPepo.saveCourseDetail(totalInfo.getParam(), courseInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void refreshData(final String param) {
        mDataPepo.getCourseDetailByParam(param, new DataSource.LoadDataCallback<CourseDetail>() {
            @Override
            public void onDataLoaded(List<CourseDetail> data) {
                courseInfo.clear();
                courseInfo.addAll(data);
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onDataNotAvailable(@Nullable String errorMsg) {
                Message msg = Message.obtain();
                msg.obj = errorMsg;
                msg.what = -1;
                mHandler.sendMessage(msg);
            }
        });

        mDataPepo.getCourseCreditAndPoint(param, new DataSource.GetDataCallback<CourseCreditPoint>() {
            @Override
            public void onDataLoaded(CourseCreditPoint data) {
                totalInfo.setPoint(data.getPoint());
                totalInfo.setCredit(data.getCredit());
                totalInfo.setParam(data.getParam());
            }

            @Override
            public void onDataNotAvailable(@Nullable String errorMsg) {
                Message msg = Message.obtain();
                msg.obj = errorMsg == null ? "未知错误" : errorMsg;
                msg.what = -1;
                mHandler.sendMessage(msg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    TSnackbar.make(mainContent,(String)msg.obj,TSnackbar.LENGTH_LONG).show();
                    break;
                case 1:
                    adapter.changeData(courseInfo);
                    break;
                }
        }
    };
}
