package com.jarimport.dl.dayhabit.utils;

import android.util.Log;

import com.jarimport.dl.dayhabit.application.MyApplication;
import com.jarimport.dl.dayhabit.entity.DaoSession;
import com.jarimport.dl.dayhabit.entity.Habit;
import com.jarimport.dl.dayhabit.entity.HabitDao;
import com.jarimport.dl.dayhabit.entity.HabitNews;
import com.jarimport.dl.dayhabit.listener.HabitNewsListener;
import com.jarimport.dl.dayhabit.listener.UpdateSycData;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hasee on 2017/5/2.
 */

public class DataUtils {
    private static DaoSession daoSession = MyApplication.getDaoSession();

    public static String getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(String currentLocation2) {
       currentLocation = currentLocation2;
    }

    private static String currentLocation;
    private static List<Habit> data = null;


    public static List<Habit> getHabitData() {
        Query query = daoSession.getHabitDao().queryBuilder()
                .where(HabitDao.Properties.Id.notEq("-1"))
                .orderDesc(HabitDao.Properties.AddTime)
                .build();

//      查询结果以 List 返回
        data = query.list();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return data;
    }

    public static void insertData(Habit habit) {

        daoSession.getHabitDao().insert(habit);
    }
    public static void deleteData(Habit habit) {

        daoSession.getHabitDao().delete(habit);
    }

    public static void getData(final HabitNewsListener habitNewsListener) {
        BmobQuery<HabitNews> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereNotEqualTo("id", "0");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<HabitNews>() {
            @Override
            public void done(List<HabitNews> list, BmobException e) {
                if (e == null) {
                    Log.e("HotShow", "--------查询成功：共" + list.size() + "条数据。");
                    habitNewsListener.getHabitNewsList(list);
                } else {
                    Log.e("HotShow", "--------查询失败：" + e.getMessage());
                    habitNewsListener.getHabitNewsList(null);
                }
            }
        });
    }

    public static void getShowData(UpdateSycData updateSycDataListener) {

    }
}
