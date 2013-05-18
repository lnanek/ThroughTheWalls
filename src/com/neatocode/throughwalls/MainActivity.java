package com.neatocode.throughwalls;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener,
		LocationListener {
	
	private static final String LOG_TAG = "ThroughWalls";
	
	//private static final int ORIENTATION_DEG = 60;
	//private static final int ORIENTATION_BUFFER = 10;
	
	// New York
	private static final float TARGET_LONG = 40.729568f;
	private static final float TARGET_LAT = -73.982277f;
	
	// Palo Alto
	private static final float PALO_ALTO_LONG = 37.440519f;
	private static final float PALO_ALTO_LAT = -122.146511f;

	private SensorManager mSensorManager;

	private Sensor mOrientation;

	private LocationManager mLocationManager;
	
	private Display mDisplay;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// TODO supposed to be more accurate to compose compass and accelerometer yourself
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mDisplay = new Display(this);

		// Use NYC as test target for now.
		// TODO add cameras, shelters, etc..
		final Location targetLocation = new Location("ThroughGlass");
		targetLocation.setLatitude(TARGET_LAT);
		targetLocation.setLongitude(TARGET_LONG);
		mDisplay.setTarget(targetLocation);
		
		// TODO use default location as Palo Alto for now.
		final Location sourceLocation = new Location("ThroughGlass");
		sourceLocation.setLatitude(PALO_ALTO_LONG);
		sourceLocation.setLongitude(PALO_ALTO_LAT);
		mDisplay.setLocation(sourceLocation);
	}

	@Override
	public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
		// Do nothing.
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
				SensorManager.SENSOR_DELAY_NORMAL);
		final List<String> providers = mLocationManager.getAllProviders();
		for(String provider : providers ) {
			// Set last known location if we have it.
			// TODO indicate to user if very out of date?
			// Time label in corner? some sort of scanner ping lines moving outward?		
			// XXX does null get last known for any location?
			final Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
			mDisplay.setLocation(lastKnownLocation);			
			
			final boolean enabled = mLocationManager.isProviderEnabled(provider);
			Log.i(LOG_TAG, "Found provider: " + provider + ", enabled = " + enabled);
			mLocationManager.requestLocationUpdates(provider, 0, 0, this);
		}		
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		mDisplay.setOrientation(azimuth_angle, pitch_angle, roll_angle);
	}
	
	@Override
	public void onLocationChanged(final Location location) {
		mDisplay.setLocation(location);
	}

	@Override
	public void onProviderDisabled(final String provider) {
		// TODO warn user if no providers enabled
	}

	@Override
	public void onProviderEnabled(final String provider) {
	}

	@Override
	public void onStatusChanged(final String provider, final int status, final Bundle extras) {
	}

}
