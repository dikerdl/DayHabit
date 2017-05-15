package com.jarimport.dl.dayhabit.listener;

import com.jarimport.dl.dayhabit.entity.HabitNews;

import java.util.List;

/**
 * Created by hasee on 2017/5/3.
 */

public interface HabitNewsListener {
    void getHabitNewsList(List<HabitNews> list);
}
