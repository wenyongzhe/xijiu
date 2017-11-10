package com.hhxh.xijiu.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络操作工具类
 * @author qiaocbao
 * @time 2015-5-26  上午10:26:55
 */
public class NetUtil {
	private static class NetUtilsHolder{
		private static NetUtil instance = new NetUtil();
	}
	
	/**
	 * 避免外界直接使用
	 */
	private NetUtil(){}
	
	public static NetUtil getInstance() {
		return NetUtilsHolder.instance;
	}
	
	private Object readResolve(){
		return getInstance();
	}
	
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return	boolean	是否连网
	 */
	public boolean isConnected(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null != connManager){
			NetworkInfo netInfo = connManager.getActiveNetworkInfo();
			if(null != netInfo && netInfo.isConnected()){
				if(netInfo.getState() == NetworkInfo.State.CONNECTED){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断Wifi是否可用
	 * @param context
	 * @return	boolean	Wifi网络是否可用
	 */
	public boolean isWifiConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
	                 .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	         if (mWiFiNetworkInfo != null) {  
	             return mWiFiNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	/**
	 * 判断移动网络是否可用
	 * @param context
	 * @return	boolean	移动网络是否可用
	 */
	public boolean isMobileConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mMobileNetworkInfo = mConnectivityManager  
	                 .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	         if (mMobileNetworkInfo != null) {  
	             return mMobileNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	/**
	 * 获取当前网络连接类型
	 * @param context
	 * @return	网络连接类型
	 */
	public static int getConnectedType(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
	             return mNetworkInfo.getType();  
	         }  
	     }  
	     return -1;  
	}
	
	/**
	 * 判断是否是wifi连接
	 * @param context
	 * @return	Wifi是否可用
	 */
	public boolean isWifi(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager != null){
			return connManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
		}
		return false;
	}
	
	/**
	 * 打开网络设置界面
	 * @param activity
	 */
	public void openSetting(Activity activity, int requestCode){
		Intent intent = new Intent();
		ComponentName cName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(cName);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 获取网络连接状态
	 */
	public boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}
