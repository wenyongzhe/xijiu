package com.hhxh.xijiu.login.view;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BasePresenterActivity;
import com.hhxh.xijiu.login.presenter.FindBackPswPresenter;
import com.hhxh.xijiu.utils.StringUtil;
import com.hhxh.xijiu.utils.ViewFocuseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class FindBackPswActivity extends BasePresenterActivity<IFindBackPswView, FindBackPswPresenter> implements View.OnClickListener, IFindBackPswView {
    /**
     * 手机号码
     */
    @BindView(R.id.telephoneEdit)
    EditText telephoneEdit;
    /**
     * 新密码
     */
    @BindView(R.id.newPswEdit)
    EditText newPswEdit;
    /**
     * 验证码
     */
    @BindView(R.id.authCodeEdit)
    EditText authCodeEdit;
    /**
     * 获取验证码
     */
    @BindView(R.id.getAuthCodeText)
    TextView getAuthCodeText;
    /**
     * 完成
     */
    @BindView(R.id.completeText)
    TextView completeText;

    /**
     * 60s倒计时
     */
    private MyCount myCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_back_psw_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        View decorView=((ViewGroup)(getWindow().findViewById(android.R.id.content))).getChildAt(0);
        ViewFocuseHelper.requesetFocuseAndHideSoftInput(decorView);
        initTitle(getString(R.string.findback_psw));
        myCount = new MyCount(60000, 1000);
    }

    @Override
    protected FindBackPswPresenter initPresenter() {
        return new FindBackPswPresenter();
    }


    @Override
    @OnClick({R.id.getAuthCodeText, R.id.completeText})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.completeText://完成
                if (checkTel() && checkInput()) {
                    mPresenter.findBackPsw();
                }
                break;
            case R.id.getAuthCodeText://获取验证码
                if (checkTel()) {
                    myCount.start();
                    getAuthCodeText.setEnabled(false);
                    mPresenter.getAuthCode();
                }

                break;
        }
    }

    /**
     * 验证新密码跟验证码输入
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(newPswEdit.getText())) {
            showShortToast(getString(R.string.input_new_psw));
            return false;
        }
        if (TextUtils.isEmpty(authCodeEdit.getText())) {
            showShortToast(getString(R.string.input_auth_code));
            return false;
        }
        return true;
    }

    /**
     * 验证手机号输入
     *
     * @return
     */
    private boolean checkTel() {
        if (TextUtils.isEmpty(telephoneEdit.getText())) {
            showShortToast(getString(R.string.input_telephone));
            return false;
        }
        if (!StringUtil.isMobileNumber(telephoneEdit.getText().toString())) {
            showShortToast(getString(R.string.input_right_telephone));
            return false;
        }
        return true;
    }

    @Override
    public String getTelephoneNum() {
        return telephoneEdit.getText().toString();
    }

    @Override
    public String getNewPassword() {
        return newPswEdit.getText().toString();
    }

    @Override
    public String getAuthCode() {
        return authCodeEdit.getText().toString();
    }

    @Override
    public Context getContext() {
        return this;
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
        int type = (int) data;
        switch (type) {
            case 1://获取要验证码返回
                showShortToast(getString(R.string.auth_code_deliveried));
                break;
            case 2://找回密码
                showShortToast(getString(R.string.psw_modify_success));
                finish();
                break;
        }
    }


    @Override
    public void loadFail(Object data) {
        if (data instanceof Integer) {
            int type = (int) data;
            switch (type) {
                case 1://获取要验证码返回
                    showShortToast(getString(R.string.get_auth_code_fail));
                    setGetAuthCodeDefault();
                    break;
                case 2://找回密码
                    showShortToast(getString(R.string.get_psw_back_fail));
                    break;
            }
        } else if (data instanceof String) {
            //获取验证码失败（附原因）
            showShortToast((String) data);
            setGetAuthCodeDefault();
        }

    }


    /**
     * 60秒倒计时
     */
    private class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setGetAuthCodeDefault();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getAuthCodeText.setText(millisUntilFinished / 1000 + getResources().getString(R.string.new_wait_time));
        }

    }

    /**
     * 设置获取验证码text为默认状态
     */
    private void setGetAuthCodeDefault() {
        getAuthCodeText.setText(getString(R.string.get_auth_code));
        getAuthCodeText.setEnabled(true);
        myCount.cancel();
    }

}
