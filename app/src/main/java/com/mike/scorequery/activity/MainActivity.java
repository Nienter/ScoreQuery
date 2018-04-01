package com.mike.scorequery.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Admini;
import com.mike.scorequery.bean.Course;
import com.mike.scorequery.bean.Student;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int RC_CAMERA_PERM = 123;
    private Toolbar mToolbar;
    private PopupWindow mPopupWindow;
    private LinearLayout mLl_EnterScore;
    private LinearLayout mLl_ScanEnterScore;
    private LinearLayout mLl_QueryScore;
    private LinearLayout mLl_ScanQueryScore;
    private TextView mTv_TermNo;
    private TextView mTv_Course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // 主标题,默认为app_label的名字
        mToolbar.setTitle("药品查询");
        mToolbar.setTitleTextColor(Color.WHITE);
        // 副标题
//        mToolbar.setSubtitle("Sub title");
//        mToolbar.setSubtitleTextColor(Color.parseColor("#80ff0000"));
        //侧边栏的按钮
        mToolbar.setNavigationIcon(R.mipmap.left_arrow);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastUtil.showToast("haah");
                finish();
            }
        });
        //设置toolBar上的MenuItem点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.action_edit:
//                        mToast.setText("click edit");
//                        break;
//                    case R.id.action_share:
//                        mToast.setText("click share");
//                        break;
                    case R.id.action_overflow:
                        //弹出自定义overflow
                        popUpMyOverflow();
                        return true;
                }
                return true;
            }
        });

        mLl_EnterScore = (LinearLayout) findViewById(R.id.main_ll_enterscore);
        mLl_ScanEnterScore = (LinearLayout) findViewById(R.id.main_ll_scan_enterscore);
        mLl_QueryScore = (LinearLayout) findViewById(R.id.main_ll_queryScore);
        mLl_ScanQueryScore = (LinearLayout) findViewById(R.id.main_ll_scan_queryScore);
        mTv_TermNo = (TextView) findViewById(R.id.main_tv_termNo);
        mTv_Course = (TextView) findViewById(R.id.main_tv_course);
        mLl_EnterScore.setOnClickListener(this);
        mLl_ScanEnterScore.setOnClickListener(this);
        mLl_QueryScore.setOnClickListener(this);
        mLl_ScanQueryScore.setOnClickListener(this);
        //ToolBar里面还可以包含子控件
//        mToolbar.findViewById(R.id.btn_diy).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mToast.setText("点击自定义按钮");
//                mToast.show();
//            }
//        });
//        mToolbar.findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mToast.setText("点击自定义标题");
//                mToast.show();
//            }
//        });

//           logTools.d(BmobUser.getCurrentUser(Admini.class));
        Admini admini = BmobUser.getCurrentUser(Admini.class);
        switch (admini.identity) {
            case "0":
                Student student = BmobUser.getCurrentUser(Student.class);
                mTv_TermNo.setText(TextUtils.isEmpty(student.currentTerm) ? "当前学期:未设置" : "当前学期:" + student.currentTerm);
                break;
            case "1":
                Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
                mTv_TermNo.setText(TextUtils.isEmpty(teacher.currentTerm) ? "当前学期:未设置" : "当前学期:" + teacher.currentTerm);
                mTv_Course.setVisibility(View.GONE);
                mTv_Course.setText(TextUtils.isEmpty(teacher.currentCourse) ? "当前课程:未设置" : "当前课程:" + teacher.currentCourse);
                mLl_EnterScore.setVisibility(View.VISIBLE);
                mLl_ScanEnterScore.setVisibility(View.VISIBLE);
                break;
            case "2":
                mLl_EnterScore.setVisibility(View.VISIBLE);
                mLl_ScanEnterScore.setVisibility(View.VISIBLE);
                break;
        }

    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top + mToolbar.getHeight();
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.action_overflow_popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 0, yOffset);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setVisibility(View.GONE);
            popView.findViewById(R.id.ll_item3).setOnClickListener(this);
            popView.findViewById(R.id.ll_item3).setVisibility(View.GONE);
            Admini admini = BmobUser.getCurrentUser(Admini.class);
            switch (admini.identity) {
                case "0":
                    break;
                case "1":
                    ((TextView) popView.findViewById(R.id.tv_item2)).setText("设置当前课程");
                    ((TextView) popView.findViewById(R.id.tv_item3)).setText("我的学生成绩");
                    break;
                case "2":
                    break;
            }
        } else {
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 0, yOffset);
        }

    }

    @Override
    public void onClick(View v) {
        Admini admini = BmobUser.getCurrentUser(Admini.class);
        switch (v.getId()) {
            case R.id.ll_item1:
                initTermPickerView();
                break;
            case R.id.ll_item2:
                switch (admini.identity) {
                    case "0":
                        Student student = BmobUser.getCurrentUser(Student.class);

                        startActivity(new Intent(this, QrCodeActivity.class));
                        break;
                    case "1":
                        Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
//                        if(TextUtils.isEmpty(teacher.currentTerm)){
//                            ToastUtil.showToast("请先设置当前学期");
//                            return;
//                        }
                        queryTotalTeachCourses(teacher);
                        break;
                    case "2":
                        break;
                }
                break;
            case R.id.ll_item3:
                switch (admini.identity) {
                    case "0":
                        Student student = BmobUser.getCurrentUser(Student.class);
                        if (TextUtils.isEmpty(student.currentTerm)) {
                            ToastUtil.showToast("请先设置当前时间");
                            return;
                        }
                        startActivity(new Intent(this, MyScoreActivity.class));
                        break;
                    case "1":
                        Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
//                        if(TextUtils.isEmpty(teacher.currentCourse)){
//                            ToastUtil.showToast("请先设置当前课程");
//                            return;
//                        }
                        startActivity(new Intent(this, TeacherScoreActivity.class));
                        break;
                    case "2":
                        break;
                }
                break;
//            case R.id.ll_item2:
//                break;
//            case R.id.ll_item3:
//                break;
//            case R.id.ll_item4:
//                break;
            case R.id.main_ll_enterscore:
                Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
                if (TextUtils.isEmpty(teacher.currentTerm)) {
                    ToastUtil.showToast("请先设置当前时间");
                    return;
                }
//                if(TextUtils.isEmpty(teacher.currentCourse)){
//                    ToastUtil.showToast("请先设置当前课程");
//                    return;
//                }
                startActivity(new Intent(this, EnterDrugActivity.class));
                break;
            case R.id.main_ll_scan_enterscore:
                Teacher teacher1 = BmobUser.getCurrentUser(Teacher.class);
//                if (TextUtils.isEmpty(teacher1.currentTerm)) {
//                    ToastUtil.showToast("请先设置当前时间");
//                    return;
//                }
//                if(TextUtils.isEmpty(teacher1.currentCourse)){
//                    ToastUtil.showToast("请先设置当前课程");
//                    return;
//                }
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    startActivity(new Intent(this, AddScanActivity.class).putExtra("scanType", "add"));
                } else {
                    cameraTask1();
                }
                break;
            case R.id.main_ll_queryScore:
                switch (admini.identity) {
                    case "0":
                        startActivity(new Intent(this, SearchActiviy.class));
                        break;
                    case "1":
                        startActivity(new Intent(this, SearchActiviy.class));
                        break;
                    case "2":
                        break;
                }

                //  startActivity(new Intent(this,EnterQueryActivity.class));
                break;
            case R.id.main_ll_scan_queryScore:
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    startActivity(new Intent(this, QueryScanActivity.class).putExtra("scanType", "query"));
                } else {
                    cameraTask();
                }
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 查询所有教的课程
     */
    private void queryTotalTeachCourses(final Teacher teacher) {
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId", teacher.getUsername()).addWhereEqualTo("termNo", teacher.currentTerm).findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() == 0) {
//                        ToastUtil.showToast("当前学期无您教的课程,您设置的当前学期是否正确?");
                        teacher.currentCourse = "";
                        teacher.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mTv_Course.setText("当前课程:无");
                                } else {
                                }
                            }
                        });
                    } else {
                        ArrayList<String> courseList = new ArrayList<String>();
                        for (int i = 0; i < list.size(); i++) {
                            if (!courseList.contains(list.get(i).courseName + "(" + list.get(i).courseNum + ")")) {
                                courseList.add(list.get(i).courseName + "(" + list.get(i).courseNum + ")");
                            }
                        }
                        initCoursePickerView(courseList);
                    }
                } else {
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });
    }

    private void initTermPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().yearList, null, false);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {

                showLoadingDialog("设置中...");
                Admini admini = BmobUser.getCurrentUser(Admini.class);
                switch (admini.identity) {
                    case "0":
                        final Student student = BmobUser.getCurrentUser(Student.class);
                        student.currentTerm = AppContext.getInstance().yearList.get(options1) + " " + AppContext.getInstance().termList.get(options1).get(option2);
                        student.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtil.showToast("设置成功");
                                    mTv_TermNo.setText("当前学期:" + student.currentTerm);
                                } else {
                                    ToastUtil.showToast("设置失败");
                                }
                                closeLoadingDialog();
                            }
                        });
                        break;
                    case "1":
                        final Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
                        teacher.currentTerm = AppContext.getInstance().yearList.get(options1) + " " + AppContext.getInstance().termList.get(options1).get(option2);
                        teacher.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtil.showToast("设置成功");
                                    mTv_TermNo.setText("当前学期:" + teacher.currentTerm);
                                    queryTotalTeachCourses(teacher);
                                } else {
                                    ToastUtil.showToast("设置失败");
                                }
                                closeLoadingDialog();
                            }
                        });

                        break;
                    case "2":
                        break;
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
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

                showLoadingDialog("设置中...");
                final Teacher teacher = BmobUser.getCurrentUser(Teacher.class);
                teacher.currentCourse = courseList.get(options1);
                teacher.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtil.showToast("设置成功");
                            mTv_Course.setText("当前课程:" + teacher.currentCourse);
                        } else {
                            ToastUtil.showToast("设置当前课程失败");
                        }
                        closeLoadingDialog();
                    }
                });

            }
        });
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startActivity(new Intent(this, QueryScanActivity.class).putExtra("scanType", "query"));
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "请求打开相机",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask1() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startActivity(new Intent(this, AddScanActivity.class).putExtra("scanType", "add"));
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "请求打开相机",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
