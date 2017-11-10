package com.hhxh.xijiu.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.view.BaseSearchFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.main.adapter.HasCheckedAdapter;
import com.hhxh.xijiu.main.modle.HasCheckedItem;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.OpenDialog;
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
 * 功能描述：已审核订单界面
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class HasCheckedFragment extends BaseSearchFragment implements HasCheckedAdapter.CallBack{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_has_completed_fragment, null);
        initListView(view);
        return view;
    }
    @Override
    protected void reloadData() {
        start=0;
        getHasCheckedData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isNeedUpdata) {
            getHasCheckedData();
        }
    }

    @Override
    protected void initListView(View view) {
        adapter = new HasCheckedAdapter(mContext);
        ((HasCheckedAdapter)adapter).setmCallBack(this);
        super.initListView(view);
        listView.setonTopRefreshListener(new RefreshListView.OnTopRefreshListener() {
            @Override
            public void onRefresh() {
                start=0;
                getHasCheckedData();
            }
        });

        listView.setonBottomRefreshListener(new RefreshListView.OnBottomRefreshListener() {
            @Override
            public void onRefresh() {
                getHasCheckedData();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 1 || position > list.size()) return;
                Intent intent=new Intent(mContext, SalesmanOrderDetaisActivity.class);
                HasCheckedItem item = (HasCheckedItem) list.get(position-1);
                intent.putExtra("id",item.getId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void search(String s) {
        HashMap<String, String> params = new HashMap<>();
        params.put("__FILTER__", "billstatus=25,30,40,100");
        params.put("__KEYWORD__", s);
        OkHttpUtils.post(Constant.getSalesmanOrderListUrl()).params(params).execute(new StringCallback() {

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                try {
                    if (response != null && response.code() == 200) {
                        list.clear();
                        JSONObject obj = new JSONObject(s);
                        JSONArray array = obj.optJSONArray("info_list");
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                HasCheckedItem item = new HasCheckedItem(array.optJSONObject(i));
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
    }

    /**
     * 获取已审核的订单数据
     */
    private  void getHasCheckedData(){
        HashMap<String,String> params=new HashMap<>();
        params.put("salesid", UserPrefs.getUserId());
        params.put("billstatus", "25,30,40,100");
        OkHttpUtils.post(Constant.getSalesmanOrderListUrl()).params(params).execute(new StringCallback() {
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
                        JSONArray array = obj.optJSONArray("info_list");
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                HasCheckedItem item = new HasCheckedItem(array.optJSONObject(i));
                                list.add(item);
                            }
                        }
                        isNeedUpdata = false;
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
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

    /***
     * 反审核回调
     *
     * @param item
     */
    @Override
    public void onClick(final HasCheckedItem item) {
        OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, getString(R.string.is_sure_uncheck_order)
                , getString(R.string.confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        uncheckOrder(item);
                    }
                }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 反审核订单
     * @param item
     */
    private void uncheckOrder(HasCheckedItem item){
        HashMap<String, String> params = new HashMap<>();
        params.put("id", item.getId());
        OkHttpUtils.post(Constant.getUncheckOrderUrl()).params(params).execute(new StringCallback() {
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.submitting));
            }
            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                stopProgressDialog();
                try{
                    JSONObject obj = new JSONObject(s);
                    if (JsonUtils.isExistObj(obj, "#message")) {//失败
                        showShortToast(obj.optString("#message"));
                    }
                }catch (Exception e){//成功
                    showShortToast(getString(R.string.submit_success));
                    getHasCheckedData();
                    ((MainActivity)getActivity()).setIsNeedUpdata(0);
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
            }
        });
    }

}
