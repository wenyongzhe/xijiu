package com.hhxh.xijiu.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.custum.CustomDialog;


/**
 * 调用对话框
 *
 * @author qiaocbao
 * @time 2014-7-25 下午5:21:36
 */
public class OpenDialog {

    public OpenDialog() {
    }
    private static class DialogHolder {
        static final OpenDialog INSTANCE = new OpenDialog();
    }

    public static OpenDialog getInstance() {
        return DialogHolder.INSTANCE;
    }

    /**
     * 弹出一个确定对话框，无操作
     *
     * @param context
     * @param content
     */
    public void showDialog(Context context, String content) {
        try {
            if (context != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(content);
                builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
                builder.setPositiveButton(context.getResources().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                // 设置你的操作事项
                            }
                        });
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回一个对话框Builder
     *
     * @param context
     * @param content
     * @return
     */
    // public CustomDialog.Builder showListenerDialog(Context context,
    // String content) {
    //
    // CustomDialog.Builder builder = new CustomDialog.Builder(context);
    // builder.setMessage(content);
    // builder.setTitle("温馨提示");
    // return builder;
    // }

    /**
     * 弹出一个确定对话框，有监听事件
     *
     * @param context    当前context
     * @param content    对话框内容
     * @param ok         按钮
     * @param okListener 按钮监听事件
     */
    public void showOneBtnListenerDialog(Context context, String title,String content,
                                         String ok, DialogInterface.OnClickListener okListener) {
        try {
            if (context != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(content);
                if (!TextUtils.isEmpty(title)){
                    builder.setTitle(title);
                }else{
                    builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
                }
                builder.setPositiveButton(ok, okListener);
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出两个按钮对话框，有监听事件
     *
     * @param context        当前context
     * @param content        对话框内容
     * @param ok             第一个按钮
     * @param okListener     第一个按钮监听事件
     * @param cancel         第二个按钮
     * @param cancelListener 第二个按钮监听事件
     */
    public void showTwoBtnListenerDialog(Context context, String content,
                                         String ok, DialogInterface.OnClickListener okListener,
                                         String cancel, DialogInterface.OnClickListener cancelListener) {
        try {
            if (context != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(content);
                builder.setTitle(context.getResources().getString(R.string.new_warm_prompt));
                builder.setPositiveButton(ok, okListener);
                builder.setNegativeButton(cancel, cancelListener);
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出两个按钮对话框，有监听事件
     *
     * @param context        当前context
     * @param content        对话框内容
     * @param ok             第一个按钮
     * @param okListener     第一个按钮监听事件
     * @param cancel         第二个按钮
     * @param cancelListener 第二个按钮监听事件
     */
    public void showTwoBtnListenerDialog(Context context, String title, String content,
                                         String ok, DialogInterface.OnClickListener okListener,
                                         String cancel, DialogInterface.OnClickListener cancelListener) {
        try {
            if (context != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(content);
                builder.setTitle(title);
                builder.setPositiveButton(ok, okListener);
                builder.setNegativeButton(cancel, cancelListener);
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNoBtnDialog(Context context, String title, String content) {
        try {
            if (context != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setMessage(content);
                builder.setTitle(title);
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 内容输入对话框
     *
     * @param context  当前context
     * @param title    标题
     * @param listener 确定按钮事件处理
     */
    public void showEditContentDialog(Context context, String title, String defualtStr, String hintStr
            , boolean isInputNum, final OnEditContentDialogClickListener listener) {
        final Dialog myDialog = new Dialog(context, R.style.Base_Theme_AppCompat_Dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_content_dialog, null);
        TextView dialogTitleText = (TextView) view.findViewById(R.id.dialogTitleText);
        if (!TextUtils.isEmpty(title)) {
            dialogTitleText.setText(title);
        }
        final EditText contentEdit = (EditText) view.findViewById(R.id.contentEdit);
        if (isInputNum) {
            contentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (!TextUtils.isEmpty(defualtStr)) {
            contentEdit.setText(defualtStr);
        }
        if (!TextUtils.isEmpty(hintStr)) {
            contentEdit.setHint(hintStr);
        }
        TextView sureText = (TextView) view.findViewById(R.id.sureText);
        TextView cancelText = (TextView) view.findViewById(R.id.cancelText);
        sureText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(myDialog, contentEdit.getText().toString().trim());
                }
            }
        });
        cancelText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setContentView(view, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams lp = myDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        myDialog.getWindow().setBackgroundDrawable(null);
        myDialog.show();
    }

    /**
     * 输入对话框
     *
     * @param context  当前context
     * @param listener 确定按钮事件处理
     */
    public void showTwoEditDialog(Context context, boolean isSingle,String firstDefualtStr,String secondDefualtStr,
                                  final OnTwoEditDialogClickListener listener) {
        final Dialog myDialog = new Dialog(context, R.style.Base_Theme_AppCompat_Dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.two_edit_dialog, null);
        View firstLayout=view.findViewById(R.id.firstLayout);
        final EditText firstEdit = (EditText) view.findViewById(R.id.firstEdit);
        final EditText secondEdit = (EditText) view.findViewById(R.id.secondEdit);
        if(isSingle){
            firstLayout.setVisibility(View.GONE);
            firstEdit.setVisibility(View.GONE);
        }
        if (firstEdit.getVisibility()==View.VISIBLE){
            firstEdit.setFocusableInTouchMode(true);
            firstEdit.requestFocus();
        }else if (secondEdit.getVisibility()==View.VISIBLE){
            secondEdit.setFocusableInTouchMode(true);
            secondEdit.requestFocus();
        }
        if (!TextUtils.isEmpty(firstDefualtStr)) {
            firstEdit.setText(firstDefualtStr);
            firstEdit.setSelection(firstDefualtStr.length());
        }
        if (!TextUtils.isEmpty(secondDefualtStr)) {
            secondEdit.setText(secondDefualtStr);
            secondEdit.setSelection(secondDefualtStr.length());
        }
        TextView sureText = (TextView) view.findViewById(R.id.sureText);
        TextView cancelText = (TextView) view.findViewById(R.id.cancelText);
        sureText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(myDialog, firstEdit.getText().toString().trim(), secondEdit.getText().toString().trim());
                }
            }
        });
        cancelText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setContentView(view, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams lp = myDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        myDialog.getWindow().setBackgroundDrawable(null);
        myDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        myDialog.show();
    }
    public interface OnTwoEditDialogClickListener {
        void onClick(Dialog dialog, String firstContent, String secondContent);
    }

    public interface OnEditDialogClickListener {
        void onClick(View v, String content);
    }

    public interface OnEditContentDialogClickListener {
        void onClick(Dialog dialog, String content);
    }

}
