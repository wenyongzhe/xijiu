package com.hhxh.xijiu.scan.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseActivity;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.main.modle.SubItem;
import com.hhxh.xijiu.utils.DateTimeUtil;
import com.hhxh.xijiu.utils.HhxhLog;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.ScanOperate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;


/**
 * 扫一扫
 *
 * @author lijq
 * @time 2016-9-5上午11:32:12
 */
public class CaptureActivity extends BaseActivity implements OnClickListener ,QRCodeView.Delegate,ScanOperate.ScanResultListerner{
    private EditText barcode;
    /**
     * 手动输入requesetCode
     */
    public static final int INPUT_ACTION = 0X11;
    /**
     * 播放器
     */
    private MediaPlayer mediaPlayer;
    /**
     * 是否支持播放声音
     */
    private boolean isSupportPlayBeep;
    /**
     * 闪光灯开关
     */
    private ImageView flasherSwithImg;
    /**
     * 声音开关
     */
    private ImageView muteSwithImg;
    /**
     * 振动开关
     */
    private ImageView shockSwithImg;
    /**
     * 手动输入
     */
    private TextView manualInputText;
    /**
     * 完成
     */
    private TextView completeText;
    /**
     * 扫描的数量
     */
    private TextView scannerNumText;
    /**
     * 扫描到的内容
     */
    private TextView scannerContentText;


    /***
     * 振动时长
     */
    private static final long VIBRATE_DURATION = 200L;
    /**
     * 振动声音大小
     */
    private static final float BEEP_VOLUME = 0.10f;
    /***
     * 已经扫过的二维码
     */
    private List<String> hasScanCodeList;

    /**
     * 已扫数量
     */
    private int hasScanCodeCount;
    /***
     * 要提交的已扫的箱码信息
     */
    private List<String> scanedBoxInfo;
    /***
     * 要提交的已扫的瓶码信息
     */
    private List<HashMap<String, String>> scanedBottleInfo;
    /***
     * 扫码订单的subList
     */
    private List<SubItem> toScanSubItemList;
    /**
     * 订单所需扫描的总数
     */
    private int needScanTotalCount;
    /**
     * 用来记录所有已扫到的瓶码
     */
    private List<String> allScannedBottleList;

   // private QRCodeView mQRCodeView;

    ScanOperate scanOperate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture_activity);
        barcode = (EditText) findViewById(R.id.barcode);
        barcode.requestFocus();
        initTitle("");
        init();
        scanOperate = new ScanOperate(this,this);


        /*barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getScanCodeInfo(s.toString());
                barcode.setClickable(false);
                barcode.clearFocus();
                barcode.setFocusable(false);
                barcode.setVisibility(View.GONE);

                Log.e("code",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mQRCodeView.startCamera();
       // mQRCodeView.startSpotAndShowRect();
    }
    @Override
    protected void onStop() {
        super.onStop();
       // mQRCodeView.stopCamera();
       // mQRCodeView.stopSpotAndHiddenRect();

    }

    @Override
    protected void onDestroy() {
       // mQRCodeView.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        initScanNeedRecordInfo(intent);
        //mQRCodeView= (QRCodeView) findViewById(R.id.zxingView);
        //mQRCodeView.setDelegate(this);
        manualInputText = (TextView) findViewById(R.id.manualInputText);
        flasherSwithImg = (ImageView) findViewById(R.id.flasherSwithImg);
        muteSwithImg = (ImageView) findViewById(R.id.muteSwithImg);
        shockSwithImg = (ImageView) findViewById(R.id.shockSwithImg);
        completeText = (TextView) findViewById(R.id.completeText);
        scannerNumText = (TextView) findViewById(R.id.scannerNumText);
        scannerContentText = (TextView) findViewById(R.id.scannerContentText);

        scannerNumText.setText(String.valueOf(hasScanCodeCount));

        flasherSwithImg.setSelected(false);
        muteSwithImg.setSelected(true);
        shockSwithImg.setSelected(true);
        manualInputText.setOnClickListener(this);
        completeText.setOnClickListener(this);
        flasherSwithImg.setOnClickListener(this);
        muteSwithImg.setOnClickListener(this);
        shockSwithImg.setOnClickListener(this);

        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            isSupportPlayBeep = false;
        } else {
            isSupportPlayBeep = true;
        }
        initBeepSound();
    }

    /**
     * 初始化扫码需要保存的数据
     */
    private void initScanNeedRecordInfo(Intent intent) {
        toScanSubItemList = (List<SubItem>) intent.getSerializableExtra("subList");
        scanedBoxInfo = (List<String>) intent.getSerializableExtra("scanedBoxInfo");
        scanedBottleInfo = (List<HashMap<String, String>>) intent.getSerializableExtra("scanedBottleInfo");
        hasScanCodeList = (List<String>) intent.getSerializableExtra("hasScanCodeList");
        allScannedBottleList = (List<String>) intent.getSerializableExtra("allScannedBottleList");
        if (toScanSubItemList != null) {
            for (SubItem subItem : toScanSubItemList) {
                needScanTotalCount += subItem.getCount();
                hasScanCodeCount = subItem.getHasScanNum();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flasherSwithImg://闪关灯开关
                if (!flasherSwithImg.isSelected()){
                  //  mQRCodeView.openFlashlight();
                }else{
                  //  mQRCodeView.closeFlashlight();
                }
                flasherSwithImg.setSelected(!flasherSwithImg.isSelected());
                break;
            case R.id.muteSwithImg://声音开关
                muteSwithImg.setSelected(!muteSwithImg.isSelected());
                break;
            case R.id.shockSwithImg://振动开关
                shockSwithImg.setSelected(!shockSwithImg.isSelected());
                break;
            case R.id.manualInputText://手动输入
                Intent intent = new Intent(mContext, ManualInputActivity.class);
                startActivityForResult(intent, INPUT_ACTION);
                break;
            case R.id.titleLeftImg://
                onBackPressed();
                break;
            case R.id.completeText://完成
                if (hasScanCodeCount < needScanTotalCount) {
                    OpenDialog.getInstance().showTwoBtnListenerDialog(mContext,
                            mContext.getString(R.string.has_not_complete_scan), mContext.getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    completeScan();
                                }
                            }, mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                } else {
                    completeScan();
                }
                break;
        }
    }

    @Override
    public void doVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
    }

    /**
     * 获取二维码信息
     *
     * @param code 二维码
     */
    public void getScanCodeInfo(final String code) {

         if (!TextUtils.isEmpty(code)) {
            //是否属于已扫过的二维码
            if (!hasScanCodeList.contains(code)) {
                hasScanCodeList.add(code);
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        return getRemoteInfo(params[0]);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (TextUtils.isEmpty(s)){

                            showShortToast("无法解析条码!");

                            return;
                        } else if(s.equals("connError")){
                            for (int i = 0; i<hasScanCodeList.size(); i++) {
                                if(hasScanCodeList.get(i).equals(code)){
                                    hasScanCodeList.remove(i);
                                }
                            }
                            showShortToast(getString(R.string.conn_fail));
                            return;
                        }
                        verifyCodeInfo(s);
                        if (hasScanCodeCount == needScanTotalCount) {
                            HhxhLog.i("complete");
                            completeScan();
                        }
                    }
                }.execute(code);
            } else {
                showShortToast(getString(R.string.code_has_scaned));
            }
        } else {
            scannerContentText.setText(getString(R.string.not_belong_order));
        }
    }

    /**
     * 获取扫码URL中的码值
     *
     * @param url
     * @return
     */
    public String getUrlParamValue(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("?")) {
                String paramsStr = url.substring(url.indexOf("?"));
                if (paramsStr.contains("&")) {
                    String[] params = paramsStr.split("&");
                    for (String param : params) {
                        if (param.contains("qrcode=")) {
                            return param.split("=")[1];
                        }
                    }
                } else {
                    if (paramsStr.contains("qrcode=")) {
                        return paramsStr.split("=")[1];
                    }
                }
            }
        }
        return url;
    }

    /**
     * 完成扫码
     */
    private void completeScan() {
        if (hasScanCodeCount > 0) {
            Intent intent = new Intent();
            intent.putExtra("subList", (Serializable) toScanSubItemList);
            intent.putExtra("scanedBoxInfo", (Serializable) scanedBoxInfo);
            intent.putExtra("scanedBottleInfo", (Serializable) scanedBottleInfo);
            intent.putExtra("hasScanCodeList", (Serializable) hasScanCodeList);
            setResult(RESULT_OK, intent);
            showToast(getString(R.string.complete_scan), Toast.LENGTH_SHORT);
        }
        finish();
    }

    /**
     * 验证二维码是否是合法且未扫过
     *
     * @param s
     */
    private void verifyCodeInfo(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            boolean isOk = obj.optBoolean("ok");
            if (isOk) {
                JSONArray tableArray = obj.optJSONArray("Table");
                if (tableArray != null) {
                    //扫到的二维码是否属于该订单
                    boolean isContains = false;
                    //返回的集合都是一个品种的酒 所以取第一个就行
                    JSONObject firstObj = tableArray.getJSONObject(0);
                    //酒的品种标识
                    String spbn = firstObj.optString("sProductIDERP");
                    //当前品种的酒还少的数量
                    int notScanNum = 0;
                    //遍历要扫的集合 假如集合里边有该酒的品种就说明扫到的酒符合该订单
                    for (SubItem subItem : toScanSubItemList) {
                        if (subItem.getSpbh().equals(spbn)) {
                            notScanNum += subItem.getCount() - subItem.getHasScanNum();
                            isContains = true;
                        }
                    }
                    if (isContains) {
                        //（1、瓶码 2、箱码）
                        int codeType = 1;
                        if (tableArray.length() > 1) {
                            codeType = 2;
                        }
                        //商品名
                        String name = firstObj.optString("sProductName");
                        scannerContentText.setText(name);
                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText(name);
                        //本次扫描到的数量
                        int scanCount = tableArray.length();
                        //重复数量
                        int repeatCount=0;
                        //防止扫到的箱码里边包含扫过的瓶码，需要做处理
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject bottleObj = tableArray.optJSONObject(i);
                            if (bottleObj != null) {
                                String bottleCode = bottleObj.optString("sProductCode");
                                //扫到的码里边不包含已扫的瓶码
                                if (!allScannedBottleList.contains(bottleCode)) {
                                    allScannedBottleList.add(bottleCode);
                                } else {//扫到的码里边包含已扫的瓶码
                                    if(codeType==1){//扫到的码是瓶码，直接返回
                                        showShortToast(getString(R.string.code_has_scaned));
                                        return;
                                    }else{
                                        //扫到的码是箱码，需要把这箱中的瓶码从要提交的瓶码信息集合中去掉。
                                        for (int j = 0; j < scanedBottleInfo.size(); j++) {
                                            HashMap<String, String> map = scanedBottleInfo.get(j);
                                            if (bottleCode.equals(map.get("sProductCode"))) {
                                                //把瓶码信息从要提交的瓶码信息里去掉
                                                scanedBottleInfo.remove(j);
                                                repeatCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        hasScanCodeCount-=repeatCount;
                        if (scanCount > notScanNum || notScanNum == 0) {
                            showShortToast(getString(R.string.scan_num_redundant));
                            return;
                        }
                        hasScanCodeCount += scanCount;
                        //设置扫描数量
                        setScanCodeCount(spbn, scanCount);
                        //保存要提交的信息
                        if (codeType == 1) {//瓶码
                            HashMap bottleMap = new HashMap();
                            bottleMap.put("sProductCode", firstObj.optString("sProductCode"));
                            bottleMap.put("sBoxCode", firstObj.optString("sBoxCode"));
                            if (!scanedBottleInfo.contains(bottleMap)) {
                                scanedBottleInfo.add(bottleMap);
                            }
                        } else if (codeType == 2) {//箱码
                            String boxCode = firstObj.optString("sBoxCode");
                            if (!scanedBoxInfo.contains(boxCode)) {
                                scanedBoxInfo.add(boxCode);
                            }
                        }
                    } else {
                        scannerContentText.setText(getString(R.string.not_belong_order));
                    }
                }
                scannerNumText.setText(String.valueOf(hasScanCodeCount));
            } else {
                showShortToast(getString(R.string.scan_fail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置扫码数量
     *
     * @param spbn 酒品种的唯一标识
     */
    private void setScanCodeCount(String spbn, int count) {
        //扫描的订单 是否有赠品
        boolean isHasFreeGift = false;
        for (SubItem subItem : toScanSubItemList) {
            if (spbn.equals(subItem.getSpbh())) {
                if (subItem.isFreeGifts()) {
                    isHasFreeGift = true;
                    break;
                }
            }
        }
        if (isHasFreeGift) {
            //赠品数量
            int subCount = count;
            for (SubItem subItem : toScanSubItemList) {
                //非赠品
                if (spbn.equals(subItem.getSpbh()) && !subItem.isFreeGifts()) {
                    int notScanNum = subItem.getCount() - subItem.getHasScanNum();
                    if (notScanNum != 0 && count <= notScanNum) {
                        subItem.setHasScanNum(subItem.getHasScanNum() + count);
                        subCount = 0;
                        break;
                    } else if (notScanNum != 0 && count > notScanNum) {
                        subItem.setHasScanNum(subItem.getCount());
                        subCount -= notScanNum;
                        break;
                    } else {
                        subItem.setHasScanNum(subItem.getCount());
                        subCount = count;
                        break;
                    }
                }
            }
//            showToast(("subCount:"+subCount),Toast.LENGTH_SHORT);
            for (SubItem subItem : toScanSubItemList) {
                //赠品
                if (spbn.equals(subItem.getSpbh()) && subItem.isFreeGifts()) {
                    subItem.setHasScanNum(subCount + subItem.getHasScanNum());
                    break;
                }
            }
        } else {
            for (SubItem subItem : toScanSubItemList) {
                if (spbn.equals(subItem.getSpbh())) {
                    subItem.setHasScanNum(subItem.getHasScanNum() + count);
                    break;
                }
            }
        }
    }


    /**
     * 获取扫描信息
     *
     * @param code
     */
    public String getRemoteInfo(String code) {
        // 命名空间
        String nameSpace = "http://tempuri.org/";
        // 调用的方法名称
        String methodName = "GetTraceInfo_JSON";
        // EndPoint
        String endPoint = "http://www.gzxijiu.com:82/Service1.asmx";
        // SOAP Action
        String soapAction = "http://tempuri.org/GetTraceInfo_JSON";

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的两个参数code、date
        rpc.addProperty("code", code);
        rpc.addProperty("date", DateTimeUtil.getCurDateStr("yyyyMMdd") + "JW");

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(endPoint);
        try {
            // 调用WebService
            transport.call(soapAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        if (object != null) {
            // 获取返回的结果
            String result = object.getProperty(0).toString();
            HhxhLog.i("RESULT:" + result);
            return result;
        }
        return null;
    }



    /**
     * 初始化声音播放
     */
    private void initBeepSound() {
        if (isSupportPlayBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = getMediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /***/
    public MediaPlayer getMediaPlayer() {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(mContext, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaplayer;
    }


    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onBackPressed() {
        if (hasScanCodeCount > 0) {
            OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, getString(R.string.give_up),
                    getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CaptureActivity.super.onBackPressed();
                            finish();
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //手动输入返回
        if (INPUT_ACTION == requestCode && RESULT_OK == resultCode) {
            String code = data.getStringExtra("manualInputCode");
            getScanCodeInfo(code);
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
       /* HhxhLog.i("result:"+result);
        //播放声音
        if (isSupportPlayBeep && muteSwithImg.isSelected()) {
            mediaPlayer.start();
        }
        //振动
        if (shockSwithImg.isSelected()){
            checkPermission(Manifest.permission.VIBRATE, Constant.VIBRATOR_REQUESETCODE,getString(R.string.requeset_vibrator_permission));
        }
        String code = getUrlParamValue(result);
        HhxhLog.i("code:" + code);
        getScanCodeInfo(code);
        mQRCodeView.startSpot();*/
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showShortToast("open camera error");
    }

    @Override
    public void scanResult(String result) throws IOException, XmlPullParserException {
        HhxhLog.i("result:" + result);
        //播放声音
        if (isSupportPlayBeep && muteSwithImg.isSelected()) {
            mediaPlayer.start();
        }
        /*//振动
        if (shockSwithImg.isSelected()){
            checkPermission(Manifest.permission.VIBRATE, Constant.VIBRATOR_REQUESETCODE,getString(R.string.requeset_vibrator_permission));
        }
        String code = getUrlParamValue(result);*/
        HhxhLog.i("code:" + result);
        getScanCodeInfo(result);
       // mQRCodeView.startSpot();

    }
}