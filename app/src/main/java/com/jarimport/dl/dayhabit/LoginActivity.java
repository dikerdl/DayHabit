package com.jarimport.dl.dayhabit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jarimport.dl.dayhabit.activity.BaseActivity;
import com.jarimport.dl.dayhabit.entity.Person;
import com.jarimport.dl.dayhabit.utils.Constants;
import com.jarimport.dl.dayhabit.utils.PhoneUtil;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;
import com.jarimport.dl.dayhabit.view.MyToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by hasee on 2017/5/2.
 */

public class LoginActivity extends BaseActivity {
    private MyToolbar mTbar;
    private EditText mUserPhone;
    private EditText mUserPwd;
    private Button mLogin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_telphone);
        //Bmob初始化
        Bmob.initialize(this, "e6db58c6907b283a83ad2fcadf73843b");
        initView();
    }

    private void initView() {
        mTbar = (MyToolbar) findViewById(R.id.mTbar);
        mUserPhone = (EditText) findViewById(R.id.mUserPhone);
        mUserPwd = (EditText) findViewById(R.id.mUserPwd);
        mLogin_btn = (Button) findViewById(R.id.mLogin_btn);
        mTbar.setTitle("登录");

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        String userName = mUserPhone.getText().toString();
        String userPwd = mUserPwd.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            ToastUtil.showToast(this, "用户名或密码不能为空！");
        } else {
            if (PhoneUtil.isMobile(userName)) {
                BmobQuery<Person> query = new BmobQuery<Person>();
                query.addWhereEqualTo("name", userName);
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(1);
                //执行查询方法
                queryByNameAndPwd(query, userName, userPwd);
            } else {
                ToastUtil.showToast(this, "请输入合法的手机号！");
                mUserPhone.setText("");
            }
        }
    }

    private void queryByNameAndPwd(BmobQuery<Person> query, final String userName, final String userPwd) {
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> persons, BmobException e) {
                if (e == null) {
                    Log.e("HotShow", "--------查询成功：共" + persons.size() + "条数据。");
                    if (persons != null) {
                        Person person = persons.get(0);
                        if (person.getName().equals(userName) && person.getPwd().equals(userPwd)) {
                            ToastUtil.showToast(LoginActivity.this, "合法用户！");
                            SharedPreferencesUtils.setParam(LoginActivity.this, "isLogin", Constants.LOGIN);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "phone", userName);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "userPwd", userPwd);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "userId", person.getObjectId());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        if(person.getName().equals(userName) && !person.getPwd().equals(userPwd)){
                            ToastUtil.showToast(LoginActivity.this, "密码错误！");
                        }
                    } else {
                        ToastUtil.showToast(LoginActivity.this, "请先注册账号！");
                    }
                } else {
                    Log.e("HotShow", "--------查询失败：" + e.getMessage());
                }
            }
        });
    }

    //用户通过mob短信验证登录
    public void loginFast(View view) {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

                    if (PhoneUtil.isMobile(phone)) {
                        BmobQuery<Person> query = new BmobQuery<Person>();
                        query.addWhereEqualTo("name", phone);
                        //返回50条数据，如果不加上这条语句，默认返回10条数据
                        query.setLimit(1);
                        //执行查询方法
                        queryByName(query, phone);
                    } else {
                        ToastUtil.showToast(LoginActivity.this, "请输入合法的手机号！");
                        mUserPhone.setText("");
                    }
                }
            }
        });
        registerPage.show(this);
    }

    private void queryByName(BmobQuery<Person> query, final String phone) {
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> persons, BmobException e) {
                if (e == null) {
                    Log.e("HotShow", "--------查询成功：共" + persons.size() + "条数据。");
                    if (persons != null) {
                        Person person = persons.get(0);
                        if (person.getName().equals(phone)) {
                            ToastUtil.showToast(LoginActivity.this, "合法用户！");
                            SharedPreferencesUtils.setParam(LoginActivity.this, "isLogin", Constants.LOGIN);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "phone", phone);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "userPwd", person.getPwd());
                            SharedPreferencesUtils.setParam(LoginActivity.this, "userId", person.getObjectId());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(LoginActivity.this, "请先注册账号！");
                    }
                } else {
                    Log.e("HotShow", "--------查询失败：" + e.getMessage());
                }
            }
        });
    }

    //用户注册
    public void regist(View view) {
        startActivity(new Intent(this, RegistActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SharedPreferencesUtils.setParam(this, "isLogin", Constants.LOGINOUT);
            finish();
        }

        return false;

    }
}
