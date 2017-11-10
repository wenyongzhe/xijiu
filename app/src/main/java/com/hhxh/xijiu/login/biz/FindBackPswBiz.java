package com.hhxh.xijiu.login.biz;

import android.support.annotation.Nullable;

import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.utils.JsonUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 找回密码
 *
 * @auth lijq
 * @date 2016/9/7
 */
public class FindBackPswBiz {
    /**
     * 获取验证码
     *
     * @param telephone           手机号码
     * @param getAuthCodeListener 回调
     */
    public void getAuthCode(String telephone, final OnGetAuthCodeListener getAuthCodeListener) {

        OkHttpUtils.post(Constant.AUTH_CODE_URL).params("mobile", telephone)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        if (response != null && response.isSuccessful()) {
                            try {
                                JSONObject obj = new JSONObject(s);
                                if (!JsonUtils.isExistObj(obj, "#message")) {
                                    getAuthCodeListener.getAuthCodeSuccess();
                                } else {
                                    getAuthCodeListener.getAuthCodeFailed(obj.optString("#message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            getAuthCodeListener.getAuthCodeFailed(null);
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        getAuthCodeListener.getAuthCodeFailed(null);
                    }
                });
    }

    /***
     * 找回密码
     *
     * @param telephone           手机号
     * @param newPsw              新密码
     * @param authCode            验证码
     * @param findBackPswListener 回调
     */
    public void findBackPsw(String telephone, String newPsw, String authCode, final OnFindBackPswListener findBackPswListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("contacttel", telephone);
        params.put("password", newPsw);
        params.put("random", authCode);
        OkHttpUtils.post(Constant.FINDBACK_PSW_URL).params(params).execute(new StringCallback() {
            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                if (response != null && response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!JsonUtils.isExistObj(obj, "#message")) {
                            findBackPswListener.findBackPswSuccess();
                        } else {
                            findBackPswListener.findBackPswFail();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    findBackPswListener.findBackPswFail();
                }
            }
        });
    }

    /**
     * 获取验证码接口
     *
     * @auth lijq
     * @date 2016/9/7
     */
    public interface OnGetAuthCodeListener {
        /**
         * 获取验证码成功
         */
        void getAuthCodeSuccess();

        /**
         * 获取验证码失败
         */
        void getAuthCodeFailed(String s);
    }

    /**
     * 找回密码接口
     *
     * @auth lijq
     * @date 2016/9/7
     */
    public interface OnFindBackPswListener {
        void findBackPswSuccess();

        void findBackPswFail();
    }
}
