package com.hhxh.xijiu.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理
 * 
 * @author qiaocbao
 * @time 2015-9-15 上午10:31:37
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true为空,false不为空
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || "".equals(str.trim())
				|| "null".equalsIgnoreCase(str.trim())
				|| "undefined".equalsIgnoreCase(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 分割字符串
	 * 
	 * @param curString
	 *            要分割的字符串
	 * @param delimiter
	 *            分割符
	 * @return String[]
	 */
	public static String[] splitString(String curString, String delimiter) {
		if (curString != null && delimiter != null
				&& curString.indexOf(delimiter) != -1) {
			return curString.split(delimiter);
		}
		return null;
	}



	/**
	 * 字符串进行unicode编码
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return String 处理后的字符串
	 */
	public static String convertUnicode(String str) {
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c > 255) {
				sb.append("\\u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
			} else {
				sb.append(c);
			}

		}
		return (new String(sb));
	}

	/**
	 * 复制文字到前切板
	 * 
	 * @param context
	 * @param content 要复制到剪切板的内容
	 */
	public static void saveToClipboard(Context context, String content) {
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content);
	}

	/**
	 * 设置字体
	 * 
	 * @param contect
	 * @param typefacePath
	 *            字体样式路径
	 * @return ypeface
	 */
	public static Typeface settingTypeface(Context contect, String typefacePath) {
		if (!StringUtil.isEmpty(typefacePath)) {
			AssetManager mgr = contect.getAssets();// 得到AssetManager
			Typeface tf = Typeface.createFromAsset(mgr, typefacePath);// 根据路径得到Typeface
			if (tf != null) {
				return tf;
			}
		}
		return null;
	}

	/**
	 * 根据时间生成msgId
	 * 
	 * @return
	 */
	public static String getMsgId() {
		return "" + System.nanoTime() + "" + new Random().nextInt(100);
	}




	/**
	 * 过滤掉名字的所有的特殊字符
	 */
	public static String deleteSpecialStr(String name) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(name);
		return m.replaceAll("").trim();

	}

	/**
	 * 判断第一个字是否是中文，是英文则将引文转为大写
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	/**
	 * 判断是否是特殊字符
	 */
	public static Boolean specialStr(String name) {

		if (name.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "")
				.length() == 0) {
			// 不包含特殊字符
			return false;
		}
		return true;
		// return m.replaceAll("").trim();
	}

	/**
	 * 手机号码否正确 （十一位数）
	 *
	 * @param s
	 * @return
	 */
	public static boolean isMobileNumber(String s) {
		Matcher m = Pattern.compile("\\d{11}").matcher(s);
		return m.matches();
	}

}