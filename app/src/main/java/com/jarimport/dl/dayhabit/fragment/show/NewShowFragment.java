package com.jarimport.dl.dayhabit.fragment.show;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.adapter.BaseRecyclerAdapter;
import com.jarimport.dl.dayhabit.adapter.RecyclerViewHolder;
import com.jarimport.dl.dayhabit.entity.HabitNews;
import com.jarimport.dl.dayhabit.entity.Person;
import com.jarimport.dl.dayhabit.fragment.BaseFragment;
import com.jarimport.dl.dayhabit.listener.HabitNewsListener;
import com.jarimport.dl.dayhabit.utils.DataUtils;
import com.jarimport.dl.dayhabit.utils.SharedPreferencesUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewShowFragment extends BaseFragment {

    private View view;
    private List<HabitNews> data2 = new ArrayList<>();
    private RecyclerView newRecycleView;
    private SwipeRefreshLayout newSwipe;
    private BaseRecyclerAdapter<HabitNews> newAdapter;
    private boolean isHeart = false;
    private boolean isCared = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e("HotShowFargment", "----更新了" + data2.size() + "条数据！");
                    if (newSwipe.isRefreshing()) {
                        newSwipe.setRefreshing(false);
                    }
                    newAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public NewShowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_show, container, false);
        findView(view);
        setRecyclerView();
        setSipeLayout();
        return view;
    }

    private void setSipeLayout() {
        newSwipe.setSize(SwipeRefreshLayout.LARGE);
        //设置刷新进度动画的颜色
        newSwipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        newSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new MyThread().start();
            }
        });
    }

    private void setRecyclerView() {
        newRecycleView.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        newRecycleView.setItemAnimator(new DefaultItemAnimator());
        newRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setNewAdapter() {
        newAdapter = new BaseRecyclerAdapter<HabitNews>(getParentFragment().getActivity(), data2) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.habit_show_item;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, final HabitNews item) {
                TextView textView = holder.getTextView(R.id.mUserId);
                TextView textView1 = holder.getTextView(R.id.mTime);
                ImageView shareOtherBtn = holder.getImageView(R.id.shareOtherHabit);
                TextView mLocation = holder.getTextView(R.id.mLocation);
                mLocation.setText(DataUtils.getCurrentLocation());
                final TextView mHeartCount = holder.getTextView(R.id.mHeartCount);
                final TextView mCaredCount = holder.getTextView(R.id.mCaredCount);
                final String currentName = item.getName();
                final int i = item.getHeartCount();
                mHeartCount.setText("" + i);
                Log.e("HotShowFragment", "---heartCount is:" + i);
                ImageView imageView = holder.getImageView(R.id.mHabitPic_show);
                //由于用户账号是手机号，此处设置objectId保护用户隐私
                //textView.setText("用户" + currentName);
                textView.setText("用户" + item.getObjectId());
                textView1.setText("" + item.getTime());
                TextView mTextHabit = holder.getTextView(R.id.mTextHabit);
                mTextHabit.setText(item.getTextHabit());
                mCaredCount.setText("" + item.getBeCaredUser().size());
                if (item.getPics().size() != 0) {
                    String picPath = item.getPics().get(0);
                    if(new File(picPath).exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        imageView.setImageBitmap(bitmap);
                    }else{
                        imageView.setImageResource(R.drawable.guide4);
                    }
                } else {
                    imageView.setImageResource(R.drawable.guide4);
                }
                shareOtherBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShare(item.getTextHabit(),item.getPics().get(0));
                    }
                });
                mHeartCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateHeartCount(mHeartCount,i,item);
                    }
                });

                mCaredCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        careCurrentUser(mCaredCount, currentName, item.getBeCaredUser().size());
                    }
                });
            }
        };
        newRecycleView.setAdapter(newAdapter);
    }
    private void updateHeartCount(final TextView mHeartCount, final int i, final HabitNews item) {
        HabitNews p3 = new HabitNews();
        int j =i+1;
        if(isHeart){
            j=i;
        }
        p3.setHeartCount(j);
        String objectId = item.getObjectId();
        p3.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if (!isHeart) {
                        ToastUtil.showToast(getActivity(),"点赞成功！");
                        mHeartCount.setText("" + (i + 1));
                        mHeartCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.good), null, null, null);
                        isHeart = true;
                    } else {
                        ToastUtil.showToast(getActivity(),"取消点赞成功！");
                        mHeartCount.setText("" + i);
                        mHeartCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.good2), null, null, null);
                        isHeart = false;
                    }
                }else{
                    ToastUtil.showToast(getActivity(),"更新失败！");
                }
            }
        });
    }
    private void careCurrentUser(final TextView v2, String currentName, final int size) {
        Person p2 = new Person();
        List<String> careUser = new ArrayList<String>();
        careUser.add(currentName);

        if (isCared) {
            careUser.remove(currentName);
        }
        p2.setCareUser(careUser);

        String objectId = (String) SharedPreferencesUtils.getParam(getActivity(), "userId", "00");
        p2.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (!isCared) {
                        ToastUtil.showToast(getActivity(), "关注成功!");
                        v2.setText("" + (size + 1));
                        v2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favorites2), null, null, null);
                        isCared = true;
                    } else {
                        ToastUtil.showToast(getActivity(), "取消关注成功!");
                        v2.setText("" + size);
                        v2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favorites), null, null, null);
                        isCared = false;
                    }
                } else {
                    ToastUtil.showToast(getActivity(), "关注失败!" + e.getMessage());
                }
            }
        });
    }

    private void findView(View view) {
        newSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipeNewLayout);
        newRecycleView = (RecyclerView) view.findViewById(R.id.mNewHabits);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setNewAdapter();
        newSwipe.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DataUtils.getData(new HabitNewsListener() {
                    @Override
                    public void getHabitNewsList(List<HabitNews> list) {
                        data2.clear();
                        if (list != null && list.size()!=0) {
                            data2.addAll(list);
                            if (data2.size() != 0 && newSwipe.isRefreshing()) {
                                newSwipe.setRefreshing(false);
                                newAdapter.notifyDataSetChanged();
                            }
                        }else{
                            newSwipe.setRefreshing(false);
                        }
                    }
                });
            }
        }, 1500);
    }

    @Override
    public View initView() {
        return view;
    }

    @Override
    public void initData() {

    }
    //新浪微博，QQ,短信都能分享
    private void showShare(String textHabit, String picPath) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("--=-=-");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(textHabit);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(picPath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(getActivity());
    }
    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            DataUtils.getData(new HabitNewsListener() {
                @Override
                public void getHabitNewsList(List<HabitNews> list) {
                    data2.clear();
                    if (list != null && list.size()!=0) {
                        data2.addAll(list);
                        mHandler.sendEmptyMessage(1);
                    }else{
                        newSwipe.setRefreshing(false);
                    }
                }
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        newSwipe.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        newSwipe.setEnabled(true);
    }
}
