package com.jarimport.dl.dayhabit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.jarimport.dl.dayhabit.activity.BaseActivity;
import com.jarimport.dl.dayhabit.entity.HabitNews;
import com.jarimport.dl.dayhabit.utils.BitmapUtil;
import com.jarimport.dl.dayhabit.utils.DataUtils;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hasee on 2017/5/3.
 */

public class UpLoadActivity extends BaseActivity {
    private EditText mHabitNewsText;
    private ImageView mUploadHabitPics;
    private List<String> habitPicPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        String habitInfo1 = intent.getStringExtra("habitInfo");
        Log.e("UploadActivity", "----" + position + " :" + habitInfo1);
//        String habitInfo = (String) intent.getCharSequenceExtra("habitInfo");
        findView();
    }

    private void findView() {
        mHabitNewsText = (EditText) findViewById(R.id.mHabitNewsText);
        mUploadHabitPics = (ImageView) findViewById(R.id.mUploadHabitPics);
    }

    public void upLoadHabit(View view) {

        String text = mHabitNewsText.getText().toString();
        String name = (String) SharedPreferencesUtils.getParam(this, "phone", "0");
        String pwd = (String) SharedPreferencesUtils.getParam(this, "userPwd", "0");
        String address = DataUtils.getCurrentLocation();
        int heartCount = 0;
        List<String> beCaredString = new ArrayList<>();
        beCaredString.add("天天DayHabiter");
        HabitNews habitNews = new HabitNews();
        habitNews.setName(name);
        habitNews.setTextHabit(text);
        habitNews.setHeartCount(heartCount);
        habitNews.setAddress(address);
        habitNews.setPassword(pwd);
        habitNews.setBeCaredUser(beCaredString);
        habitNews.setPics(habitPicPaths);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String time = dateFormat.format(new Date());
        habitNews.setTime(time);

        habitNews.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    habitPicPaths.clear();
                    Log.e("UploadActivity" + habitPicPaths.size(), "----upload success!" + s);
                    ToastUtil.showToast(UpLoadActivity.this, "----上传成功!" + s);
                    finish();
                } else {
                    Log.e("UploadActivity", "----upload failed!" + e.getMessage());
                    ToastUtil.showToast(UpLoadActivity.this, "----上传失败!" + s);
                    finish();
                }
            }
        });

    }

    public void getHabitPic(View view) {
        String[] items = new String[]{"系统相机拍照", "手机相册"};
        new AlertDialog.Builder(this)
                .setTitle("设置用户头像")
                .setIcon(R.drawable.ic_launcher)
                .setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("userFragment", "-----" + which);
                        if (which == 0) {
                            //系统相机拍照
                            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(camera, 3);
                        } else {
                            //调用系统相册
                            Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(picture, 7);
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                Log.e("userfragmnet", "sd card unmount");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File(Environment.getExternalStorageDirectory() + "/" + "pintu");
            file.mkdirs();
            String filename = file.getPath() + "/" + name;
            try {
                fout = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fout);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                habitPicPaths.add(filename);
                SharedPreferencesUtils.setParam(UpLoadActivity.this, "picPath", filename);
                SharedPreferencesUtils.setParam(UpLoadActivity.this, "isClearedCache", false);
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //显示图片
//            Bitmap bitmap4 = BitmapUtil.toRoundBitmap(bitmap);
            mUploadHabitPics.setImageBitmap(bitmap);
        }
        if (requestCode == 7 && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = UpLoadActivity.this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            habitPicPaths.add(picturePath);
            SharedPreferencesUtils.setParam(UpLoadActivity.this, "picPath", picturePath);
            SharedPreferencesUtils.setParam(UpLoadActivity.this, "isClearedCache", false);
            c.close();
            //获取图片并显示
            Bitmap bitmap2 = BitmapFactory.decodeFile(picturePath);
//            Bitmap bitmap3 = BitmapUtil.toRoundBitmap(bitmap2);
            mUploadHabitPics.setImageBitmap(bitmap2);
        }
    }
}
