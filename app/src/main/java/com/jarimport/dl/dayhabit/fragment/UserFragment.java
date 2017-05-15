package com.jarimport.dl.dayhabit.fragment;

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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarimport.dl.dayhabit.LoginActivity;
import com.jarimport.dl.dayhabit.MainActivity;
import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.utils.BitmapUtil;
import com.jarimport.dl.dayhabit.utils.Constants;
import com.jarimport.dl.dayhabit.utils.DataCleanManager;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;
import com.jarimport.dl.dayhabit.view.MyCircleView;
import com.jarimport.dl.dayhabit.view.MyToolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static java.util.ResourceBundle.clearCache;


public class UserFragment extends BaseFragment {
    private View view;
    private MyToolbar mToolBar4;
    private ImageView mUserPortrait;
    private TextView mPersonName;
    private Button mChangeAccount;
    private TextView mClearCache;
    private Button mExitApp;
    private boolean isClearedCache = false;
    private TextView mShowCacheSize;

    public UserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mToolBar4 = (MyToolbar) view.findViewById(R.id.mToolbar4);
        mToolBar4.setTitle("个人中心");
        mUserPortrait = (ImageView) view.findViewById(R.id.mUserPortrait);

        mPersonName = (TextView) view.findViewById(R.id.mPersonName);
        mShowCacheSize = (TextView) view.findViewById(R.id.mShowCacheSize);
        mPersonName.setText((String) SharedPreferencesUtils.getParam(getActivity(), "phone", "userNUll"));

        mChangeAccount = (Button) view.findViewById(R.id.mChangeAccount);
        mClearCache = (TextView) view.findViewById(R.id.mClearCache);
        mExitApp = (Button) view.findViewById(R.id.mExitApp);


        mShowCacheSize.setText(getDirFileSize());

        String picPath = (String) SharedPreferencesUtils.getParam(getActivity(), "picPortrait", Environment.getExternalStorageDirectory() + "/pintu");
        Log.e("userFragmnet","----"+picPath);

        boolean isClearedCache = (boolean) SharedPreferencesUtils.getParam(getActivity(), "isClearedCache", false);

        if (!isClearedCache && !picPath.equals(Environment.getExternalStorageDirectory() + "/pintu")) {
            if(new File(picPath).exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                mUserPortrait.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
            }else{
                mUserPortrait.setImageResource(R.drawable.user);
            }

        }else{
            SharedPreferencesUtils.setParam(getActivity(), "picPortrait", Environment.getExternalStorageDirectory() + "/pintu");
            picPath =(String) SharedPreferencesUtils.getParam(getActivity(), "picPortrait", Environment.getExternalStorageDirectory() + "/pintu");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    private void setListener() {
        mUserPortrait.setOnClickListener(new MyClickListener());
        mChangeAccount.setOnClickListener(new MyClickListener());
        mClearCache.setOnClickListener(new MyClickListener());
        mExitApp.setOnClickListener(new MyClickListener());

    }

    @Override
    public View initView() {
        return view;
    }

    @Override
    public void initData() {

    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mUserPortrait:
                    setUserPortrait();
                    break;
                case R.id.mClearCache:
                    clearAppCache();
                    break;
                case R.id.mChangeAccount:
                    changeAccount();
                    break;
                case R.id.mExitApp:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeAccount() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        MainActivity ma = (MainActivity) getActivity();
        ma.getFragments().clear();
        getActivity().finish();
    }

    private void clearAppCache() {
       /*缓存：
        file-普通的文件存储
        database-数据库文件（.db文件）
        sharedPreference-配置数据(.xml文件）
        cache-图片缓存文件*/
        //此处只清理外部自定义缓存目录下的文件(直接删除该目录下的文件)
        MyCircleView myCircleView = new MyCircleView(getActivity());
        myCircleView.setLayoutParams(new LinearLayout.LayoutParams(80,80));
        myCircleView.refresh(2.0f);
        myCircleView.setScrollBarFadeDuration(800);
        myCircleView.setVisibility(View.VISIBLE);

        File file = new File(Constants.PATH);
        double totalSize =0;
        if(file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file2 : files) {
                    file2.delete();
                }
            }
        }
        mClearCache.setText("清除缓存");
        mShowCacheSize.setText("");
        ToastUtil.showToast(getActivity(),"成功清除缓存！");
    }

    private void setUserPortrait() {
        String[] items = new String[]{"系统相机拍照", "手机相册"};
        new AlertDialog.Builder(getActivity())
                .setTitle("设置用户头像")
                .setIcon(R.drawable.ic_launcher)
                .setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("userFragment", "-----" + which);
                        if (which == 0) {
                            //系统相机拍照
                            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(camera, 12);
                        } else {
                            //调用系统相册
                            Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(picture, 21);
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == Activity.RESULT_OK && null != data) {
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
                SharedPreferencesUtils.setParam(getActivity(), "picPortrait", filename);
                SharedPreferencesUtils.setParam(getActivity(), "isClearedCache", false);
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //显示图片
            Bitmap bitmap4 = BitmapUtil.toRoundBitmap(bitmap);
            mUserPortrait.setImageBitmap(bitmap4);
        }
        if (requestCode == 21 && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            SharedPreferencesUtils.setParam(getActivity(), "picPortrait", picturePath);
            SharedPreferencesUtils.setParam(getActivity(), "isClearedCache", false);
            c.close();
            //获取图片并显示
            Bitmap bitmap2 = BitmapFactory.decodeFile(picturePath);
            Bitmap bitmap3 = BitmapUtil.toRoundBitmap(bitmap2);
            mUserPortrait.setImageBitmap(bitmap3);
        }
    }
    private String getDirFileSize(){
        File file = new File(Constants.PATH);
        double totalSize =0;
        if(file.exists()){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (File file2: files) {
                    totalSize += file2.length();
                    Log.e("UserFragment","---file size:"+file2.length());
                }
            }
        }
        String formatSize = DataCleanManager.getFormatSize(totalSize);
        Log.e("UserFragment","---dir size:"+formatSize);
        return formatSize;
    }
}
