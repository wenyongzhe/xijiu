package com.hhxh.xijiu.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.modle.BaseItem;
import com.hhxh.xijiu.base.view.BaseSearchFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.main.adapter.OrderNotCompletedAdapter;
import com.hhxh.xijiu.main.modle.OrderNotCompleteItem;
import com.hhxh.xijiu.main.modle.SubItem;
import com.hhxh.xijiu.scan.view.CaptureActivity;
import com.hhxh.xijiu.utils.HhxhLog;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.OpenDialog;
import com.hhxh.xijiu.utils.StringUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 订单未完成 fragment
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class OrderNotCompletedFragment extends BaseSearchFragment implements OrderNotCompletedAdapter.CallBack {

    /**
     * 即将扫码的subList
     */
    private List<SubItem> toScanSubItemList;
    /**
     * 要提交的扫描到的箱信息
     */
    private List<String> scanedBoxInfo;
    /**
     * 要提交的扫描到的瓶信息
     */
    private List<HashMap<String, String>> scanedBottleInfo;
    /**
     * 用来记录所有已扫到的瓶码
     */
    private List<String> allScannedBottleList;
    /***
     * 已经扫过的二维码
     */
    private List<String> hasScanCodeList;
    /**正在操作的订单id*/
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_has_completed_fragment, null);
        initListView(view);
        reloadData();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isNeedUpdata) {
            getNotCompleteData();
        }
    }

    @Override
    protected void initListView(View view) {
        adapter = new OrderNotCompletedAdapter(mContext);
        ((OrderNotCompletedAdapter) adapter).setmCallBack(this);
        super.initListView(view);
        listView.setonTopRefreshListener(new RefreshListView.OnTopRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0;
                getNotCompleteData();
            }
        });

        listView.setonBottomRefreshListener(new RefreshListView.OnBottomRefreshListener() {
            @Override
            public void onRefresh() {
                getNotCompleteData();
            }
        });
    }

    @Override
    protected void reloadData() {
        start = 0;
        getNotCompleteData();
    }

    /**
     * 获取未完成数据
     */
    private void getNotCompleteData() {
        HashMap<String,String> params=new HashMap<>();
        params.put("billstatus","30");
//        params.put("__ORDER_BY__","c_xdrq asc");
        OkHttpUtils.get(Constant.getDeliveryUrl()).params(params).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
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
                        JSONArray array = obj.optJSONArray("info_list");
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                OrderNotCompleteItem item = new OrderNotCompleteItem(array.optJSONObject(i));
                                list.add(item);
                            }
                        }
                        setHasScanCodeNum();
                        isNeedUpdata = false;
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                showShortToast(getString(R.string.get_data_fail));

            }
            @Override
            public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onAfter(isFromCache, s, call, response, e);
                stopProgressDialog();
                listView.onRefreshComplete();
            }

        });
    }


    @Override
    public void search(String s) {
        if (!StringUtil.isEmpty(s)) {
            OkHttpUtils.get(Constant.getDeliveryUrl()).params("billsatus", "30")
                    .params("__KEYWORD__", s)
                    .execute(new StringCallback() {
                        @Override
                        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                            try {
                                if (response != null && response.code() == 200) {
                                    list.clear();
                                    JSONObject obj = new JSONObject(s);
                                    JSONArray array = obj.optJSONArray("info_list");
                                    if (array != null) {
                                        for (int i = 0; i < array.length(); i++) {
                                            OrderNotCompleteItem item = new OrderNotCompleteItem(array.optJSONObject(i));
                                            list.add(item);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            start = 0;
            getNotCompleteData();
        }

    }

    /**
     * 扫码
     *
     * @param item
     */
    @Override
    public void onScanCallBack(OrderNotCompleteItem item, int position) {
//        if(!NetUtil.getInstance().isConnected(mContext)){
//            showShortToast(getString(R.string.conn_fail));
//            return;
//        }
        //当操作的订单变化时先清空前次扫码保存的信息
        if (!TextUtils.isEmpty(id)&&!id.equals(item.getId())){
            resetScanedInfo();
        }
        //保存当前扫码的订单id
        this.id=item.getId();
        this.startCameraIntent= new Intent(mContext, CaptureActivity.class);
        if (item != null) {
            List<SubItem> subItemList = item.getSubItemList();
            initScanNeedRecordInfo();
            toScanSubItemList.clear();
            toScanSubItemList.addAll(subItemList);
            startCameraIntent.putExtra("subList", (Serializable) toScanSubItemList);
            startCameraIntent.putExtra("scanedBoxInfo", (Serializable) scanedBoxInfo);
            startCameraIntent.putExtra("scanedBottleInfo", (Serializable) scanedBottleInfo);
            startCameraIntent.putExtra("hasScanCodeList", (Serializable) hasScanCodeList);
            startCameraIntent.putExtra("allScannedBottleList", (Serializable) allScannedBottleList);
        }
        checkCameraPermission();
    }



    /**
     * 交货按钮点击事件处理
     *
     * @param id
     */
    @Override
    public void deliveryGoods(final String id, final int position) {
        //检查扫码数量
        if (checkHasScanedCount(position)) {
            OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, getString(R.string.confirm_submit)
                    , getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            submitGoodsData(id, null,position);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            final String reason= mContext.getString(R.string.input_reason);
            OpenDialog.getInstance().showEditContentDialog(mContext, null, null,reason, false
                    , new OpenDialog.OnEditContentDialogClickListener() {
                        @Override
                        public void onClick(Dialog dialog, String content) {
                            if (TextUtils.isEmpty(content)) {
                                showToast(reason, Toast.LENGTH_SHORT);
                                return;
                            }
                            dialog.dismiss();
                            submitGoodsData(id, content,position);
                        }
                    }
            );
        }
    }

    /**
     * 提交货物信息
     *
     * @param id
     */
    private void submitGoodsData(String id, String reason,int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("data", getScanedCodeInfo(id,position));
        if (!TextUtils.isEmpty(reason)) {
            params.put("jhdescription", reason);
        }
        HhxhLog.i("id:" + id + "\n" + "data:" + getScanedCodeInfo(id,position));
        OkHttpUtils.post(Constant.getSubmitCodeInfoUrl()).params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        startProgressDialog(getString(R.string.submitting));
                    }

                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            stopProgressDialog();
                            JSONObject obj = new JSONObject(s);
                            if (JsonUtils.isExistObj(obj, "returns")) {//成功
                                resetScanedInfo();
                                showShortToast(getString(R.string.submit_success));
                                getNotCompleteData();
                                //让已完成界面刷新
                                ((MainActivity) getActivity()).setIsNeedUpdata(1);
                                return;
                            }
                            if ("com.hhxh.industry.common.res.text.Ptywyc".equals(obj.optString("#code"))) {//业务异常
                                showShortToast(obj.optString("#message"));
                            } else if ("com.hhxh.industry.common.res.text.Ptxtyc".equals(obj.optString("#code"))) {//系统异常
                                showShortToast(getString(R.string.system_exception));
                            } else {//提交失败
                                showShortToast(getString(R.string.submit_fail));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        stopProgressDialog();
                        showShortToast(getString(R.string.submit_fail));
                    }
                });
    }

    /**
     * 初始化扫码要记录的信息
     */
    private  void initScanNeedRecordInfo(){
        if (toScanSubItemList == null) {
           toScanSubItemList =new ArrayList<>();
        }
        if (scanedBottleInfo==null){
            scanedBottleInfo=new ArrayList<>();
        }
        if (scanedBoxInfo==null){
            scanedBoxInfo=new ArrayList<>();
        }
        if (hasScanCodeList==null){
            hasScanCodeList=new ArrayList<>();
        }
        if(allScannedBottleList==null){
            allScannedBottleList=new ArrayList<>();
        }

    }
    /**
     * 重置扫描到的信息
     */
    private void resetScanedInfo(){
        if (toScanSubItemList != null&& toScanSubItemList.size()>0) {
            toScanSubItemList.clear();
        }
        if (scanedBottleInfo!=null&&scanedBottleInfo.size()>0){
                scanedBottleInfo.clear();
                scanedBottleInfo = null;
        }
        if (scanedBoxInfo!=null&&scanedBoxInfo.size()>0){
                scanedBoxInfo.clear();
        }
        if (hasScanCodeList!=null&&hasScanCodeList.size()>0){
            hasScanCodeList.clear();
        }
        if (allScannedBottleList!=null&&allScannedBottleList.size()>0){
            allScannedBottleList.clear();
        }
        this.id=null;
    }
    /**
     * 检查已扫数量是否等于要扫数量
     */
    private boolean checkHasScanedCount(int position) {
        OrderNotCompleteItem item = (OrderNotCompleteItem) list.get(position);
        List<SubItem> subItemList = item.getSubItemList();
        for (SubItem subItem : subItemList) {
            if (subItem.getHasScanNum() < subItem.getCount()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取扫描到的数据
     *
     * @return
     */
    private String getScanedCodeInfo(String id,int position) {
        JSONObject data = new JSONObject();
        try {
            JSONObject map = new JSONObject();
            List<SubItem> subList = ((OrderNotCompleteItem)list.get(position)).getSubItemList();
            for (BaseItem item : subList) {
                SubItem subItem = (SubItem) item;
                map.put(subItem.getId(), subItem.getHasScanNum());
            }
            data.put("qtyMap", map);
            JSONObject allCodesObj = new JSONObject();
            //验证保存的扫码信息是否属于当前提交的订单,防止错误的提交
            boolean  verifyOrder=false;
            if (!TextUtils.isEmpty(this.id)&&this.id.equals(id)){
                verifyOrder=true;
            }
            //箱
            JSONArray boxCodeArray = new JSONArray();
            if (scanedBoxInfo!=null&&verifyOrder){
                for (String info : scanedBoxInfo) {
                    boxCodeArray.put(info);
                }
            }
            //瓶
            JSONArray bottleCodeArray = new JSONArray();
            if (scanedBottleInfo!=null&&verifyOrder){
                for (HashMap<String, String> info : scanedBottleInfo) {
                    JSONObject obj = new JSONObject();
                    Iterator it = info.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = (Map.Entry) it.next();
                        obj.put(entry.getKey(), entry.getValue());
                    }
                    bottleCodeArray.put(obj);
                }
            }
            allCodesObj.put("boxs", boxCodeArray);
            allCodesObj.put("bottle", bottleCodeArray);
            data.put("qrCodes", allCodesObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    /***
     * 设置已扫的数量
     */
    private void setHasScanCodeNum() {
        if (toScanSubItemList != null) {
            for (int i=0;i<list.size();i++){
                OrderNotCompleteItem item= (OrderNotCompleteItem) list.get(i);
                if (item.getId().equals(id)){
                    List<SubItem> subItemList = item.getSubItemList();
                    subItemList.clear();
                    subItemList.addAll(this.toScanSubItemList);
                    ((OrderNotCompletedAdapter) adapter).notifySubItemDataChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CAMERA_REQUESTE_CODE && resultCode == Activity.RESULT_OK) {//扫码返回
            toScanSubItemList= (List<SubItem>) data.getSerializableExtra("subList" );
            scanedBoxInfo= (List<String>) data.getSerializableExtra("scanedBoxInfo" );
            scanedBottleInfo= (List<HashMap<String, String>>) data.getSerializableExtra("scanedBottleInfo");
            hasScanCodeList= (List<String>) data.getSerializableExtra("hasScanCodeList");
            setHasScanCodeNum();
            HhxhLog.i("dataChanged");
            adapter.notifyDataSetChanged();
        }
    }
}
