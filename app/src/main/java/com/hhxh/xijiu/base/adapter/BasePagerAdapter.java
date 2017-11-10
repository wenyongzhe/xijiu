package com.hhxh.xijiu.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * viewpager+fragment  Adapter
 * @auth lijq
 * @date 2016-9-2
 */
public class BasePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentsList;
    private int mChildCount;

    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public void notifyDataSetChanged() {
        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
        mChildCount = getCount();
        super.notifyDataSetChanged();

    }

    @Override
    public int getItemPosition(Object object) {
        if ( mChildCount > 0) {
            // 这里利用判断执行若干次不缓存，刷新
            mChildCount --;
            // 返回这个是itemPOSITION_NONE
            return POSITION_NONE;
        }
        // 这个则是POSITION_UNCHANGED
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
