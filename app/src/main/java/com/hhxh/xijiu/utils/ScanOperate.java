package com.hhxh.xijiu.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/5.
 *
 *
 */

public class ScanOperate {
    private Context context;
    private ScanResultListerner resultListerner;
    public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    public static final String SCN_CUST_EX_SCODE = "scannerdata";

    public ScanOperate(Context context, ScanResultListerner resultListerner) {
        this.resultListerner = resultListerner;
        this.context = context;
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        context.registerReceiver(scanReceiver, intentFilter);
    }

    public void openScanLight(boolean is_on) {
        if (is_on) {
            context.sendBroadcast(new Intent("com.android.server.scannerservice.onoff").putExtra("scanneronoff", 1));
        } else {
            context.sendBroadcast(new Intent("com.android.server.scannerservice.onoff").putExtra("scanneronoff", 0));
        }
    }

    public void onDestroy(Context context) {
        context.unregisterReceiver(scanReceiver);
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (resultListerner != null && intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                // 第一代需要这样改m.obj = message.substring(0, message.length()-1);
                // 因为获取到的条码后会加个\n的缘故第二代的不需要，字符串trim方法即可取消空白字符
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE).trim();
                try {
                    resultListerner.scanResult(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static interface ScanResultListerner {
        void scanResult(String result) throws IOException, XmlPullParserException;
    }
}
