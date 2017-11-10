package com.hhxh.xijiu.login.presenter;

import android.text.TextUtils;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.presenter.BasePresenter;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.login.biz.LoginBiz;
import com.hhxh.xijiu.login.view.ILoginView;
/**
 * 登陆presenter
 * @author  lijq
 * @date 2016-09-07
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    /**登陆业务处理*/
    private LoginBiz mLoginBiz;
    public LoginPresenter(){
        this.mLoginBiz=new LoginBiz();
    }

    /**
     * 登陆
     */
    public void login(){
        mView.showLoading(mView.getContext().getString(R.string.logining));
        mLoginBiz.login(mView.getUserName(), mView.getPassword(), new LoginBiz.OnLoginListener() {
            @Override
            public void loginSuccess() {
                if (mView!=null){
                    mView.hideLoading();
                    mView.loadSuccess(mView.getContext().getString(R.string.login_success));
                }
            }

            @Override
            public void loginFailed(String s) {
                if (mView!=null){
                    mView.hideLoading();
                    if (TextUtils.isEmpty(s)){
                        mView.loadFail(mView.getContext().getString(R.string.login_fail));
                    }else{
                        mView.loadFail(s);
                    }
                }
            }
        });
    }

    /**
     * 是否自动登陆
     *
     */
    public void checkIsAutoLogin(){
        if(UserPrefs.getIsAutoLogin()){
            if (mView!=null){
                mView.setUserName(UserPrefs.getUserAccount());
                mView.setPassword(UserPrefs.getUserPwd());
            }
            login();
        }
    }

}
