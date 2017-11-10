package com.hhxh.xijiu.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.adapter.BaseListAdapter;
import com.hhxh.xijiu.main.modle.OrderDetailsItem;

/**
 * 功能描述：订单详情中商品信息adpater
 *
 * @auth lijq
 * @date 2016/11/29
 */
public class OrderDetailsAdapter extends BaseListAdapter {
    /**
     * 赠品类型
     */
    public static final int FREEGIFT_TYPE = 0;
    /**
     * 商品类型
     */
    public static final int GOODS_TYPE = 1;

    /**
     * 是否可修改
     */
    private boolean isEditable;

    public OrderDetailsAdapter(Context mContext) {
        super(mContext);
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderDetailsItem.Goods item = (OrderDetailsItem.Goods) list.get(position);
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            holder = new ViewHolder();
            if (type == FREEGIFT_TYPE) {//赠品
                convertView = mInflater.inflate(R.layout.order_details_gift_item, null);
                holder.tagText = (TextView) convertView.findViewById(R.id.tagText);
                holder.countText = (TextView) convertView.findViewById(R.id.countText);
                holder.goodsNameText = (TextView) convertView.findViewById(R.id.goodsNameText);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
            } else if (type == GOODS_TYPE) {//商品
                convertView = mInflater.inflate(R.layout.order_details_normal_item, null);
                holder.amountText = (TextView) convertView.findViewById(R.id.amountText);
                holder.countText = (TextView) convertView.findViewById(R.id.countText);
                holder.goodsNameText = (TextView) convertView.findViewById(R.id.goodsNameText);
                holder.unitText = (TextView) convertView.findViewById(R.id.unitText);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
            }
            if (isEditable) {
                holder.img.setVisibility(View.VISIBLE);
            } else {
                holder.img.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        }
        //代金卷
        if (item.isVoucher()) {
            holder.tagText.setText(mContext.getString(R.string.voucher));
        } else if (item.isFreegift()) {//赠品
            holder.tagText.setText(mContext.getString(R.string.freegift));
        } 
        holder.goodsNameText.setText(item.getName());
        holder.countText.setText("×" + item.getCount());
        if (type == GOODS_TYPE) {
            holder.unitText.setText(item.getUnit());
            holder.amountText.setText(String.valueOf(item.getUnitPrice()));
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        OrderDetailsItem.Goods item = (OrderDetailsItem.Goods) list.get(position);
        boolean isFreegift = item.isFreegift() || item.isVoucher();
        return isFreegift ? FREEGIFT_TYPE : GOODS_TYPE;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ViewHolder {
        /**
         * 赠品或代金卷标签
         */
        private TextView tagText;
        /**
         * 数量
         */
        private TextView countText;
        /**
         * 商品名
         */
        private TextView goodsNameText;
        /**
         * 金额
         */
        private TextView amountText;
        /**
         * 单位
         */
        private TextView unitText;

        private ImageView img;
    }
}
