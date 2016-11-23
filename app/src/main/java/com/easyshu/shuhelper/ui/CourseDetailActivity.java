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

import com.androidadvance.topsnackbar.TSnackbar;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.adapter.CourseDetailAdapter;
import com.easyshu.shuhelper.databinding.ActivityCourseDetailBinding;
import com.easyshu.shuhelper.handler.CourseItemHandler;
import com.easyshu.shuhelper.model.Course;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetailActivity extends AppCompatActivity {

    private String tag = getClass().getName();

    private Course course;

    private ObservableField<String> credit = new ObservableField<>()
            , point = new ObservableField<>();
    private ArrayList<CourseDetail> courseInfo = new ArrayList<>();

    @BindView(R.id.main_content) protected RecyclerView mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCourseDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_course_detail);
        ButterKnife.bind(this);
        course = getIntent().getParcelableExtra(CourseItemHandler.COURSE_PARAM);
        binding.setCourse(course);
        binding.setCredit(credit);
        binding.setPoint(point);
        refreshData(course.getParam());
        mainContent.setLayoutManager(new LinearLayoutManager(this));

    }

    public void refreshData(final String param){
        String postData = String.format("academicTermID=%s", param);

        Request request = new Request.Builder()
                .url("http://cj.shu.edu.cn/StudentPortal/CtrlScoreQuery")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData))
                .build();
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

                    if (doc.title().contains("登录")){
                        mHandler.sendEmptyMessage(-3);
                        return;
                    }

                    if (doc.body().text().contains("成绩未发布")){
                        mHandler.sendEmptyMessage(-2);
                        return;
                    }

                    Elements elements = doc.select("td:not(.right)");
                    Element e = doc.select("td.right").get(0);
                    Log.d(tag, e.html());
                    Pair<String,String> temp = FormatUtils.formatTotalScore(e);
                    if (temp != null){
                        credit.set(temp.first);
                        point.set(temp.second);
                        Log.d(tag,temp.first + "#" + temp.second);
                    }
                    for(int i=0; i<elements.size(); i+= 6){
                        courseInfo.add(new CourseDetail(elements.get(i).text(),elements.get(i+1).text(),
                                elements.get(i+2).text(),elements.get(i+3).text(),elements.get(i+4).text(),
                                elements.get(i+5).text()));
                    }

                    mHandler.sendEmptyMessage(1);

                }else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    TSnackbar.make(mainContent,"IOException",TSnackbar.LENGTH_SHORT).show();
                    break;
                case 0:
                    TSnackbar.make(mainContent,"Response Failed", TSnackbar.LENGTH_SHORT).show();
                    break;
                case 1:
                    mainContent.setAdapter(new CourseDetailAdapter(courseInfo));
                    break;
                case -2:
                    TSnackbar.make(mainContent,"本学期成绩未发布！",TSnackbar.LENGTH_SHORT).show();
                    break;
                case -3:
                    TSnackbar.make(mainContent,"Cookie已经失效，需要重新登录",TSnackbar.LENGTH_LONG).show();
            }
        }
    };
}
