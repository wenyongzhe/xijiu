package com.hhxh.xijiu.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * 功能描述：
 *
 * @auth lijq
 * @date 2017/2/15
 */
public class ViewFocuseHelper {
    private ViewFocuseHelper(){}

    public static void requesetFocuseAndHideSoftInput(View v){
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                KeyBoardUtil.hideKeyboard(v);
                return false;
            }
        });
    }
}
