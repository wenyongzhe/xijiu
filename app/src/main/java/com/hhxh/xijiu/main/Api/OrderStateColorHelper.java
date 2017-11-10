package com.hhxh.xijiu.main.Api;

import android.content.res.Resources;
import android.widget.TextView;

import com.hhxh.xijiu.R;

/**
 * 功能描述：
 *
 * @auth lijq
 * @date 2017/2/9
 */
public class OrderStateColorHelper {
    private OrderStateColorHelper(){

    }

    /**
     * 根据订单状态设置颜色
     * @param state
     * @param text
     */
    public static void setTextColorByState(int state,TextView text){
        Resources resources=text.getContext().getResources();
        switch (state){
            case 10://未确认
                text.setTextColor(resources.getColor(R.color.light_gray));
            break;
            case 20://已提交
                text.setTextColor(resources.getColor(R.color.light_blue));
                break;
            case 25://已审核
                text.setTextColor(resources.getColor(R.color.light_green));
                break;
            case 30://部分开票
                text.setTextColor(resources.getColor(R.color.orange));
                break;
            case 40://全部开票
                text.setTextColor(resources.getColor(R.color.light_red));
                break;
            case 100://作废
                text.setTextColor(resources.getColor(R.color.light_gray));
                break;
            default:
                text.setTextColor(resources.getColor(R.color.light_gray));
                break;
        }
    }
}
