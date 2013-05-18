package com.neatocode.throughwalls.http;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Finds shelters from http://sdcountyemergency.com/shelters/
 * http://www.sdcountyemergency.com/handlers/shelterlocations.ashx
 * San Diego County Emergency Site
 *
 */
public class FindSheltersCall extends AsyncCall {
	
	public static interface OnFindSheltersListener {
		void onFindShelters(List<Placemark> data);
	}
	
	public static final float M_PER_MI = 1609.344f;
	
	private static final String LOG_TAG = "FindSheltersCall";
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if ( null == msg.obj ) {
				DialogUtil.safeDismiss(mProgressDialog);
				showErrorDialog("Couldn't connect.");
				return;
			}
			
			List<Placemark> data = (List<Placemark>) msg.obj;
			
			for(Placemark mark : data) {
				mark.setDistanceFromDestination(mDestination.getDistanceInMiles(mark.getLat(), mark.getLon()));
			}

			DialogUtil.safeDismiss(mProgressDialog);
			mReceiver.onFindShelters(data);
										
		}
	};
	
	private final OnFindSheltersListener mReceiver;
	
	private final FindRequestData mDestination;
	
	public FindSheltersCall(final Activity activity,
			final OnFindSheltersListener receiver, final FindRequestData destination) {

		
		super(activity);
		Log.i(LOG_TAG, "FindSheltersCall");
		mReceiver = receiver;
		mDestination = destination;
	}

	public void downloadShelters() {
		Log.i(LOG_TAG, "downloadShelters");
				
    	showProgressDialog("Loading shelters...", false, null);
    	
    	Thread thread = new Thread() {
			@Override
			public void run() {
				String url = buildUrl();
				
				List<Placemark> data = ShelterResultParser.getPlacemarks(url);
				
				Message message = mHandler.obtainMessage(0, data);
				mHandler.sendMessage(message);
			}
    	};
    	thread.start();
    }
	
	private String buildUrl() {
		String url = "http://www.sdcountyemergency.com/handlers/shelterlocations.ashx";
		return url;

	}
	
}
