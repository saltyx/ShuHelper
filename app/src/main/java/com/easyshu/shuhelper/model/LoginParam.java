package com.easyshu.shuhelper.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.easyshu.shuhelper.utils.Utils;

/**
 * Created by shiyan on 2016/11/21.
 */

public class LoginParam extends BaseObservable {

    private String studentID;
    private String password;
    private String validateCode;
    private String imageUrl;

    public LoginParam() {
        imageUrl = "http://cj.shu.edu.cn/User/GetValidateCode?"+ Utils.getTimeStamp();
    }

    public LoginParam(String studentID, String password) {
        this.studentID = studentID;
        this.password = password;
    }

    public LoginParam(String password, String studentID, String validateCode) {
        this.password = password;
        this.studentID = studentID;
        this.validateCode = validateCode;
    }
    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);

    }
    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
    @Bindable
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
        notifyPropertyChanged(BR.studentID);
    }

    @Bindable
    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
        notifyPropertyChanged(BR.validateCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LoginParam) {
            if (((LoginParam) obj).getStudentID() != this.studentID
                    || ((LoginParam) obj).getPassword() != this.password) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }
}
