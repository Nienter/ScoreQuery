package com.mike.scorequery.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Course;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Mike on 2017/4/14.
 */
public class TeacherScoreActivity extends BaseTitleBarActivity {
    private TextView mTv_Term;
    private TextView mTv_Total;
    private TextView mTv_Page;
    private TextView mTv_Name;
    private ListView mListView;
    private List<Score> mDataList;
    private TextView mTv_Select;
    private TextView mTv_Result;
    private MyScoreListAapter scoreListAapter;
    private boolean isOpen;
    private String termNo;
    private String courseNum;

    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_teacher_score,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Term= (TextView) findViewById(R.id.teacherscore_tv_term);
        mTv_Name= (TextView) findViewById(R.id.teacherscore_tv_name);
        mTv_Select= (TextView) findViewById(R.id.teacherscore_tv_select);
        mTv_Result= (TextView) findViewById(R.id.teacherscore_tv_result);
        mTv_Total= (TextView) findViewById(R.id.teacherscore_tv_total);
        mTv_Page= (TextView) findViewById(R.id.teacherscore_tv_page);
        mTv_Select.setOnClickListener(this);
        mTv_Result.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.teacherscore_lv);

        mDataList=new ArrayList<>();
        scoreListAapter=new MyScoreListAapter(mDataList);
        mListView.setAdapter(scoreListAapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isOpen=!isOpen;
                ViewHolder holder= (ViewHolder) view.getTag();
                holder.ll_Content.setVisibility(isOpen?View.VISIBLE:View.GONE);
                holder.iv_Arrow.setImageResource(isOpen?R.mipmap.up_arrow:R.mipmap.down_arrow);
            }
        });
        Teacher teacher= BmobUser.getCurrentUser(Teacher.class);
        mTv_Term.setText(teacher.currentTerm);
        mTv_Name.setText(teacher.name+"(教师号:"+teacher.getUsername()+")");
        mTv_Title.setText(teacher.currentCourse);
        mTv_Right.setText("其他课");
        String courseNum=StringUtils.split(teacher.currentCourse,"(")[1];
        courseNum=StringUtils.substring(courseNum,0,courseNum.length()-1);
        queyTotalMyScore(teacher.currentTerm,courseNum,-1,"");
        this.courseNum=courseNum;
        this.termNo=teacher.currentTerm;
    }

    /**
     * 查询当前学期所有课程
     */
    private void queyTotalMyScore(final String termNo,String courseNum,int type,String type1) {
        final Teacher teacher= BmobUser.getCurrentUser(Teacher.class);
        final BmobQuery<Score> query=new BmobQuery<>();
        query.setLimit(50);
        logTools.d(courseNum);
        query.addWhereEqualTo("teacherId", teacher.getUsername());
        query.addWhereEqualTo("termNo",termNo);
        query.addWhereEqualTo("courseNum", courseNum);
        switch (type){
            case -1:
                break;
            case 6:
                query.addWhereEqualTo("profession",type1);
                break;
            case 7:
                query.addWhereEqualTo("profession",StringUtils.split(type1,"-")[0]);
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
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("未查询到学生成绩");
                        mDataList.clear();
                        mDataList.addAll(list);
                        mListView.setAdapter(scoreListAapter);
                        mTv_Page.setText("1/1页");
                        mTv_Total.setText("总共0条");
                    }else{
                        mDataList.clear();
                        mDataList.addAll(list);
                        mListView.setAdapter(scoreListAapter);
                    }
                    query.count(Score.class, new CountListener() {
                                @Override
                                public void done(Integer integer, BmobException e) {
                                    if(e==null){
                                        mTv_Total.setText("总共:"+integer+"条");
                                        mTv_Page.setText("1/"+((integer/50)+1)+"页");
                                    }else{
                                        ToastUtil.showToast(e.getMessage());
                                    }
                                }
                            });
                }else{
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.titlebar_tv_right:
                initTermPickerView();
                break;
            case R.id.teacherscore_tv_result:
                Intent intent=new Intent(this,ScoreResultActivity.class);
                intent.putExtra("termNo",termNo);
                intent.putExtra("courseNum",courseNum);
                startActivity(intent);
                break;
            case R.id.teacherscore_tv_select:
                initScoreResultPickerView();
                break;
        }
    }


    public class MyScoreListAapter extends BaseAdapter {
        private List<Score> mDataList;
        public MyScoreListAapter(List<Score> list){
            this.mDataList=list;
        }


        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // logTools.d(map.get(position)+":"+position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(TeacherScoreActivity.this, R.layout.item_teacherscore, null);
                holder = new ViewHolder();
                holder.tv_Name = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_name);
                holder.tv_Score = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_score);
                holder.tv_Profession = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_profession);
                holder.tv_PeaceScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_peace);
                holder.tv_ExpScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_exp);
                holder.tv_FinalScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_final);
                holder.tv_MidScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_mid);
                holder.ll_Content = (LinearLayout) convertView.findViewById(R.id.item_teacherscore_ll_content);
                holder.iv_Arrow = (ImageView) convertView.findViewById(R.id.item_teacherscore_head_iv_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_Content.setVisibility(View.GONE);
            holder.tv_Name.setText(mDataList.get(position).studentName+"(学号:"+mDataList.get(position).studentId+")");
            if(mDataList.get(position).courseType.equals("考试课")){
                holder.tv_Score.setText("  成绩:"+mDataList.get(position).totalScore+"分");
            }else{
                holder.tv_Score.setText("  成绩:"+(mDataList.get(position).totalScore==60?"合格":"不合格"));
            }
            holder.tv_Score.setTextColor(mDataList.get(position).totalScore<60?Color.RED:getResources().getColor(R.color.primary_blue));
            holder.tv_Profession.setText(mDataList.get(position).profession+"-"+mDataList.get(position).team);
            holder.tv_PeaceScore.setText(mDataList.get(position).peaceScore+"分("+mDataList.get(position).arr.get(1)+"%)");
            holder.tv_ExpScore.setText(mDataList.get(position).expScore+"分("+mDataList.get(position).arr.get(0)+"%)");
            holder.tv_MidScore.setText(mDataList.get(position).midScore+"分("+mDataList.get(position).arr.get(2)+"%)");
            holder.tv_FinalScore.setText(mDataList.get(position).finalScore+"分("+mDataList.get(position).arr.get(3)+"%)");

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tv_Name;
        public TextView tv_Profession;
        public TextView tv_Score;
        public TextView tv_PeaceScore;
        public TextView tv_ExpScore;
        public TextView tv_MidScore;
        public TextView tv_FinalScore;
        public LinearLayout ll_Content;
        private ImageView iv_Arrow;
    }

    private void initScoreResultPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().scoreResultlist);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                if(options1==5){//全部
                    mListView.setAdapter(scoreListAapter);
                }else if(options1==6){//按专业
                    queryTotalTeachProfession(6);
                }else if(options1==7){//按班级
                    queryTotalTeachProfession(7);
                }else{
                    queyTotalMyScore(termNo,courseNum,options1,"");
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

    private void initTermPickerView() {
        //条件选择器
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().yearList,AppContext.getInstance().termList,null,false);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.show();
        //选择后回调
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(final int options1, int option2, int options3) {
                String termNo=AppContext.getInstance().yearList.get(options1)+" "+AppContext.getInstance().termList.get(options1).get(option2);
                mTv_Term.setText(termNo);
                TeacherScoreActivity.this.termNo=termNo;
                queryTotalTeachCourses(termNo);

            }
        });
    }


    /**
     * 查询所有教的课程
     */
    private void queryTotalTeachCourses(final String termNo) {
        BmobQuery<Course> query=new BmobQuery<>();
        Teacher teacher=BmobUser.getCurrentUser(Teacher.class);
        query.addWhereEqualTo("teacherId",teacher.getUsername()).addWhereEqualTo("termNo",termNo).findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("当前学期无您教的课程");
                        mTv_Title.setText("无课程");
                        mDataList.clear();
                        scoreListAapter.notifyDataSetChanged();
                        mTv_Page.setText("1/1页");
                        mTv_Total.setText("总共0条");
                    }else{
                        ArrayList<String> courseList=new ArrayList<String>();
                        for (int i = 0; i < list.size(); i++) {
                            if(!courseList.contains(list.get(i).courseName+"("+list.get(i).courseNum+")")){
                                courseList.add(list.get(i).courseName+"("+list.get(i).courseNum+")");
                            }
                        }
                        initCoursePickerView(courseList,termNo);
                    }
                }else{
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });
    }
    private void initCoursePickerView(final ArrayList<String> courseList, final String termNo) {
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
                 mTv_Title.setText(courseList.get(options1));
                String courseNum=StringUtils.split(courseList.get(options1),"(")[1];
                courseNum=StringUtils.substring(courseNum,0,courseNum.length()-1);
                TeacherScoreActivity.this.courseNum=courseNum;
                queyTotalMyScore(termNo,courseNum,-1,"");
            }
        });
    }


    /**
     * 查询所有教的课程
     */
    private void queryTotalTeachProfession(final int type) {
        BmobQuery<Course> query=new BmobQuery<>();
        Teacher teacher=BmobUser.getCurrentUser(Teacher.class);
        String courseNum=StringUtils.split(teacher.currentCourse,"(")[1];
        courseNum=StringUtils.substring(courseNum,0,courseNum.length()-1);
        query.addWhereEqualTo("teacherId",teacher.getUsername()).addWhereEqualTo("termNo",teacher.currentTerm)
                .addWhereEqualTo("courseNum",courseNum).findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("当前学期无您教的课程,您设置的当前学期是否正确?");
                    }else{
                        ArrayList<String> courseList=new ArrayList<String>();
                        switch (type){
                            case 6:
                                for (int i = 0; i < list.size(); i++) {
                                    if(!courseList.contains(list.get(i).profession)){
                                        courseList.add(list.get(i).profession);
                                    }
                                }
                                break;
                            case 7:
                                for (int i = 0; i < list.size(); i++) {
                                    if(!courseList.contains(list.get(i).profession+"-"+list.get(i).team)){
                                        courseList.add(list.get(i).profession+"-"+list.get(i).team);
                                    }
                                }
                                break;
                        }

                        initPickerView(courseList, type);
                    }
                }else{
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });
    }


    private void initPickerView(final ArrayList<String> courseList,final int type) {
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
               queyTotalMyScore(termNo,courseNum,type,courseList.get(options1));
            }
        });
    }

}
