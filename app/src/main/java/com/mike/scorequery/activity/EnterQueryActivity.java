package com.mike.scorequery.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mike.scorequery.R;
import com.mike.scorequery.bean.Admini;
import com.mike.scorequery.bean.Drug;
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
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by mike on 2017/4/9.
 */
public class EnterQueryActivity extends BaseTitleBarActivity {
    private TextView mTv_Name;
    private TextView mTv_Total;
    private TextView mTv_Page;
    private ListView mListView;
    private List<Drug> mDataList;
    private MyScoreListAapter scoreListAapter;
    private boolean isOpen;

    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_enterquery,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Title.setText("药品查询");
        //mTv_Right.setText("确定");
        mTv_Name= (TextView) findViewById(R.id.enterquery_tv_name);
        mTv_Total= (TextView) findViewById(R.id.enterquery_tv_total);
        mTv_Page= (TextView) findViewById(R.id.enterquery_tv_page);
        mListView= (ListView) findViewById(R.id.enterquery_lv);
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

        if(!TextUtils.isEmpty(getIntent().getStringExtra("courseName"))){
            String drug_name = StringUtils.split(getIntent().getStringExtra("courseName"),"(")[0];
            String courseNum= StringUtils.split(getIntent().getStringExtra("courseName"),"(")[1];
            courseNum=StringUtils.substring(courseNum,0,courseNum.length()-1);
            Student student=BmobUser.getCurrentUser(Student.class);
//            mTv_Name.setText(student.name+"(编号:"+student.getUsername()+")");
            mTv_Name.setText(drug_name+"(编号:"+courseNum+")");
            queryTotalCourses(student,courseNum);
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("studentName"))){
            String drug_name = StringUtils.split(getIntent().getStringExtra("studentName"),"(")[0];
            String studentId= StringUtils.split(getIntent().getStringExtra("studentName"),"(")[1];
            studentId=StringUtils.substring(studentId,0,studentId.length()-1);
            Teacher teacher=BmobUser.getCurrentUser(Teacher.class);
            mTv_Name.setText(drug_name+"(编号:"+studentId+")");
            queyTotalMyStudent(studentId);
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("code"))){
            String[] strings= StringUtils.split(getIntent().getStringExtra("code"),"|");
            String drug_name = strings[2];
            String courseNum= strings[1];
            Student student=BmobUser.getCurrentUser(Student.class);
//            mTv_Name.setText(student.name+"(编号:"+student.getUsername()+")");
            mTv_Name.setText(drug_name+"(编号:"+courseNum+")");
            queryTotalCourses(student,courseNum);
        }
    }



    /**
     * 查询所有学习的课程
     */
    private void queryTotalCourses(Student student,final String courseNum) {
       final BmobQuery<Drug> query=new BmobQuery<>();
        query.addWhereEqualTo("drug_id",courseNum).findObjects(new FindListener<Drug>() {
            @Override
            public void done(List<Drug> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("未查询到");
                        mDataList.clear();
                        scoreListAapter.notifyDataSetChanged();
                        mTv_Page.setText("1/1页");
                        mTv_Total.setText("总共0条");
                    }else{
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
                    }
                    query.count(Drug.class, new CountListener() {
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

    /**
     * 查询当前学期所有课程
     */
    private void queyTotalMyStudent(String studentId) {
        Teacher teacher= BmobUser.getCurrentUser(Teacher.class);
        final BmobQuery<Drug> query=new BmobQuery<>();
        query.setLimit(50);
        query.addWhereEqualTo("drug_id",studentId).findObjects(new FindListener<Drug>() {
            @Override
            public void done(List<Drug> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("未查询到药品信息");
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
                        mTv_Page.setText("1/1页");
                        mTv_Total.setText("总共0条");
                    }else{
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
                    }
                    query.count(Drug.class, new CountListener() {
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


    public class MyScoreListAapter extends BaseAdapter {
        private List<Drug> mDataList;
        public MyScoreListAapter(List<Drug> list){
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
                convertView = View.inflate(EnterQueryActivity.this, R.layout.item_teacherscore, null);
                holder = new ViewHolder();
                holder.tv_Name = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_name);
                holder.tv_Score = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_score);
                holder.tv_Profession = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_profession);
                holder.tv_PeaceScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_peace);
                holder.tv_ExpScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_exp);
                holder.tv_FinalScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_final);
                holder.tv_MidScore = (TextView) convertView.findViewById(R.id.item_teacherscore_content_tv_mid);
                holder.tv_TermNo = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_termNo);
                holder.tv_Personname = (TextView) convertView.findViewById(R.id.item_teacherscore_tv_pesonName);
                holder.ll_Content = (LinearLayout) convertView.findViewById(R.id.item_teacherscore_ll_content);
                holder.iv_Arrow = (ImageView) convertView.findViewById(R.id.item_teacherscore_head_iv_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_Content.setVisibility(View.GONE);

            Admini admini=BmobUser.getCurrentUser(Admini.class);
//            switch (admini.identity){
//                case "0":
//                    holder.tv_Personname.setText("老师:"+mDataList.get(position).teacherName+"("+mDataList.get(position).teacherId+")");
//                    break;
//                case "1":
//                    holder.tv_Personname.setText("学生:"+mDataList.get(position).studentName+"("+mDataList.get(position).studentId+")");
//                    break;
//                case "2":
//                    break;
//            }
            holder.tv_Name.setText(mDataList.get(position).drug_name);
            holder.tv_Score.setText(mDataList.get(position).drug_id);
            holder.tv_PeaceScore.setText(mDataList.get(position).drug_time);
            holder.tv_ExpScore.setText(mDataList.get(position).drug_num);
            holder.tv_MidScore.setText(mDataList.get(position).drug_des);
            holder.tv_FinalScore.setText(mDataList.get(position).drug_point);

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
        private TextView tv_TermNo;
        private TextView tv_Personname;
        private ImageView iv_Arrow;
    }

}
