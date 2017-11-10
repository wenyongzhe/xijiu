package com.hhxh.xijiu.login.modle;


import com.hhxh.xijiu.base.modle.BaseItem;

import org.json.JSONObject;

/**
 * user实体类
 * @author  lijq
 * @date 2016-09-07
 */
public class User implements BaseItem{
    /**用户名*/
    private String userName;
    /**账号*/
    private String userAcount;
    /**密码*/
    private String password;
    /**token*/
    private String token;
    /**useid*/
    private  String userId;
    /**是否是配送员*/
    private boolean isDeliveryPart;
    /**是否是业务员*/
    private boolean isSalesmanPart;
    public User(JSONObject obj){
        this.token=obj.optString("a");
        this.userId=obj.optString("user_id");
        this.userName=obj.optString("user_name");
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDeliveryPart() {
        return isDeliveryPart;
    }

    public void setDeliveryPart(boolean deliveryPart) {
        isDeliveryPart = deliveryPart;
    }

    public boolean isSalesmanPart() {
        return isSalesmanPart;
    }

    public void setSalesmanPart(boolean salesmanPart) {
        isSalesmanPart = salesmanPart;
    }

    public String getUserAcount() {
        return userAcount;
    }

    public void setUserAcount(String userAcount) {
        this.userAcount = userAcount;
    }
}
