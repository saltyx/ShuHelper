package com.easyshu.shuhelper.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.databinding.library.baseAdapters.BR;
import com.androidadvance.topsnackbar.TSnackbar;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.adapter.CourseItemAdapter;
import com.easyshu.shuhelper.data.DataSource;
import com.easyshu.shuhelper.data.source.DataRepo;
import com.easyshu.shuhelper.databinding.ActivityMainBinding;
import com.easyshu.shuhelper.handler.PreLoginHandler;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.Student;
import com.easyshu.shuhelper.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final private static String COURSE_TAG = "COURSE_TAG";
    final private static String STUDENT_TAG = "STUDENT_TAG";

    private Student student;
    private ArrayList<Course> courses = new ArrayList<>();

    @NonNull
    private CourseItemAdapter courseItemAdapter;

    @NonNull
    private DataRepo mDataRepo;

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

        ButterKnife.bind(this);
        mCourse.setLayoutManager(new LinearLayoutManager(this));
        mDataRepo = Injection.provideDataRepo(getApplicationContext());
        courseItemAdapter = new CourseItemAdapter(getApplicationContext(),courses);
        mCourse.setAdapter(courseItemAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataRepo.saveStudent(student);

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();

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
            Student temp = ((Student)intent.getSerializableExtra(PreLoginHandler.RETURN_DATA));
            mDataRepo.saveStudent( temp );
        }

        refreshData();

    }

    private void refreshData(){

        mDataRepo.getStudent(null, new DataSource.GetDataCallback<Student>() {
            @Override
            public void onDataLoaded(Student data) {
                student.setName(data.getName());
            }

            @Override
            public void onDataNotAvailable(@Nullable String errorMsg) {
                //不作处理
                if (errorMsg != null) {

                }
            }
        });

        mDataRepo.getAllCourse(new DataSource.LoadDataCallback<Course>() {
            @Override
            public void onDataLoaded(List<Course> data) {
                courses.clear();
                courses.addAll(data);

                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onDataNotAvailable(@Nullable String errorMsg) {
                Message message = mHandler.obtainMessage();
                message.what = -1;
                message.obj = errorMsg == null ? "未知错误" : errorMsg;
                mHandler.sendMessage(message);
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    TSnackbar.make(mCourse,(String)msg.obj,TSnackbar.LENGTH_SHORT).show();
                    break;
                case 1:
                    courseItemAdapter.changeData(courses);
                    break;
            }
        }
    };
}
