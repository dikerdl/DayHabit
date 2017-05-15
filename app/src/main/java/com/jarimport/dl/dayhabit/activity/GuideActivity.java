package com.jarimport.dl.dayhabit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jarimport.dl.dayhabit.LoginActivity;
import com.jarimport.dl.dayhabit.MainActivity;
import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.utils.Constants;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;

import static android.R.attr.value;


/**
 * @version V1.0 <描述当前版本功能>
 * @desc:
 * @author: Dengzhenhua
 * @date: 2017-04-24 12:39
 */

public class GuideActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_guide);
        int launchTime = (int) SharedPreferencesUtils.getParam(this, "launchTime1", Constants.APP_UNLAUNCH);
        boolean isLogin = (boolean) SharedPreferencesUtils.getParam(this, "isLogin", Constants.LOGINOUT);
        String phone = (String) SharedPreferencesUtils.getParam(this, "phone", "0");
        String pwd = (String) SharedPreferencesUtils.getParam(this, "userPwd","0");

        if(0!=launchTime && launchTime==Constants.APP_LAUNCH){
            Log.e("GuideActivity","user Second launch this application!");
            if(!isLogin &&  phone.equals("0")||pwd.equals("0")){
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
            }else{
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            }
            finish();
        }else{
            SharedPreferencesUtils.setParam(GuideActivity.this,"launchTime1",Constants.APP_LAUNCH);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                    Log.e("GuideActivity","user first launch this application!");
                    finish();
                }
            },3000);
        }
    }
}
