package com.easyshu.shuhelper.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by shiyan on 2016/11/21.
 */

public class CourseDetail extends BaseObservable {

    private String id;
    private String courseNo;

    private String title;
    private String credit;
    private String score;
    private String point;

    public CourseDetail(String title, String credit, String score, String point) {
        this.title = title;
        this.credit = credit;
        this.score = score;
        this.point = point;
    }

    public CourseDetail(String id, String courseNo, String title,
                        String credit, String score, String point ) {
        this.courseNo = courseNo;
        this.credit = credit;
        this.id = id;
        this.point = point;
        this.score = score;
        this.title = title;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
        notifyPropertyChanged(BR.credit);
    }

    @Bindable
    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
        notifyPropertyChanged(BR.point);
    }

    @Bindable
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}
