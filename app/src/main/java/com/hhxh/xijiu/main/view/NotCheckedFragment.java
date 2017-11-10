package com.hhxh.xijiu.main.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.base.modle.BaseItem;
import com.hhxh.xijiu.base.view.BaseSearchFragment;
import com.hhxh.xijiu.configs.Constant;
import com.hhxh.xijiu.custum.CustomListAdapter;
import com.hhxh.xijiu.custum.CustomListDialog;
import com.hhxh.xijiu.custum.CustomListItem;
import com.hhxh.xijiu.custum.RefreshListView;
import com.hhxh.xijiu.data.UserPrefs;
import com.hhxh.xijiu.main.adapter.NotCheckedAdapter;
import com.hhxh.xijiu.main.modle.NotCheckedItem;
import com.hhxh.xijiu.utils.JsonUtils;
import com.hhxh.xijiu.utils.OpenDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述：未审核订单
 *
 * @auth lijq
 * @date 2016/11/28
 */
public class NotCheckedFragment extends BaseSearchFragment implements NotCheckedAdapter.CallBack {
    /**
     * 详情请求码
     */
    public static final int DETAILS_REQUESTCODE = 0x11;
    private int curPosition;

    /**
     * 指派人对话框
     */
    private CustomListDialog listDialog;
    /**
     * 指派人Adapter
     */
    private CustomListAdapter dialogAdapter;
    /**
     * 指派人list
     */
    private List<BaseItem> personList;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 初始指派人对话框
     */
    private void initPersonDialog() {
        personList = new ArrayList<>();
        dialogAdapter = new CustomListAdapter(mContext);
        CustomListDialog.Builder myCenterBuilder = new CustomListDialog.Builder(
                mContext);
        myCenterBuilder.setListViewAdapter(dialogAdapter);
        myCenterBuilder.setItemClickListener(onItemClickListener);
        myCenterBuilder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listDialog.dismiss();
            }
        });
        myCenterBuilder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int length = personList.size();
                if(length > 0){
                   for (int i = 0;i<length; i++){
                      CustomListItem item =  (CustomListItem)personList.get(i);
                       if(item.isSelected()){
                           setDesignate(item.getPersonId());
                           listDialog.dismiss();
                           break;
                       }
                   }
                }

            }
        });
        listDialog = myCenterBuilder.create();
//        Window dialogWindow = listDialog.getWindow();
//        dialogWindow.setWindowAnimations(R.style.AnimationFade);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
//        lp.width = DensityUtil.getInstance().getScreenWidth(mContext) / 3;
//        lp.y = getResources().getDimensionPixelSize(R.dimen.title_height);
//        dialogWindow.setAttributes(lp);
    }

    /**
     * 中级题库对话框点击事件
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
//            CustomListItem it = (CustomListItem) personList.get(arg2);
//            ViewHolder holder = (ViewHolder)arg1.getTag();
//            holder.selectCheck.toggle();
//            it.setSelected(holder.selectCheck.isChecked());
            int length = personList.size();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    CustomListItem it = (CustomListItem) personList.get(i);
                    if (i == arg2) {
                        it.setSelected(true);
                    } else {
                        it.setSelected(false);
                    }
                }
            }
            dialogAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_has_completed_fragment, null);
        initListView(view);
        initPersonDialog();
        reloadData();
        return view;
    }

    @Override
    protected void reloadData() {
        start = 0;
        getNotCheckedData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isNeedUpdata) {
            getNotCheckedData();
        }
    }

    @Override
    protected void initListView(View view) {
        adapter = new NotCheckedAdapter(mContext);
        ((NotCheckedAdapter) adapter).setmCallBack(this);
        super.initListView(view);
        listView.setonTopRefreshListener(new RefreshListView.OnTopRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0;
                getNotCheckedData();
            }
        });

        listView.setonBottomRefreshListener(new RefreshListView.OnBottomRefreshListener() {
            @Override
            public void onRefresh() {
                getNotCheckedData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 1 || position > list.size()) return;
                Intent intent = new Intent(mContext, SalesmanOrderDetaisActivity.class);
                NotCheckedItem item = (NotCheckedItem) list.get(position - 1);
                intent.putExtra("id", item.getId());
                intent.putExtra("isEditable", true);
                curPosition = position;
                startActivityForResult(intent, DETAILS_REQUESTCODE);
            }
        });

    }

    @Override
    public void search(String s) {
        HashMap<String, String> params = new HashMap<>();
        params.put("__FILTER__", "billstatus=20");
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
                                NotCheckedItem item = new NotCheckedItem(array.optJSONObject(i));
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
     * 获取未审核的订单数据
     */
    private void getNotCheckedData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("salesid", UserPrefs.getUserId());
//        params.put("__FILTER__", "billstatus=20");
        params.put("billstatus","20");
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
                                NotCheckedItem item = new NotCheckedItem(array.optJSONObject(i));
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
     * 审核回调
     *
     * @param item
     */
    @Override
    public void onClick(final NotCheckedItem item) {
        OpenDialog.getInstance().showTwoBtnListenerDialog(mContext, getString(R.string.is_sure_check_order)
                , getString(R.string.confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkOrder(item);
                    }
                }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /***
     * 指派回调
     *
     * @param item
     */
    @Override
    public void onDesignate(final NotCheckedItem item) {
            gerDesignate(item);
    }

    /**
     * 审核订单
     *
     * @param item
     */
    private void checkOrder(NotCheckedItem item) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", item.getId());
        OkHttpUtils.post(Constant.getCheckOrderUrl()).params(params).execute(new StringCallback() {
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.submitting));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                stopProgressDialog();
                try {
                    JSONObject obj = new JSONObject(s);
                    if (JsonUtils.isExistObj(obj, "#message")) {//失败
                        showShortToast(obj.optString("#message"));
                    }
                } catch (Exception e) {//成功
                    showShortToast(getString(R.string.submit_success));
                    getNotCheckedData();
                    ((MainActivity) getActivity()).setIsNeedUpdata(1);
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
            }
        });
    }

    /**
     * 获取业务员列表
     *
     * @param item
     */
    private void gerDesignate(NotCheckedItem item) {
        orderId = item.getId();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", orderId);
        OkHttpUtils.post(Constant.getDesignateUrl()).params(params).execute(new StringCallback() {
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.submitting));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                stopProgressDialog();
                try {
                    JSONObject obj = new JSONObject(s);
                    if (JsonUtils.isExistObj(obj, "#message")) {//失败
                        showShortToast(obj.optString("#message"));
                    }
                    if (personList == null) {
                        personList = new ArrayList<BaseItem>();
                    }
                    if (personList.size() > 0) {
                        personList.clear();
                    }
                    if (JsonUtils.isExistObj(obj, "listsp")) {
                        JSONArray jArray = obj.optJSONArray("listsp");
                        int length = jArray.length();
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                CustomListItem item = new CustomListItem(jArray.optJSONObject(i));
                                if(item.getPersonId().equals(UserPrefs.getUserId())){
                                    continue;
                                }
                                personList.add(item);
                            }
                        }
                    }
                    if (personList.size() > 0) {
                        dialogAdapter.setContactList(personList);
                        dialogAdapter.notifyDataSetChanged();
                        if (listDialog != null && !listDialog.isShowing()) {
                            listDialog.show();
                        }
                    } else {
                        showShortToast(getString(R.string.not_person));
                    }
                } catch (Exception e) {//成功
                    showShortToast(getString(R.string.submit_success));
                    ((MainActivity) getActivity()).setIsNeedUpdata(1);
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
            }
        });
    }

    /**
     * 指派业务员
     *
     * @param salesid
     */
    private void setDesignate(String salesid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", orderId);
        params.put("salesid", salesid);
        OkHttpUtils.post(Constant.setDesignateUrl()).params(params).execute(new StringCallback() {
            public void onBefore(BaseRequest request) {
                startProgressDialog(getString(R.string.submitting));
            }

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                stopProgressDialog();
                try {
                    JSONObject obj = new JSONObject(s);
                    if (JsonUtils.isExistObj(obj, "#message")) {//失败
                        showShortToast(obj.optString("#message"));
                    }
                } catch (Exception e) {//成功
                    showShortToast(getString(R.string.submit_success));
                    getNotCheckedData();
                    ((MainActivity) getActivity()).setIsNeedUpdata(1);
                }
            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                stopProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case DETAILS_REQUESTCODE://查看详情
                    String totalAmount = data.getStringExtra("totalAmount");
                    NotCheckedItem item = (NotCheckedItem) list.get(curPosition - 1);
                    item.setTotalAmount(totalAmount);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
