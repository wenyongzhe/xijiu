package com.hhxh.xijiu.scan.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseActivity;
import com.hhxh.xijiu.utils.ViewFocuseHelper;

/**
 * 手动输入
 *
 * @author lijq
 * @time 2016-9-6 上午11:32:12
 */
public class ManualInputActivity extends BaseActivity implements View.OnClickListener{
    /**手动输入*/
    private EditText manualInputEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_input_activity);
        initView();
    }

    private void initView() {
        initTitle(getString(R.string.manual_input));
        TextView switchToScanText= (TextView) findViewById(R.id.switchToScanText);
        TextView comfirmText= (TextView) findViewById(R.id.comfirmText);
        manualInputEdit= (EditText) findViewById(R.id.manualInputEdit);
        comfirmText.setOnClickListener(this);
        switchToScanText.setOnClickListener(this);
        View decorView=((ViewGroup)(getWindow().findViewById(android.R.id.content))).getChildAt(0);
        ViewFocuseHelper.requesetFocuseAndHideSoftInput(decorView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switchToScanText://切换到扫码
                finish();
                break;
            case R.id.comfirmText://确认
                if (!TextUtils.isEmpty(manualInputEdit.getText())){
                    Intent intent=new Intent();
                    intent.putExtra("manualInputCode",manualInputEdit.getText().toString().trim());
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    showShortToast(getString(R.string.input_code_num));
                }
                break;
        }
    }
}
