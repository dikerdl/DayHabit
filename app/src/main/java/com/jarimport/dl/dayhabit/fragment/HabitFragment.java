package com.jarimport.dl.dayhabit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.UpLoadActivity;
import com.jarimport.dl.dayhabit.adapter.BaseRecyclerAdapter;
import com.jarimport.dl.dayhabit.adapter.RecyclerViewHolder;
import com.jarimport.dl.dayhabit.application.MyApplication;
import com.jarimport.dl.dayhabit.entity.DaoSession;
import com.jarimport.dl.dayhabit.entity.Habit;
import com.jarimport.dl.dayhabit.utils.DataUtils;
import com.jarimport.dl.dayhabit.utils.ToastUtil;
import com.jarimport.dl.dayhabit.view.MyToolbar;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 自定义生活习惯
 */
public class HabitFragment extends BaseFragment {

    private RecyclerView mHabitListView;
    private ImageButton mImageButton;
    private DaoSession daoSession;
    private BaseRecyclerAdapter adapter;
    private List<Habit> habits1 = new ArrayList<>();
    private MyToolbar mToolbar;
    private View view;
    private LinearLayout mWriteHabit;
    private EditText mHabitInput;
    private TextView mHabitAdd;
    private ProgressBar progressBar2;
    private List<Integer> pics = new ArrayList<>();

    public HabitFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_habit, container, false);
        initView(view);
        return view;
    }

    private int clickTimes = 0;
private boolean isRepeatAdd = false;
    private void initView(View view) {
        mHabitListView = (RecyclerView) view.findViewById(R.id.mHabitSelf);
        //设置布局管理器
        mHabitListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        mHabitListView.setItemAnimator(new DefaultItemAnimator());

        mImageButton = (ImageButton) view.findViewById(R.id.floatButton);
        mToolbar = (MyToolbar) view.findViewById(R.id.mToolbar);
        daoSession = MyApplication.getDaoSession();
        mWriteHabit = (LinearLayout) view.findViewById(R.id.mWriteHabit);
        mHabitInput = (EditText) view.findViewById(R.id.mHabitInput);
        mHabitAdd = (TextView) view.findViewById(R.id.mHabitAdd);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbar.setTitle("");
                mHabitInput.setText("");
                mImageButton.setVisibility(View.GONE);
                mWriteHabit.setVisibility(View.VISIBLE);
            }
        });
        mHabitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbar.setTitle("天天习惯");
                String text = mHabitInput.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showToast(getActivity(), "习惯不能为空");
                    mImageButton.setVisibility(View.VISIBLE);
                    mWriteHabit.setVisibility(View.GONE);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String addTime = dateFormat.format(new Date());
                    Habit haa = new Habit(null, "" + text, addTime);
                    try {
                        for (int i=0;i<habits1.size();i++) {
                            if(habits1.get(i).getHabitInfo().equals(text)){
                                isRepeatAdd = true;
                            }else{
                                if(!habits1.get(i).getAddTime().equals(addTime)){
                                    if(i==habits1.size()-1){
                                        DataUtils.insertData(haa);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        ToastUtil.showToast(getActivity(), "insert failed");
                    }

                    Log.e("HabitFragment", "-----here for adding habit!");

                    mImageButton.setVisibility(View.VISIBLE);
                    mWriteHabit.setVisibility(View.GONE);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("HabitFragment", "-----habit size:" + habits1.size());
//                    adapter.notifyDataSetChanged();
                    //刷新是使用notifyItemInserted(position)与notifyItemRemoved(position)
                    //否则没有动画效果。
                    if(!isRepeatAdd){
                        adapter.add(0, haa);
                        ToastUtil.showToast(getActivity(), "习惯添加成功！");
                    }else{
                        ToastUtil.showToast(getActivity(), "该习惯已存在不能重复插入！");
                        isRepeatAdd=false;
                    }

                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (habits1.size() > 0) {
            habits1.clear();
        }
        //查询GreenDao数据库
        habits1.addAll(DataUtils.getHabitData());
        Log.e("HabitFragment", "-----habit size:" + habits1.size());

        initRandomPics();

        adapter = new BaseRecyclerAdapter<Habit>(getActivity(), habits1) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.habit_listview_item;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, Habit item) {
                //调用holder.getView(),getXXX()方法根据id得到控件实例，进行数据绑定即可
                TextView textView = holder.getTextView(R.id.mHabit);
                TextView mHabitTime = holder.getTextView(R.id.mHabitTime);
                ImageView mHabitPic = holder.getImageView(R.id.mHabitPic);

                mHabitPic.setImageResource(pics.get(new Random().nextInt(12)));

                textView.setText(item.getHabitInfo());
                mHabitTime.setText(item.getAddTime());
            }
        };
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.e("habitFragnent", "---第" + pos + "项被点击");
                Intent intent = new Intent(getActivity(), UpLoadActivity.class);
                intent.putExtra("position", pos);
                Habit view = (Habit) habits1.get(pos);
                intent.putExtra("habitInfo", view.getHabitInfo());
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int pos) {

                //GreenDao数据库删除Habit表中的该项
                Habit hab = (Habit) habits1.get(pos);
                DataUtils.deleteData(hab);
                //重新获取habit
                habits1.clear();
                habits1.addAll(DataUtils.getHabitData());
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ToastUtil.showToast(getActivity(), "删除第" + (pos + 1) + "项成功！");
                adapter.notifyItemRemoved(pos);

            }
        });
        mHabitListView.setAdapter(adapter);
    }

    private void initRandomPics() {
        pics.add(R.mipmap.pic01);
        pics.add(R.mipmap.pic05);
        pics.add(R.mipmap.pic07);
        pics.add(R.mipmap.pic09);
        pics.add(R.mipmap.pic11);
        pics.add(R.mipmap.pic12);
        pics.add(R.mipmap.pic16);
        pics.add(R.mipmap.pic19);
        pics.add(R.mipmap.pic20);
        pics.add(R.mipmap.pic21);
        pics.add(R.mipmap.pic22);
        pics.add(R.mipmap.pic23);
    }

    @Override
    public View initView() {
        return view;
    }

    @Override
    public void initData() {

    }

}