package com.hhxh.xijiu.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseSearchFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.main.adapter.OrderHasCompletedAdapter;
import com.hhxh.xijiu.main.modle.OrderHasCompleteItem;
import com.hhxh.xijiu.utils.StringUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 订单已完成 fragment
 *
 * @auth lijq
 * @date 2016-9-2
 */
public class OrderHasCompletedFragment extends BaseSearchFragment {
    /**收款请求码*/
    public static final int RECEIVE_AMOUNT_REQUEST_CODE=0x11;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_has_completed_fragment, null);
        initListView(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isNeedUpdata) {
            getHasCompletedData();
        }
    }


    @Override
    protected void initListView(View view) {
        adapter = new OrderHasCompletedAdapter(mContext);
        super.initListView(view);
        listView.setonTopRefreshListener(new RefreshListView.OnTopRefreshListener() {
            @Override
            public void onRefresh() {
                start=0;
                getHasCompletedData();
            }
        });

        listView.setonBottomRefreshListener(new RefreshListView.OnBottomRefreshListener() {
            @Override
            public void onRefresh() {
                getHasCompletedData();
            }
        });
    }

    @Override
    protected void reloadData() {
        start=0;
        getHasCompletedData();
    }

    /**
     * 获取已完成数据
     */
    private void getHasCompletedData() {
        HashMap<String,String> params=new HashMap<>();
        params.put("__FILTER__","billstatus=40,50");
//        params.put("__ORDER_BY__","jhdate desc");
        OkHttpUtils.post(Constant.getDeliveryUrl()).params(params).execute(new StringCallback() {
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
                                OrderHasCompleteItem item = new OrderHasCompleteItem(array.optJSONObject(i));
                                list.add(item);
                            }
                        }
                        isNeedUpdata=false;
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                showToast(getString(R.string.get_data_fail), Toast.LENGTH_SHORT);
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
        if (!StringUtil.isEmpty(s)){
            OkHttpUtils.get(Constant.getDeliveryUrl())
                    .params("billstatus", "40")
                    .params("__KEYWORD__", s)
                    .execute(new StringCallback() {
                @Override
                public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                    try {
                        if (response != null && response.code() == 200) {
                            if (start==0){
                                list.clear();
                            }
                            JSONObject obj = new JSONObject(s);
                            JSONArray array = obj.optJSONArray("info_list");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    OrderHasCompleteItem item = new OrderHasCompleteItem(array.optJSONObject(i));
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
        }else{
            getHasCompletedData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK==resultCode){
            switch (requestCode){
                case RECEIVE_AMOUNT_REQUEST_CODE://收款返回
                    getHasCompletedData();
                    break;
            }
        }
    }
}
