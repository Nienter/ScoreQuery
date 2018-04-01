package com.mike.scorequery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Course;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Student;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by mike on 2017/4/9.
 */
public class
EnterScoreActivity extends BaseTitleBarActivity {
    private EditText mEdt_SudentId;
    private TextView mTv_SeclectCourse;
    private EditText mEdt_ExpSocre, mEdt_PeaceScore, mEdt_MidScore, mEdt_FinalScore;
    private TextView mTv_Term;
    private TextView mTv_Result;
    private LinearLayout mLl_Score;
    private LinearLayout mLl_Score1;
    private Course course;

    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_enterscore, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Title.setText("药品信息录入");
        mEdt_SudentId = (EditText) findViewById(R.id.enterscore_edt_studentNo);
        mTv_SeclectCourse = (TextView) findViewById(R.id.enterscore_tv_course);
        mTv_Term = (TextView) findViewById(R.id.enterscore_tv_term);
        mEdt_ExpSocre = (EditText) findViewById(R.id.enterscore_edt_expscore);
        mEdt_PeaceScore = (EditText) findViewById(R.id.enterscore_edt_peacescore);
        mEdt_MidScore = (EditText) findViewById(R.id.enterscore_edt_midscore);
        mEdt_FinalScore = (EditText) findViewById(R.id.enterscore_edt_finalscore);
        mTv_Result = (TextView) findViewById(R.id.enterscore_tv_result);
        mLl_Score = (LinearLayout) findViewById(R.id.enterscore_ll_score);
        mLl_Score1 = (LinearLayout) findViewById(R.id.enterscore_ll_score1);
        mTv_SeclectCourse.setOnClickListener(this);
        mTv_Term.setOnClickListener(this);
        mTv_Result.setOnClickListener(this);
        Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
        mTv_Term.setText(teacher.currentTerm);
        mTv_SeclectCourse.setText(teacher.currentCourse);

        String courseNum1 = StringUtils.split(teacher.currentCourse, "(")[1];
        courseNum1 = StringUtils.substring(courseNum1, 0, courseNum1.length() - 1);
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId", teacher.getUsername())
                .addWhereEqualTo("courseNum", courseNum1).findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() == 0) {
                        ToastUtil.showToast("无该课程记录");
                        closeLoadingDialog();
                    } else {
                        course = list.get(0);
                        if (course.courseType.equals("考查课")) {
                            mLl_Score1.setVisibility(View.GONE);
                            mLl_Score.setVisibility(View.GONE);
                            mTv_Result.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ToastUtil.showToast(e.getMessage());
                    closeLoadingDialog();
                }
            }
        });

        mTv_Right.setText("确定");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("scoreQrcode"))) {//二维码信息
            final String[] strings = StringUtils.split(getIntent().getStringExtra("scoreQrcode"), "|");
            if (strings.length == 2) {
                mEdt_SudentId.setText(strings[1]);
            } else if (strings.length == 3) {
                String courseNum = StringUtils.split(teacher.currentCourse, "(")[1];
                courseNum = StringUtils.substring(courseNum, 0, courseNum.length() - 1);
                if (!courseNum.equals(strings[2])) {
                    ToastUtil.showToast("药品设置有误");
                    finish();
                } else {
                    mEdt_SudentId.setText(strings[1]);
                }
            }
        } else {

        }
    }

    private void initPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().courseList);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                mTv_SeclectCourse.setText(AppContext.getInstance().courseList.get(options1));
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        final Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
        switch (view.getId()) {

            case R.id.titlebar_tv_right:
                final Score score = new Score();
                BmobQuery<Student> bmobQuery = new BmobQuery<Student>();
                switch (course.courseType){
                    case "考试课":
                        if (TextUtils.isEmpty(mTv_Term.getText().toString())) {
                            ToastUtil.showToast("请选择学期");
                            return;
                        }
                        if (TextUtils.isEmpty(mTv_SeclectCourse.getText().toString())) {
                            ToastUtil.showToast("请选择课程");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_SudentId.getText().toString())) {
                            ToastUtil.showToast("请输入学号");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_ExpSocre.getText().toString())) {
                            ToastUtil.showToast("请输入实验课成绩");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_PeaceScore.getText().toString())) {
                            ToastUtil.showToast("请输入平时成绩");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_MidScore.getText().toString())) {
                            ToastUtil.showToast("请输入期中成绩");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_FinalScore.getText().toString())) {
                            ToastUtil.showToast("请输入期末成绩");
                            return;
                        }
                        showLoadingDialog("成绩录入中...");
                        score.expScore = Integer.valueOf(mEdt_ExpSocre.getText().toString());
                        score.peaceScore = Integer.valueOf(mEdt_PeaceScore.getText().toString());
                        score.midScore = Integer.valueOf(mEdt_MidScore.getText().toString());
                        score.finalScore = Integer.valueOf(mEdt_FinalScore.getText().toString());
                        score.termNo = teacher.currentTerm;
                        score.teacherId = teacher.getUsername();
                        score.teacherName = teacher.name;
                        score.courseNum = course.courseNum;
                        score.courseName = course.courseName;
                        score.courseType = course.courseType;
//                        score.arr = course.arr;
//                        score.courseType = course.courseType;
//                        score.totalScore = (float) (score.expScore * score.arr.get(0)
//                                + score.peaceScore * score.arr.get(1)
//                                + score.midScore * score.arr.get(2) + score.finalScore * score.arr.get(3)) / 100;
                        bmobQuery.addWhereEqualTo("username", mEdt_SudentId.getText().toString()).findObjects(new FindListener<Student>() {
                            @Override
                            public void done(List<Student> list, BmobException e) {
                                if (e == null) {
                                    if (list != null && list.size() == 0) {
                                        ToastUtil.showToast("该学号未注册");
                                        closeLoadingDialog();
                                    } else {
                                        score.studentId = list.get(0).getUsername();
                                        score.studentName = list.get(0).name;
                                        score.sex = list.get(0).sex;
                                        score.profession = list.get(0).profession;
                                        score.grade = list.get(0).grade;
                                        score.team = list.get(0).team;
                                        score.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    ToastUtil.showToast("录入成功");
                                                    closeLoadingDialog();
                                                } else {
                                                    ToastUtil.showToast(e.getMessage());
                                                    closeLoadingDialog();
                                                }
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
                    case "考查课":
                        if (TextUtils.isEmpty(mTv_Term.getText().toString())) {
                            ToastUtil.showToast("请选择学期");
                            return;
                        }
                        if (TextUtils.isEmpty(mTv_SeclectCourse.getText().toString())) {
                            ToastUtil.showToast("请选择课程");
                            return;
                        }
                        if (TextUtils.isEmpty(mEdt_SudentId.getText().toString())) {
                            ToastUtil.showToast("请输入学号");
                            return;
                        }
                        if (TextUtils.isEmpty(mTv_Result.getText().toString())) {
                            ToastUtil.showToast("请选择考试结果");
                            return;
                        }
                        showLoadingDialog("成绩录入中...");
                        score.expScore = 0;
                        score.peaceScore = 0;
                        score.midScore = 0;
                        score.finalScore = 0;
                        score.termNo = teacher.currentTerm;
                        score.teacherId = teacher.getUsername();
                        score.teacherName = teacher.name;
                        score.courseNum = course.courseNum;
                        score.courseName = course.courseName;
                        score.courseType = course.courseType;
                        score.arr = course.arr;
                        score.courseType = course.courseType;
                        score.totalScore = mTv_Result.getText().toString().equals("合格")?60f:0f;
                        bmobQuery.addWhereEqualTo("username", mEdt_SudentId.getText().toString()).findObjects(new FindListener<Student>() {
                            @Override
                            public void done(List<Student> list, BmobException e) {
                                if (e == null) {
                                    if (list != null && list.size() == 0) {
                                        ToastUtil.showToast("该学号未注册");
                                        closeLoadingDialog();
                                    } else {
                                        score.studentId = list.get(0).getUsername();
                                        score.studentName = list.get(0).name;
                                        score.sex = list.get(0).sex;
                                        score.profession = list.get(0).profession;
                                        score.grade = list.get(0).grade;
                                        score.team = list.get(0).team;
                                        score.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    ToastUtil.showToast("录入成功");
                                                    closeLoadingDialog();
                                                } else {
                                                    ToastUtil.showToast(e.getMessage());
                                                    closeLoadingDialog();
                                                }
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
                }

                break;
            case R.id.enterscore_tv_course:
//                 initPickerView();
                break;
            case R.id.enterscore_tv_term:
//                 initTermPickerView();
                break;
            case R.id.enterscore_tv_result:
                initResultPickerView();
                break;
        }
    }

    private void initTermPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().yearList, AppContext.getInstance().termList, null, false);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                mTv_Term.setText(AppContext.getInstance().yearList.get(options1) + " "
                        + AppContext.getInstance().termList.get(options1).get(option2));
            }
        });
    }

    private void initResultPickerView() {
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("合格");
        arrayList.add("不合格");
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(arrayList);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                mTv_Result.setText(arrayList.get(options1));
            }
        });
    }
}
