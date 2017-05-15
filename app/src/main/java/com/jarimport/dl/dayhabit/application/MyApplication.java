package com.jarimport.dl.dayhabit.application;

import android.app.Application;
import android.content.Context;

import com.jarimport.dl.dayhabit.entity.DaoMaster;
import com.jarimport.dl.dayhabit.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

import cn.smssdk.SMSSDK;


/**
 * @version V1.0 <描述当前版本功能>
 * @desc:
 * @author: Dengzhenhua
 * @date: 2017-04-24 12:23
 */

public class MyApplication extends Application {
    private static DaoSession mDaoSession;
    private static final String DATA_BASE_NAME = "habit3";

    @Override
    public void onCreate() {
        super.onCreate();
        // "您的appkey", "您的appsecret"
        SMSSDK.initSDK(this,"1d5010db497bf","09c9014dafb325ef64750543d8708a4d");

        setupDataBase(getApplicationContext());
    }
    private void setupDataBase(Context context){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoSession(){
        return mDaoSession;
    }
}
