package com.hhxh.xijiu.custum;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listivew 解决scrollview包裹listview导致listview显示不全
 * @author qiaocbao
 * @time 2014-3-31 下午3:46:38
 */
public class MyListview extends ListView {

	public MyListview(Context context) {
		super(context);
	}

	public MyListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
