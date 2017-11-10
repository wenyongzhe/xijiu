package com.hhxh.xijiu.custum;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhxh.xijiu.R;

/**
 * 带进度条的dialog
 *
 * @author qiaocbao
 * @time 2014-10-6 下午7:01:39
 */
public class MyProgressDialog extends Dialog {
	private OnBackPressedLisenter onBackPressedLisenter;

	private MyProgressDialog(Context context) {
		super(context);
	}

	private MyProgressDialog(Context context, int theme) {
		super(context, theme);
	}


	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}

	/**
	 * setMessage 提示内容
	 *
	 * @param strMessage
	 * @return
	 */
	public MyProgressDialog setMessage(String strMessage) {
		if (TextUtils.isEmpty(strMessage)) return this;
		TextView tvMsg = (TextView)findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return this;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK://返回
				if (onBackPressedLisenter != null) {
					onBackPressedLisenter.onBackPressed();
				}
				break;
		}
		return super.dispatchKeyEvent(event);
	}

	public void setOnBackPressedLisenter(OnBackPressedLisenter onBackPressedLisenter) {
		this.onBackPressedLisenter = onBackPressedLisenter;
	}

	public interface OnBackPressedLisenter {
		void onBackPressed();
	}

	public static class Builder {
		private MyProgressDialog mProgressDialog;
		private Context context;

		public Builder(Context context) {
			this.context = context;
		}

		public MyProgressDialog create() {
			mProgressDialog = new MyProgressDialog(context, R.style.progressDialog);
			mProgressDialog.setContentView(R.layout.custom_progress_dialog);
			mProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
			mProgressDialog.getWindow().getAttributes().dimAmount = 0f;
			mProgressDialog.setCancelable(true);
			mProgressDialog.setCanceledOnTouchOutside(false);
			return mProgressDialog;
		}
	}
}
