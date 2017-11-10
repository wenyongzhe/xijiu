package com.hhxh.xijiu.base.view;

import android.os.Bundle;

import com.hhxh.xijiu.base.presenter.BasePresenter;


/**
 * @auth lijq
 * @date 2016/9/13
 */
public abstract class BasePresenterActivity<V,T extends BasePresenter<V>> extends BaseActivity{
    protected T mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=initPresenter();
        mPresenter.attach((V)this);
    }
    protected abstract T initPresenter();

    @Override
    protected void onDestroy() {
        mPresenter.dettach();
        super.onDestroy();
    }

}
