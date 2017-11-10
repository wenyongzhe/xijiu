package com.hhxh.xijiu.login.view;

import com.hhxh.xijiu.base.view.BaseView;
/**
 * 登陆界面接口
 * @auth lijq
 * @date 2016/9/7
 */
public interface ILoginView extends BaseView{
    /**获取用户名*/
    String getUserName();
    /**获取密码*/
    String getPassword();
    /**设置用户名*/
    void setUserName(String userName);
    /**设置密码*/
    void setPassword(String psw);
    /**清除用户名*/
    void clearUserName();
    /**清除密码*/
    void clearPassword();

}
