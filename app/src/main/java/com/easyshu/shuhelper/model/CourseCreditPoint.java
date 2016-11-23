package com.easyshu.shuhelper.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by shiyan on 2016/11/23.
 */

public class CourseCreditPoint extends BaseObservable {

    private String param;
    private String credit;
    private String point;

    public CourseCreditPoint(String credit, String param, String point) {
        this.credit = credit;
        this.param = param;
        this.point = point;
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
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
        notifyPropertyChanged(BR.param);
    }

    @Bindable
    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
        notifyPropertyChanged(BR.point);
    }
}
