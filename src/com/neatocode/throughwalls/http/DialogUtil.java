package com.neatocode.throughwalls.http;

import android.app.Activity;
import android.app.Dialog;
import android.view.WindowManager.BadTokenException;

/**
 * Works with dialogs.
 * 
 */
public class DialogUtil {
	
	public static void safeDismiss(final Dialog dialog) {
		if ( null == dialog || !dialog.isShowing() ) {
			return;
		}
		
		try {
			dialog.dismiss();
		} catch(IllegalArgumentException e) {
			//Do nothing. Happens when dismiss is called for a dialog already 
			//removed because an activity was left:
			//IllegalArgumentException: View not attached to window manager
		}
	}
	
	public static void safeDismiss(final Activity activity, final int dialogId) {
		if ( null == activity ) {
			return;
		}
		
		try {
			activity.dismissDialog(dialogId);
		} catch(IllegalArgumentException e) {
			//Do nothing. Happens when this instance of the activity hasn't shown the dialog yet.
		}
	}
	
	public static void safeShow(final Dialog dialog) {
		if ( null == dialog ) {
			return;
		}
		
		try {
			dialog.show();
		} catch(BadTokenException e) {
			//Do nothing. Happens on spurious calls after an activity has ended, like if a thread finishes.
		}
	}
	
	public static void safeShow(final Activity activity, final int dialogId) {
		if ( null == activity ) {
			return;
		}
		
		try {
			activity.showDialog(dialogId);
		} catch(BadTokenException e) {
			//Do nothing. Happens on spurious calls after an activity has ended, like if a thread finishes.
		}
	}

}
