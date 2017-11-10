package com.hhxh.xijiu.update;

import android.os.Environment;

/**
 * Created by Administrator on 2017/11/7.
 */

public class Config {

    //更新版本号标志号
    public static String ProductNum ="LS001\\XiJiu\\XiJiu002";
    //下载文件路径
    public static final String download_dir = "XiJiu";

    public static String updateFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
            + download_dir + "/Update/";

    //更新APK的文件名
    public static String appApkName="com.supoin.xijiu.apk";

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
