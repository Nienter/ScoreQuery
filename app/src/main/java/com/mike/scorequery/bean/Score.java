package com.mike.scorequery.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Mike on 2017/4/13.
 */
public class Score extends BmobObject {



    public Integer expScore;//实验课成绩
    public Integer peaceScore;//平时成绩
    public Integer midScore;//期中成绩
    public Integer finalScore;//期末成绩
    public Float totalScore;//最后成绩
    public List<Integer> arr;//权重

    public Integer score;
    public String studentId;
    public String studentName;
    public String courseNum;
    public String courseName;
    public String courseType;
    public String termNo;
    public String sex;
    public String profession;
    public String grade;
    public String team;
    public String teacherId;
    public String teacherName;

}
