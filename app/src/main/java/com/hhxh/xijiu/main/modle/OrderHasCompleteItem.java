package com.hhxh.xijiu.main.modle;

import com.hhxh.xijiu.base.modle.BaseItem;
import com.hhxh.xijiu.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth lijq
 * @date 2016-9-2
 * 已完成订单item
 */
public class OrderHasCompleteItem implements BaseItem {
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
     * 总金额
     */
    private String totalAmount;
    /**
     * 分录集合
     */
    private List<SubItem> subItemList;
    /**交货描述*/
    private String description;
    /**订单状态*/
    private String state;
    /**实收金额*/
    private String paidAmount;
    /**应收金额*/
    private String needReciverAmount;
    /**是否需要收款*/
    private boolean isNeedReciveAmount;
    public OrderHasCompleteItem() {

    }

    public OrderHasCompleteItem(JSONObject obj) {
        try {
            this.id = obj.optString("id");
            this.deliveryNum = obj.optString("nunber");
            JSONObject orderObj= obj.optJSONObject("orderid");
            if (orderObj!=null){
                this.orderNum =orderObj.optString("title");
            }
            this.orderState = obj.optString("billsatus");
            JSONObject buyerObj= obj.optJSONObject("buyerid");
            if (buyerObj!=null){
                this.customerName =buyerObj.optString("title");
            }

            this.contactPerson = obj.optString("contactperson");

            JSONObject depotObj=obj.optJSONObject("warehouseid");
            if (depotObj!=null){
                this.depot = depotObj.optString("name");
            }

            this.contactTel = obj.optString("contacttel");
            this.totalAmount = obj.optString("ckd_totalamount");
            this.address = obj.optString("address");
            this.description=obj.optString("jhdescription");
            JSONObject stateObj=obj.optJSONObject("billstatus");
            if (stateObj!=null){
                this.state=stateObj.optString("title");
            }
            this.needReciverAmount=obj.optString("jhamount");
            this.paidAmount=obj.optString("ckd_yfkje");
            this.isNeedReciveAmount=this.needReciverAmount.equals(this.paidAmount)?false:true;
            this.subItemList = new ArrayList<>();
            if (JsonUtils.isExistObj(obj, "ckdfl_list")) {
                JSONArray array = obj.optJSONArray("ckdfl_list");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        SubItem subItem = new SubItem(array.getJSONObject(i));
                        this.subItemList.add(subItem);
                    }
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

    public void setAddress(String address) {
        this.address = address;
    }


    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {

        return totalAmount;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
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

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public List<SubItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getNeedReciverAmount() {
        return needReciverAmount;
    }

    public void setNeedReciverAmount(String needReciverAmount) {
        this.needReciverAmount = needReciverAmount;
    }

    public boolean isNeedReciveAmount() {
        return isNeedReciveAmount;
    }

    public void setNeedReciveAmount(boolean needReciveAmount) {
        isNeedReciveAmount = needReciveAmount;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }
}
