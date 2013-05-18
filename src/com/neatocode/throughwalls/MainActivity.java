package com.neatocode.throughwalls;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class MainActivity extends Activity implements SensorEventListener,
		LocationListener {

	// TODO use wake lock to turn on screen when run
	
	// TODO center webview, show traffic camera image better
	
	// TODO sort targets by closest target

	// TODO option to pick other targets: shelter vs. camera, etc.

	private static final String LOG_TAG = "ThroughWalls";

	private SensorManager mSensorManager;

	private Sensor mOrientation;

	private LocationManager mLocationManager;

	private Display mDisplay;

	private List<Target> mTargets;

	private int mCurrentTargetIndex;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		Log.i(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// TODO supposed to be more accurate to compose compass and
		// accelerometer yourself
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mDisplay = new Display(this);

		// TODO sort nearest first
		// TODO add cameras, shelters, etc..
		mTargets = Target.CAMERAS;
		// mTargets = new LinkedList<Target>();
		// mTargets.add(Target.BEIJING);
		// mTargets.add(Target.NYC);
		mDisplay.showTarget(mTargets.get(mCurrentTargetIndex));

		// TODO use default location as Palo Alto for now.
		mDisplay.setLocation(Target.PALO_ALTO.asLocation());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(LOG_TAG, "onTouchEvent, event = " + event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.i(LOG_TAG, "dispatchTouchEvent, event = " + ev);
		
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			toggleShowUrl();
			return true;
		}

		return super.dispatchTouchEvent(ev);
	}

	private boolean showUrl() {
		Log.i(LOG_TAG, "showUrl");

		if (!mDisplay.isWebViewVisible()) {
			final Target currentTarget = mTargets.get(mCurrentTargetIndex);
			if (null != currentTarget.url) {
				mDisplay.showUrl(currentTarget.url);
			}
			return true;
		}
		return false;
	}

	private void nextTarget() {
		Log.i(LOG_TAG, "nextTarget");

		mCurrentTargetIndex++;
		if (mCurrentTargetIndex >= mTargets.size()) {
			mCurrentTargetIndex = 0;
		}
		mDisplay.showTarget(mTargets.get(mCurrentTargetIndex));
	}

	private void previousTarget() {
		Log.i(LOG_TAG, "previousTarget");

		mCurrentTargetIndex--;
		if (mCurrentTargetIndex < 0) {
			mCurrentTargetIndex = mTargets.size() - 1;
		}
		mDisplay.showTarget(mTargets.get(mCurrentTargetIndex));
	}

	@Override
	public boolean dispatchKeyEvent(final KeyEvent event) {
		Log.i(LOG_TAG, "dispatchKeyEvent, event = " + event);

		final int action = event.getAction();
		if (action != KeyEvent.ACTION_DOWN) {
			return false;
		}

		final int keyCode = event.getKeyCode();
		switch (keyCode) {
		// Back button on standard Android, swipe down on Google Glass
		case KeyEvent.KEYCODE_BACK:
			// If showing main AR screen on down swipe, leave program.
			if (!mDisplay.isWebViewVisible()) {
				finish();
			// If showing a camera, go back to AR screen.
			} else {
				mDisplay.showUrl(null);
			}
			return true;

			// Left and right swipe through the cameras on Google Glass.
			// On phone, volume keys move through cameras
		case KeyEvent.KEYCODE_TAB:
		case KeyEvent.KEYCODE_VOLUME_UP:
			if ( event.isShiftPressed() ) {
				previousTarget();
			} else {
				nextTarget();
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			previousTarget();
			return true;

			// Tapping views the camera.
		case KeyEvent.KEYCODE_DPAD_CENTER:
			toggleShowUrl();
			return true;

		default:
			return super.dispatchKeyEvent(event);
		}
	}
	
	private void toggleShowUrl() {
		// If showing webview, hide it.
		if (mDisplay.isWebViewVisible()) {
			mDisplay.showUrl(null);
			
		// Otherwise show it.
		} else {
			showUrl();
		}		
	}

	@Override
	public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
		// Do nothing.
	}

	@Override
	protected void onResume() {
		// Log.i(LOG_TAG, "onResume");

		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
				SensorManager.SENSOR_DELAY_NORMAL);
		final List<String> providers = mLocationManager.getAllProviders();
		for (String provider : providers) {
			// Set last known location if we have it.
			// TODO indicate to user if very out of date?
			// Time label in corner? some sort of scanner ping lines moving
			// outward?
			final Location lastKnownLocation = mLocationManager
					.getLastKnownLocation(provider);
			mDisplay.setLocation(lastKnownLocation);

			final boolean enabled = mLocationManager
					.isProviderEnabled(provider);
			Log.i(LOG_TAG, "Found provider: " + provider + ", enabled = "
					+ enabled);
			mLocationManager.requestLocationUpdates(provider, 0, 0, this);
		}
	}

	@Override
	protected void onPause() {
		// Log.i(LOG_TAG, "onPause");

		super.onPause();
		mSensorManager.unregisterListener(this);
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		// Log.i(LOG_TAG, "onSensorChanged");

		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		mDisplay.setOrientation(azimuth_angle, pitch_angle, roll_angle);
	}

	@Override
	public void onLocationChanged(final Location location) {
		// Log.i(LOG_TAG, "onLocationChanged");

		mDisplay.setLocation(location);
	}

	@Override
	public void onProviderDisabled(final String provider) {
		// Log.i(LOG_TAG, "onProviderDisabled");
		// TODO warn user if no providers enabled
	}

	@Override
	public void onProviderEnabled(final String provider) {
		// Log.i(LOG_TAG, "onProviderEnabled");
	}

	@Override
	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
		// Log.i(LOG_TAG, "onStatusChanged");
	}

}
