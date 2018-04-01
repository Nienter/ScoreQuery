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

import com.bigkoo.pickerview.OptionsPickerView;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Student;
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
 * Created by Mike on 2017/4/13.
 */
public class MyScoreActivity extends BaseTitleBarActivity {


    private TextView mTv_Profession;
    private TextView mTv_Name;
    private TextView mTv_Total;
    private TextView mTv_Page;
    private ListView mListView;
    private List<Score> mDataList;
    private TextView mTv_Select;
    //private TextView mTv_Result;
    private MyScoreListAapter scoreListAapter;
    private boolean isOpen;

    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_myscore,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Profession= (TextView) findViewById(R.id.myscore_tv_profession);
        mTv_Name= (TextView) findViewById(R.id.myscore_tv_name);
        mTv_Select= (TextView) findViewById(R.id.myscore_tv_select);
        //mTv_Result= (TextView) findViewById(R.id.myscore_tv_result);
        mTv_Total= (TextView) findViewById(R.id.myscore_tv_total);
        mTv_Page= (TextView) findViewById(R.id.myscore_tv_page);
        mTv_Select.setOnClickListener(this);
       // mTv_Result.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.myscore_lv);

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
        if(!TextUtils.isEmpty(getIntent().getStringExtra("scoreQrcode"))){//二维码信息
            final String[] strings= StringUtils.split(getIntent().getStringExtra("scoreQrcode"),"|");

            final BmobQuery<Student> query=new BmobQuery<>();
            query.addWhereEqualTo("username",strings[1]).findObjects(new FindListener<Student>() {
                @Override
                public void done(List<Student> list, BmobException e) {
                    if(e==null){
                        if(list!=null&&list.size()==0){
                            ToastUtil.showToast("未查询到该学生");
                        }else{
                            Student student=list.get(0);
                            mTv_Profession.setText(student.profession+"-"+student.team);
                            mTv_Name.setText(student.name+"(学号:"+student.getUsername()+")");
                            mTv_Title.setText(student.currentTerm);
                            mTv_Right.setVisibility(View.GONE);
                            if(strings.length==2){
                                queyTotalMyScore(student,student.currentTerm);
                            }else if(strings.length==3){
                                queyOneMyScore(student.currentTerm,strings[2]);
                            }
                        }
                    }else{
                        ToastUtil.showToast(e.getMessage());
                    }
                }
            });
        }else{
            Student student= BmobUser.getCurrentUser(Student.class);
            mTv_Profession.setText(student.profession+"-"+student.team);
            mTv_Name.setText(student.name+"(学号:"+student.getUsername()+")");
            mTv_Title.setText(student.currentTerm);
            mTv_Right.setText("历学期");
            queyTotalMyScore(student,student.currentTerm);
        }
    }

    /**
     * 查询当前学期所有课程
     */
    private void queyTotalMyScore(Student student,String termNo) {
        final BmobQuery<Score> query=new BmobQuery<>();
        query.setLimit(50);
        query.addWhereEqualTo("studentId", student.getUsername()).addWhereEqualTo("termNo",termNo).findObjects(new FindListener<Score>() {
            @Override
            public void done(List<Score> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("未查询到课程");
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
                    }else{
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
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
    /**
     * 查询当前学期指定课程
     */
    private void queyOneMyScore(final String termNo,String courseNum) {
        final Student student= BmobUser.getCurrentUser(Student.class);
        final BmobQuery<Score> query=new BmobQuery<>();
        query.setLimit(50);
        query.addWhereEqualTo("studentId", student.getUsername()).addWhereEqualTo("termNo",termNo)
                .addWhereEqualTo("courseNum",courseNum).findObjects(new FindListener<Score>() {
            @Override
            public void done(List<Score> list, BmobException e) {
                if(e==null){
                    if(list!=null&&list.size()==0){
                        ToastUtil.showToast("未查询到课程");
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
                    }else{
                        mDataList.clear();
                        mDataList.addAll(list);
                        scoreListAapter.notifyDataSetChanged();
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
//            case R.id.myscore_tv_result:
//                break;
            case R.id.myscore_tv_select:
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
                convertView = View.inflate(MyScoreActivity.this, R.layout.item_myscore, null);
                holder = new ViewHolder();
                holder.tv_Name = (TextView) convertView.findViewById(R.id.item_myscore_tv_name);
                holder.tv_Score = (TextView) convertView.findViewById(R.id.item_myscore_tv_score);
                holder.tv_PeaceScore = (TextView) convertView.findViewById(R.id.item_myscore_content_tv_peace);
                holder.tv_ExpScore = (TextView) convertView.findViewById(R.id.item_myscore_content_tv_exp);
                holder.tv_FinalScore = (TextView) convertView.findViewById(R.id.item_myscore_content_tv_final);
                holder.tv_MidScore = (TextView) convertView.findViewById(R.id.item_myscore_content_tv_mid);
                holder.ll_Content = (LinearLayout) convertView.findViewById(R.id.item_myscore_ll_content);
                holder.iv_Arrow = (ImageView) convertView.findViewById(R.id.item_myscore_head_iv_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_Content.setVisibility(View.GONE);
            holder.tv_Name.setText("课程名称:"+mDataList.get(position).courseName+"("+mDataList.get(position).courseNum+")");
            holder.tv_Name.setTextColor(mDataList.get(position).totalScore<60? Color.RED:getResources().getColor(R.color.primary_blue));
            if(mDataList.get(position).courseType.equals("考试课")){
                holder.tv_Score.setText("  成绩:"+mDataList.get(position).totalScore+"分");
            }else{
                holder.tv_Score.setText("  成绩:"+(mDataList.get(position).totalScore==60?"合格":"不合格"));
            }
            holder.tv_Score.setTextColor(mDataList.get(position).totalScore<60? Color.RED:getResources().getColor(R.color.primary_blue));
            holder.tv_PeaceScore.setText(mDataList.get(position).peaceScore+"分("+mDataList.get(position).arr.get(1)+"%)");
            holder.tv_ExpScore.setText(mDataList.get(position).expScore+"分("+mDataList.get(position).arr.get(0)+"%)");
            holder.tv_MidScore.setText(mDataList.get(position).midScore+"分("+mDataList.get(position).arr.get(2)+"%)");
            holder.tv_FinalScore.setText(mDataList.get(position).finalScore+"分("+mDataList.get(position).arr.get(3)+"%)");

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tv_Name;
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
                if(options1==5){
                    mListView.setAdapter(scoreListAapter);
                }else{
                    List<Score> list=new ArrayList<>();
                    for (int i = 0; i <mDataList.size() ; i++) {
                        if(switchScore(Float.valueOf(mDataList.get(i).totalScore))==options1){
                            list.add(mDataList.get(i));
                        }
                    }

                    mTv_Total.setText("总共:"+list.size()+"条");
                    mTv_Page.setText("1/"+((list.size()/50)+1)+"页");
                    mListView.setAdapter(new MyScoreListAapter(list));
                }
            }
        });
    }

    public int switchScore(float score) {
        if(score>=90){
            return 0;
        }else if(score>=80&&score<90){
            return 1;
        }else if(score>=70&&score<80){
            return 2;
        }else if(score>=60&&score<70){
            return 3;
        }else if(score<60){
            return 4;
        }
        return -1;
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
                mTv_Title.setText(termNo);
                Student student=BmobUser.getCurrentUser(Student.class);
                queyTotalMyScore(student,termNo);

            }
        });
    }
}
