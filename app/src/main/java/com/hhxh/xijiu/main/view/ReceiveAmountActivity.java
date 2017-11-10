package com.hhxh.xijiu.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseActivity;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.ViewFocuseHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述：收款
 *
 * @auth lijq
 * @date 2016/11/21
 */
public class ReceiveAmountActivity extends BaseActivity implements OnClickListener{

    /**收款金额*/
    private EditText receivedAmountEdit;
    /**收款凭证*/
    private EditText evidenceEdit;
    /**备注*/
    private EditText remarkEdit;
    /**收款的订单id*/
    private String id;
    /**订单还未收的钱*/
    private float maxAmout;
    private Spinner spinner;
    /**凭证layout*/
    private LinearLayout evidenceLayout;
    private View divideView;
    /**是否线上收款*/
    private boolean  isOnLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_amount_activity);
        initView();
    }

    @Override
    protected void initTitle(String str) {
        super.initTitle(str);
        titleLeftImg.setImageResource(R.drawable.ic_back_red);
        titleLeftImg.setVisibility(View.VISIBLE);
    }

    private void initView() {
        View decorView=((ViewGroup)(getWindow().findViewById(android.R.id.content))).getChildAt(0);
        ViewFocuseHelper.requesetFocuseAndHideSoftInput(decorView);
        initTitle(getString(R.string.receive_amount));
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        maxAmout=intent.getFloatExtra("maxAmout",0);
        receivedAmountEdit= (EditText) findViewById(R.id.receivedAmountEdit);
        evidenceEdit= (EditText) findViewById(R.id.evidenceEdit);
        remarkEdit= (EditText) findViewById(R.id.remarkEdit);
        spinner= (Spinner) findViewById(R.id.spinner);
        divideView=findViewById(R.id.divideView);
        evidenceLayout= (LinearLayout) findViewById(R.id.evidenceLayout);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    isOnLine=false;
                    evidenceLayout.setVisibility(View.GONE);
                    divideView.setVisibility(View.GONE);
                }else{
                    isOnLine=true;
                    evidenceLayout.setVisibility(View.VISIBLE);
                    divideView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView receiveAmountText= (TextView) findViewById(R.id.receiveAmountText);
        receiveAmountText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (verifyInput()){
            receiveAmount();
        }
    }

    /**
     * 验证输入
     * @return
     */
    private boolean verifyInput(){
        if (TextUtils.isEmpty(receivedAmountEdit.getText())){
            showShortToast(getString(R.string.input_received_amount));
            return false;
        }
        if (Integer.parseInt(receivedAmountEdit.getText().toString())>maxAmout){
            showShortToast(getString(R.string.receive_amount_too_much));
            return false;
        }
        if (TextUtils.isEmpty(remarkEdit.getText())){
            showShortToast(getString(R.string.input_remark));
            return false;
        }
        return  true;
    }

    private void receiveAmount(){
        HashMap<String,String> params=new HashMap<>();
        params.put("parentid",id);
        params.put("amountreceived",receivedAmountEdit.getText().toString());
        params.put("payeeremarks",remarkEdit.getText().toString());
        String url=null;
        if (isOnLine){
            url=Constant.getReceiveOnlineUrl();
            params.put("vouther",evidenceEdit.getText().toString());
        }else{
            url=Constant.getReceiveOfflineUrl();
        }
        OkHttpUtils.post(url).params(params).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                startProgressDialog(getString(R.string.submitting));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                if (response!=null&&response.isSuccessful()){
                    try {
                        JSONObject obj=new JSONObject(s);
                        if (JsonUtils.isExistObj(obj, "#message")) {//失败
                            showShortToast(obj.optString("#message"));
                          return;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    //成功
                    setResult(RESULT_OK);
                    finish();
                }else{
                    showShortToast(getString(R.string.submit_fail));
                }
            }

            @Override
            public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onAfter(isFromCache, s, call, response, e);
                stopProgressDialog();
            }
        });
    }
}
