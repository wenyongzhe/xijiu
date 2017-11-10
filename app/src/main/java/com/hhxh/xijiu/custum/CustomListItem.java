package com.hhxh.xijiu.custum;

import com.hhxh.xijiu.base.modle.BaseItem;

import org.json.JSONObject;

/**
 * Created by qiaocb.
 * DateTime 2017/4/15 16:24.
 */

public class CustomListItem implements BaseItem {
    private String personId;
    private String personName;
    private String phone;
    private boolean isSelected;

    public CustomListItem(){}

    public CustomListItem(JSONObject obj){
        this.personId = obj.optString("id");
        this.personName = obj.optString("name");
        this.phone = obj.optString("mobile_phone");
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
