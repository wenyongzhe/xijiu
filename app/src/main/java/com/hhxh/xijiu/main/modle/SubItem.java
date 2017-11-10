package com.hhxh.xijiu.main.modle;

import com.hhxh.xijiu.base.modle.BaseItem;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 送货订单subItem
 * @auth lijq
 * @date 2016/9/26
 */
public class SubItem implements BaseItem ,Serializable{
    private String id;
    /**商品名称*/
    private String materialName;
    /**规格型号*/
    private String model;
    /**购买数量*/
    private int count;
    /**单位*/
    private String unit;
    /**商品标识*/
    private String spbh;
    /**已扫数量*/
    private int hasScanNum;
    /**是否是赠品*/
    private boolean isFreeGifts;
    /**是否需要扫码（不一定需要扫码,可以手动填数量）*/
    private boolean isNeedScan;
    /**每箱数量*/
    private int quantityPerBox;
    /**订单箱数*/
    private int  boxNum;
    /**已扫箱数*/
    private int  hasScanBoxNum;
    public SubItem(JSONObject obj){
        this.id=obj.optString("id");
        this.materialName=obj.optString("materialName");
        this.model=obj.optString("model");
        this.count=obj.optInt("qty");
        this.unit=obj.optString("measureunit");
        this.spbh=obj.optString("spbh");
        this.hasScanNum=obj.optInt("realqty");
        this.isFreeGifts="1".equals(obj.optString("isfreegifts"));
        this.isNeedScan="1".equals(obj.optString("scancode"));
        this.quantityPerBox=obj.optInt("perpackagebottleqty");
        if (quantityPerBox!=0){
            this.boxNum=count/quantityPerBox;
            this.hasScanBoxNum=hasScanNum/quantityPerBox;
        }
    }
    public SubItem(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHasScanNum() {
        return hasScanNum;
    }

    public void setHasScanNum(int hasScanNum) {
        this.hasScanNum = hasScanNum;
        if(quantityPerBox!=0){
            this.hasScanBoxNum=hasScanNum/quantityPerBox;
        }
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpbh() {
        return spbh;
    }

    public void setSpbh(String spbh) {
        this.spbh = spbh;
    }

    public boolean isFreeGifts() {
        return isFreeGifts;
    }

    public void setFreeGifts(boolean freeGifts) {
        isFreeGifts = freeGifts;
    }

    public boolean isNeedScan() {
        return isNeedScan;
    }

    public void setNeedScan(boolean needScan) {
        isNeedScan = needScan;
    }

    public int getQuantityPerBox() {
        return quantityPerBox;
    }

    public void setQuantityPerBox(int quantityPerBox) {
        this.quantityPerBox = quantityPerBox;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }

    public int getHasScanBoxNum() {
        return hasScanBoxNum;
    }

    public void setHasScanBoxNum(int hasScanBoxNum) {
        this.hasScanBoxNum = hasScanBoxNum;
    }
}
