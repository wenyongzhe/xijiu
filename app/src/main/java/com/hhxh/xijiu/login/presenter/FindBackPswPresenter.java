package com.hhxh.xijiu.login.presenter;

import android.text.TextUtils;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.presenter.BasePresenter;
import com.hhxh.xijiu.login.biz.FindBackPswBiz;
import com.hhxh.xijiu.login.view.IFindBackPswView;

/**
 * 找回密码presenter
 *
 * @auth lijq
 * @date 2016/9/7
 */
public class FindBackPswPresenter extends BasePresenter<IFindBackPswView> {
    private FindBackPswBiz mFindBackPswBiz;

    public FindBackPswPresenter() {
        mFindBackPswBiz = new FindBackPswBiz();
    }

    /**
     * 获取验证码
     */
    public void getAuthCode() {
        mView.showLoading(mView.getContext().getString(R.string.getting));
        mFindBackPswBiz.getAuthCode(mView.getTelephoneNum(), new FindBackPswBiz.OnGetAuthCodeListener() {
            @Override
            public void getAuthCodeSuccess() {
                if(mView!=null){
                    mView.hideLoading();
                    mView.loadSuccess(1);
                }
            }

            @Override
            public void getAuthCodeFailed(String s) {
                if(mView!=null){
                    mView.hideLoading();
                    if (TextUtils.isEmpty(s)){
                        mView.loadFail(1);
                    }else{
                        mView.loadFail(s);
                    }

                }
            }
        });
    }

    /***
     * 找回密码
     */
    public void findBackPsw() {
        mView.showLoading(mView.getContext().getString(R.string.submitting));
        mFindBackPswBiz.findBackPsw(mView.getTelephoneNum(), mView.getNewPassword(),
                mView.getAuthCode(), new FindBackPswBiz.OnFindBackPswListener() {
                    @Override
                    public void findBackPswSuccess() {
                        mView.hideLoading();
                        mView.loadSuccess(2);
                    }

                    @Override
                    public void findBackPswFail() {
                        mView.hideLoading();
                        mView.loadFail(2);
                    }
                });
    }

}
