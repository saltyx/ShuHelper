package com.easyshu.shuhelper.data;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.CourseCreditPoint;
import com.easyshu.shuhelper.model.CourseDetail;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;

import java.util.List;

/**
 * Created by shiyan on 2016/11/22.
 */

public interface DataSource {

    String IO_ERROR = "IO_Exception";
    String RESPONSE_IS_NOT_SUCCESS = "RESPONSE_IS_NOT_SUCCESS";
    String COOKIE_FAILED = "Cookie信息已经失效，需要重新登录";
    String RESULTS_NOT_READY = "成绩尚未公布";
    String PARAM_IS_NULL = "参数为空";
    String PARSE_PAGE_ERROR = "页面解析出错";

    interface LoadDataCallback<T> {

        void onDataLoaded(List<T> data);

        void onDataNotAvailable(@Nullable String errorMsg);
    }

    interface GetDataCallback<T> {

        void onDataLoaded(T data);

        void onDataNotAvailable(@Nullable String errorMsg);
    }

    void getAllCourse(@NonNull LoadDataCallback callback );

    void getCourseDetailByParam(@NonNull String param, LoadDataCallback callback);

    //loginparam和student信息仅在本地存一份
    void getLoginParam(@NonNull GetDataCallback callback);

    void getStudent(@Nullable LoginParam param, @NonNull GetDataCallback callback);

    void getCourseCreditAndPoint(@NonNull String param,@NonNull GetDataCallback callback );

    void saveStudent(@NonNull Student student);

    void saveAllCourses(@NonNull List<Course> courses);

    //每个param对应一系列课程
    void saveCourseDetail(@NonNull String param, @NonNull List<CourseDetail> detail);

    void saveLoginParam(@NonNull LoginParam param);

    void saveCourseCreditAndPoint(@NonNull CourseCreditPoint param);

    void deleteCourseCreditAndPoint();

    void deleteStudent();

    void deleteAllCourses();

    void deleteLoginParam();

    void deleteCourseDetail();
}
