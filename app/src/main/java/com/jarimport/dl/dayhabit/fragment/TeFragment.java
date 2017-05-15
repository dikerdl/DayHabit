package com.jarimport.dl.dayhabit.fragment;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.listener.GetPushInfoListener;
import com.jarimport.dl.dayhabit.receiver.MyPushMessageReceiver;
import com.jarimport.dl.dayhabit.view.MyToolbar;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class TeFragment extends BaseFragment{
    private View view;
    private MyToolbar mToolBar3;
    private TextView mshowPushInfo;

    public TeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_te, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mToolBar3 = (MyToolbar) view.findViewById(R.id.mToolbar3);
        mshowPushInfo = (TextView) view.findViewById(R.id.mshowPushInfo);
        mToolBar3.setTitle("消息");
    }


    @Override
    public void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View initView() {
        return view;
    }


}
