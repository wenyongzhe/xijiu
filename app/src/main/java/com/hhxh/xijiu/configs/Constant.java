package com.hhxh.xijiu.configs;

import com.hhxh.xijiu.data.UserPrefs;

/**
 * 测试版本与正式版切换时修改此处的常量
 * @author qiaocbao
 * @version 2014-3-31 下午2:22:56
 */
public class Constant {
    /**
     * Log打印开关，debug版本下打开，release版本下关闭
     */
    public final static boolean DEBUG = true;
    public final static String TAG = "hhxh";

    /** 本地缓存根文件夹 */
    public static final String HHXH_BASE = "/Hhxh";
    /** 文件 */
    public static final String HHXH_FILEDIR = HHXH_BASE + "/files/";
    /**相机权限请求码*/
    public static final int TACK_PHOTO_REQUESETCODE=0X11;
    /**内存卡权限请求码*/
    public static final int STORAGE_REQUESETCODE=0X12;
    /**振动权限请求码*/
    public static final int VIBRATOR_REQUESETCODE=0X13;
    /**
     * 启动相机请求吗
     */
    public static final int CAMERA_REQUESTE_CODE = 0X14;
//    /**正式库*/
    public static String URL="http://fx.gzxijiu.com";
    /**测试库*/
//    public static String URL="http://fx.gzxijiu.com:9286";


    /**登陆地址*/
    public static final String LOGIN_URL=URL+"/web/api/login";
//    public static final String LOGIN_URL=URL+"/web/sys/login";
    /**送货订单地址*/
    public static String getDeliveryUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_CKD.query_for_delivery";
    }
    /**交货地址*/
    public static String getDeliveryGoodsUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_CKD.CKDJH";
    }
    /**交货地址*/
    public static String getSubmitCodeInfoUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_CKD.CKDJH_API";
    }
    /**修改密码地址*/
    public static String getModifyPswUrl(){
        return URL+"/web/api/invoke/"+UserPrefs.getToken()+"/employee.set_passwordByOwer";
    }
    /**检查版本更新*/
    public static String getCheckVersionUrl(){
        return URL +"/web/api/invoke/"+ UserPrefs.getToken()+"/c_BBGL.query";
    }

    /**
     * 获取验证码地址
     */
    public static final String AUTH_CODE_URL= URL+"/web/api/invoke/employee.appFindUserPassword";
    /**找回密码地址*/
    public static final String FINDBACK_PSW_URL= URL+"/web/api/invoke/user.changeUserPwdApi";

    /**
     *线上收款地址
     * @return
     */
    public static String getReceiveOnlineUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_SKDJ.create";
    }
    /**
     *线下收款地址
     * @return
     */
    public static String getReceiveOfflineUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_SKDJ_XX.create";
    }

    /**
     *销售列表地址
     * @return
     */
    public static String getSalesmanOrderListUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.query_ltywy_api";
    }
    /**
     *查看订单详情地址
     * @return
     */
    public static String getSalesmanOrderDetailsUrl(){
        return URL+"/web/api/data/"+ UserPrefs.getToken()+"/c_XSDD.view";
    }
    /**
     *审核订单地址
     * @return
     */
    public static String getCheckOrderUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.xsdd_sh";
    }
    /**
     *反审核订单地址
     * @return
     */
    public static String getUncheckOrderUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.ssdd_fcsh";
    }
    /**
     *更新订单地址
     * @return
     */
    public static String getEditOrderUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.c_XSDD_Update_sales_order";
    }

    /**
     *用户角色地址
     * @return
     */
    public static String getUserPartUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/employee.getMyRolesApi";
    }

    /**
     *指派人列表地址
     * @return
     */
    public static String getDesignateUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.sales_list";
    }

    /**
     *指派业务员地址
     * @return
     */
    public static String setDesignateUrl(){
        return URL+"/web/api/invoke/"+ UserPrefs.getToken()+"/c_XSDD.point_salesid";
    }
}
