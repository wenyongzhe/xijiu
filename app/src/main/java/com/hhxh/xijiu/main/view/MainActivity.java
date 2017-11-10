package com.hhxh.xijiu.main.view;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BasePagerAdapter;
import com.hhxh.xijiu.base.view.BaseActivity;
import com.hhxh.xijiu.base.view.BaseFragment;
import com.hhxh.xijiu.base.view.BaseSearchFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.CustomDialog;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.login.view.LoginActivity;
import com.hhxh.xijiu.system.MyApplication;
import com.hhxh.xijiu.update.Config;
import com.hhxh.xijiu.update.T_VersionInfo;
import com.hhxh.xijiu.utils.AlertUtil;
import com.hhxh.xijiu.utils.DensityUtil;
import com.hhxh.xijiu.utils.FileUtil;
import com.hhxh.xijiu.utils.HhxhLog;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.KeyBoardUtil;
import com.hhxh.xijiu.utils.MyOpenDialog;
import com.hhxh.xijiu.utils.NetUtil;
import com.hhxh.xijiu.utils.NetworkUtility;
import com.hhxh.xijiu.utils.OpenDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

import static com.hhxh.xijiu.update.Config.updateFile;

/**
 * 主界面
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    /**
     * 搜索
     */
    @BindView(R.id.searchEdit)
    EditText searchEdit;
    /**
     * 未完成
     */
    @BindView(R.id.notCompleteText)
    TextView notCompleteText;
    /**
     * 已完成
     */
    @BindView(R.id.hasCompletedText)
    TextView hasCompletedText;
    /**
     * 页卡游标
     */
    @BindView(R.id.tabCursorView)
    View tabCursorView;
    /**
     * viewPager
     */
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    /**
     * 主布局
     */
    @BindView(R.id.mainRtLatout)
    RelativeLayout mMainRtLatout;
    /**
     * 侧滑布局
     */
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    /**
     * 搜索布局
     */
    @BindView(R.id.searchRtLayout)
    RelativeLayout searchRtLayout;
    /**
     * 标题栏
     */
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;

    /**
     * 当前页下标
     */
    private int curPagerIndex = -1;

    /**
     * list
     */
    private List<Fragment> fragmentList;

    private AlarmManager alarmManager;
    private PendingIntent pi;
    private MyReciver myReciver;
    private BasePagerAdapter adapter;
    private FragmentManager fm;

    private List<T_VersionInfo> versionInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_activity);
        ButterKnife.bind(this);
        initView();
        initAlarm();

        if ( NetUtil.getInstance().isConnectingToInternet(this))
        {
            check_update();
        }

    }

    @Override
    protected void initTitle(String str) {
        super.initTitle(str);
        titleLeftText.setVisibility(View.INVISIBLE);
        titleLeftImg.setVisibility(View.VISIBLE);
        titleLeftImg.setImageResource(R.drawable.ic_left);
//        titleRightImg.setVisibility(View.VISIBLE);
//        titleRightImg.setImageResource(R.drawable.ic_search);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                BaseSearchFragment currentFragment = (BaseSearchFragment) fragmentList.get(curPagerIndex);
                currentFragment.search(s.toString());
            }
        });

    }

    private void initDrawerLayout() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                HhxhLog.i("onDrawerSlide");
                if (drawerView.getTag().equals("LEFT")) {
                    mMainRtLatout.setTranslationX(DensityUtil.getInstance().dipToPixels(mContext, 250) * slideOffset);
                    mMainRtLatout.invalidate();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                titleLeftImg.setSelected(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                titleLeftImg.setSelected(false);
                HhxhLog.i("onDrawerClosed");
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    private void initView() {
        initTitle(getString(R.string.deliver_goods_order));
        hasCompletedText = (TextView) findViewById(R.id.hasCompletedText);
        notCompleteText = (TextView) findViewById(R.id.notCompleteText);
        tabCursorView = findViewById(R.id.tabCursorView);

        mMainRtLatout.addOnLayoutChangeListener(this);

        ViewGroup.LayoutParams params = tabCursorView.getLayoutParams();
        params.width = DensityUtil.getInstance().getScreenWidth(mContext) / 2;
        tabCursorView.setLayoutParams(params);

        hasCompletedText.setOnClickListener(this);
        notCompleteText.setOnClickListener(this);
        initViewPager();
        initDrawerLayout();
    }

    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fm = getSupportFragmentManager();
        adapter = new BasePagerAdapter(fm, fragmentList);
        viewPager.setAdapter(adapter);
        initByPart(UserPrefs.getUserIsDeliveryPart() ? 0 : 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int tabCursorWidth = tabCursorView.getWidth();
                Animation animation = new TranslateAnimation(tabCursorWidth * curPagerIndex, position * tabCursorWidth, 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                tabCursorView.startAnimation(animation);
                setFocus(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFocus(0, false);
    }

    /**
     * 根据用户角色进行初始化
     *
     * @param part 0：配送员 1：业务员
     */
    public void initByPart(int part) {
        HhxhLog.i("initByPart" + part);
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragmentList) {
                ft.remove(fragment);
            }
            ft.commit();
        }
        fragmentList.clear();
        switch (part) {
            case 0://配送员
                titleText.setText(getString(R.string.delivery_orders));
                hasCompletedText.setText(getString(R.string.has_completed));
                notCompleteText.setText(getString(R.string.not_complete));
                OrderNotCompletedFragment notCompletedFragment = new OrderNotCompletedFragment();
                OrderHasCompletedFragment hasCompletedFragment = new OrderHasCompletedFragment();
                fragmentList.add(notCompletedFragment);
                fragmentList.add(hasCompletedFragment);
                break;
            case 1://业务员
                titleText.setText(getString(R.string.salesman_orders));
                hasCompletedText.setText(getString(R.string.has_checked));
                notCompleteText.setText(getString(R.string.not_checked));
                NotCheckedFragment saNotCompletedFragment = new NotCheckedFragment();
                HasCheckedFragment sahasCompletedFragment = new HasCheckedFragment();
                fragmentList.add(saNotCompletedFragment);
                fragmentList.add(sahasCompletedFragment);
                break;
        }
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置当前页
     *
     * @param position
     * @param isTranslation
     */
    private void setFocus(int position, boolean isTranslation) {
        if (curPagerIndex == position) {
            return;
        }
        curPagerIndex = position;
        if (curPagerIndex == 0) {
            notCompleteText.setSelected(true);
            hasCompletedText.setSelected(false);
        } else {
            notCompleteText.setSelected(false);
            hasCompletedText.setSelected(true);
        }
        viewPager.setCurrentItem(curPagerIndex, isTranslation);
    }

    /**
     * 初始化定时任务
     */
    private void initAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction("com.hhxh.xijiu.action.reconnection");
        pi = PendingIntent.getBroadcast(this, 0x11, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //14分钟
        long millis = 14 * 6 * 1000;
        //开始时间
        long firstime = SystemClock.elapsedRealtime() + millis;
        //每隔14分钟发一次广播
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstime, millis, pi);
        myReciver = new MyReciver();
        IntentFilter filter = new IntentFilter("com.hhxh.xijiu.action.reconnection");
        registerReceiver(myReciver, filter);
    }

    /**
     * 取消定时任务
     */
    private void cancelAlarm() {
        alarmManager.cancel(pi);
        unregisterReceiver(myReciver);
    }

    public void closeDrawerLayout() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    @OnClick({R.id.notCompleteText, R.id.hasCompletedText, R.id.titleLeftImg, R.id.titleRightImg,
            R.id.cancelSearchImg, R.id.cancelSearchText})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLeftImg://侧滑
                if (!titleLeftImg.isSelected()) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case R.id.notCompleteText://未完成
                setFocus(0, false);
                break;
            case R.id.hasCompletedText://已完成
                setFocus(1, false);
                break;
            case R.id.titleRightImg://搜索
                isInSearch(true);
                break;
            case R.id.cancelSearchImg://删除搜索
                searchEdit.setText("");
                break;
            case R.id.cancelSearchText://取消搜索
                isInSearch(false);
                break;
        }
    }

    /***
     * 是否正处于搜索状态
     *
     * @param isInSearch
     */
    private void isInSearch(boolean isInSearch) {
        if (isInSearch) {
            searchRtLayout.setVisibility(View.VISIBLE);
            titleLayout.setVisibility(View.GONE);
            searchEdit.requestFocus();
            KeyBoardUtil.showKeyboard(searchEdit);
        } else {
            searchRtLayout.setVisibility(View.GONE);
            titleLayout.setVisibility(View.VISIBLE);
            searchEdit.clearFocus();
            KeyBoardUtil.hideKeyboard(searchEdit);
        }
    }


    /**
     * 返回键(最小化，不退出)
     */
    @Override
    public void onBackPressed() {
        if (titleLeftImg.isSelected()) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            int currentVersion = Build.VERSION.SDK_INT;
            if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            } else {// android2.1
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.restartPackage(getPackageName());
            }
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        int keyHeight = DensityUtil.getInstance().getScreenHeight(mContext) / 3;
        HhxhLog.i("keyHeight:" + (bottom - oldBottom));
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//软键盘弹起

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//软键盘隐藏
            isInSearch(false);
        }
    }

    /***
     * 设置是否需要刷新
     *
     * @param i
     */
    public void setIsNeedUpdata(int i) {
        BaseFragment fragment = (BaseFragment) fragmentList.get(i);
        if (fragment != null) {
            fragment.setNeedUpdata(true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment=fragmentList.get(curPagerIndex);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 用来定时更换session
     */
    private class MyReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            HhxhLog.i("onReceive");
            OkHttpUtils.get(Constant.LOGIN_URL)
                    .params("u", UserPrefs.getUserAccount())
                    .params("p", UserPrefs.getUserPwd())
                    .execute(new StringCallback() {
                        @Override
                        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                            try {
                                if (response != null && 200 == response.code()) {
                                    JSONObject obj = new JSONObject(s);
                                    if (!JsonUtils.isExistObj(obj, "#message")) {
                                        String token = obj.optString("a");
                                        if (!TextUtils.isEmpty(token)) {
                                            UserPrefs.setToken(token);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    @Override
    protected void onDestroy() {
        cancelAlarm();
        super.onDestroy();
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
                    //openLoginActivity();
                    break;
                case 7:
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     *显示更新dialog
     * @param title
     * @param content
     */
    public void showUpdateDialog(String title, String content) {
        OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, title, content, getString(R.string.update),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!FileUtil.isExternalStorageWritable()) {
                            showShortToast(getString(R.string.new_no_found_sd));
                            return;
                        }
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Constant.STORAGE_REQUESETCODE, getString(R.string.requeset_storage_permission));
                    }
                }, getString(R.string.exit_new), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MyApplication.getInstance().quitApp();
                    }
                });
    }

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
//                        MainActivity.this, "正在下载版本，请稍候");
            //CustomDialog mmCustomDialog = MyOpenDialog.showNoBtnDialog(MainActivity.this,"提示","正在下载版本，请稍候");

                //显示下载进度
                final Dialog myDialog = new Dialog(MainActivity.this, R.style.Base_Theme_AppCompat_Dialog);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
                    lp.width = DensityUtil.getInstance().getScreenWidth(MainActivity.this) * 4 / 5;
                    lp.gravity = Gravity.CENTER;
                    myDialog.show();
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
                protected void onProgressUpdate(Integer... values) {
                    titleText.setText(MainActivity.this.getString(R.string.downloading));
                    progressText.setText((int) (values[0]) + "%");
                    progressBar.setProgress((int) (values[0]));
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(Object result) {
                    myDialog.dismiss();
                    if (result instanceof IOException) {
                        AlertUtil.showToast("网络连接失败，您可继续尝试上报",
                                MainActivity.this);
                        mHandler.sendEmptyMessage(6);
                    } else if (result instanceof Exception) {
                        AlertUtil.showToast(
                                "出错了:" + ((Exception) result).getMessage(),
                                MainActivity.this);
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
                            MainActivity.this.startActivity(i);
                            finish();
                        } else if (result.toString().equals("false")) {
                            AlertUtil.showToast("下载失败", MainActivity.this);
                            mHandler.sendEmptyMessage(6);
                        } else {
                            AlertUtil.showToast("解析服务端应答消息出错",
                                    MainActivity.this);
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

//            ProgressDialog dialog = AlertUtil.showNoButtonProgressDialog(
//                    MainActivity.this, "正在检查版本，请稍候");

            CustomDialog mmCustomDialog = MyOpenDialog.showDialog(MainActivity.this,"正在检查版本，请稍候");

            @Override
            protected Object doInBackground(Void... params) {

                try {
                    // String retStr=NetworkUtility.getSomeInfo();
                    String result = NetworkUtility
                            .JsonVersionGetInfoOfLater(
                                    Config.ProductNum,
                                    GetAppVersion(MainActivity.this));
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
                            MainActivity.this);
                    mHandler.sendEmptyMessage(6);
                } else if (result instanceof Exception) {
                    AlertUtil.showToast("更新版本出现异常！", MainActivity.this);
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
                                                mHandler.sendEmptyMessage(6);
                                            }
                                        });



//                                AlertUtil.showAlert(MainActivity.this, R.string.dialog_title, "系统需要更新，是否现在更新？", R.string.ok,
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
                                MainActivity.this);
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
