package com.hhxh.xijiu.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseActivity;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.ViewFocuseHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 修改密码
 *
 * @auth lijq
 * @date 2016/10/19
 */
public class ModifyPswActivity extends BaseActivity {
    /**
     * 老密码
     */
    @BindView(R.id.oldPswEdit)
    EditText oldPswEdit;
    /**
     * 新密码
     */
    @BindView(R.id.newPswEdit)
    EditText newPswEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_psw_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitle(getString(R.string.modify_psw));
        View decorView=((ViewGroup)(getWindow().findViewById(android.R.id.content))).getChildAt(0);
        ViewFocuseHelper.requesetFocuseAndHideSoftInput(decorView);
    }

    @Override
    protected void initTitle(String str) {
        super.initTitle(str);
        titleLeftImg.setImageResource(R.drawable.ic_back_red);
        titleLeftImg.setVisibility(View.VISIBLE);
    }

    /**
     * 修改密码
     */
    private void modifyPsw() {
        HashMap params = new HashMap();
        final String newPsw = newPswEdit.getText().toString().trim();
        params.put("password", newPsw);
        params.put("id", UserPrefs.getUserId());
        OkHttpUtils.get(Constant.getModifyPswUrl()).params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        startProgressDialog(getString(R.string.submitting));
                    }

                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            JSONObject obj = new JSONObject(s);
                            if (JsonUtils.isExistObj(obj, "#message")) {//失败
                                showShortToast(obj.optString("#message"));
                            }
                        } catch (Exception e) {
                            showShortToast(getString(R.string.psw_modify_success));
                            UserPrefs.setUserPwd(newPsw);
                            finish();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        showShortToast(getString(R.string.psw_modify_fail));
                    }

                    @Override
                    public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onAfter(isFromCache, s, call, response, e);
                        stopProgressDialog();
                    }
                });
    }

    /**
     * 验证输入
     *
     * @return
     */
    private boolean verifyInput() {
        if (TextUtils.isEmpty(oldPswEdit.getText())) {
            showShortToast(getString(R.string.input_old_psw));
            return false;
        }
        if (!TextUtils.equals(oldPswEdit.getText(), UserPrefs.getUserPwd())) {
            showShortToast(getString(R.string.old_psw_wrong));
            return false;
        }
        if (TextUtils.isEmpty(newPswEdit.getText())) {
            showShortToast(getString(R.string.input_new_psw));
            return false;
        }
        return true;
    }

    @OnClick(R.id.comfirmText)
    public void onClick() {
        if (verifyInput()) {
            modifyPsw();
        }
    }
}
