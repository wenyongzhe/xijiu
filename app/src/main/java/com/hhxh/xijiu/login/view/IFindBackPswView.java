package com.hhxh.xijiu.login.view;

import com.hhxh.xijiu.base.view.BaseView;

/**
 * 找回密码界面接口
 * @auth lijq
 * @date 2016/9/7
 */
public interface IFindBackPswView extends BaseView{
    /**获取手机号码*/
    String getTelephoneNum();
    /**获取新密码*/
    String getNewPassword();
    /**获取验证*/
    String getAuthCode();
}
