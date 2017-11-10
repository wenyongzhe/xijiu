package com.hhxh.xijiu.main.modle;

import com.hhxh.xijiu.base.modle.BaseItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 未完成订单adapter
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class OrderNotCompleteItem implements BaseItem ,Serializable{
    private String id;
    /**
     * 配送编号
     */
    private String deliveryNum;
    /**
     * 订单编号
     */
    private String orderNum;
    /**
     * 订单状态
     */
    private String orderState;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 联系人
     */
    private String contactPerson;
    /***/
    private String contactTel;
    /**
     * 地址
     */
    private String address;
    /**
     * 发货仓库
     */
    private String depot;
    /**
     * 分录集合
     */
    private List<SubItem> subItemList;
    /**
     * 总金额
     */
    private String totalAmount;
    /**订单状态*/
    private String state;

    public OrderNotCompleteItem() {
    }

    public OrderNotCompleteItem(JSONObject obj) {
        try {
            this.id = obj.optString("id");
            this.deliveryNum = obj.optString("nunber");
            JSONObject orderObj= obj.optJSONObject("orderid");
            if (orderObj!=null){
                this.orderNum =orderObj.optString("title");
            }
            JSONObject buyerObj= obj.optJSONObject("buyerid");
            if (buyerObj!=null){
                this.customerName =buyerObj.optString("title");
            }

            JSONObject depotObj=obj.optJSONObject("warehouseid");
            if (depotObj!=null){
                this.depot = depotObj.optString("name");
            }
            this.orderState = obj.optString("billsatus");
            this.contactTel = obj.optString("contacttel");
            this.contactPerson = obj.optString("contactperson");
            this.totalAmount = obj.optString("ckd_totalamount");
            this.address = obj.optString("address");
            JSONObject stateObj=obj.optJSONObject("billstatus");
            if (stateObj!=null){
                this.state=stateObj.optString("title");
            }
            this.subItemList = new ArrayList<>();
            JSONArray array = obj.optJSONArray("ckdfl_list");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    SubItem subItem = new SubItem(array.getJSONObject(i));
                    this.subItemList.add(subItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {

        return totalAmount;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getOrderState() {
        return orderState;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public List<SubItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }
}
