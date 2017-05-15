package com.jarimport.dl.dayhabit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarimport.dl.dayhabit.MainActivity;
import com.jarimport.dl.dayhabit.R;
import com.jarimport.dl.dayhabit.utils.FragmentUtil;
import com.jarimport.dl.dayhabit.view.BanViewPager;

import java.util.ArrayList;


public class ShowFragment extends BaseFragment {
    private TabLayout mTabLayout;
    private ViewPager mShowBvp;
    private ArrayList<Fragment> showFragments = null;
    private MyShowAdapter myShowAdapter;
    private String[] titles = {"热门", "关注", "最新"};
    private MainActivity activity;
    private View view;

    public ShowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity)getActivity();
        view = inflater.inflate(R.layout.fragment_show, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mShowBvp = (ViewPager) view.findViewById(R.id.showBvp);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        setAdapter();
    }

    @Override
    public View initView() {
        return view;
    }

    @Override
    public void initData() {

    }

    private void setAdapter() {
        myShowAdapter = new MyShowAdapter(getActivity(), getChildFragmentManager());
        mShowBvp.setAdapter(myShowAdapter);
        //绑定TabLyout和ViewPager
        mTabLayout.setupWithViewPager(mShowBvp);
    }

    private void getData() {
        if(null==showFragments){
            showFragments = FragmentUtil.getShowFragments();
        }else{}

        Log.e("ShowFragment","-----"+showFragments.size());
    }
    private  class MyShowAdapter extends FragmentPagerAdapter{

        public MyShowAdapter(Context con, FragmentManager cfm) {
            super(cfm);
        }

            @Override
            public Fragment getItem(int position) {
                return showFragments.get(position);
            }

            @Override
            public int getCount() {
                return showFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
