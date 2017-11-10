package com.hhxh.xijiu.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxh.xijiu.R;

public class AlertUtil {
	private static CustomDialog mDialog;
	private static CustomDialog2 mDialog2;
	private static Context mContext;
	private static ProgressDialog mProgressDialog;

	public static void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mDialog = null;
	}

	public static void dismissDialog2() {
		if (mDialog2 != null && mDialog2.isShowing()) {
			mDialog2.dismiss();
		}
		mDialog2 = null;
	}

	public static void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mProgressDialog = null;
	}

	public static void dismissDialog1() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	public static void dismissDialog(OnDismissListener listener) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.setOnDismissListener(listener);
			mDialog.dismiss();
		}
		mDialog = null;
	}

	private static void dimBehind(Dialog dialog) {
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.alpha = 1.0f;
		lp.flags = lp.flags | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lp.dimAmount = 0.5f;
		dialog.getWindow().setAttributes(lp);
	}

	public static void showAlert(Context paramContext, int title, int message) {
		mContext = paramContext;
		if (mDialog != null) {
			return;
		}
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissDialog();
			}
		});
		mDialog.show();
		adjustDialogPosition(mDialog);
	}

	public static void showAlert(Context paramContext, int title, int message,
								 int positiveText, View.OnClickListener listener) {
		mContext = paramContext;
		showAlert(paramContext, getString(title), getString(message),
				getString(positiveText), listener);
	}

	public static String getString(int resId) {
		return mContext.getString(resId);
	}

	public static void showAlert(Context paramContext, CharSequence title,
								 CharSequence message, CharSequence positiveText,
								 View.OnClickListener listener) {
		mContext = paramContext;
		if (mDialog != null && mDialog.isShowing()) {
			return;
		}
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(positiveText, listener);
		// mDialog.setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// mDialog = null;
		// }
		// });
		mDialog.setCancelable(false);
		mDialog.show();
		adjustDialogPosition(mDialog);
	}

	public static void showAlert(Context paramContext, int title, int message,
								 int positiveText, View.OnClickListener positiveBtnListener,
								 int negativeText, View.OnClickListener negativeBtnListener) {
		mContext = paramContext;
		if (mDialog != null && mDialog.isShowing()) {
			return;
		}
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(positiveText, positiveBtnListener);
		mDialog.setNegativeButton(negativeText, negativeBtnListener);
//		mDialog.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				mDialog = null;
//			}
//		});
		mDialog.setCancelable(false);
		mDialog.show();
		adjustDialogPosition(mDialog);
	}

	public static void showSystemAlert(Context paramContext, CharSequence title,
									   CharSequence message, CharSequence positiveText,
									   View.OnClickListener listener) {
		mContext = paramContext;
		if (mDialog != null && mDialog.isShowing()) {
			return;
		}
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(positiveText, listener);
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mDialog.setCancelable(false);
		mDialog.show();
		adjustDialogPosition(mDialog);
	}

	public static void showAlert2(Context paramContext, int title, int message,
								  int positiveText, View.OnClickListener positiveBtnListener,
								  int negativeText, View.OnClickListener negativeBtnListener) {
		mContext = paramContext;
		if (mDialog2 != null && mDialog2.isShowing()) {
			return;
		}
		mDialog2 = new CustomDialog2(paramContext,
				R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog2);
		mDialog2.setTitle(title);
		mDialog2.setMessage(message);
		mDialog2.setPositiveButton(positiveText, positiveBtnListener);
		mDialog2.setNegativeButton(negativeText, negativeBtnListener);
//		mDialog2.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				mDialog2 = null;
//			}
//		});
		mDialog2.setCancelable(false);
		mDialog2.show();
		adjustDialogPosition(mDialog2);
	}

	/**
	 * 这个把message用String类型了，方便格式化
	 *
	 * @param paramContext
	 * @param title
	 * @param message
	 * @param positiveText
	 * @param positiveBtnListener
	 * @param negativeText
	 * @param negativeBtnListener
	 */
	public static void showAlert(Context paramContext, int title,
								 CharSequence message, int positiveText,
								 View.OnClickListener positiveBtnListener, int negativeText,
								 View.OnClickListener negativeBtnListener) {
		showAlert(paramContext, title, R.string.dialog_title, positiveText,
				positiveBtnListener, negativeText, negativeBtnListener);
		mDialog.setMessage(message);
		adjustDialogPosition(mDialog);
	}

	public static void showAlert2(Context paramContext, int title,
								  CharSequence message, int positiveText,
								  View.OnClickListener positiveBtnListener, int negativeText,
								  View.OnClickListener negativeBtnListener) {
		showAlert2(paramContext, title, R.string.dialog_title, positiveText,
				positiveBtnListener, negativeText, negativeBtnListener);
		mDialog2.setMessage(message);
		adjustDialogPosition(mDialog2);
	}

	public static void showAlert(Context paramContext, String title,
								 String message) {
		mContext = paramContext;
		if (mDialog != null && mDialog.isShowing()) {
			return;
		}
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog_whitebg);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissDialog();
			}
		});
		adjustDialogPosition(mDialog);
		mDialog.show();
	}

	/**
	 *
	 * @param paramContext
	 * @param title
	 * @param message
	 * @param paramOnClickListener
	 */
	public static void showDialog(Context paramContext, int title, int message,
								  View.OnClickListener paramOnClickListener) {
		mContext = paramContext;
		mDialog = new CustomDialog(paramContext, R.style.cusdom_dialog);
		dimBehind(mDialog);
		mDialog.setTitle(title);
		mDialog.setMessage(message);
		mDialog.setPositiveButton(paramOnClickListener);
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mDialog = null;
			}
		});
		adjustDialogPosition(mDialog);
		mDialog.show();
	}

	public static ProgressDialog showProgressDialog(Context context,
													String message) {
		mContext = context;
		mProgressDialog = ProgressDialog.show(context, "请稍等", "", true);
		mProgressDialog.setContentView(R.layout.progressbar_content_center);

		mMainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				((AnimationDrawable) ((ImageView) mProgressDialog
						.findViewById(R.id.progressbar_center_loading))
						.getDrawable()).start();
			}
		}, 50L);
		((TextView) mProgressDialog.findViewById(R.id.progressbar_textview))
				.setText(message);
		((Button) mProgressDialog.findViewById(R.id.progressbar_button1))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mProgressDialog != null
								&& mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
						mProgressDialog = null;
					}
				});
		mProgressDialog.setCancelable(false);
		adjustDialogPosition(mProgressDialog);
		return mProgressDialog;
	}

	public static ProgressDialog showProgressDialog(Context context,
													String message, View.OnClickListener paramOnClickListener) {
		mContext = context;
		mProgressDialog = ProgressDialog.show(context, "请稍等", "", true);
		mProgressDialog
				.setContentView(R.layout.progressbar_content_button_center);
		mMainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				((AnimationDrawable) ((ImageView) mProgressDialog
						.findViewById(R.id.progressbar_center_button_loading))
						.getDrawable()).start();
			}
		}, 50L);
		((TextView) mProgressDialog
				.findViewById(R.id.progressbar_button_textview))
				.setText(message);
		((Button) mProgressDialog.findViewById(R.id.progressbar_button))
				.setOnClickListener(paramOnClickListener);
		mProgressDialog.setCancelable(false);
		adjustDialogPosition(mProgressDialog);
		return mProgressDialog;
	}

	// 无按钮dialog
	public static ProgressDialog showNoButtonProgressDialog(Context context,
															String message) {
		mContext = context;
		mProgressDialog = ProgressDialog.show(context, "请稍等", "", true);
		mProgressDialog.setContentView(R.layout.progressbar_nobutton);
		mMainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				((AnimationDrawable) ((ImageView) mProgressDialog
						.findViewById(R.id.progressbar_center_button_loading1))
						.getDrawable()).start();
			}
		}, 50L);
		((TextView) mProgressDialog
				.findViewById(R.id.progressbar_button_textview1))
				.setText(message);
		mProgressDialog.setCancelable(false);
		adjustDialogPosition(mProgressDialog);
		return mProgressDialog;
	}


	public static ProgressDialog showNoButtonProgressDialog(Context context,
															int messageResId) {
		mContext = context;
		mProgressDialog = ProgressDialog.show(context, "请稍等", "", true);
		mProgressDialog.setContentView(R.layout.progressbar_nobutton);
		mMainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				((AnimationDrawable) ((ImageView) mProgressDialog
						.findViewById(R.id.progressbar_center_button_loading1))
						.getDrawable()).start();
			}
		}, 50L);
		((TextView) mProgressDialog
				.findViewById(R.id.progressbar_button_textview1))
				.setText(messageResId);
		mProgressDialog.setCancelable(false);
		adjustDialogPosition(mProgressDialog);
		return mProgressDialog;
	}

	public static void setMessage(String message) {
		if (mProgressDialog != null)
			((TextView) mProgressDialog
					.findViewById(R.id.progressbar_button_textview))
					.setText(message);
	}
	public static void setNoButtonMessage(String message){
		if(mProgressDialog!=null)
			((TextView) mProgressDialog.findViewById(R.id.progressbar_button_textview1))
					.setText(message);
	}
	private static void adjustDialogPosition(Dialog dialog) {
		Window dialogWindow = dialog.getWindow();
		dialogWindow
				.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP/* CENTER_VERTICAL */);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();

		Point size = getDisplaySize(mContext);
		lp.y = (int) (size.y * 0.26);
		dialogWindow.setAttributes(lp);
	}

	public static Point getDisplaySize(Context context) {
		Point size = new Point();
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if (Build.VERSION.SDK_INT < 13) {
			size.x = display.getWidth();
			size.y = display.getHeight();
		} else {
			display.getSize(size);
		}
		return size;
	}

	private static final int SHOW_TOAST = 0;

	private static Handler mMainHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			Object obj = msg.obj;

			if (SHOW_TOAST == what) {
				Toast toast = new Toast(mContext);
				View localView = ((LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.toast, null);
				((TextView) localView.findViewById(R.id.toast_message))
						.setText((CharSequence) obj);
				toast.setView(localView);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		}
	};



	/**
	 * 在子线程也可以调用
	 */
	public static void showToast(int message, Context paramContext) {
		mContext = paramContext;
		mMainHandler.obtainMessage(SHOW_TOAST, getString(message))
				.sendToTarget();
	}

	private static String oldMsg;
	private static long time;
	/**
	 * 在子线程也可以调用
	 */
	public static void showToast(String message, Context paramContext) {
		//mContext = paramContext;
		//mMainHandler.obtainMessage(SHOW_TOAST, message).sendToTarget();
		if (!message.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
			mContext = paramContext;
			mMainHandler.obtainMessage(SHOW_TOAST, message).sendToTarget();
			time = System.currentTimeMillis();
		} else {
			// 显示内容一样时，只有间隔时间大于2秒时才显示
			if (System.currentTimeMillis() - time > 2500) {
				mContext = paramContext;
				mMainHandler.obtainMessage(SHOW_TOAST, message).sendToTarget();
				time = System.currentTimeMillis();
			}
		}
		oldMsg = message;
	}

	/**
	 * 在子线程也可以调用
	 */
	public static void showToastSingle( Context context,String msg,int duration) {

		if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
			mContext = context;
			mMainHandler.obtainMessage(SHOW_TOAST, msg).sendToTarget();
			time = System.currentTimeMillis();
		} else {
			// 显示内容一样时，只有间隔时间大于2秒时才显示
			if (System.currentTimeMillis() - time > 2500) {
				mContext = context;
				mMainHandler.obtainMessage(SHOW_TOAST, msg).sendToTarget();
				time = System.currentTimeMillis();
			}
		}
		oldMsg = msg;
	}

	public static void showSdNoExist(final Context context) {
//		Intent intent = new Intent();
//		intent.setClass(context, USBTipsActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//				| Intent.FLAG_ACTIVITY_NEW_TASK); //注意本行的FLAG设置
//		context.startActivity(intent);

//		AlertUtil.showSystemAlert(context, "SD卡未加载",
//				"程序需要加载SD卡，请先加载SD卡或者USB连接模式改为非U盘模式。", "确定",
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						AlertUtil.dismissDialog();
//
//						context.stopService(new Intent(context,
//								NotificationService.class));
//
//
//
//						myApplication.getInstance().AppExit();
//						android.os.Process.killProcess(android.os.Process
//								.myPid());
//						System.exit(1);
//					}
//				});
	}

}

