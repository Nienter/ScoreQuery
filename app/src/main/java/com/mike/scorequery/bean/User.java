package com.mike.scorequery.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @description
 * @auhor niupengkai
 * @time 2018.03.22 12:29
 */

public class User extends BmobObject{
    public List<Integer> idl;

    public List<Integer> getIdl() {
        return idl;
    }

    public void setIdl(List<Integer> idl) {
        this.idl = idl;
    }
}
