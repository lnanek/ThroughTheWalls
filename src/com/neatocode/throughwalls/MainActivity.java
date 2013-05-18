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
	private static final int ORIENTATION_BUFFER = 10;
	
	// New York
	private static final float TARGET_LONG = 40.729568f;
	private static final float TARGET_LAT = -73.982277f;
	
	// Palo Alto
	private static final float PALO_ALTO_LONG = 37.440519f;
	private static final float PALO_ALTO_LAT = -122.146511f;

	private TextView text;

	private TextView locationText;

	private SensorManager mSensorManager;

	private Sensor mOrientation;

	private LocationManager mLocationManager;
	
	Double networkLat;
	
	Double networkLon;
	
	Double gpsLat;
	
	Double gpsLon;
	
	private ViewGroup frame;
	
	private static void removeBackgrounds(final View aView) {
		aView.setBackgroundDrawable(null);
		aView.setBackgroundColor(Color.TRANSPARENT);
		aView.setBackgroundResource(0);
		if (aView instanceof ViewGroup) {
			final ViewGroup group = (ViewGroup) aView;
			final int childCount = group.getChildCount();
			for(int i = 0; i < childCount; i++) {
				final View child = group.getChildAt(i);
				removeBackgrounds(child);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		
		//removeBackgrounds(getWindow().getDecorView());
		
		frame = (ViewGroup) findViewById(R.id.frame);

		text = (TextView) findViewById(R.id.text);

		locationText = (TextView) findViewById(R.id.location);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for(Sensor sensor : sensors) {
			Log.i(LOG_TAG, "Found sensor: " + sensor.getName());
		}
		
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> providers = mLocationManager.getAllProviders();
		for(String provider : providers ) {
			Log.i(LOG_TAG, "Found provider: " + provider);
		}
		
		
		// getting GPS status
		boolean isGPSEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		boolean isNetworkEnabled = mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		locationText.setText("isGPSEnabled:" + isGPSEnabled
				+ "\nisNetworkEnabled:" + isNetworkEnabled);

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, this);
		//mLocationManager.requestLocationUpdates(
		//		LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
		// You must implement this callback in your code.
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		
		
		final Location targetLocation = new Location("ThroughGlass");
		targetLocation.setLatitude(TARGET_LAT);
		targetLocation.setLongitude(TARGET_LONG);
		final Location sourceLocation = new Location("ThroughGlass");
		sourceLocation.setLatitude(PALO_ALTO_LONG);
		sourceLocation.setLongitude(PALO_ALTO_LAT);
		
		float targetBearing = sourceLocation.bearingTo(targetLocation);
		
		showRedBackgroundOnTargetBearing(azimuth_angle, targetBearing);
		

		// Do something with these orientation angles.
		text.setText(
				  "a = " + azimuth_angle + "\n"
				+ "p = " + pitch_angle + "\n" 
				+ "r = " + roll_angle + "\n" 
				+ "b = " + targetBearing
				
				);
	}
	
	private void showRedBackgroundOnTargetBearing(float azimuth_angle, float target) {
		final boolean aboveThreshold = azimuth_angle > (target - ORIENTATION_BUFFER);
		final boolean belowThreshold = azimuth_angle < (target + ORIENTATION_BUFFER);
		if ( aboveThreshold && belowThreshold ) {
			frame.setBackgroundColor(Color.RED);
		} else {
			frame.setBackgroundColor(Color.GREEN);
		}		
	}

	@Override
	public void onLocationChanged(Location location) {
		locationText.setText(location.getLatitude() + "\n"
				+ location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
