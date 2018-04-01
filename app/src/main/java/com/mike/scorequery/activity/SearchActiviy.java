package com.mike.scorequery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.mike.scorequery.R;
import com.mike.scorequery.bean.Admini;
import com.mike.scorequery.bean.Drug;
import com.mike.scorequery.bean.Score;
import com.mike.scorequery.bean.Student;
import com.mike.scorequery.bean.Teacher;
import com.mike.scorequery.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by mike on 2017/4/9.
 */
public class SearchActiviy extends BaseActivity implements View.OnClickListener {


    private ListView mListView;
    private ImageView mIv_Left;
    private SearchView mSearchView;
    private ArrayList<String> mDataList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView= (ListView) findViewById(R.id.search_lv_search);
        mIv_Left= (ImageView) findViewById(R.id.search_search_iv);
        mIv_Left.setOnClickListener(this);
        mListView.setTextFilterEnabled(true);
        mDataList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mDataList);
        mListView.setAdapter(arrayAdapter);
        mSearchView= (SearchView) findViewById(R.id.search_search_sv);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Admini admini= BmobUser.getCurrentUser(Admini.class);
                switch (admini.identity){
                    case "0":
                        startActivity(new Intent(SearchActiviy.this,EnterQueryActivity.class).putExtra("courseName",mDataList.get(position)));
                        break;
                    case "1":
                        startActivity(new Intent(SearchActiviy.this,EnterQueryActivity.class).putExtra("studentName",mDataList.get(position)));
                        break;
                    case "2":
                        break;
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Admini admini= BmobUser.getCurrentUser(Admini.class);
                switch (admini.identity){
                    case "0":
                      queryTotalCourseName(newText);
                        break;
                    case "1":
                        queryTotalStudentName(newText);
                        break;
                    case "2":
                        break;
                }
                return false;
            }
        });

        Admini admini= BmobUser.getCurrentUser(Admini.class);
        switch (admini.identity){
            case "0":
                break;
            case "1":
                break;
            case "2":
                break;
        }
    }

    private void queryTotalStudentName(String keyWord) {
        char[] chars=keyWord.toCharArray();
        boolean flag=true;
        for (int i = 0; i < chars.length; i++) {
            if(!(chars[i]>='0'&&chars[i]<='9')){
                flag=false;
                break;
            }
        }
        Teacher teacher=BmobUser.getCurrentUser(Teacher.class);
        BmobQuery<Drug> query=new BmobQuery<>();
        query.addWhereEqualTo("drug_id",keyWord);
        ArrayList<String> list=new ArrayList<>();
//        list.add(keyWord);
//        query.addWhereEqualTo("teacherId",teacher.getUsername());
//        if(flag){
//            query.addWhereContainedIn("studentId",list) ;
//        }else{
//            query.addWhereContainedIn("studentName",list) ;
//        }

//        if(flag){
//            query.addWhereContains("studentId", keyWord);
//        }else{
//            query.addWhereContains("studentName", keyWord);
//        }
        query.findObjects(new FindListener<Drug>() {
           @Override
           public void done(List<Drug> list, BmobException e) {
               if(e==null){
                   mDataList.clear();
                   for (int i = 0; i <list.size() ; i++) {
                       if(!mDataList.contains(list.get(i).drug_name+"("+list.get(i).drug_id+")")){
                           mDataList.add(list.get(i).drug_name+"("+list.get(i).drug_id+")");
                       }
                   }
                   arrayAdapter.notifyDataSetChanged();
               }else{
                   ToastUtil.showToast(e.getMessage());
               }
           }
       });
    }
    private void queryTotalCourseName(String keyWord) {
        char[] chars=keyWord.toCharArray();
        boolean flag=true;
        for (int i = 0; i < chars.length; i++) {
            if(!(chars[i]>='0'&&chars[i]<='9')){
                flag=false;
                break;
            }
        }
        ArrayList<String> list=new ArrayList<>();
        Student student=BmobUser.getCurrentUser(Student.class);
        BmobQuery<Drug> query = new BmobQuery<>();
        query.addWhereEqualTo("drug_id",keyWord);
//        BmobQuery<Score> query=new BmobQuery<>();
//        list.add(keyWord);
//        query.addWhereEqualTo("studentId",student.getUsername());
//        if(flag){
//            query.addWhereContainedIn("courseNum",list);
//        }else{
//            query.addWhereContainedIn("courseName",list);
//        }

//        if(flag){
//            query.addWhereContains("courseNum", keyWord);
//        }else{
//            query.addWhereContains("courseName", keyWord);
//        }
        query.findObjects(new FindListener<Drug>() {
           @Override
           public void done(List<Drug> list, BmobException e) {
               if(e==null){
                   mDataList.clear();
                   for (int i = 0; i <list.size() ; i++) {
                       if(!mDataList.contains(list.get(i).drug_name+"("+list.get(i).drug_id+")")){
                           mDataList.add(list.get(i).drug_name+"("+list.get(i).drug_id+")");
                       }
                   }
                   arrayAdapter.notifyDataSetChanged();
               }else{
                   ToastUtil.showToast(e.getMessage());
               }
           }
       });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_search_iv:
                finish();
                break;
        }
    }
}
