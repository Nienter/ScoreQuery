package com.mike.scorequery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Course;
import com.mike.scorequery.bean.Student;
import com.mike.scorequery.utils.BitmapUtils;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Mike on 2017/3/28.
 */
public class QrCodeActivity extends BaseTitleBarActivity {

    private ImageView mIv_Qrcode;
    private TextView mTv_SetCourse;
    private TextView mTv_CourseName;
    private TextView mTv_CancelCourse;
    private LinearLayout mLl_SetCourse;
    private ArrayList<String> courseList;
    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_qrcode,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Title.setText("我的二维码");
        mIv_Qrcode= (ImageView) findViewById(R.id.qrcode_iv_qrcode);
        mTv_SetCourse= (TextView) findViewById(R.id.qrcode_tv_setcourse);
        mTv_CourseName= (TextView) findViewById(R.id.qrcode_tv_courseName);
        mTv_CancelCourse= (TextView) findViewById(R.id.qrcode_tv_cancel);
        mLl_SetCourse= (LinearLayout) findViewById(R.id.qrcode_ll_secourse);
        mTv_SetCourse.setOnClickListener(this);
        mTv_CancelCourse.setOnClickListener(this);
        Student student= BmobUser.getCurrentUser(Student.class);
        String  qrcontent="score|"+student.getUsername();
        queryTotalStudyCourses(student.currentTerm);
        mIv_Qrcode.setImageBitmap(BitmapUtils.createQRCode(qrcontent, R.mipmap.ic_launcher, this));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.qrcode_tv_setcourse:
                if(courseList==null||courseList.size()==0){
                    ToastUtil.showToast("您没有课程可选");
                    return;
                }
                initCoursePickerView(courseList);
                break;
            case R.id.qrcode_tv_cancel:
                mLl_SetCourse.setVisibility(View.GONE);
                Student student= BmobUser.getCurrentUser(Student.class);
                String  qrcontent="score|"+student.getUsername();
                queryTotalStudyCourses(student.currentTerm);
                mIv_Qrcode.setImageBitmap(BitmapUtils.createQRCode(qrcontent, R.mipmap.ic_launcher, this));
                break;
        }
    }

    private void initCoursePickerView(final ArrayList<String> courseList) {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(courseList);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                mTv_CourseName.setText(courseList.get(options1));
                mLl_SetCourse.setVisibility(View.VISIBLE);
                String courseNum= StringUtils.split(courseList.get(options1),"(")[1];
                courseNum=StringUtils.substring(courseNum,0,courseNum.length()-1);
                Student student= BmobUser.getCurrentUser(Student.class);
                String  qrcontent="score|"+student.getUsername()+"|"+courseNum;
                mIv_Qrcode.setImageBitmap(BitmapUtils.createQRCode(qrcontent, R.mipmap.ic_launcher, QrCodeActivity.this));
            }
        });
    }



    /**
     * 查询所有教的课程
     */
    private void queryTotalStudyCourses(final String termNo) {
        BmobQuery<Course> query=new BmobQuery<>();
        Student student= BmobUser.getCurrentUser(Student.class);
        logTools.d(student.grade);
        logTools.d(student.profession);
        logTools.d(student.team);
        logTools.d(termNo);
        query.addWhereEqualTo("grade",student.grade)
                .addWhereEqualTo("profession",student.profession)
                .addWhereEqualTo("team",student.team).addWhereEqualTo("termNo",termNo).findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("当前学期无您学习的课程");
                    }else{
                        ArrayList<String> courseList=new ArrayList<String>();
                        for (int i = 0; i < list.size(); i++) {
                            if(!courseList.contains(list.get(i).courseName+"("+list.get(i).courseNum+")")){
                                courseList.add(list.get(i).courseName+"("+list.get(i).courseNum+")");
                            }
                        }
                        QrCodeActivity.this.courseList=courseList;
                    }
                }else{
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });
    }
}
