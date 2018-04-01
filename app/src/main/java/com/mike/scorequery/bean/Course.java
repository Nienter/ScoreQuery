package com.mike.scorequery.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Mike on 2017/4/10.
 */
public class Course extends BmobObject {
   // courseNum,courseName,grade,termNo,profession,teacherId,teacherName,arr,team
    public String courseName;//课程名称
    public String courseNum;//课程编号
    public String courseType;//课程编号
    public String grade;//课程编号
    public String termNo;//课程编号
    public String profession;//课程编号
    public String teacherId;//课程编号
    public String teacherName;//课程编号
    public List<Integer> arr;//课程编号
    public String team;//课程编号
    public BmobRelation students;//所选学生
    public BmobRelation teachers;//老师选择教的课程
}
