package com.jarimport.dl.dayhabit.utils;

import android.support.v4.app.Fragment;

import com.jarimport.dl.dayhabit.fragment.HabitFragment;
import com.jarimport.dl.dayhabit.fragment.ShowFragment;
import com.jarimport.dl.dayhabit.fragment.TeFragment;
import com.jarimport.dl.dayhabit.fragment.UserFragment;
import com.jarimport.dl.dayhabit.fragment.show.CareShowFragment;
import com.jarimport.dl.dayhabit.fragment.show.HotShowFragment;
import com.jarimport.dl.dayhabit.fragment.show.NewShowFragment;

import java.util.ArrayList;

/**
 * @version V1.0 <描述当前版本功能>
 * @desc:
 * @author: myName
 * @date: 2017-04-24 14:48
 */

public class FragmentUtil {
    private static HabitFragment hf;
    private static ShowFragment sf;
    private  static TeFragment tf;
    private static UserFragment uf;

    private static HotShowFragment hsf;
    private static CareShowFragment csf;
    private static NewShowFragment nsf;

    static ArrayList<Fragment> fragments = new ArrayList<>();
    static ArrayList<Fragment> showFragments = new ArrayList<>();
    static{
        hf = new HabitFragment();
        sf = new ShowFragment();
        tf = new TeFragment();
        uf = new UserFragment();

        hsf = new HotShowFragment();
        csf = new CareShowFragment();
        nsf = new NewShowFragment();
    }
    public static ArrayList<Fragment> getFragments(){
        fragments.add(hf);
        fragments.add(sf);
        fragments.add(tf);
        fragments.add(uf);
        return  fragments;
    }
    public static ArrayList<Fragment> getShowFragments(){
        showFragments.add(hsf);
        showFragments.add(csf);
        showFragments.add(nsf);
        return  showFragments;
    }
}
