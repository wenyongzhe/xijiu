package com.hhxh.xijiu.main.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.CustomDialog;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.login.view.LoginActivity;
import com.hhxh.xijiu.main.Api.AppUpdateApi;
import com.hhxh.xijiu.system.MyApplication;
import com.hhxh.xijiu.update.Config;
import com.hhxh.xijiu.update.T_VersionInfo;
import com.hhxh.xijiu.utils.AlertUtil;
import com.hhxh.xijiu.utils.DensityUtil;
import com.hhxh.xijiu.utils.FileUtil;
import com.hhxh.xijiu.utils.HhxhLog;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.MyOpenDialog;
import com.hhxh.xijiu.utils.NetworkUtility;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.VersionUtil;
import com.hhxh.xijiu.utils.ZipCompressUtility;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.hhxh.xijiu.update.Config.updateFile;

/**
 * 侧滑菜单
 *
 * @auth lijq
 * @date 2016/10/19
 */
public class MainLeftFragment extends BaseFragment implements View.OnClickListener {
    //apK下载地址
    private String downloadUrl;
    //版本
    private LinearLayout versionLayout;
    private List<T_VersionInfo> versionInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_left_frame, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView exitText = (TextView) view.findViewById(R.id.exitText);
        TextView modifyPswText = (TextView) view.findViewById(R.id.modifyPswText);
        TextView telephoneText = (TextView) view.findViewById(R.id.telephoneText);
        TextView versionText = (TextView) view.findViewById(R.id.versionText);
        TextView deliveryOrderText = (TextView) view.findViewById(R.id.deliveryOrderText);
        TextView salesmanOrderText = (TextView) view.findViewById(R.id.salesmanOrderText);

        versionLayout = (LinearLayout) view.findViewById(R.id.versionLayout);
        telephoneText.setText(UserPrefs.getUserName());
        versionText.setText(VersionUtil.getAppVersionName(mContext));
        setDrawable(versionText);
        exitText.setOnClickListener(this);
        modifyPswText.setOnClickListener(this);
        if (UserPrefs.getUserIsDeliveryPart() && UserPrefs.getUserIsSalesmanPart()) {
            deliveryOrderText.setVisibility(View.VISIBLE);
            deliveryOrderText.setOnClickListener(this);
            salesmanOrderText.setVisibility(View.VISIBLE);
            salesmanOrderText.setOnClickListener(this);
        } else {
            deliveryOrderText.setVisibility(View.GONE);
            salesmanOrderText.setVisibility(View.GONE);
        }
        versionLayout.setOnClickListener(this);

    }

    /**
     * 设置版本右边的小红点
     * @param text
     */
    private void setDrawable(TextView text) {
        if (VersionUtil.getAppVersionName(mContext).equals(UserPrefs.getServerAppVerision())) {
            text.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_red_dot);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            text.setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitText://退出
                OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, getString(R.string.exit_login),
                        getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                UserPrefs.clearUser();
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.modifyPswText://修改密码
                Intent intent = new Intent(mContext, ModifyPswActivity.class);
                startActivity(intent);
                break;
            case R.id.versionLayout://检查版本更新
                check_update();
                //appUpdate();
                break;
            case R.id.deliveryOrderText://配送订单
                ((MainActivity) getActivity()).initByPart(0);
                ((MainActivity) getActivity()).closeDrawerLayout();
                break;
            case R.id.salesmanOrderText://销售订单
                ((MainActivity) getActivity()).initByPart(1);
                ((MainActivity) getActivity()).closeDrawerLayout();
                break;
        }
    }

    @Override
    public void doWriteStorage() {
        AppUpdateApi.downloadApk(mContext, downloadUrl);
    }


    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    //openLoginActivity();
                    break;
                case 4:
                    startUpdateVersion();
                    break;
                case 5:
                    check_update();
                    break;
                case 6:
                    MyOpenDialog.showOneBtnListenerDialog(mContext,
                            getResources().getString(R.string.dialog_title),
                            "已经是最新版本！",
                            getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    //openLoginActivity();
                    break;
                case 7:
                    getActivity().finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void startUpdateVersion() {
        try {

            final String serverFullPath = versionInfoList.get(versionInfoList
                    .size() - 1).CPath;
            String FileName = serverFullPath.substring(serverFullPath
                    .lastIndexOf("\\") + 1);
            final String downFileFullName = updateFile + FileName;
            FileUtil.Delete(downFileFullName);

            // 下载更新文件
            String pathName = updateFile + Config.appApkName;
            // 下载文件前先删除
            FileUtil.Delete(pathName);
            final File file = new File(downFileFullName);
            new AsyncTask<Void, Integer, Object>() {
//                ProgressDialog dialog = AlertUtil.showNoButtonProgressDialog(
//                        getActivity(), "正在下载版本，请稍候");
                //CustomDialog mmCustomDialog = MyOpenDialog.showNoBtnDialog(getActivity(),"提示","正在下载版本，请稍候");

                //显示下载进度
                final Dialog myDialog = new Dialog(getActivity(), R.style.Base_Theme_AppCompat_Dialog);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.progress_dialog, null);
                final TextView titleText = (TextView) view.findViewById(R.id.dialogTitleText);
                final TextView progressText = (TextView) view.findViewById(R.id.progressText);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    myDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    WindowManager.LayoutParams lp = myDialog.getWindow().getAttributes();
                    myDialog.getWindow().setBackgroundDrawable(null);
                    lp.width = DensityUtil.getInstance().getScreenWidth(getActivity()) * 4 / 5;
                    lp.gravity = Gravity.CENTER;
                    myDialog.show();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    titleText.setText(getActivity().getString(R.string.downloading));
                    progressText.setText((int) (values[0]) + "%");
                    progressBar.setProgress((int) (values[0]));
                    super.onProgressUpdate(values);
                }

                @Override
                protected Object doInBackground(Void... params) {

                    try {
                        if (isCancelled())
                            return null;
                        long IstartPost = 0;
                        String result;
                        long total = Long.parseLong(NetworkUtility
                                .GetUpdateFileLenth(serverFullPath));
                        long myTotal = Long.parseLong(NetworkUtility
                                .GetUpdateFileLenth(serverFullPath));
                        publishProgress(0);
                        while (total > 0) {
                            byte[] buffer = NetworkUtility
                                    .LoadFileByBlock(IstartPost,
                                            serverFullPath);
                            if (buffer == null) {
                                throw new IOException("联网超时");
                            }
                            FileUtil.writeFile(file, buffer, 0, buffer.length);
                            IstartPost += buffer.length;
                            total -= buffer.length;
                            if(total!=0){
                                publishProgress(100-(int) (((float) total /(float)  myTotal) * 100));
                            }
                        }
                        if (total == 0) {
                            publishProgress(100);

                            FileUtil.Delete(updateFile
                                    + Config.appApkName);
                            FileUtil.Delete(updateFile + "update.xml");
                            ZipCompressUtility.UnZipFile(downFileFullName, updateFile.substring(0,
                                    updateFile.length() - 1));
                            result = "true";
                        } else {
                            result = "false";
                        }
                        return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e;
                    }
                }

                @Override
                protected void onPostExecute(Object result) {
                    myDialog.dismiss();
                    if (result instanceof IOException) {
                        AlertUtil.showToast("网络连接失败，您可继续尝试上报",
                                getActivity());
                        mHandler.sendEmptyMessage(6);
                    } else if (result instanceof Exception) {
                        AlertUtil.showToast(
                                "出错了:" + ((Exception) result).getMessage(),
                                getActivity());
                        mHandler.sendEmptyMessage(6);
                    } else {
                        if (result.toString().equals("true")) {
                            // 提示安装
                            File apkfile = new File(updateFile,
                                    Config.appApkName);
                            if (!apkfile.exists()) {
                                return;
                            }
                            // 通过Intent安装APK文件
                            Intent i = new Intent(Intent.ACTION_VIEW);

                            i.setDataAndType(
                                    Uri.parse("file://" + apkfile.toString()),
                                    "application/vnd.android.package-archive");
                            getActivity().startActivity(i);
                            getActivity().finish();
                        } else if (result.toString().equals("false")) {
                            AlertUtil.showToast("下载失败", getActivity());
                            mHandler.sendEmptyMessage(6);
                        } else {
                            AlertUtil.showToast("解析服务端应答消息出错",
                                    getActivity());
                            mHandler.sendEmptyMessage(6);
                        }
                    }
                }

                @Override
                protected void onCancelled() {
                    myDialog.dismiss();
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check_update()
    {
        String pathName = updateFile + "update.xml";
        final File file = new File(pathName);

        new AsyncTask<Void, Void, Object>()
        {
            CustomDialog mmCustomDialog = MyOpenDialog.showNoBtnDialog(getActivity(),"提示","正在检查版本，请稍候");

//            ProgressDialog dialog = AlertUtil.showNoButtonProgressDialog(
//                    getActivity(), "正在检查版本，请稍候");

            @Override
            protected Object doInBackground(Void... params) {

                try {
                    // String retStr=NetworkUtility.getSomeInfo();
                    String result = NetworkUtility
                            .JsonVersionGetInfoOfLater(
                                    Config.ProductNum,
                                    GetAppVersion(getActivity()));
                    if (TextUtils.isEmpty(result)) {
                        throw new IOException("联网超时");
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                mmCustomDialog.dismiss();
                if (result instanceof IOException) {
                    AlertUtil.showToast("网络连接失败，您可继续尝试上报",
                            getActivity());
                    mHandler.sendEmptyMessage(6);
                } else if (result instanceof Exception) {
                    AlertUtil.showToast("更新版本出现异常！", getActivity());
                    mHandler.sendEmptyMessage(6);
                } else {
                    if (result.toString().equals("latest")){
                        File apkfile = new File(updateFile,
                                Config.appApkName);
                        if (apkfile.exists()) {
                            apkfile.delete();
                        }
                        mHandler.sendEmptyMessage(6);
                        return;
                    }

                    JSONObject jsonstr;
                    try {
                        jsonstr = new JSONObject(result.toString());
                        String versionInfoStr = jsonstr
                                .getString("T_VersionInfo");
                        if (!TextUtils.isEmpty(versionInfoStr))
                        {
                            JSONArray data = new JSONArray(versionInfoStr);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jobj = data.getJSONObject(i);
                                T_VersionInfo versionInfo = JSON.parseObject(
                                        jobj.toString(), T_VersionInfo.class);
                                if (versionInfo != null)
                                    versionInfoList.add(versionInfo);
                            }

                            if (versionInfoList.size() <= 0) {
                                File apkFile = new File(updateFile,
                                        Config.appApkName);
                                if (apkFile.exists()) {
                                    if(apkFile.delete()) {
                                        mHandler.sendEmptyMessage(6);
                                        return;
                                    }
                                }
                                mHandler.sendEmptyMessage(6);
                                return;
                            }
                            else
                            {

                                MyOpenDialog.showTwoBtnListenerDialog(mContext,
                                        getResources().getString(R.string.dialog_title),
                                        "系统需要更新，是否现在更新？",
                                        getString(R.string.update),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                file.delete();
                                                mHandler.sendEmptyMessage(4);
                                            }
                                        }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });


//                                AlertUtil.showAlert(getActivity(), R.string.dialog_title, "系统需要更新，是否现在更新？", R.string.ok,
//                                        new View.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    View v) {
//                                                // TODO
//                                                AlertUtil.dismissDialog();
//                                                file.delete();
//                                                mHandler.sendEmptyMessage(4);
//                                            }
//                                        },
//                                        R.string.cancel,
//                                        new View.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    View v) {
//                                                // TODO
//                                                // Auto-generatedmethodstub
//                                                AlertUtil.dismissDialog();
//                                                mHandler.sendEmptyMessage(6);
//                                            }
//                                        });
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        AlertUtil.showToast("解析服务端应答消息出错",
                                getActivity());
                        mHandler.sendEmptyMessage(6);
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();
    }

    public String GetAppVersion(Context context){
        PackageInfo packinfo;
        String version="";
        try {
            packinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionname = packinfo.versionName;
            version = versionname;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            version="1.0";
        }
        return version;
    }
}
