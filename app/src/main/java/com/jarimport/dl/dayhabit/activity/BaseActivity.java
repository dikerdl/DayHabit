package com.jarimport.dl.dayhabit.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jarimport.dl.dayhabit.utils.StatusBarCompat;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //为每一个继承该类的Activity实现沉浸式状态栏
       // StatusBarCompat.compat(this, Color.BLUE);
        StatusBarCompat.compat(this,Color.parseColor("#499b4d"));
    }
}