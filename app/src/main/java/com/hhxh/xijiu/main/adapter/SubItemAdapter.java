package com.hhxh.xijiu.main.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.custum.Toast.ToastCompat;
import com.hhxh.xijiu.main.modle.SubItem;
import com.hhxh.xijiu.utils.DensityUtil;
import com.hhxh.xijiu.utils.OpenDialog;

import java.util.List;

/**
 * 送货订单subApter
 * @auth lijq
 * @date 2016/9/26
 */
public class SubItemAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected List<SubItem> list;
    protected Context mContext;
    /**扫*/
    private ImageSpan scanImageSpan;
    /**赠*/
    private ImageSpan freeImageSpan;
    /**
     * 子item是否可被点击
     */
    private boolean isNotCompleteItem;


    public SubItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        Drawable scanDrawable=mContext.getResources().getDrawable(R.drawable.ic_little_scan);
        scanDrawable.setBounds(0,0, DensityUtil.getInstance().spToPx(mContext,14),DensityUtil.getInstance().spToPx(mContext,14));
        scanImageSpan=new ImageSpan(scanDrawable,ImageSpan.ALIGN_BASELINE);
        Drawable freeDrawable=mContext.getResources().getDrawable(R.drawable.ic_little_free);
        freeDrawable.setBounds(0,0,DensityUtil.getInstance().spToPx(mContext,14),DensityUtil.getInstance().spToPx(mContext,14));
        freeImageSpan=new ImageSpan(freeDrawable,ImageSpan.ALIGN_BASELINE);
    }

    public List<SubItem> getList() {
        return list;
    }

    public void setList(List<SubItem> list) {
        this.list = list;
    }

    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setNotCompleteItem(boolean notCompleteItem) {
        isNotCompleteItem = notCompleteItem;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubItem item = list.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.sub_item, null);
            holder = new ViewHolder();
            holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            holder.numberText = (TextView) convertView.findViewById(R.id.numberText);
            holder.hasScannedNumText = (TextView) convertView.findViewById(R.id.hasScannedNumText);
            holder.hasScannedNumTitleText= (TextView) convertView.findViewById(R.id.hasScannedNumTitleText);
            holder.rootRtLayout = convertView.findViewById(R.id.rootRtLayout);
            holder.onclickLister = new MyOnclickLister();
            holder.rootRtLayout.setOnClickListener(holder.onclickLister);
            convertView.setTag(holder);
        }
        //是否一定需要扫码
        if (item.isNeedScan()) {
            holder.hasScannedNumText.setTextColor(mContext.getResources().getColor(R.color.light_gray));
            holder.onclickLister.setClickAble(false);
            holder.hasScannedNumTitleText.setText(mContext.getString(R.string.has_scanned));
        } else {
            holder.hasScannedNumText.setTextColor(mContext.getResources().getColor(R.color.light_blue));
            if (isNotCompleteItem){
                holder.onclickLister.setClickAble(true);
                holder.hasScannedNumTitleText.setText(mContext.getString(R.string.modify_has_scan));
            }else{
                holder.onclickLister.setClickAble(false);
                holder.hasScannedNumTitleText.setText(mContext.getString(R.string.has_scanned));
            }
        }
        holder.nameText.setText(item.getModel());
        if (item.isFreeGifts() && item.isNeedScan()) {//赠品且需要扫
            SpannableStringBuilder suffixName = new SpannableStringBuilder(mContext.getString(R.string.scan));
            suffixName.append(mContext.getString(R.string.free));
            suffixName.setSpan(scanImageSpan,0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            suffixName.setSpan(freeImageSpan,suffixName.length()-1,suffixName.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.nameText.append(mContext.getString(R.string.space));
            holder.nameText.append(suffixName);
        } else if (item.isNeedScan()) {//需要 扫
            SpannableString suffixName = new SpannableString(mContext.getString(R.string.scan));
            suffixName.setSpan(scanImageSpan,0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.nameText.append(mContext.getString(R.string.space));
            holder.nameText.append(suffixName);
        } else if (item.isFreeGifts()) {//赠品
            SpannableString suffixName = new SpannableString(mContext.getString(R.string.scan));
            suffixName.setSpan(freeImageSpan,0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.nameText.append(mContext.getString(R.string.space));
            holder.nameText.append(suffixName);
        }
        holder.onclickLister.setPosition(position);
        StringBuilder countStr = new StringBuilder();
        if (item.getBoxNum() > 0) {
            countStr.append(item.getBoxNum()).append(mContext.getString(R.string.box)).append("(")
                    .append(item.getCount()).append(item.getUnit()).append(")");

        } else {
            countStr.append(item.getCount()).append(item.getUnit());
        }
        holder.numberText.setText(countStr);
        StringBuilder hasScanNumStr = new StringBuilder();
        if (item.getHasScanBoxNum() > 0) {
            hasScanNumStr.append(item.getHasScanBoxNum()).append(mContext.getString(R.string.box))
                    .append("(").append(item.getHasScanNum()).append(item.getUnit()).append(")");
        } else {
            hasScanNumStr.append(item.getHasScanNum()).append(item.getUnit());
        }
        holder.hasScannedNumText.setText(hasScanNumStr);
        return convertView;
    }

    private class MyOnclickLister implements View.OnClickListener {
        /**
         * 是否可点击
         */
        private boolean isClickAble;
        private int position;

        public void setClickAble(boolean isClickAble) {
            this.isClickAble = isClickAble;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (isClickAble && isNotCompleteItem) {
                final SubItem item = list.get(position);
                String defaultStr=item.getCount()+"";
                OpenDialog.getInstance().showEditContentDialog(mContext, mContext.getString(R.string.input_has_scan_num),
                        defaultStr,  mContext.getString(R.string.input_has_scan_num),true
                        , new OpenDialog.OnEditContentDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, String content) {
                                if (!TextUtils.isEmpty(content)) {
                                    int inputCount = Integer.parseInt(content);
                                    //输入数量不能大于订单数量
                                    if (inputCount > item.getCount()) {
                                        ToastCompat.makeText(mContext, mContext.getString(R.string.input_too_much)
                                                , Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    item.setHasScanNum(inputCount);
                                    notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        });
            }
        }
    }

    class ViewHolder {
        /**
         * 商品名称
         */
        private TextView nameText;
        /**
         * 购买数量
         */
        private TextView numberText;
        /**
         * 已扫数量
         */
        private TextView hasScannedNumText;

        /**
         * 根布局
         */
        private View rootRtLayout;
        /**已扫*/
        private TextView hasScannedNumTitleText;

        private MyOnclickLister onclickLister;
    }

}
