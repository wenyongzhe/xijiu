package com.hhxh.xijiu.main.modle;

import com.hhxh.xijiu.base.modle.BaseItem;
import com.hhxh.xijiu.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：订单详情item
 *
 * @auth lijq
 * @date 2016/11/29
 */
public class OrderDetailsItem implements BaseItem {
    /**
     * 下单时间
     */
    private String placeTime;
    /**
     * 审核人
     */
    private String checkPeople;
    /**
     * 审核时间
     */
    private String checkTime;
    /**
     * 商家名称
     */
    private String sellerName;
    /**
     * 商家联系人
     */
    private String contactPerson;
    /**
     * 收货人
     */
    private String takeDeliveryPeople;
    /**
     * 收货人联系电话
     */
    private String takeDeliveryTel;
    /**
     * 收货地址
     */
    private String takeDeliveryAddress;
    /**
     * 订单编号
     */
    private String orderNum;
    /**
     * 订单状态
     */
    private String orderState;
    /**
     * 订单状态(10:未确认,20://已提交,25://已审核,30://部分开票,40://全部开票,100://作废)
     */
    private int orderStateNum;
    /**
     * 总金额
     */
    private String totalAmount;
    /**
     * 已开发票金额
     */
    private String invoiceAmount;
    /**
     * 应收金额
     */
    private String needReceiveAmount;
    /**
     * 已收金额
     */
    private String hasReceiveAmount;
    /**
     * 商品列表
     */
    private List<BaseItem> goodsList;

    public OrderDetailsItem(JSONObject obj) {
        this.placeTime = obj.optString("bizdate");
        if(obj.optJSONObject("auditorid")!=null){
            this.checkPeople = obj.optJSONObject("auditorid").optString("name");
        }
        this.checkTime = obj.optString("auditdate");
        this.sellerName = obj.optString("dealername");
        this.contactPerson = obj.optString("dealermaincontactperson") + " " + obj.optString("dealercontacttel");
        this.takeDeliveryPeople = obj.optString("contactperson");
        this.takeDeliveryTel = obj.optString("contacttel");
        this.takeDeliveryAddress = obj.optString("address");
        this.orderNum = obj.optString("number");
        JSONObject stateObj= obj.optJSONObject("billstatus");
        if (stateObj!=null){
            this.orderState =stateObj.optString("title");
            this.orderStateNum=stateObj.optInt("value");
        }
        this.totalAmount = obj.optString("totalamout");
        this.needReceiveAmount = obj.optString("jhamount");
        this.hasReceiveAmount = obj.optString("ysamount");
        this.invoiceAmount = obj.optString("billtotalamount");
        this.goodsList = new ArrayList<>();
        JSONArray array = obj.optJSONArray("items");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                Goods goods = new Goods(object);
                this.goodsList.add(goods);
            }
        }
        JSONArray voucherArray = obj.optJSONArray("items2");
        if (voucherArray != null) {
            for (int i = 0; i < voucherArray.length(); i++) {
                JSONObject object = voucherArray.optJSONObject(i);
                Goods goods = new Goods(object);
                goods.setVoucher(true);
                this.goodsList.add(goods);
            }
        }
    }

    public class Goods implements BaseItem {
        private String id;
        /**
         * 是否赠品
         */
        private boolean isFreegift;
        /**
         * 数量
         */
        private int count;
        /**
         * 商品名
         */
        private String name;
        /**
         * 单位
         */
        private String unit;
        /**
         * 单价
         */
        private float unitPrice;

        /**
         * 金额
         */
        private String amount;
        /**
         * 是否是代金卷
         */
        private boolean isVoucher;

        public Goods(JSONObject obj) {
            this.id = obj.optString("id");
            JSONObject isfreegiftsObj = obj.optJSONObject("isfreegifts");
            if (isfreegiftsObj != null) {
                this.isFreegift = "1".equals(isfreegiftsObj.optString("value"));
            }
            if (JsonUtils.isExistObj(obj,"model")){
                this.name = obj.optString("model");
            }else{
                this.name=obj.optJSONObject("voucherid").optString("name");
            }

            this.unit = obj.optString("measureunitid");
            this.unitPrice = obj.optInt("price");
            this.amount = obj.optString("amount");
            this.count = obj.optInt("qty");
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isFreegift() {
            return isFreegift;
        }

        public void setFreegift(boolean freegift) {
            isFreegift = freegift;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public boolean isVoucher() {
            return isVoucher;
        }

        public void setVoucher(boolean voucher) {
            isVoucher = voucher;
        }

        public float getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(float unitPrice) {
            this.unitPrice = unitPrice;
        }

    }

    public String getPlaceTime() {
        return placeTime;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getTakeDeliveryPeople() {
        return takeDeliveryPeople;
    }

    public String getTakeDeliveryTel() {
        return takeDeliveryTel;
    }

    public void setTakeDeliveryTel(String takeDeliveryTel) {
        this.takeDeliveryTel = takeDeliveryTel;
    }

    public String getTakeDeliveryAddress() {
        return takeDeliveryAddress;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setPlaceTime(String placeTime) {
        this.placeTime = placeTime;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setTakeDeliveryPeople(String takeDeliveryPeople) {
        this.takeDeliveryPeople = takeDeliveryPeople;
    }

    public void setTakeDeliveryAddress(String takeDeliveryAddress) {
        this.takeDeliveryAddress = takeDeliveryAddress;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public int getOrderStateNum() {
        return orderStateNum;
    }

    public void setOrderStateNum(int orderStateNum) {
        this.orderStateNum = orderStateNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getNeedReceiveAmount() {
        return needReceiveAmount;
    }

    public void setNeedReceiveAmount(String needReceiveAmount) {
        this.needReceiveAmount = needReceiveAmount;
    }

    public String getHasReceiveAmount() {
        return hasReceiveAmount;
    }

    public void setHasReceiveAmount(String hasReceiveAmount) {
        this.hasReceiveAmount = hasReceiveAmount;
    }

    public List<BaseItem> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<BaseItem> goodsList) {
        this.goodsList = goodsList;
    }
}
