package com.hhxh.xijiu.custum.Toast;

/**
 * 自定义Toast 防止没有消息权限后无法弹出Toast
 *
 * @author ljq
 * @time 2016-8-30 下午4:43:49
 */
public interface IToast {

    IToast setGravity(int gravity, int xOffset, int yOffset);

    IToast setDuration(long durationMillis);


    IToast setMargin(float horizontalMargin, float verticalMargin);

    IToast setText(String text);

    void show();

    void cancel();
}
