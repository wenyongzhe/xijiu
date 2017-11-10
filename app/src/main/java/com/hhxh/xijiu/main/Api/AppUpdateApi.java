package com.hhxh.xijiu.main.Api;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.Toast.ToastCompat;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.utils.DensityUtil;
import com.hhxh.xijiu.utils.FileUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述：APP更新
 *
 * @auth lijq
 * @date 2016/11/24
 */
public class AppUpdateApi {
    /***
     * 下载apk
     *
     * @param downloadUrl
     */
    public static void downloadApk(final Context context, String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl))
            return;
        String apkName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        final File file = new File(FileUtil.getSDCardPath() + Constant.HHXH_FILEDIR, apkName);
        if (file.exists()) {
            file.delete();
        } else {
            file.mkdir();
        }
        //显示下载进度
        final Dialog myDialog = new Dialog(context, R.style.Base_Theme_AppCompat_Dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        final TextView titleText = (TextView) view.findViewById(R.id.dialogTitleText);
        final TextView progressText = (TextView) view.findViewById(R.id.progressText);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        myDialog.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager.LayoutParams lp = myDialog.getWindow().getAttributes();
        myDialog.getWindow().setBackgroundDrawable(null);
        lp.width = DensityUtil.getInstance().getScreenWidth(context) * 4 / 5;
        lp.gravity = Gravity.CENTER;
        myDialog.show();
        OkHttpUtils.get(downloadUrl).execute(new FileCallback(FileUtil.getSDCardPath() + Constant.HHXH_FILEDIR, apkName) {
                    @Override
                    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {

                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        titleText.setText(context.getString(R.string.downloading));
                        progressText.setText((int) (100 * progress) + "%");
                        progressBar.setProgress((int) (100 * progress));
                        //下载完成
                        if (progress == 1) {
                            myDialog.dismiss();
                            installApk(context,file.getAbsolutePath());
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        ToastCompat.makeText(context,context.getString(R.string.download_fail), Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                    }
                });
    }

    /***
     * 安装APK
     *
     * @param filePath
     */
    public static void installApk(Context context,String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 是否需要弹出自动更新(本地保存的服务器版本总是保存最新的)
     *
     * @param serverAppVersion
     * @return
     */
    public static boolean isNeedUpdate(String serverAppVersion) {
        String recordVersion = UserPrefs.getServerAppVerision();
        if (TextUtils.equals(serverAppVersion, recordVersion)) {
            return false;
        } else {
            UserPrefs.setServerAppVerision(serverAppVersion);
            return true;
        }
    }
}
