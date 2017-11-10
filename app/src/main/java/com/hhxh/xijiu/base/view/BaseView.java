package com.hhxh.xijiu.base.view;

import android.content.Context;

/**
 * @auth lijq
 * @date 2016/9/8
 */
public interface BaseView {
    /**开始获取数据*/
    void showLoading(String content);
    /**获取数据完毕*/
    void hideLoading();

    /**
     * 获取数据成功
     * @param data  返回数据
     */
    void loadSuccess(Object data);
    /**
     * 获取数据失败
     * @param data  返回数据
     */
    void loadFail(Object data);

    Context getContext();
}
