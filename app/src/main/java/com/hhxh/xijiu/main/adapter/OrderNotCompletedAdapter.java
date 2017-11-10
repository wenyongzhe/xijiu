package com.hhxh.xijiu.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BaseListAdapter;
import com.hhxh.xijiu.main.modle.OrderNotCompleteItem;

/**
 * 未完成订单adapter
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class OrderNotCompletedAdapter extends BaseListAdapter {
    /**回调*/
    private CallBack mCallBack;


    public OrderNotCompletedAdapter(Context context) {
        super(context);
    }

    public void setmCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderNotCompleteItem item = (OrderNotCompleteItem) list.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.order_not_completed_item, null);
            holder = new ViewHolder();
            holder.deliveryNumText = (TextView) convertView.findViewById(R.id.deliveryNumText);
            holder.orderNumText = (TextView) convertView.findViewById(R.id.orderNumText);
            holder.orderStateText = (TextView) convertView.findViewById(R.id.orderStateText);
            holder.customerNameText = (TextView) convertView.findViewById(R.id.customerNameText);
            holder.contactPersonText = (TextView) convertView.findViewById(R.id.contactPersonText);
            holder.contactTelText = (TextView) convertView.findViewById(R.id.contactTelText);
            holder.addressText = (TextView) convertView.findViewById(R.id.addressText);
            holder.depotText = (TextView) convertView.findViewById(R.id.depotText);
            holder.totalAmountText = (TextView) convertView.findViewById(R.id.totalAmountText);
            holder.deliverGoodsText = (TextView) convertView.findViewById(R.id.deliverGoodsText);
            holder.scannerText = (TextView) convertView.findViewById(R.id.scannerText);
            holder.listener = new MyOnClickListener();
            holder.scannerText.setOnClickListener(holder.listener);
            holder.deliverGoodsText.setOnClickListener(holder.listener);
            holder.subListView = (ListView) convertView.findViewById(R.id.subListView);
            holder.subAdapter = new SubItemAdapter(mContext);
            holder.subAdapter.setNotCompleteItem(true);
            holder.subListView.setAdapter(holder.subAdapter);
            convertView.setTag(holder);
        }
        holder.orderStateText.setText(item.getState());
        holder.deliveryNumText.setText(item.getDeliveryNum());
        holder.listener.setItem(item);
        holder.listener.setPosition(position);
        holder.subAdapter.setList(item.getSubItemList());
        holder.subAdapter.notifyDataSetChanged();
        holder.orderNumText.setText(item.getOrderNum());
        holder.customerNameText.setText(item.getCustomerName());
        holder.contactPersonText.setText(item.getContactPerson());
        holder.contactTelText.setText(item.getContactTel());
        holder.addressText.setText(item.getAddress());
        holder.depotText.setText(item.getDepot());
        holder.totalAmountText.setText(item.getTotalAmount());
        return convertView;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private OrderNotCompleteItem item;
        private int position;

        public void setItem(OrderNotCompleteItem item) {
            this.item = item;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.deliverGoodsText://交货
                    if (mCallBack != null && item != null) {
                        mCallBack.deliveryGoods(item.getId(), position);
                    }
                    break;
                case R.id.scannerText://扫描
                    if (mCallBack != null) {
                        mCallBack.onScanCallBack(item,position);
                    }
                    break;
            }

        }
    }
    public void notifySubItemDataChanged(int position){
       ViewHolder holder= (ViewHolder) getView(position,null,null).getTag();
        holder.subAdapter.notifyDataSetChanged();
    }
    class ViewHolder {
        private TextView deliveryNumText;
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
         * 地址
         */
        private TextView addressText;

        /** 发货仓库 */
        private TextView depotText;

        /**
         * 总金额
         */
        private TextView totalAmountText;
        /**
         * 交货
         */
        private TextView deliverGoodsText;
        /**
         * 扫描
         */
        private TextView scannerText;

        private MyOnClickListener listener;
        private ListView subListView;
        private SubItemAdapter subAdapter;
    }

    public interface CallBack {
        /**
         * 扫码
         */
        void onScanCallBack(OrderNotCompleteItem item,int position);

        /**
         * 交货
         */
        void deliveryGoods(String id, int position);
    }

}
