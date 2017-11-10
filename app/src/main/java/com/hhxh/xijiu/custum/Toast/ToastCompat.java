package com.hhxh.xijiu.custum.Toast;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.hhxh.xijiu.utils.HhxhLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ToastCompat 当关掉消息权限后弹出MyToast 否则弹出系统自带toast
 *
 * @author ljq
 * @time 2016-9-5 下午4:43:49
 */
public class ToastCompat implements IToast {
    private IToast mIToast;

    private ToastCompat(Context context, String text, int duration) {
        //权限不可用是弹自定义Toast，否则弹系统的
        if (!isNotificationEnabled(context)) {
            HhxhLog.i("CustomToast");
            mIToast = MyToast.makeText(context, text, duration);
        } else {
            HhxhLog.i("SystemToast");
            mIToast = SystemToast.makeText(context, text, duration);
        }
    }

    public static IToast makeText(Context context, String text, int duration) {
        return new ToastCompat(context, text, duration);
    }

    /**
     * 检查消息通知栏权限是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                ApplicationInfo appInfo = context.getApplicationInfo();

                String pkg = context.getApplicationContext().getPackageName();
                int uid = appInfo.uid;
                Class appOpsClass = Class.forName(AppOpsManager.class.getName());

                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public IToast setGravity(int gravity, int xOffset, int yOffset) {
        return mIToast.setGravity(gravity, xOffset, yOffset);
    }

    @Override
    public IToast setDuration(long durationMillis) {
        return mIToast.setDuration(durationMillis);
    }

    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        return mIToast.setMargin(horizontalMargin, verticalMargin);
    }

    @Override
    public IToast setText(String text) {
        return mIToast.setText(text);
    }

    @Override
    public void show() {
        mIToast.show();
    }

    @Override
    public void cancel() {
        mIToast.cancel();
    }
}
