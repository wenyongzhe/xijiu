package com.hhxh.xijiu.custum;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hhxh.xijiu.R;
import com.hhxh.xijiu.utils.DensityUtil;
import com.hhxh.xijiu.utils.StringUtil;


/**
 * 自定义弹出对话框
 * @author qiaocbao
 * @time 2014-7-25 下午5:11:48
 */
public class CustomListDialog extends Dialog {

	public CustomListDialog(Context context) {
		super(context);
	}

	public CustomListDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private BaseAdapter personAdapter;
		private AdapterView.OnItemClickListener personItemClickListener;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setItemClickListener(AdapterView.OnItemClickListener personItemClickListener){
			this.personItemClickListener = personItemClickListener;
			return this;
		}

		public Builder setListViewAdapter(BaseAdapter personAdapter) {
			this.personAdapter = personAdapter;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param
		 * @return
		 */
		public Builder setPositiveButton(OnClickListener listener) {
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}


		public CustomListDialog create() {
			LayoutInflater inflater = LayoutInflater.from(context);
			final CustomListDialog dialog = new CustomListDialog(context,
					R.style.Base_Theme_AppCompat_Dialog);
			View layout = inflater.inflate(R.layout.dialog_list_layout, null);

			if(!StringUtil.isEmpty(title)){
				((TextView) layout.findViewById(R.id.title)).setText(title);
			}

			if (negativeButtonClickListener != null) {
				((TextView) layout.findViewById(R.id.negativeButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								negativeButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);
							}
						});
			}

			if (positiveButtonClickListener != null) {
				((TextView) layout.findViewById(R.id.positiveButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								positiveButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							}
						});
			}

			if (personAdapter != null) {
				ListView personListView = (ListView) layout.findViewById(R.id.personListView);
				personListView.setAdapter(personAdapter);
				if(personItemClickListener != null){
					personListView.setOnItemClickListener(personItemClickListener);
				}
			}
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setContentView(layout, new WindowManager.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setBackgroundDrawable(null);
			lp.width= DensityUtil.getInstance().getScreenWidth(context)*4/5;
			lp.gravity = Gravity.CENTER;
			return dialog;
		}
	}
}
