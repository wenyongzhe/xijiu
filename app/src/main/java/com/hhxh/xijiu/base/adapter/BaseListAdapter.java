package com.hhxh.xijiu.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.hhxh.xijiu.base.modle.BaseItem;

import java.util.List;


/**
 * 自定义adapter基类
 * @author qiaocbao
 * @time 2015-6-5  上午9:43:27
 */

public abstract class BaseListAdapter extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected List<BaseItem> list;
	protected Context mContext;
	public BaseListAdapter(){
		
	}
	public BaseListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;

	}

	public List<BaseItem> getList() {
		return list;
	}

	public void setList(List<BaseItem> list) {
		this.list = list;
	}

	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	public Object getItem(int position) {
		if (list != null && list.size() > 0 && position < list.size()
				&& position > -1) {
			return list.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

}
