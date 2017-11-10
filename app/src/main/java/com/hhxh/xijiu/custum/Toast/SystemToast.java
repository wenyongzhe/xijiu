package com.hhxh.xijiu.custum.Toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.hhxh.xijiu.utils.DensityUtil;

/**
 * 系统自带Toast
 *
 * @author ljq
 * @time 2016-8-30 下午4:43:49
 */
public class SystemToast implements IToast {
    private Toast mToast;

    private Context mContext;

    public static IToast makeText(Context context, String text, long duration) {
        return new SystemToast(context)
                .setText(text)
                .setDuration(duration)
                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM ,0, DensityUtil.getInstance().dipToPixels(context,64));
    }

    public SystemToast(Context context) {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public IToast setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
        return this;
    }

    @Override
    public IToast setDuration(long durationMillis) {
        mToast.setDuration((int) durationMillis);
        return this;
    }


    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
        return this;
    }

    /**
     *
     * @param text 传入字符串
     *
     * @return 自身对象
     */
    @Override
    public IToast setText(String text) {
        mToast.setText(text);
        return this;
    }

    @Override
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    @Override
    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
