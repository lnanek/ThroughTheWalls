package com.neatocode.throughwalls.http;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neatocode.throughwalls.R;

public abstract class AsyncCall {
	
	private static final String LOG_TAG = "AsyncCall";

	private final Handler mShowErrorDialogHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			showErrorDialog((String) msg.obj);
		}
	};
	
	protected final Activity mActivity;
	
	protected Dialog mProgressDialog;
	
	public AsyncCall(final Activity activity) {
		mActivity = activity;
	}

	protected void showErrorDialogFromOtherThread(final String errorMessage) {
		Message uiMessage = mShowErrorDialogHandler.obtainMessage(0, errorMessage);
		mShowErrorDialogHandler.sendMessage(uiMessage);
	}

	protected void showErrorDialog(final String message) {

		DialogUtil.safeDismiss(mProgressDialog);
		
		Toast.makeText(mActivity, "Failed to get data...", Toast.LENGTH_LONG).show();
		mActivity.finish();
	}
	
	protected void showProgressDialog(final String message, final boolean cancelable, 
			final DialogInterface.OnCancelListener cancelListener) {
		if ( null == mProgressDialog ) {
			
			mProgressDialog = new ProgressDialog(mActivity);

			mProgressDialog.setCancelable(cancelable);
			if ( cancelable ) {
				mProgressDialog.setCanceledOnTouchOutside(true);
				mProgressDialog.setOnCancelListener(cancelListener);
			}
			
			mProgressDialog.show();
			
		}
		if ( !mProgressDialog.isShowing() ) {
			mProgressDialog.show();
		}
	}
	
	public static final String encode(String value) {
		return AsyncCall.encode(value, false);
	}
	
	public static final String encode(String value, boolean usePercentForSpaces) {
		if ( null == value ) return "";
		
		try {
			
			String encoded = URLEncoder.encode(value, "UTF-8");
			
			encoded.replaceAll("\\+", "%20");
			
			return encoded;
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "Error encoding parameter with value: " + value, e);
			return "";
		}		
	}
	
}
