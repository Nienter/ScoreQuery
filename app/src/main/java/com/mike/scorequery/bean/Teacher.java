package com.mike.scorequery.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by Mike on 2017/4/10.
 */
public class Teacher extends Admini {
    public String name;
    public String currentTerm;
    public String currentCourse;//当前所教的课程
    public String profession;//专业
    public String team;//队别
    public String grade;
    public BmobRelation courses;//所教的课
   // public String teacherId;//教师id
    public String sex;

}
