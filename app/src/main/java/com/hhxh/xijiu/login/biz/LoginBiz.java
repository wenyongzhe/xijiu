package com.hhxh.xijiu.login.biz;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.login.modle.User;
import com.hhxh.xijiu.utils.HhxhLog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登陆逻辑
 *
 * @auth lijq
 * @date 2016/9/7
 */
public class LoginBiz {
    /***
     * 登陆
     *
     * @param userAcount      用户名
     * @param password      密码
     * @param loginListener 回调
     */
    public void login(final String userAcount, final String password, final OnLoginListener loginListener) {
        OkHttpUtils.get(Constant.LOGIN_URL)
                .params("u", userAcount)
                .params("p", password)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (response != null && 200 == response.code()) {
                                JSONObject obj = new JSONObject(s);
                                String error=obj.optString("#message");
                                if (TextUtils.isEmpty(error)) {
                                    User user=new User(obj);
                                    user.setPassword(password);
                                    user.setUserAcount(userAcount);
                                    UserPrefs.setToken(user.getToken());
                                    getUserPart(loginListener,user);
                                } else {
                                    loginListener.loginFailed(error);
                                }
                            } else {
                                loginListener.loginFailed(null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loginListener.loginFailed(null);
                    }
                });
    }

    /**
     * 获取账号的角色
     */
    public  void getUserPart(final OnLoginListener loginListener , final User user){
        OkHttpUtils.post(Constant.getUserPartUrl()).execute(new StringCallback() {

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                try {
                    JSONObject respObj=new JSONObject(s);
                    JSONArray array=respObj.optJSONArray("returns");
                    if(array!=null){
                        for (int i=0;i<array.length();i++){
                            JSONObject obj=array.optJSONObject(i);
                            String userPart=obj.getString("code");
                            if("com.hhxh.industry.sys.obj.cJxsAdmin.js.psy".equals(userPart)){//配送员
                                user.setDeliveryPart(true);
                            }else  if("com.hhxh.industry.sys.obj.cJxsAdmin.js.ltywy".equals(userPart)){//业务员
                                user.setSalesmanPart(true);
                            }
                        }
                        UserPrefs.setUser(user);
                        HhxhLog.i("IsDeliveryPart:"+UserPrefs.getUserIsDeliveryPart()+"IsSalesmanPart:"+UserPrefs.getUserIsSalesmanPart());
                        loginListener.loginSuccess();
                    }else{
                        loginListener.loginFailed(null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loginListener.loginFailed(null);
                }
            }
        });
    }
    /**
     * 登陆回调接口
     *
     * @auth lijq
     * @date 2016/9/7
     */
    public interface OnLoginListener {
        void loginSuccess();

        void loginFailed(String s);

    }

}
