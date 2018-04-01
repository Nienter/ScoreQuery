package com.mike.scorequery;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import java.util.ArrayList;

/**
 * 替代application 使其能在各层被调用
 * Created by Mike on 2016/10/11.
 */
public class AppContext {

    private static AppContext appContext;
    private Handler handler;
    public ArrayList<String> gradeList;
    public ArrayList<String> scoreResultlist;
    public ArrayList<String> courseList;
    public ArrayList<String> professionList;
    public ArrayList<ArrayList<String>> teamList;
    public ArrayList<ArrayList<String>> termList;
    public ArrayList<String> yearList;
    public ArrayList<String> startTimeList;
    public ArrayList<String> endTimeList;
    public ArrayList<String> identityList;

    public Context getContext() {
        return context;
    }

    private Context context;


    public static AppContext getInstance() {
        if (appContext == null) {
            synchronized (AppContext.class) {
                if (appContext == null) {
                    appContext = new AppContext();
                }
            }
        }
        return appContext;
    }

    public void init(Context context) {
        this.context = context;
        this.handler = new Handler();
        initData();
    }

    private void initData(){
        initYearList();
        initTermList();
        initTimeList();
        initCourseList();
        initProfessionList();
        initTeamList();
        initgradeList();
        initIdentityList();
        initScoreResultList();
    }

    private void initCourseList(){
        courseList=initArrayData(R.array.courses);
    }
    private void initProfessionList(){
        professionList=initArrayData(R.array.profession);
    }
    private void initIdentityList(){
        identityList=initArrayData(R.array.identity);
    }
    private void initScoreResultList(){
        scoreResultlist=initArrayData(R.array.score_result);
    }
    private void initgradeList(){
        ArrayList<String> list=new ArrayList<>();
        for (int i = 0; i <11 ; i++) {
            list.add(2010+i+"级");
        }
        gradeList=list;
    }
    private void initTeamList(){
        ArrayList<ArrayList<String>> list=new ArrayList<>();
        ArrayList<String> teamlist=initArrayData(R.array.team);
        for (int i = 0; i <professionList.size() ; i++) {
            list.add(teamlist);
        }
        teamList=list;
    }
    private void initTermList(){
        ArrayList<ArrayList<String>> list=new ArrayList<>();
        ArrayList<String> termlist=initArrayData(R.array.term_no);
        for (int i = 0; i <yearList.size() ; i++) {
            list.add(termlist);
        }
        termList=list;
    }

    private void initTimeList(){
        startTimeList=new ArrayList<>();endTimeList=new ArrayList<>();
        startTimeList.add("08:00");endTimeList.add("08:45");
        startTimeList.add("08:50");endTimeList.add("09:35");
        startTimeList.add("09:55");endTimeList.add("10:40");
        startTimeList.add("10:45");endTimeList.add("11:30");
        startTimeList.add("11:35");endTimeList.add("12:15");
        startTimeList.add("14:00");endTimeList.add("14:45");
        startTimeList.add("14:50");endTimeList.add("15:35");
        startTimeList.add("15:55");endTimeList.add("16:40");
        startTimeList.add("16:45");endTimeList.add("17:30");
        startTimeList.add("18:30");endTimeList.add("19:15");
        startTimeList.add("19:20");endTimeList.add("08:05");
        startTimeList.add("20:10");endTimeList.add("20:55");
        startTimeList.add("21:00");endTimeList.add("21:45");
        startTimeList.add("21:50");endTimeList.add("22:35");
        startTimeList.add("22:40");endTimeList.add("23:25");
        startTimeList.add("23:15");endTimeList.add("24:00");
    }

    private void initYearList() {
        ArrayList<String> list=new ArrayList<>();
        for (int i = 2010; i <=2020 ; i++) {
            list.add(i+"-"+(i+1));
        }
        yearList=list;
    }


    public ArrayList<String> initArrayData(int id){
        String[] strs=getResources().getStringArray(id);
        ArrayList<String> list=new ArrayList<>();
        for (int i = 0; i <strs.length ; i++) {
           list.add(strs[i]);
        }
        return list;
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * 是否有无网络
     */

    public String getString(int id) {
        return context.getResources().getString(id);
    }

    public Resources getResources() {
        return context.getResources();
    }



}
