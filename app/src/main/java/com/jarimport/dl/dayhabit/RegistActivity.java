package com.jarimport.dl.dayhabit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jarimport.dl.dayhabit.activity.BaseActivity;
import com.jarimport.dl.dayhabit.entity.Person;
import com.jarimport.dl.dayhabit.utils.Constants;
import com.jarimport.dl.dayhabit.utils.PhoneUtil;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;
import com.jarimport.dl.dayhabit.view.MyToolbar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hasee on 2017/5/2.
 */

public class RegistActivity extends BaseActivity {
    private MyToolbar regist_mTbar;
    private EditText regist_mUserPhone;
    private EditText regist_mUserPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        regist_mTbar = (MyToolbar) findViewById(R.id.regist_mTbar);
        regist_mUserPhone = (EditText) findViewById(R.id.regist_mUserPhone);
        regist_mUserPwd = (EditText) findViewById(R.id.regist_mUserPwd);
        regist_mTbar.setTitle("注册");
    }

    public void registByPhone(View view) {
        final String regist_name = regist_mUserPhone.getText().toString();
        String regist_pwd = regist_mUserPwd.getText().toString();
        if (TextUtils.isEmpty(regist_name) || TextUtils.isEmpty(regist_pwd)) {
            ToastUtil.showToast(this, "账号或密码不能为空！");
        } else {
            //验证手机号合法（正则表达式判断）
            if (PhoneUtil.isMobile(regist_name)) {
                Person person = new Person();
                person.setName(regist_name);
                person.setPwd(regist_pwd);
                List<String> careUser = new ArrayList<>();
                person.setCareUser(careUser);

                person.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("RegistActivity", "用户数据上传成功！"+s);
                            ToastUtil.showToast(RegistActivity.this, "注册成功请重新登录！");
                        } else {
                            Log.e("RegistActivity", "用户数据上传失败！"+e.getMessage());
                            String back ="unique index cannot has duplicate value: "+regist_name;
                            if(back.equals(e.getMessage())){
                                ToastUtil.showToast(RegistActivity.this, "用户已存在可直接登录！");
                            }
                        }
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            } else {
                ToastUtil.showToast(this, "请输入合法的手机号！");
            }
        }
    }

}
