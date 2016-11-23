package com.easyshu.shuhelper.data.source;

import android.content.Context;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.easyshu.shuhelper.data.DataSource;
import com.easyshu.shuhelper.data.source.local.LocalDataSource;
import com.easyshu.shuhelper.data.source.remote.RemoteDataSource;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.CourseCreditPoint;
import com.easyshu.shuhelper.model.CourseDetail;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;

import java.util.List;

/**
 * Created by shiyan on 2016/11/22.
 * 处理相关逻辑
 */

public class DataRepo implements DataSource {

    private static DataRepo _DATA_REPO_;

    private final DataSource mLocalDataSource;

    private final DataSource mRemoteDataSource;

    private DataRepo(Context context){
        mLocalDataSource = LocalDataSource.getInstance(context);
        mRemoteDataSource = RemoteDataSource.getInstance();
    }

    public static DataRepo getInstance(@NonNull Context context) {
        if (_DATA_REPO_ == null){
            synchronized (DataRepo.class){
                if (_DATA_REPO_ == null){
                    _DATA_REPO_ = new DataRepo(context);
                }
            }
        }

        return _DATA_REPO_;
    }

    /*
    * 先加载本地
    * 如果未找到加载网络
    * 从网上加载成功本地存一份*/
    @Override
    public void getAllCourse(@NonNull final LoadDataCallback callback) {

        mLocalDataSource.getAllCourse(new LoadDataCallback<Course>() {
            @Override
            public void onDataLoaded(List<Course> data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable(@Nullable String error) {
                mRemoteDataSource.getAllCourse(new LoadDataCallback<Course>() {
                    @Override
                    public void onDataLoaded(List<Course> data) {
                        callback.onDataLoaded(data);
                        mLocalDataSource.saveAllCourses(data);
                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String error) {
                        callback.onDataNotAvailable(error);
                    }
                });
            }
        });
    }

    /*
    * 先加载本地
    * 如果未找到加载网络
    * 从网上加载成功本地存一份*/
    @Override
    public void getCourseDetailByParam(@NonNull final String param, @NonNull final LoadDataCallback callback) {
        mLocalDataSource.getCourseDetailByParam(param, new LoadDataCallback<CourseDetail>() {
            @Override
            public void onDataLoaded(List<CourseDetail> data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable(@Nullable String error) {
                mRemoteDataSource.getCourseDetailByParam(param, new LoadDataCallback<CourseDetail>() {
                    @Override
                    public void onDataLoaded(List<CourseDetail> data) {
                        callback.onDataLoaded(data);
                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String error) {
                        callback.onDataNotAvailable(error);
                    }
                });
            }
        });
    }

    /*只能从本地获取登录参数*/
    @Override
    public void getLoginParam(@NonNull final GetDataCallback callback) {
        mLocalDataSource.getLoginParam(new GetDataCallback<LoginParam>() {
            @Override
            public void onDataLoaded(LoginParam data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable(@Nullable String error) {
                callback.onDataNotAvailable(error);
            }
        });
    }

    /*
       * 先加载本地
       * 如果未找到加载网络
       * 从网上加载成功本地存一份
       * Note: 由于网站很慢，此过程可能需要很长时间*/
    @Override
    public void getStudent(@Nullable final LoginParam param, @NonNull final GetDataCallback callback) {
        mLocalDataSource.getStudent(null ,new GetDataCallback<Student>() {
            @Override
            public void onDataLoaded(Student data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable(@Nullable String error) {
                mRemoteDataSource.getStudent(param, new GetDataCallback<Student>() {
                    @Override
                    public void onDataLoaded(Student data) {
                        callback.onDataLoaded(data);
                        mLocalDataSource.saveStudent(data);
                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String errorMsg) {
                        callback.onDataNotAvailable(errorMsg);
                    }
                });

            }
        });
    }

    /*
   * 先加载本地
   * 如果未找到加载网络
   * 从网上加载成功本地存一份*/
    @Override
    public void getCourseCreditAndPoint(@NonNull final String param, @NonNull final GetDataCallback callback) {
        mLocalDataSource.getCourseCreditAndPoint(param, new GetDataCallback<CourseCreditPoint>() {
            @Override
            public void onDataLoaded(CourseCreditPoint data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable(@Nullable String errorMsg) {
                mRemoteDataSource.getCourseCreditAndPoint(param, new GetDataCallback<CourseCreditPoint>() {
                    @Override
                    public void onDataLoaded(CourseCreditPoint data) {
                        callback.onDataLoaded(data);

                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String errorMsg) {
                        callback.onDataNotAvailable(errorMsg);
                    }
                });
            }
        });
    }

    /*save操作只在本地*/
    @Override
    public void saveAllCourses(List<Course> courses) {
        mLocalDataSource.saveAllCourses(courses);
    }

    @Override
    public void saveStudent(@NonNull Student student) {
        mLocalDataSource.saveStudent(student);
    }

    @Override
    public void saveCourseDetail(@NonNull String param, @NonNull List<CourseDetail> detail) {
        mLocalDataSource.saveCourseDetail(param, detail);
    }

    @Override
    public void saveLoginParam(LoginParam param) {
        mLocalDataSource.saveLoginParam(param);
    }

    @Override
    public void saveCourseCreditAndPoint(@NonNull CourseCreditPoint param) {

    }

    /*delete操作只在本地*/
    @Override
    public void deleteLoginParam() {
        mLocalDataSource.deleteLoginParam();
    }

    @Override
    public void deleteStudent() {
        mLocalDataSource.deleteStudent();
    }

    @Override
    public void deleteAllCourses() {
        mLocalDataSource.deleteAllCourses();
    }

    @Override
    public void deleteCourseDetail() {
        mLocalDataSource.deleteCourseDetail();
    }

    @Override
    public void deleteCourseCreditAndPoint() {

    }
}
