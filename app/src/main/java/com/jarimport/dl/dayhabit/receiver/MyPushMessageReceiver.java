package com.jarimport.dl.dayhabit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jarimport.dl.dayhabit.listener.GetPushInfoListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {
    private static GetPushInfoListener gpi = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.e("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));
            String info = intent.getStringExtra("msg");
            JSONObject job = null;

            try {
                job = new JSONObject(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                info = job.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (gpi != null) {
                gpi.getPushInfo(info);
            }
        }
    }

    public static void setPushListener(GetPushInfoListener gpL) {
        gpi = gpL;
    }
}