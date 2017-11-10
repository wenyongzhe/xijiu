package com.hhxh.xijiu.data;

import com.hhxh.xijiu.login.modle.User;
import com.hhxh.xijiu.utils.SharedPrefsHelper;

/**
 * 用户信息存储获取
 *
 * @author qiaocbao
 * @time 2014-10-7 上午9:36:37
 */
public class UserPrefs {
    private final static String USER_PREFS_NAME = "hhxh_user";
    private static SharedPrefsHelper mUserPrefs;

    /**
     * 用户token
     */
    private static final String USER_TOKEN = "token";
    /**
     * 用户名
     */
    private static final String USERNAME = "userName";
    /***/
    private static final String USER_ID = "userId";
    /**
     * 用户登录帐号
     */
    public static final String USER_ACCOUNT = "userAccount";
    /**
     * 用户登录密码
     */
    public static final String USER_PWD = "userPwd";
    /**
     * 配送员
     */
    public static final String DELIVERY_PART = "deliveryPart";
    /**
     * 业务员
     */
    public static final String SALESMAN_PART = "salesmanPart";

    /**
     * 是否自动登录
     */
    public static final String IS_AUTOLOGIN = "isAutoLogin";
    /**
     * 服务器上最新的版本号
     */
    public static final String SERVER_APP_VERISION = "serverAppVersion";


    /**
     * 上一次登录的帐号
     */
    public static final String PREVIOUS_ACCOUNT = "previousAccount";
    /**
     * 上一次登录的密码
     */
    public static final String PREVIOUS_PWD = "previousPwd";

    private static SharedPrefsHelper getInstance() {
        if (mUserPrefs == null) {
            mUserPrefs = new SharedPrefsHelper(USER_PREFS_NAME);
        }
        return mUserPrefs;
    }


    /**
     * 保存用户信息
     *
     * @param user
     */
    public static void setUser(User user) {
        getInstance().getEditor().putString(USERNAME, user.getUserName())
                .putString(USER_TOKEN, user.getToken())
                .putString(USER_PWD, user.getPassword())
                .putString(USER_ID, user.getUserId())
                .putString(USER_ACCOUNT,user.getUserAcount())
                .putBoolean(DELIVERY_PART,user.isDeliveryPart())
                .putBoolean(SALESMAN_PART,user.isSalesmanPart())
                .putBoolean(IS_AUTOLOGIN,true)
                .commit();
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getInstance().getStringValue(USER_TOKEN, "");
    }

    /**
     * 保存token
     *
     * @return
     */
    public static void setToken(String token) {
        getInstance().getEditor().putString(USER_TOKEN, token).commit();
    }

    public static String getUserId() {
        return getInstance().getStringValue(USER_ID);
    }

    /**
     * 获取用户帐号
     *
     * @return
     */
    public static String getUserAccount() {
        return getInstance().getStringValue(USER_ACCOUNT, "");
    }

    /**
     * 保存用户帐号
     *
     * @return
     */
    public static void setUserAccount(String userAccount) {
        getInstance().getEditor().putString(USER_ACCOUNT, userAccount).commit();
    }

    /**
     * 获取用户密码
     *
     * @return
     */
    public static String getUserPwd() {
        return getInstance().getStringValue(USER_PWD, "");
    }

    /**
     * 保存用户密码
     *
     * @return
     */
    public static void setUserPwd(String userPwd) {
        getInstance().getEditor().putString(USER_PWD, userPwd).commit();
    }

    /**
     * 获取用户是否是配送员角色
     *
     * @return
     */
    public static boolean getUserIsDeliveryPart() {
        return getInstance().getBooleanValue(DELIVERY_PART, false);
    }

    /**
     * 保存用户是否是配送员角色
     *
     * @return
     */
    public static void setUserIsDeliveryPart(boolean isDeliveryPart) {
        getInstance().getEditor().putBoolean(DELIVERY_PART, isDeliveryPart).commit();
    }
    /**
     * 获取用户是否是业务员角色
     *
     * @return
     */
    public static boolean getUserIsSalesmanPart() {
        return getInstance().getBooleanValue(SALESMAN_PART, false);
    }

    /**
     * 保存用户是否是配送员角色
     *
     * @return
     */
    public static void setUserIsSalesmanPart(boolean isSalesmanPart) {
        getInstance().getEditor().putBoolean(SALESMAN_PART, isSalesmanPart).commit();
    }
    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName() {
        return getInstance().getStringValue(USERNAME, "");
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public static boolean getIsAutoLogin() {
        return getInstance().getBooleanValue(IS_AUTOLOGIN, false);
    }

    /**
     * 保存是否自动登录
     *
     * @return
     */
    public static void setIsAutoLogin(boolean isAutoLogin) {
        getInstance().getEditor().putBoolean(IS_AUTOLOGIN, isAutoLogin).commit();
    }


    /**
     * 获取上一次登录的帐号
     *
     * @return
     */
    public static String getPreviousAccount() {
        return getInstance().getStringValue(PREVIOUS_ACCOUNT);
    }

    /**
     * 保存上一次登录的帐号
     *
     * @return
     */
    public static void setPreviousAccount(String previousAccount) {
        getInstance().getEditor().putString(PREVIOUS_ACCOUNT, previousAccount)
                .commit();
    }

    /**
     * 保存检测到的服务器上APP的最新版本号
     */
    public static void setServerAppVerision(String serverAppVerision) {
        getInstance().getEditor().putString(SERVER_APP_VERISION, serverAppVerision).commit();
    }

    /**
     * 获取保存的服务器上APP的最新版本号
     *
     * @return
     */
    public static String getServerAppVerision() {
        return getInstance().getStringValue(SERVER_APP_VERISION);
    }

    /**
     * 获取上一次登录的密码
     *
     * @return
     */
    public static String getPreviousPwd() {
        return getInstance().getStringValue(PREVIOUS_PWD);
    }

    /**
     * 保存上一次登录的密码
     *
     * @return
     */
    public static void setPreviousPwd(String previousPwd) {
        getInstance().getEditor().putString(PREVIOUS_PWD, previousPwd).commit();
    }

    /**
     * 清空用户数据
     */
    public static void clearUser() {
        getInstance().getEditor().putString(USERNAME, "")
                .putString(USER_TOKEN, "")
                .putString(USER_PWD, "")
                .putString(USER_ID, "")
                .putString(USER_ACCOUNT,"")
                .putBoolean(DELIVERY_PART,false)
                .putBoolean(SALESMAN_PART,false)
                .putBoolean(IS_AUTOLOGIN,false)
                .commit();
    }
}
