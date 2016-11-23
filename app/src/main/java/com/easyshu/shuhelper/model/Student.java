package com.easyshu.shuhelper.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;

/**
 * Created by shiyan on 2016/11/20.
 */

public class Student extends BaseObservable implements
        Cloneable,Serializable {

    private String name;
    private String newImageUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1";
    public Student(String name) {
        this.name = name;
    }

    public Student(Parcel in){
        name = in.readString();
    }

    public Student() {

    }
    @Bindable
    public String getNewImageUrl() {
        return newImageUrl;
    }

    public void setNewImageUrl(String newImageUrl) {
        this.newImageUrl = newImageUrl;

    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student)super.clone();
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
