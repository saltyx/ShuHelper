package com.easyshu.shuhelper.data.source.remote;

import android.util.Log;
import android.util.Pair;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.easyshu.shuhelper.data.DataSource;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.CourseCreditPoint;
import com.easyshu.shuhelper.model.CourseDetail;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;
import com.easyshu.shuhelper.utils.FormatUtils;
import com.easyshu.shuhelper.utils.NetUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ProcessingInstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shiyan on 2016/11/23.
 */

public class RemoteDataSource implements DataSource {

    private static final String SCORE_QUERY_URL = "http://cj.shu.edu.cn/StudentPortal/ScoreQuery";
    private static final String COURSE_DETAIL_URL = "http://cj.shu.edu.cn/StudentPortal/CtrlScoreQuery";
    private static final String CJ_URL = "http://cj.shu.edu.cn";

    private static RemoteDataSource _REMOTE_DATA_SOURCE_;

    //根据param缓存成绩
    private static final Map<String, Pair<String, String>> cache = new HashMap<>();

    private RemoteDataSource() {

    }

    public static RemoteDataSource getInstance() {
        if (_REMOTE_DATA_SOURCE_ == null) {
            synchronized (RemoteDataSource.class) {
                if (_REMOTE_DATA_SOURCE_ == null ){
                    _REMOTE_DATA_SOURCE_ = new RemoteDataSource();
                }
            }
        }
        return _REMOTE_DATA_SOURCE_;
    }

    /*从网络获取数据*/
    @Override
    public void getAllCourse(@NonNull final LoadDataCallback callback) {
        Request request = new Request.Builder()
                .url(SCORE_QUERY_URL).build();

        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable(IO_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String data = response.body().string();
                    List<Course> courses = new ArrayList<>();
                    Document doc = Jsoup.parse(data);
                    Elements elements = doc.select("#AcademicTermID");
                    if (elements.size() != 0 ){
                        Element e = elements.get(0);
                        for (int i= e.children().size() -1;i>=0 ; --i) {
                            Element x = e.child(i);
                            courses.add(FormatUtils.formatCourse(x));
                        }
                    }
                    callback.onDataLoaded(courses);
                }
            }
        });
    }

    @Override
    public void getCourseDetailByParam(@NonNull final String param, final LoadDataCallback callback) {
        String postData = String.format("academicTermID=%s", param);

        Request request = new Request.Builder()
                .url(COURSE_DETAIL_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData))
                .build();
        NetUtils.getInstance().newCall(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable(IO_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String data = response.body().string();

                    Document doc = Jsoup.parse(data);

                    List<CourseDetail> details = new ArrayList<>();

                    if (doc.title().contains("登录")){
                        callback.onDataNotAvailable(COOKIE_FAILED);
                        return;
                    }

                    if (doc.body().text().contains("成绩未发布")){
                        callback.onDataNotAvailable(RESULTS_NOT_READY);
                        return;
                    }

                    Elements elements = doc.select("td:not(.right)");
                    Element e = doc.select("td.right").get(0);

                    //pair第一位为credit 第二位为point
                    Pair<String,String> temp = FormatUtils.formatTotalScore(e);
                    if (temp != null){
                        cache.put(param, temp);
                    } else {
                        callback.onDataNotAvailable(PARSE_PAGE_ERROR);
                        return;
                    }

                    for(int i=0; i<elements.size(); i+= 6){
                        details.add(new CourseDetail(elements.get(i).text(),elements.get(i+1).text(),
                                elements.get(i+2).text(),elements.get(i+3).text(),elements.get(i+4).text(),
                                elements.get(i+5).text()));
                    }
                    callback.onDataLoaded(details);

                }else {

                    callback.onDataNotAvailable(RESPONSE_IS_NOT_SUCCESS);
                }
            }
        });
    }
    /*loginParam无法从网络获取*/
    @Override
    public void getLoginParam(@NonNull GetDataCallback callback) {

    }

    @Override
    public void getStudent(LoginParam param, @NonNull final GetDataCallback callback) {

        if (param != null) {

            String postData = String.format("url=&txtUserNo=%s&txtPassword=%s&txtValidateCode=%s",
                    param.getStudentID(),param.getPassword(),param.getValidateCode());
            okhttp3.Request request = new Request.Builder()
                    .url(CJ_URL)
                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData))
                    .build();
            NetUtils.getInstance().newCall(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onDataNotAvailable(IO_ERROR);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        String result = response.body().string();

                        Document doc = Jsoup.parse(result);

                        if (!doc.title().contentEquals("登录")){

                            Elements e = doc.select("#leftmenu_Accordion > div:nth-child(2) > div:nth-child(2)");
                            if (e!= null && e.size() > 0){

                                callback.onDataLoaded(new Student(e.get(0).text()));
                            }
                        }else{

                            Elements elements = doc.select("#divLoginAlert");
                            if (elements != null && elements.size() > 0){
                                callback.onDataNotAvailable(elements.get(0).text());
                            }
                        }
                    }else {
                        callback.onDataNotAvailable(RESPONSE_IS_NOT_SUCCESS);
                    }
                }
            });

        } else {
            callback.onDataNotAvailable(PARAM_IS_NULL);
        }

    }

    /*检查cache中是否存在
    * 如果存在则从cache中直接读取
    * 否则调用getCourseDetail去网络拿到后更新cache，再返回cache*/
    @Override
    public void getCourseCreditAndPoint(@NonNull final String param, @NonNull final GetDataCallback callback) {
        if (param != null) {
            Pair<String, String> data = cache.get(param);

            if (data != null ){
                callback.onDataLoaded(new CourseCreditPoint(data.first,param,data.second));

            } else {
                getCourseDetailByParam(param, new LoadDataCallback() {
                    @Override
                    public void onDataLoaded(List data) {
                        Pair<String, String> data1 = cache.get(param);

                        if (data1 != null ) {
                            callback.onDataLoaded(new CourseCreditPoint(data1.first, param, data1.second));
                        } else {
                            callback.onDataNotAvailable(null);
                        }
                    }

                    @Override
                    public void onDataNotAvailable(@Nullable String errorMsg) {
                        callback.onDataNotAvailable(errorMsg);
                    }
                });
            }

        } else {
            callback.onDataNotAvailable(PARAM_IS_NULL);
        }
    }

    /*删除与存储功能移动端均不需要实现*/
    @Override
    public void saveStudent(@NonNull Student student) {

    }

    @Override
    public void saveAllCourses(@NonNull List<Course> courses) {

    }

    @Override
    public void saveCourseDetail(@NonNull String param, @NonNull List<CourseDetail> detail) {

    }

    @Override
    public void saveLoginParam(@NonNull LoginParam param) {

    }

    @Override
    public void saveCourseCreditAndPoint(@NonNull CourseCreditPoint param) {

    }

    @Override
    public void deleteCourseCreditAndPoint() {

    }

    @Override
    public void deleteStudent() {

    }

    @Override
    public void deleteLoginParam() {

    }

    @Override
    public void deleteCourseDetail() {

    }

    @Override
    public void deleteAllCourses() {

    }

}
