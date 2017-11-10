package com.hhxh.xijiu.base.presenter;

/**
 * @auth lijq
 * @date 2016/9/13
 */
public class BasePresenter<V>{
    public V mView;

    public void attach(V mView){
        this.mView=mView;
    }

    public  void dettach(){
        mView=null;
    }
    /**
     * 取消网络请求
     */
    protected void cancelNetLoad(){
//        if (mTag!=null){
//            OkHttpUtils.getInstance().cancelTag(mTag);
//        }

    }

}

