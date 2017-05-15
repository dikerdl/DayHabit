package com.jarimport.dl.dayhabit.entity;

import java.util.List;
import cn.bmob.v3.BmobObject;

/**
 * Created by hasee on 2017/5/2.
 */

public class HabitNews extends BmobObject {
    private String name;                //用户名，电话或昵称
    private String password;            //密码

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTextHabit() {
        return textHabit;
    }

    public void setTextHabit(String textHabit) {
        this.textHabit = textHabit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public Integer getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(Integer heartCount) {
        this.heartCount = heartCount;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String textHabit;                //每日习惯记录语
    private String address;             //分享时的地理位置
    private List<String> pics;       //分享的图片
    private Integer  heartCount;          //被点赞的次数

    private String time;             //分享的时间

    public List<String> getBeCaredUser() {
        return beCaredUser;
    }

    public void setBeCaredUser(List<String> beCaredUser) {
        this.beCaredUser = beCaredUser;
    }

    private List<String> beCaredUser; //关注当前用户的其他用户
}
