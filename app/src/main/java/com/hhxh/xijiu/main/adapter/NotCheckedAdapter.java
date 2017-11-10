package com.hhxh.xijiu.main.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BaseListAdapter;
import com.hhxh.xijiu.main.Api.OrderStateColorHelper;
import com.hhxh.xijiu.main.modle.NotCheckedItem;

/**
 * 功能描述：未审核adapter
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class NotCheckedAdapter extends BaseListAdapter {
    /**审核回调*/
    private CallBack mCallBack;

    public NotCheckedAdapter(Context mContext) {
        super(mContext);
    }

    public void setmCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public View getView(int position, View convertView, ViewGroup parent) {
        NotCheckedItem item = (NotCheckedItem) list.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.not_checked_item, null);
            holder = new ViewHolder();
            holder.orderNumText = (TextView) convertView.findViewById(R.id.orderNumText);
            holder.orderStateText = (TextView) convertView.findViewById(R.id.orderStateText);
            holder.customerNameText = (TextView) convertView.findViewById(R.id.customerNameText);
            holder.contactPersonText = (TextView) convertView.findViewById(R.id.contactPersonText);
            holder.contactTelText = (TextView) convertView.findViewById(R.id.contactTelText);
            holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
            holder.totalAmountText = (TextView) convertView.findViewById(R.id.totalAmountText);
            holder.designateText = (TextView) convertView.findViewById(R.id.designateText);
            holder.checkText = (TextView) convertView.findViewById(R.id.checkText);
            holder.clickListener = new MyClickListener(position);
            holder.designateText.setOnClickListener(holder.clickListener);
            holder.checkText.setOnClickListener(holder.clickListener);
            Resources resources=mContext.getResources();
            try {
                XmlResourceParser parser=resources.getXml(R.drawable.text_color_white_and_orange_selecor);
                ColorStateList  colorStateList = ColorStateList.createFromXml(resources, parser);
                holder.checkText.setTextColor(colorStateList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setTag(holder);
        }
        if (item.isSelsected()){
            holder.checkText.setSelected(true);
        }else{
            holder.checkText.setSelected(false);
        }
        holder.orderNumText.setText(item.getOrderNum());
        OrderStateColorHelper.setTextColorByState(item.getOrderStateNum(),holder.orderStateText);
        holder.orderStateText.setText(item.getOrderState());
        holder.customerNameText.setText(item.getCustomerName());
        holder.contactPersonText.setText(item.getContactPerson());
        holder.contactTelText.setText(item.getContactTel());
        holder.timeText.setText(item.getTime());
        holder.totalAmountText.setText(item.getTotalAmount());
        holder.clickListener.setPosition(position);
        return convertView;
    }

    private class MyClickListener implements View.OnClickListener {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public MyClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.designateText://指派
                    if(mCallBack!=null){
                        NotCheckedItem item= (NotCheckedItem) list.get(position);
                        mCallBack.onDesignate(item);
                    }
                    break;
                case R.id.checkText://审核
                    if(mCallBack!=null){
                        NotCheckedItem item= (NotCheckedItem) list.get(position);
                        setItemSelected(position);
                        mCallBack.onClick(item);
                    }
                break;
            }
        }
    }

    private void setItemSelected(int position){
        for(int i=0;i<list.size();i++){
            NotCheckedItem item= (NotCheckedItem) list.get(i);
            if (i==position){
                item.setSelsected(true);
            }else{
                item.setSelsected(false);
            }
        }
        notifyDataSetChanged();
    }
    public interface CallBack{
        /** 审核 */
        void onClick(NotCheckedItem item);
        /** 指派 */
        void onDesignate(NotCheckedItem item);
    }
    class ViewHolder {
        /**
         * 订单编号
         */
        private TextView orderNumText;
        /**
         * 订单状态
         */
        private TextView orderStateText;
        /**
         * 客户名称
         */
        private TextView customerNameText;
        /**
         * 联系人
         */
        private TextView contactPersonText;
        /**
         * 联系电话
         */
        private TextView contactTelText;
        /**
         * 订单时间
         */
        private TextView timeText;
        /**
         * 总金额
         */
        private TextView totalAmountText;
        /**
         * 指派
         */
        private TextView designateText;
        /**
         * 审核
         */
        private TextView checkText;
        /**
         * 点击事件监听
         */
        private MyClickListener clickListener;
    }
}
