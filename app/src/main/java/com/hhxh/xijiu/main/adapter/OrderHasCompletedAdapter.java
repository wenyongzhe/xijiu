package com.hhxh.xijiu.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BaseListAdapter;
import com.hhxh.xijiu.main.modle.OrderHasCompleteItem;
import com.hhxh.xijiu.main.view.OrderHasCompletedFragment;
import com.hhxh.xijiu.main.view.ReceiveAmountActivity;

/**
 * 已完成订单adapter
 * @auth lijq
 * @date 2016-9-2
 */
public class OrderHasCompletedAdapter  extends BaseListAdapter{
    public OrderHasCompletedAdapter(Context context){
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderHasCompleteItem item = (OrderHasCompleteItem) list.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.order_has_completed_item, null);
            holder = new ViewHolder();
            holder.deliveryNumText= (TextView) convertView.findViewById(R.id.deliveryNumText);
            holder.orderNumText = (TextView) convertView.findViewById(R.id.orderNumText);
            holder.orderStateText = (TextView) convertView.findViewById(R.id.orderStateText);
            holder.customerNameText = (TextView) convertView.findViewById(R.id.customerNameText);
            holder.contactPersonText = (TextView) convertView.findViewById(R.id.contactPersonText);
            holder.contactTelText= (TextView) convertView.findViewById(R.id.contactTelText);
            holder.addressText = (TextView) convertView.findViewById(R.id.addressText);
            holder.depotText = (TextView) convertView.findViewById(R.id.depotText);
            holder.needAmountText = (TextView) convertView.findViewById(R.id.needAmountText);
            holder.paidAmountText = (TextView) convertView.findViewById(R.id.paidAmountText);
            holder.receiverAmountText = (TextView) convertView.findViewById(R.id.receiverAmountText);
            holder.clickListener=new MyClickListener(position);
            holder.receiverAmountText.setOnClickListener(holder.clickListener);
            holder.subListView= (ListView) convertView.findViewById(R.id.subListView);
            holder.descriptionLayout=convertView.findViewById(R.id.descriptionLayout);
            holder.descrptionText= (TextView) convertView.findViewById(R.id.descriptionText);
            holder.subAdapter=new SubItemAdapter(mContext);
            holder.subListView.setAdapter(holder.subAdapter);
            convertView.setTag(holder);
        }
        holder.clickListener.setPosition(position);
        holder.deliveryNumText.setText(item.getDeliveryNum());
        holder.orderStateText.setText(item.getState());
        holder.subAdapter.setList(item.getSubItemList());
        holder.subAdapter.notifyDataSetChanged();
        holder.contactTelText.setText(item.getContactTel());
        holder.orderNumText.setText(item.getOrderNum());
        holder.customerNameText.setText(item.getCustomerName());
        holder.contactPersonText.setText(item.getContactPerson());
        holder.addressText.setText(item.getAddress());
        holder.depotText.setText(item.getDepot());
        holder.paidAmountText.setText(item.getPaidAmount());
        holder.needAmountText.setText(item.getNeedReciverAmount());
        if (!TextUtils.isEmpty(item.getDescription())){
            holder.descriptionLayout.setVisibility(View.VISIBLE);
            holder.descrptionText.setText(item.getDescription());
        }else{
            holder.descriptionLayout.setVisibility(View.GONE);
        }
        if (item.isNeedReciveAmount()){
            holder.receiverAmountText.setVisibility(View.VISIBLE);
        }else{
            holder.receiverAmountText.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class MyClickListener implements View.OnClickListener{
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public MyClickListener(int position){
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            OrderHasCompleteItem item = (OrderHasCompleteItem) list.get(position);
            Intent intent=new Intent(mContext,ReceiveAmountActivity.class);
            intent.putExtra("id",item.getId());
            try{
                intent.putExtra("maxAmout",Float.parseFloat(item.getNeedReciverAmount()));
            }catch (Exception e){
                e.printStackTrace();
            }
            ( (Activity) mContext).startActivityForResult(intent, OrderHasCompletedFragment.RECEIVE_AMOUNT_REQUEST_CODE);
        }
    }
    class ViewHolder {
        /**配送编号*/
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
         * 地址
         */
        private TextView addressText;
        /**联系电话*/
        private TextView contactTelText;
        /** 发货仓库 */
        private TextView depotText;
        /**
         * 应收金额
         */
        private TextView needAmountText;
        /**收款*/
        private TextView receiverAmountText;
        /**
         * 已收金额
         */
        private TextView paidAmountText;
        /**收款点击事件监听*/
        private MyClickListener clickListener;
        /**描述布局*/
        private View descriptionLayout;
        /**交货描述*/
        private TextView descrptionText;
        /**子listView*/
        private ListView subListView;
        /**子adapter*/
        private SubItemAdapter subAdapter;
    }
}
