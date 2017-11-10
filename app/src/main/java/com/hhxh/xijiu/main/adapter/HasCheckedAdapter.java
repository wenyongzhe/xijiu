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
import com.hhxh.xijiu.main.modle.HasCheckedItem;

/**
 * 功能描述：已审核adaper
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class HasCheckedAdapter extends BaseListAdapter{
    /**反审核回调*/
    private CallBack mCallBack;
    public HasCheckedAdapter(Context mContext) {
        super(mContext);
    }
    public void setmCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }
    @Override
    @SuppressWarnings("ResourceType")
    public View getView(int position, View convertView, ViewGroup parent) {
        HasCheckedItem item= (HasCheckedItem) list.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.has_checked_item, null);
            holder=new ViewHolder();
            holder.orderNumText = (TextView) convertView.findViewById(R.id.orderNumText);
            holder.orderStateText = (TextView) convertView.findViewById(R.id.orderStateText);
            holder.customerNameText = (TextView) convertView.findViewById(R.id.customerNameText);
            holder.contactPersonText = (TextView) convertView.findViewById(R.id.contactPersonText);
            holder.contactTelText = (TextView) convertView.findViewById(R.id.contactTelText);
            holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
            holder.totalAmountText = (TextView) convertView.findViewById(R.id.totalAmountText);
            holder.uncheckText = (TextView) convertView.findViewById(R.id.uncheckText);
            Resources resources=mContext.getResources();
            try {
                XmlResourceParser parser=resources.getXml(R.drawable.text_color_white_and_red_selecor);
                ColorStateList colorStateList = ColorStateList.createFromXml(resources, parser);
                holder.uncheckText.setTextColor(colorStateList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.clickListener=new MyClickListener(position);
            holder.uncheckText.setOnClickListener(holder.clickListener);
            convertView.setTag(holder);
        }
        if (item.isSelsected()){
            holder.uncheckText.setSelected(true);
        }else{
            holder.uncheckText.setSelected(false);
        }
        if (item.getOrderStateNum()==30||item.getOrderStateNum()==40||item.getOrderStateNum()==100){//全部开票iao或部分开票不能反审核
            holder.uncheckText.setVisibility(View.GONE);
        }else {
            holder.uncheckText.setVisibility(View.VISIBLE);
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
            switch (v.getId()){
                case R.id.uncheckText://反审核
                    if(mCallBack!=null){
                        HasCheckedItem item= (HasCheckedItem) list.get(position);
                        setItemSelected(position);
                        mCallBack.onClick(item);
                    }
                    break;
            }
        }
    }
    private void setItemSelected(int position){
        for(int i=0;i<list.size();i++){
            HasCheckedItem item= (HasCheckedItem) list.get(i);
            if (i==position){
                item.setSelsected(true);
            }else{
                item.setSelsected(false);
            }
        }
        notifyDataSetChanged();
    }
    public interface CallBack{
        void onClick(HasCheckedItem item);
    }
    class ViewHolder{
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
         * 反审核
         */
        private TextView uncheckText;
        /**点击事件监听*/
        private MyClickListener clickListener;
    }
}
