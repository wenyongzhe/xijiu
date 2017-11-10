package com.hhxh.xijiu.main.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseListActivity;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.main.Api.OrderStateColorHelper;
import com.hhxh.xijiu.main.adapter.OrderDetailsAdapter;
import com.hhxh.xijiu.main.modle.OrderDetailsItem;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.OpenDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述：销售订单详情
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class SalesmanOrderDetaisActivity extends BaseListActivity implements View.OnClickListener{
    /**
     * 订单id
     */
    private String id;
    /**
     * 是否可修改
     */
    private boolean isEditable;
    /**
     * 订单编号
     */
    private TextView orderNumText;
    /**
     * 订单状态
     */
    private TextView orderStateText;
    /**
     * 审核人
     */
    private TextView checkPeopleText;
    /**
     * 下单时间
     */
    private TextView placeTimeText;
    /**
     * 审核时间
     */
    private TextView checkTimeText;
    /**
     * 商家
     */
    private TextView sellerNameText;
    /**
     * 联系电话
     */
    private TextView contactPersonText;
    /**
     * 收货人
     */
    private TextView takeDeliveryPeopleText;
    /**
     * 收货地址
     */
    private TextView takeDeliveryAddressText;
    /**
     * 总金额
     */
    private TextView totalAmountText;
    /**
     * 已开发票金额
     */
    private TextView invoiceAmountText;
    /**
     * 应收金额
     */
    private TextView needReceiveAmountText;
    /**
     * 已收金额
     */
    private TextView hasReceiveAmountText;
    /**是否有修改过数据*/
    private boolean  isHasEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesman_order_detais_activity);
        initView();
        getOrderDatailsInfo();
    }

    private void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isEditable = intent.getBooleanExtra("isEditable", false);
        initTitle(getString(R.string.order_details));
        titleLeftImg.setVisibility(View.VISIBLE);
        titleLeftImg.setOnClickListener(this);
        adapter = new OrderDetailsAdapter(mContext);
        ((OrderDetailsAdapter) adapter).setEditable(isEditable);
        initListView();
        View header = LayoutInflater.from(mContext).inflate(R.layout.salesman_order_details_header, null);
        initHeaderView(header);
        View footer = LayoutInflater.from(mContext).inflate(R.layout.salesman_order_details_footer, null);
        initFooterView(footer);
        if (isEditable) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (position < 2 || position > list.size() + 1) return;
                   OrderDetailsItem.Goods goods=(OrderDetailsItem.Goods)(list.get(position-2));
//                    boolean isSingle=goods.isFreegift()||goods.isVoucher();
                    String unitPrice=null;
                    if (goods.isVoucher()){
                        unitPrice=goods.getAmount();
                    }else {
                        unitPrice=String.valueOf(goods.getUnitPrice());
                    }
                    OpenDialog.getInstance().showTwoEditDialog(mContext, true,unitPrice
                            ,String.valueOf(goods.getCount()), new OpenDialog.OnTwoEditDialogClickListener() {
                        @Override
                        public void onClick(Dialog dialog, String firstContent, String secondContent) {
                            dialog.dismiss();
                            if (TextUtils.isEmpty(firstContent)) {
                                showShortToast(getString(R.string.input_price));
                                return;
                            }
                            if (TextUtils.isEmpty(secondContent)) {
                                showShortToast(getString(R.string.input_count));
                                return;
                            }
                            eidtGoodsInfo(position, firstContent, secondContent);
                        }
                    });
                }
            });
        }

        listView.setonTopRefreshListener(new RefreshListView.OnTopRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0;
                getOrderDatailsInfo();
            }
        });
        listView.setonBottomRefreshListener(new RefreshListView.OnBottomRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderDatailsInfo();
            }
        });
    }

    /**
     * 初始化底部
     *
     * @param footer
     */
    private void initFooterView(View footer) {
        TextView hasReceiveAmountTitleText=(TextView) footer.findViewById(R.id.hasReceiveAmountTitleText);
        TextView needReceiveAmountTitleText=(TextView) footer.findViewById(R.id.needReceiveAmountTitleText);
        TextView invoiceAmountTitleText=(TextView) footer.findViewById(R.id.invoiceAmountTitleText);
        totalAmountText = (TextView) footer.findViewById(R.id.totalAmountText);
        invoiceAmountText = (TextView) footer.findViewById(R.id.invoiceAmountText);
        needReceiveAmountText = (TextView) footer.findViewById(R.id.needReceiveAmountText);
        hasReceiveAmountText = (TextView) footer.findViewById(R.id.hasReceiveAmountText);
        if (isEditable){
            hasReceiveAmountTitleText.setVisibility(View.GONE);
            needReceiveAmountTitleText.setVisibility(View.GONE);
            invoiceAmountTitleText.setVisibility(View.GONE);
            hasReceiveAmountText.setVisibility(View.GONE);
            needReceiveAmountText.setVisibility(View.GONE);
            invoiceAmountText.setVisibility(View.GONE);
        }
        listView.addCustomFooterView(footer);
    }

    /**
     * 设置底部数据
     * @param orderDetailsItem
     */
    private void initFooterData(OrderDetailsItem orderDetailsItem) {
        totalAmountText.setText(orderDetailsItem.getTotalAmount());
        if(Float.parseFloat(orderDetailsItem.getInvoiceAmount())==0){
            invoiceAmountText.setTextColor(getResources().getColor(R.color.light_gray));
        }else{
            invoiceAmountText.setTextColor(getResources().getColor(R.color.black));
        }
        invoiceAmountText.setText(orderDetailsItem.getInvoiceAmount());
        needReceiveAmountText.setText(orderDetailsItem.getNeedReceiveAmount());
        hasReceiveAmountText.setText(orderDetailsItem.getHasReceiveAmount());
    }

    /**
     * 初始化头部
     *
     * @param header
     */
    private void initHeaderView(View header) {
        TextView checkPeopleTitleText=(TextView) header.findViewById(R.id.checkPeopleTitleText);
        TextView checkTimeTitleText=(TextView) header.findViewById(R.id.checkTimeTitleText);
        orderNumText = (TextView) header.findViewById(R.id.orderNumText);
        orderStateText = (TextView) header.findViewById(R.id.orderStateText);
        checkPeopleText = (TextView) header.findViewById(R.id.checkPeopleText);
        placeTimeText = (TextView) header.findViewById(R.id.placeTimeText);
        checkTimeText = (TextView) header.findViewById(R.id.checkTimeText);
        sellerNameText = (TextView) header.findViewById(R.id.sellerNameText);
        contactPersonText = (TextView) header.findViewById(R.id.contactPersonText);
        takeDeliveryPeopleText = (TextView) header.findViewById(R.id.takeDeliveryPeopleText);
        takeDeliveryAddressText = (TextView) header.findViewById(R.id.takeDeliveryAddressText);
        if (isEditable){
            checkPeopleTitleText.setVisibility(View.GONE);
            checkTimeTitleText.setVisibility(View.GONE);
            checkPeopleText.setVisibility(View.GONE);
            checkTimeText.setVisibility(View.GONE);
        }
        listView.addHeaderView(header, null, false);
    }

    @Override
    protected void reloadData() {
        getOrderDatailsInfo();
    }

    /**
     * 初始化头部数据
     *
     * @param orderDetailsItem
     */
    private void initHeaderData(OrderDetailsItem orderDetailsItem) {
        orderNumText.setText(orderDetailsItem.getOrderNum());
        OrderStateColorHelper.setTextColorByState(orderDetailsItem.getOrderStateNum(),orderStateText);
        orderStateText.setText(orderDetailsItem.getOrderState());
        checkPeopleText.setText(orderDetailsItem.getCheckPeople());
        placeTimeText.setText(orderDetailsItem.getPlaceTime());
        checkTimeText.setText(orderDetailsItem.getCheckTime());
        sellerNameText.setText(orderDetailsItem.getSellerName());
        contactPersonText.setText(orderDetailsItem.getContactPerson());
        takeDeliveryPeopleText.setText(orderDetailsItem.getTakeDeliveryPeople()+" "+orderDetailsItem.getTakeDeliveryTel());
        takeDeliveryAddressText.setText(orderDetailsItem.getTakeDeliveryAddress());
    }

    /**
     * 获取订单详情信息
     */
    private void getOrderDatailsInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        OkHttpUtils.post(Constant.getSalesmanOrderDetailsUrl()).params(params).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.getting_data));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                try {
                    if (response != null && response.code() == 200) {
                        if (start == 0) {
                            list.clear();
                        }
                        JSONObject obj = new JSONObject(s);
                        OrderDetailsItem orderDetailsItem = new OrderDetailsItem(obj);
                        initHeaderData(orderDetailsItem);
                        initFooterData(orderDetailsItem);
                        list.addAll(orderDetailsItem.getGoodsList());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
                listView.onRefreshComplete();
            }
        });
    }

    /**
     * 修改商品信息
     *
     * @param position 下标
     * @param price    单价
     * @param count    数量
     */
    private void eidtGoodsInfo(final int position, final String price, final String count) {
        final OrderDetailsItem.Goods goods = (OrderDetailsItem.Goods) list.get(position - 2);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", goods.getId());
        params.put("price", price);
        params.put("qty", count);
        if (goods.isVoucher()) {
            params.put("type", "voucher");
        } else {
            params.put("type", "wares");
        }

        OkHttpUtils.post(Constant.getEditOrderUrl()).params(params).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.submitting));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                try {
                    JSONObject obj = new JSONObject(s);
                    if (JsonUtils.isExistObj(obj, "#message")) {//失败
                        showShortToast(obj.optString("#message"));
                    } else {//成功
                        showShortToast(getString(R.string.submit_success));
                        isHasEdit=true;
                        OrderDetailsItem.Goods newGoods = goods;
                        int newCount=Integer.parseInt(count);
                        if (!goods.isVoucher()&&!goods.isFreegift()){//只有是普通商品的时候才会影响到总金额
                            float oldTotalAmount = Float.parseFloat(totalAmountText.getText().toString());
                            float oldAmount = Float.parseFloat(goods.getAmount());
                            float newPrice=Float.parseFloat(price);
                            float newAmount=newCount*newPrice;
                            newGoods.setUnitPrice(newPrice);
                            newGoods.setAmount(String.valueOf(newAmount));
                            double newTotalAmount = oldTotalAmount - oldAmount + newAmount;
                            totalAmountText.setText(newTotalAmount + "");
                        }
                        newGoods.setCount(newCount);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    showShortToast(getString(R.string.submit_fail));
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                showShortToast(getString(R.string.submit_fail));
            }

            @Override
            public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(isHasEdit){
            Intent result=new Intent();
            result.putExtra("totalAmount",totalAmountText.getText());
            setResult(RESULT_OK,result);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleLeftImg:
                onBackPressed();
                break;
        }
    }
}
