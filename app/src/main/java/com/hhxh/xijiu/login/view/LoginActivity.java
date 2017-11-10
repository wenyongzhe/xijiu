package com.hhxh.xijiu.login.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BasePresenterActivity;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.login.presenter.LoginPresenter;
import com.hhxh.xijiu.main.Api.AppUpdateApi;
import com.hhxh.xijiu.main.view.MainActivity;
import com.hhxh.xijiu.system.MyApplication;
import com.hhxh.xijiu.update.Config;
import com.hhxh.xijiu.update.T_VersionInfo;
import com.hhxh.xijiu.utils.AlertUtil;
import com.hhxh.xijiu.utils.FileUtil;
import com.hhxh.xijiu.utils.HhxhLog;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.NetUtil;
import com.hhxh.xijiu.utils.NetworkUtility;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.ScanOperate;
import com.hhxh.xijiu.utils.StringUtil;
import com.hhxh.xijiu.utils.VersionUtil;
import com.hhxh.xijiu.utils.ViewFocuseHelper;
import com.hhxh.xijiu.utils.ZipCompressUtility;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

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
 * 登陆界面
 *
 * @author lijq
 * @date 2016-09-01
 */
public class LoginActivity extends BasePresenterActivity<ILoginView, LoginPresenter> implements View.OnClickListener, ILoginView ,ScanOperate.ScanResultListerner{
    /**
     * 用户名
     */
    @BindView(R.id.userNameEdit)
    EditText userNameEdit;
    /**密码*/
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    /**
     * APK下载路径
     */
    private String downloadUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //wenyongzhe 屏蔽原来代码中的更新功能
//        checkIsNeedUpdate();
        mPresenter.checkIsAutoLogin();
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        View decorView=((ViewGroup)(getWindow().findViewById(android.R.id.content))).getChildAt(0);
        ViewFocuseHelper.requesetFocuseAndHideSoftInput(decorView);
    }


    @Override
    @OnClick({R.id.findBackPswText,R.id.loginText})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginText://登陆
                if (verifyInput()) {
                    mPresenter.login();
                }
                break;
            case R.id.findBackPswText://找回密码
                Intent findPswIntent = new Intent(mContext, FindBackPswActivity.class);
                startActivity(findPswIntent);
                break;
        }
    }

    @Override
    public String getUserName() {
        return userNameEdit.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEdit.getText().toString();
    }

    @Override
    public void setUserName(String userName) {
        userNameEdit.setText(userName);
    }

    @Override
    public void setPassword(String psw) {
        passwordEdit.setText(psw);
    }


    @Override
    public void clearUserName() {
        userNameEdit.setText("");
    }

    @Override
    public void clearPassword() {
        passwordEdit.setText("");
    }

    @Override
    public void showLoading(String content) {
        startProgressDialog(content);
    }

    @Override
    public void hideLoading() {
        stopProgressDialog();
    }

    @Override
    public void loadSuccess(Object data) {
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void loadFail(Object data) {
        if (data != null && data instanceof String) {
            showShortToast((String) data);
        } else {
            showShortToast(getString(R.string.username_or_password_error));
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 验证输入
     *
     * @return 是否输入正确
     */
    private boolean verifyInput() {
        boolean isLogin = true;
        if (StringUtil.isEmpty(userNameEdit.getText().toString())) {
            showToast(getString(R.string.please_input_username), Toast.LENGTH_SHORT);
            isLogin = false;
        } else if (StringUtil.isEmpty(passwordEdit.getText().toString())) {
            showToast(getString(R.string.please_input_password), Toast.LENGTH_SHORT);
            isLogin = false;
        }
        return isLogin;
    }

    @Override
    public void doWriteStorage() {
        AppUpdateApi.downloadApk(mContext, downloadUrl);
    }

    @Override
    public void scanResult(String result) throws IOException, XmlPullParserException {

    }

}
