package com.hhxh.xijiu.main.modle;

import com.hhxh.xijiu.base.modle.BaseItem;

import org.json.JSONObject;

/**
 * 功能描述：已审核item
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class HasCheckedItem implements BaseItem {
    /**订单id*/
    private String id;
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
     * 客户名称
     */
    private String customerName;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactTel;
    /**
     * 时间
     */
    private String time;
    /**
     * 总金额
     */
    private String totalAmount;

    private boolean  isSelsected;

    public HasCheckedItem(JSONObject obj) {
        this.id=obj.optString("id");
        this.orderNum = obj.optString("number");
        JSONObject statusObj= obj.optJSONObject("billstatus");
        if (statusObj!=null){
            this.orderState =statusObj.optString("title");
            this.orderStateNum=statusObj.optInt("value");
        }
        JSONObject buyerObj=obj.optJSONObject("buyerid");
        if (buyerObj!=null){
            this.customerName = buyerObj.optString("title");
        }
        this.contactPerson = obj.optString("contactperson");
        this.contactTel = obj.optString("contacttel");
        this.totalAmount = obj.optString("totalamout");
        this.time=obj.optString("bizdate");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isSelsected() {
        return isSelsected;
    }

    public int getOrderStateNum() {
        return orderStateNum;
    }

    public void setOrderStateNum(int orderStateNum) {
        this.orderStateNum = orderStateNum;
    }

    public void setSelsected(boolean selsected) {
        isSelsected = selsected;
    }
}
