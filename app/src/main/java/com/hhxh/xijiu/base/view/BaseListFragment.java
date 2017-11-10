package com.hhxh.xijiu.base.view;

import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BaseListAdapter;
import com.hhxh.xijiu.base.modle.BaseItem;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 有listview的fragment基类
 * @author qiaocbao
 * @time 2015-6-5 下午2:36:45
 */
public abstract class BaseListFragment extends BaseFragment {
    /** 数据list */
    protected List<BaseItem> list;
    /** listivew */
    protected RefreshListView listView;
    /** adapter适配器 */
    protected BaseListAdapter adapter;
    /** 总条数 */
    protected int total = 0;
    /** 起始位置 */
    protected int start = 0;
    /** 页数 */
    protected int page = 1;
    /** 每次请求的条数 */
    protected int limit = 10;

    /**
     * 初始化listview
     *
     * @param view 视图
     */
    protected void initListView(View view) {
        listView = (RefreshListView) view.findViewById(R.id.list_view);
        list = new ArrayList<BaseItem>();
        adapter.setList(list);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.hidFootView();
        if (view instanceof ViewGroup){
            TextView  noDataText=new TextView(mContext);
            noDataText.setText(getString(R.string.no_data));
            noDataText.setTextColor(getResources().getColor(R.color.mid_gray));
            noDataText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            Drawable noDataDrawable=getResources().getDrawable(R.drawable.bg_no_data);
            noDataDrawable.setBounds(0,0,noDataDrawable.getIntrinsicWidth(),noDataDrawable.getIntrinsicHeight());
            noDataText.setCompoundDrawables(null,noDataDrawable,null,null);
            noDataText.setCompoundDrawablePadding(DensityUtil.getInstance().dipToPixels(mContext,5));
            noDataText.setGravity(Gravity.CENTER);
            if (view instanceof RelativeLayout){
                RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                noDataText.setLayoutParams(params);
            }
            ((ViewGroup)view).addView(noDataText);
            listView.setEmptyView(noDataText);
            noDataText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadData();
                }
            });
        }
    }

    /**
     * 重新加载数据
     */
    protected abstract void reloadData();
}
