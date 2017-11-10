package com.hhxh.xijiu.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.MyProgressDialog;
import com.hhxh.xijiu.custum.Toast.ToastCompat;
import com.hhxh.xijiu.system.MyApplication;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.StringUtil;

/**
 * baseActivity
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 显示加载进度条
     */
    protected MyProgressDialog myProgressDialog;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 此基类实例
     */
    public Activity mActivity;
    /**
     * 头部textView控件
     */
    protected TextView titleText, titleLeftText, titleRightText;
    /**
     * 头部ImageView控件
     */
    protected ImageView titleLeftImg, titleRightImg, titleAddImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        MyApplication.getInstance().addActivity(this);
    }

    /**
     * 初始化标题栏
     *
     * @param str 标题
     */
    protected void initTitle(String str) {
        titleText = (TextView) findViewById(R.id.titleText);
        titleLeftText = (TextView) findViewById(R.id.titleLeftText);
        titleLeftImg = (ImageView) findViewById(R.id.titleLeftImg);
        titleRightImg = (ImageView) findViewById(R.id.titleRightImg);
        titleRightText = (TextView) findViewById(R.id.titleRightText);
        titleAddImg = (ImageView) findViewById(R.id.titleAddImg);
        titleText.setText(str);
        String returnName = getIntent().getStringExtra("returnName");
        if (!StringUtil.isEmpty(returnName)) {
            titleLeftText.setText(returnName);
        }
        titleLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }
        });
    }

    /**
     * 初始化蓝色标题栏
     *
     * @param str 标题
     */
    protected void initBlueTitle(String str) {
//        titleText = (TextView) findViewById(R.id.titleText);
//        titleLeftText = (TextView) findViewById(R.id.titleLeftText);
//        titleLeftImg = (ImageView) findViewById(R.id.titleLeftImg);
//        titleRightText = (TextView) findViewById(R.id.titleRightText);
//        titleRightImg = (ImageView) findViewById(R.id.titleRightImg);
//        titleText.setText(str);
//        String returnName = getIntent().getStringExtra("returnName");
//        if (!StringUtil.isEmpty(returnName)) {
//            titleLeftText.setText(returnName);
//        } else {
//            titleLeftText.setText(getString(R.string.back));
//        }
//        titleLeftText.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right);
//            }
//        });
//        titleLeftImg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right);
//            }
//        });
    }


    /**
     * 初始化更多功能标题栏
     *
     * @param titleName 标题
     */
    protected void initMoreFunctionTitle(String titleName) {
//        moreTitleName = (TextView) findViewById(R.id.moreTitleName);
//        moreTitleLeftText = (TextView) findViewById(R.id.moreTitleLeftText);
//        moreTitleLeftImage = (ImageView) findViewById(R.id.moreTitleLeftImage);
//        moreTitleTwoRight = (ImageView) findViewById(R.id.moreTitleTwoRight);
//        moreTitleOneRight = (ImageView) findViewById(R.id.moreTitleOneRight);
//        moreTitleName.setText(titleName);
//        String returnName = getIntent().getStringExtra("returnName");
//        if (!StringUtil.isEmpty(returnName)) {
//            moreTitleLeftText.setText(returnName);
//        }
//
//        moreTitleLeftImage.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right);
//            }
//        });
//        moreTitleLeftText.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right);
//            }
//        });
    }


    /**
     * 弹Toast
     *
     * @param text
     * @param duration
     */
    protected void showToast(String text, int duration) {
        ToastCompat.makeText(mContext, text, duration).show();
    }

    /**
     * 弹Toast
     *
     * @param text
     */
    protected void showShortToast(String text) {
        ToastCompat.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动进度条
     *
     * @param content 进度条上的加载提示
     */
    public void startProgressDialog(String content) {
        if (StringUtil.isEmpty(content))
            return;
        myProgressDialog = new MyProgressDialog.Builder(mContext).create();
        myProgressDialog.setMessage(content).show();
    }

    /**
     * 停止进度条
     */
    public void stopProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog.dismiss();
            myProgressDialog = null;
        }
    }

    /**
     * 检查权限
     */
    protected void checkPermission(final String permissionName, final int requestCode, String description) {
        if (TextUtils.isEmpty(permissionName) || TextUtils.isEmpty(description)) return;
        if (ActivityCompat.checkSelfPermission(mContext, permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionName)) {
                OpenDialog.getInstance().showOneBtnListenerDialog(mContext,null, description
                        , getString(R.string.confirm)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请权限
                                ActivityCompat.requestPermissions(mActivity, new String[]{permissionName},
                                        requestCode);
                            }
                        });
            } else {
                //申请权限
                ActivityCompat.requestPermissions(mActivity, new String[]{permissionName}, requestCode);
            }
        } else {
            checkSuccessAndDoNext(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//权限申请成功
            checkSuccessAndDoNext(requestCode);
        } else {//权限申请失败
            if (permissions != null) {
                //不能再次申请
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[0])) {
                    showShortToast(getString(R.string.requeset_permission_fail));
                }
                //继续申请权限
                else {
                    ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
                }
            }
        }
    }

    /**
     * 检查权限成功进行下一步操作
     */
    private void checkSuccessAndDoNext(int  requestCode){
        switch (requestCode) {
            //申请内存卡权限
            case Constant.STORAGE_REQUESETCODE:
                doWriteStorage();
                break;
            //申请振动权限
            case Constant.VIBRATOR_REQUESETCODE:
                doVibrator();
                break;
        }
    }

    /**
     * 读写内存卡操作（空实现  具体由子类实现）
     */
    public void doWriteStorage() {

    }
    /**
     * 振动操作（空实现  具体由子类实现）
     */
    public void doVibrator(){

    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
