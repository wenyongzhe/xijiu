package com.hhxh.xijiu.custum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.modle.BaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaocb.
 * DateTime 2017/4/15 15:58.
 */

public class CustomListAdapter extends BaseAdapter {
    public List<BaseItem> contactList;
    public Context mContext;
    public LayoutInflater mInflater;

    public CustomListAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        contactList = new ArrayList<>();
    }

    public void setContactList(List<BaseItem> contactList){
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListItem item = (CustomListItem) contactList.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.dialog_list_item, null);
            holder = new ViewHolder();
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.selectCheck = (CheckBox) convertView
                    .findViewById(R.id.selectCheck);
            convertView.setTag(holder);
        }
        holder.selectCheck.setSelected(item.isSelected());
        holder.nameText.setText(item.getPersonName());
        return convertView;
    }

    public static class ViewHolder {
        /** 指派人名字 */
        private TextView nameText;
        /** 选择框 */
        public CheckBox selectCheck;
    }
}
