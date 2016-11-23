package com.easyshu.shuhelper.ui;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.widget.LinearLayout;

import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.databinding.ActivityLoginBinding;
import com.easyshu.shuhelper.handler.LoginEventHandler;
import com.easyshu.shuhelper.handler.LoginParamWatcher;
import com.easyshu.shuhelper.handler.PreLoginHandler;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements
    SwipeRefreshLayout.OnRefreshListener{

    private LoginParam param;
    private ProgressDialog dialog;
    private Student student;

    @BindView(R.id.main_content) protected SwipeRefreshLayout mRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        ButterKnife.bind(this);

        student = (Student) getIntent().getSerializableExtra(PreLoginHandler.RETURN_DATA);

        mRoot.setOnRefreshListener(this);
        mRoot.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorIcons),getResources().getColor(R.color.colorDivider));

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        param = new LoginParam();
        binding.setParam(param);
        binding.setEvent(new LoginParamWatcher(param));
        binding.setLogin(new LoginEventHandler(this,mRoot,dialog,param,student));
    }

    @Override
    public void onRefresh(){

    }
}
