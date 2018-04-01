package com.mike.scorequery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Admini;
import com.mike.scorequery.utils.SPConstant;
import com.mike.scorequery.utils.SPUtils;
import com.mike.scorequery.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;



@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.login_edt_name)
    private EditText mEdt_Name;
    @ViewInject(R.id.login_edt_password)
    private EditText mEdt_Password;
    @ViewInject(R.id.login_tv_login)
    private TextView mTv_Login;
    @ViewInject(R.id.login_tv_register)
    private TextView mTv_Register;
    @ViewInject(R.id.login_tv_forget)
    private TextView mTv_Forget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一：默认初始化
        Bmob.initialize(this, "8b43216a77a231638b489936826b14d6");
        mEdt_Name.setText(SPUtils.getString(this,SPConstant.USERNAME));
        mTv_Forget.setOnClickListener(this);
        mTv_Login.setOnClickListener(this);
        mTv_Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_tv_forget:
                ToastUtil.showToast("请联系管理员找回密码！");
                break;
            case R.id.login_tv_login:
                if(TextUtils.isEmpty(mEdt_Name.getText().toString())){
                    ToastUtil.showToast("请输入你登录号");
                    return;
                }
                if(TextUtils.isEmpty(mEdt_Password.getText().toString())){
                    ToastUtil.showToast("请输入你的密码");
                    return;
                }
                showLoadingDialog("登录中...");
                final Admini admini=new Admini();
                admini.setUsername(mEdt_Name.getText().toString());
                admini.setPassword(mEdt_Password.getText().toString());
                admini.login(new SaveListener<Admini>() {
                    @Override
                    public void done(Admini admini, BmobException e) {
                        if(e==null){
                        if(!admini.getUsername().equals(SPUtils.getString(LoginActivity.this,SPConstant.USERNAME))){
                            SPUtils.clearData(LoginActivity.this);
                            SPUtils.setString(LoginActivity.this, SPConstant.USERNAME,admini.getUsername());
                            SPUtils.setString(LoginActivity.this,SPConstant.USERID,admini.getObjectId());
                            SPUtils.setString(LoginActivity.this,SPConstant.USEROBJECT,new Gson().toJson(admini));
                        }
                            ToastUtil.showToast("登录成功");
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else{
                           ToastUtil.showToast("登录失败:"+e.getMessage());
                        }
                        closeLoadingDialog();
                    }
                });
                break;
            case R.id.login_tv_register:
              initIdentityPickerView();
                break;
        }
    }
    @SuppressWarnings("unchecked")
    private void initIdentityPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().identityList);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class).putExtra("identity",options1));
            }
        });
    }
}
