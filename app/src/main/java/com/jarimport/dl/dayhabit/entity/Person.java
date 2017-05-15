package com.jarimport.dl.dayhabit.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {
    private String name;
    private String pwd;

    public List<String> getCareUser() {
        return careUser;
    }

    public void setCareUser(List<String> careUser) {
        this.careUser = careUser;
    }

    private List<String> careUser;  //关注的人

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}