package com.hhxh.xijiu.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author lijq
 * @time 2016-10-26 下午3:13:13
 */
public class FileUtil {

	/**
	 * 是否有SD卡
	 *
	 * @return true:有 false:没有
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取SD卡路径
	 *
	 * @return 路径
	 */
	public static String getSDCardPath() {
		String sdPath = "";
		if (isExternalStorageWritable()) {
			// 得到当前外部存储设备的目录( /SDCARD )
			sdPath = Environment.getExternalStorageDirectory().getPath();
		} else {
			sdPath = Environment.getExternalStorageDirectory().toString();// 获取跟目录
		}
		return sdPath;
	}

	/**
	 * 获取当前应用 SD卡的绝对路径
	 *
	 * @return String:路径 null获取失败
	 */
	public String getAbsolutePath(Context context) {
		File root = context.getExternalFilesDir(null);
		if (root != null) {
			return root.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 获取当前应用的 SD卡缓存文件夹绝对路径
	 *
	 * @return String:路径 null获取失败
	 */
	public String getCachePath(Context context) {
		File root = context.getExternalCacheDir();
		if (root != null) {
			return root.getAbsolutePath();
		}
		return null;
	}

	public static void Delete(String pathName){
		File file= new File(pathName);
		if(file.exists())
			file.delete();
	}

	public static void writeFile(File file, byte[] data, int offset, int count) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(data, offset, count);
		fos.flush();
		fos.close();
	}

}
