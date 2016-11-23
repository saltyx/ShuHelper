package com.easyshu.shuhelper.handler;

import android.text.Editable;
import android.text.TextWatcher;

import com.easyshu.shuhelper.model.LoginParam;

/**
 * Created by shiyan on 2016/11/21.
 */

public class LoginParamWatcher {

    private LoginParam param;

    public LoginParamWatcher(LoginParam param) {
        this.param = param;
    }

    public TextWatcher studentCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            param.setStudentID(s.toString());
        }
    };

    public TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            param.setPassword(s.toString());
        }
    };

    public TextWatcher validateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            param.setValidateCode(s.toString());
        }
    };

}
