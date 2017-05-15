package com.jarimport.dl.dayhabit.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.jarimport.dl.dayhabit.R;

/**
 * @version V1.0 <描述当前版本功能>
 * @desc:
 * @author: myName
 * @date: 2017-04-25 13:01
 */

public class MyToolbar extends Toolbar {
    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#499b4d"));
        setTitle("天天习惯");
        setTitleMarginStart(-20);
        setNavigationIcon(R.mipmap.ic_noti_bar);
    }
}
