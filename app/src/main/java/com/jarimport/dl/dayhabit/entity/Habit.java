package com.jarimport.dl.dayhabit.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @version V1.0 <描述当前版本功能>
 * @desc:
 * @author: myName
 * @date: 2017-04-25 14:01
 */
@Entity
public class Habit {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String habitInfo;
    @Unique
    private String addTime;
    @Generated(hash = 1552509528)
    public Habit(Long id, String habitInfo, String addTime) {
        this.id = id;
        this.habitInfo = habitInfo;
        this.addTime = addTime;
    }
    @Generated(hash = 1591984218)
    public Habit() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHabitInfo() {
        return this.habitInfo;
    }
    public void setHabitInfo(String habitInfo) {
        this.habitInfo = habitInfo;
    }
    public String getAddTime() {
        return this.addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
