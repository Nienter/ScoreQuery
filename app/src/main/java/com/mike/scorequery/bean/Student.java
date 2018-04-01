package com.mike.scorequery.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Mike on 2017/4/10.
 */
public class Student extends Admini {
    public String name;
    public String sex;//性别
    public String grade;
    public String currentTerm;
    public String profession;//专业
    public BmobRelation courses;//所选的课程
    public String team;//队别
   // public String studentId;//学号
    //public String myClass;//班级

}
