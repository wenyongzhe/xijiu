package com.hhxh.xijiu.base.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.MyProgressDialog;
import com.hhxh.xijiu.custum.Toast.ToastCompat;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.StringUtil;

/**
 * @auth lijq
 * @date ${date} @time ${time}
 */
public class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;
    /** 显示加载进度条 */
    private MyProgressDialog myProgressDialog = null;
    /** 标题TextView控件 */
    protected TextView titleText, titleLeftText, titleRightText;
    /** 标题ImageView控件 */
    protected ImageView titleLeftImg, titleRightImg;

    /** 更多功能标题, 标题栏左边Text */
    protected TextView moreTitleName, moreTitleLeftText;
    /** 更多功能标题栏左边Image,标题栏右边第二个按钮，标题栏右边第一个按钮 */
    protected ImageView moreTitleLeftImage, moreTitleTwoRight,
            moreTitleOneRight;
    /**数据是否需要更新*/
    protected boolean isNeedUpdata;
    /**启动扫码intent*/
    protected Intent startCameraIntent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
        mContext=getActivity();
        isNeedUpdata=true;
    }

    /**
     * 初始化标题栏
     *
     * @param view 视图
     * @param str 标题
     */
    protected void initTitle(View view, String str) {
        titleText = (TextView) view.findViewById(R.id.titleText);
        titleLeftText = (TextView) view.findViewById(R.id.titleLeftText);
        titleLeftImg = (ImageView) view.findViewById(R.id.titleLeftImg);
        titleRightText = (TextView) view.findViewById(R.id.titleRightText);
        titleRightImg = (ImageView) view.findViewById(R.id.titleRightImg);
        titleLeftText.setText(getString(R.string.back));
        titleText.setText(str);
    }

    /**
     * 初始化更多功能标题栏
     * @param view 视图
     * @param titleName 标题
     */
    protected void initMoreFunctionTitle(View view, String titleName) {
//        moreTitleName = (TextView) view.findViewById(R.id.moreTitleName);
//        moreTitleLeftText = (TextView) view
//                .findViewById(R.id.moreTitleLeftText);
//        moreTitleLeftImage = (ImageView) view
//                .findViewById(R.id.moreTitleLeftImage);
//        moreTitleTwoRight = (ImageView) view
//                .findViewById(R.id.moreTitleTwoRight);
//        moreTitleOneRight = (ImageView) view
//                .findViewById(R.id.moreTitleOneRight);
//        moreTitleName.setText(titleName);
//        String returnName = getActivity().getIntent().getStringExtra(
//                "returnName");
//        if (!StringUtil.isEmpty(returnName)) {
//            moreTitleLeftText.setText(returnName);
//        }
    }

    protected <T> T getView(int id) {

        return (T) mActivity.findViewById(id);

    }

    public void setNeedUpdata(boolean needUpdata) {
        isNeedUpdata = needUpdata;
    }

    /**
     * 弹Toast
     * @param text
     * @param duration
     */
    protected void showToast(String text,int duration){
        ToastCompat.makeText(mContext,text,duration).show();
    }

    /**
     * 弹Toast
     * @param text
     */
    protected void showShortToast(String text){
        ToastCompat.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动进度条
     *
     * @param content 进度条上的加载提示
     */
    public void startProgressDialog( String content) {
        if (StringUtil.isEmpty(content))
            return;
        myProgressDialog = new MyProgressDialog.Builder(mContext).create();
        myProgressDialog.setMessage(content).show();
    }

    /**
     * 停止进度条
     *
     */
    public void stopProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog.dismiss();
            myProgressDialog = null;
        }
    }
    /**
     * 检查相机权限
     */
    protected void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                OpenDialog.getInstance().showOneBtnListenerDialog(mContext,null,
                        getString(R.string.requeset_camera_permission)
                        ,getString(R.string.confirm)
                        ,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA},
                                        Constant.TACK_PHOTO_REQUESETCODE);
                            }
                        });
            }else{
                //申请相机权限
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA},
                        Constant.TACK_PHOTO_REQUESETCODE);
            }
        }else{
            startCameraActivity(startCameraIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==Constant.TACK_PHOTO_REQUESETCODE){
            //相机权限请求成功
            if (grantResults!=null&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                startCameraActivity(startCameraIntent);
            }else {
                //相机权限申请失败
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                    showShortToast(getString(R.string.camera_permission_refuse));
                }
            }
        }
    }

    /***
     * 启动相机
     * @param intent
     */
    protected void startCameraActivity(Intent intent) {
        startActivityForResult(intent, Constant.CAMERA_REQUESTE_CODE);
    }

    /**
     * 检查读写内存卡权限
     */
    protected void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                OpenDialog.getInstance().showOneBtnListenerDialog(mContext,null,
                        getString(R.string.requeset_camera_permission)
                        , getString(R.string.confirm)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请内存卡权限
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        Constant.STORAGE_REQUESETCODE);
                            }
                        });
            } else {
                //申请内存卡权限
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.STORAGE_REQUESETCODE);
            }
        } else {
            doWriteStorage();
        }
    }

    /**
     * 读写内存卡操作（空实现  具体由子类实现）
     */
    public void doWriteStorage() {

    }
}

