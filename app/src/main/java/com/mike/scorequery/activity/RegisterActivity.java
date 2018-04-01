package com.mike.scorequery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mike.scorequery.R;
import com.mike.scorequery.bean.Admini;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Student;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by mike on 2017/3/30.
 */

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.register_edt_studentNo)
    private EditText mEdt_StudentNo;
    @ViewInject(R.id.register_edt_password)
    private EditText mEdt_Password;
    @ViewInject(R.id.register_tv_register)
    private TextView mTv_Register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getIntent().getIntExtra("identity", 0)) {
            case 0://普通
                break;
            case 1://管理员
                break;
            case 2://暂时没用
                break;

        }
        mTv_Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tv_register:
                if (TextUtils.isEmpty(mEdt_StudentNo.getText().toString())) {
                    ToastUtil.showToast("请输入帐号");
                    return;
                }
                if (TextUtils.isEmpty(mEdt_Password.getText().toString())) {
                    ToastUtil.showToast("请输入你的密码");
                    return;
                }
                showLoadingDialog("注册中");
                BmobQuery<Score> query = new BmobQuery<>();
                switch (getIntent().getIntExtra("identity", 0)) {
                    case 0://普通
                        final Student student = new Student();
                        student.identity = "0";
                        student.setUsername(mEdt_StudentNo.getText().toString());
                        query.addWhereEqualTo("studentId", mEdt_StudentNo.getText().toString());
                        query.findObjects(new FindListener<Score>() {
                            @Override
                            public void done(List<Score> list, BmobException e) {
                                if (e == null) {
                                    if (list != null && list.size() == 0) {
                                        ToastUtil.showToast("注册失败");
                                        closeLoadingDialog();
                                    } else {
                                        //student.studentId=mEdt_StudentNo.getText().toString();
                                        student.profession = list.get(0).profession;
                                        student.grade = list.get(0).grade;
                                        student.team = list.get(0).team;
                                        student.sex = list.get(0).sex;
                                        student.name = list.get(0).studentName;
                                        student.setPassword(mEdt_Password.getText().toString());
                                        student.signUp(new SaveListener<Student>() {
                                            @Override
                                            public void done(Student student, BmobException e) {
                                                if (e == null) {
                                                    ToastUtil.showToast("注册成功");
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    ToastUtil.showToast("注册失败:" + e.getMessage());
                                                }
                                                closeLoadingDialog();
                                            }
                                        });
                                    }
                                } else {
                                    Log.e("error", "done: " + e.getMessage() + " " + e.getErrorCode());
                                    closeLoadingDialog();
                                }
                            }
                        });
                        break;
                    case 1://管理员
                        final Teacher teacher = new Teacher();
                        teacher.identity = "1";
                        teacher.setUsername(mEdt_StudentNo.getText().toString());
                        query.addWhereEqualTo("teacherId", mEdt_StudentNo.getText().toString()).findObjects(new FindListener<Score>() {
                            @Override
                            public void done(List<Score> list, BmobException e) {
                                if (e == null) {
                                    if (list != null && list.size() == 0) {
                                        ToastUtil.showToast("注册失败");
                                        closeLoadingDialog();
                                    } else {
                                        //teacher.teacherId=mEdt_StudentNo.getText().toString();
                                        teacher.setPassword(mEdt_Password.getText().toString());
                                        teacher.name = list.get(0).teacherName;
                                        teacher.signUp(new SaveListener<Teacher>() {
                                            @Override
                                            public void done(Teacher teacher, BmobException e) {
                                                if (e == null) {
                                                    ToastUtil.showToast("注册成功");
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    ToastUtil.showToast("注册失败:" + e.getMessage());
                                                }
                                                closeLoadingDialog();
                                            }
                                        });
                                    }
                                } else {
                                    ToastUtil.showToast(e.getMessage());
                                    closeLoadingDialog();
                                }
                            }
                        });
                        break;
                    case 2://无用
                        Admini admini = new Admini();
                        admini.identity = "2";
                        admini.setUsername(mEdt_StudentNo.getText().toString());
                        //teacher.teacherId=mEdt_StudentNo.getText().toString();
                        admini.setPassword(mEdt_Password.getText().toString());
                        admini.signUp(new SaveListener<Admini>() {
                            @Override
                            public void done(Admini admini, BmobException e) {
                                if (e == null) {
                                    ToastUtil.showToast("注册成功");
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    ToastUtil.showToast("注册失败:" + e.getMessage());
                                }
                                closeLoadingDialog();
                            }
                        });
                        break;

                }

                break;
        }
    }

}
