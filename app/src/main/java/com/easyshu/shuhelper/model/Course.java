package com.easyshu.shuhelper.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.easyshu.shuhelper.BR;

/**
 * Created by shiyan on 2016/11/20.
 */

public class Course extends BaseObservable implements Parcelable {

    private String name;/*学期*/
    private String year;/*学年*/
    private String param;

    public Course(String name,String year, String param) {
        this.name = name;
        this.param = param;
        this.year = year;
    }

    public Course() {

    }

    public Course(Parcel in) {
        this.name = in.readString();
        this.year = in.readString();
        this.param = in.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>(){
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(year);
        parcel.writeString(param);
    }

    @Bindable
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        notifyPropertyChanged(BR.year);
    }

    @Bindable
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
    @Bindable
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
        notifyPropertyChanged(BR.param);
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
