package com.mike.scorequery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mike.scorequery.R;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Mike on 2017/4/14.
 */
public class ScoreResultActivity extends BaseTitleBarActivity {
    private TextView mTv_Total;
    private TextView mTv_Best;
    private TextView mTv_NoPass;
    private TextView mTv_Liang;
    private TextView mTv_Zhong;
    private TextView mTv_Jige;
    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_scoreresult,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Title.setText("成绩概况");
        mTv_Total= (TextView) findViewById(R.id.scoreresult_tv_total);
        mTv_Best= (TextView) findViewById(R.id.scoreresult_tv_youxiu);
        mTv_NoPass= (TextView) findViewById(R.id.scoreresult_tv_nopass);
        mTv_Liang= (TextView) findViewById(R.id.scoreresult_tv_liang);
        mTv_Zhong= (TextView) findViewById(R.id.scoreresult_tv_zhong);
        mTv_Jige= (TextView) findViewById(R.id.scoreresult_tv_jige);
        showLoadingDialog("概况查询中...");
        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),-1,"");
    }


    /**
     * 查询当前学期所有课程
     */
    private void queyTotalMyScore(final String termNo, String courseNum, final int type, String type1) {
        final Teacher teacher= BmobUser.getCurrentUser(Teacher.class);
        final BmobQuery<Score> query=new BmobQuery<>();
        query.setLimit(50);
        logTools.d(courseNum);
        query.addWhereEqualTo("teacherId", teacher.getUsername());
        query .addWhereEqualTo("termNo",termNo);
        query.addWhereEqualTo("courseNum", courseNum);
        switch (type){
            case -1:
                break;
            case 6:
                query.addWhereEqualTo("profession",type1);
                break;
            case 7:
                query.addWhereEqualTo("profession", StringUtils.split(type1,"-")[0]);
                query.addWhereEqualTo("team",StringUtils.split(type1,"-")[1]);
                break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                switchScore(type,query);
                break;
        }
        query.findObjects(new FindListener<Score>() {
            @Override
            public void done(List<Score> list, BmobException e) {
                if(e==null){
                    query.count(Score.class, new CountListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if(e==null){
                                switch(type){
                                    case -1:
                                        mTv_Total.setText("总共人数:"+integer+"人");
                                        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),0,"");
                                        break;
                                    case 0:
                                        mTv_Best.setText("优秀人数:"+integer+"人");
                                        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),1,"");
                                        break;
                                    case 1:
                                        mTv_Liang.setText("良好人数:"+integer+"人");
                                        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),2,"");
                                        break;
                                    case 2:
                                        mTv_Zhong.setText("中等人数:"+integer+"人");
                                        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),3,"");
                                        break;
                                    case 3:
                                        mTv_Jige.setText("及格人数:"+integer+"人");
                                        queyTotalMyScore(getIntent().getStringExtra("termNo"),getIntent().getStringExtra("courseNum"),4,"");
                                        break;
                                    case 4:
                                        mTv_NoPass.setText("不及格人数:"+integer+"人");
                                        closeLoadingDialog();
                                        break;
                                }

                            }else{
                                ToastUtil.showToast(e.getMessage());
                                logTools.d(e.getMessage());
                                closeLoadingDialog();
                            }
                        }
                    });
                }else{
                    ToastUtil.showToast(e.getMessage());
                    logTools.d(e.getMessage());
                    closeLoadingDialog();
                }
            }
        });

    }


    public void switchScore(int option,BmobQuery<Score> query) {
        switch (option){
            case 0:
                query.addWhereGreaterThanOrEqualTo("totalScore",90);
                break;
            case 1:
                query.addWhereGreaterThanOrEqualTo("totalScore",80);
                query.addWhereLessThan("totalScore",90);
                break;
            case 2:
                query.addWhereGreaterThanOrEqualTo("totalScore",70);
                query.addWhereLessThan("totalScore",80);
                break;
            case 3:
                query.addWhereGreaterThanOrEqualTo("totalScore",60);
                query.addWhereLessThan("totalScore",70);
                break;
            case 4:
                query.addWhereLessThan("totalScore",60);
                break;
        }
    }
}
