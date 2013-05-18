package com.neatocode.throughwalls;

import android.graphics.Color;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class Display {
	
	private static final String LOG_TAG = "ThroughWalls";
		
	private static final float SCREEN_WIDTH_DEGREES = 15f;

	private Float azimuth;

	private Float roll;

	private Float pitch;

	private Double targetLat;

	private Double targetLon;

	private Double currentLat;

	private Double currentLon;

	private Float currentLocationAccuracy;

	private TextView text;

	private TextView locationText;

	private ViewGroup frame;
	
	private OffsetIndicatorView indicator;
	
	private View leftIndicator;
	
	private View rightIndicator;

	public Display(final MainActivity aActivity) {
		aActivity.getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		aActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		aActivity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		aActivity.setContentView(R.layout.activity_main);
		leftIndicator= (View) aActivity.findViewById(R.id.leftIndicator);
		rightIndicator= (View) aActivity.findViewById(R.id.rightIndicator);

		indicator = (OffsetIndicatorView) aActivity.findViewById(R.id.indicator);
		frame = (ViewGroup) aActivity.findViewById(R.id.frame);
		text = (TextView) aActivity.findViewById(R.id.text);
		locationText = (TextView) aActivity.findViewById(R.id.location);
	}

	public void setLocation(final Location aLocation) {
		if (null == aLocation) {
			// Keep previous point, if any.
			return;
		}

		// Accept new point if no previous or previous less accurate.
		if (null == currentLocationAccuracy
				|| currentLocationAccuracy <= aLocation.getAccuracy()) {
			// TODO throw out old points as well.

			currentLat = aLocation.getLatitude();
			currentLon = aLocation.getLongitude();
			currentLocationAccuracy = aLocation.getAccuracy();
		}
		updateDisplay();
	}

	public void setTarget(final Location aLocation) {
		if (null == aLocation) {
			targetLat = null;
			targetLon = null;
			updateDisplay();
			return;
		}

		targetLat = aLocation.getLatitude();
		targetLon = aLocation.getLongitude();
		//locationText.setText(location.getLatitude() + "\n"
		//		+ location.getLongitude());
		updateDisplay();
	}

	public void setOrientation(final float aAzimuth, final float aRoll,
			final float aPitch) {
		azimuth = aAzimuth;
		roll = aRoll;
		pitch = aPitch;
		updateDisplay();
	}

	public void updateDisplay() {
		if ( null == azimuth || null == roll || null == pitch ) {
			return;
		}
		
		if ( null == targetLat || null == targetLon ) {
			return;
		}
		final Location targetLocation = new Location("ThroughGlass");
		targetLocation.setLatitude(targetLat);
		targetLocation.setLongitude(targetLon);

		if ( null == currentLat || null == currentLon ) {
			return;
		}
		final Location currentLocation = new Location("ThroughGlass");
		currentLocation.setLatitude(currentLat);
		currentLocation.setLongitude(currentLon);
		
		float targetBearing = currentLocation.bearingTo(targetLocation);
		float delta = targetBearing - azimuth;
		// Do something with these orientation angles.
		text.setText(
				  "a = " + azimuth + "\n"
				+ "p = " + pitch + "\n" 
				+ "r = " + roll + "\n" 
				+ "b = " + targetBearing + "\n"
				+ "d = " + delta
				);
				
		leftIndicator.setVisibility(View.GONE);
		rightIndicator.setVisibility(View.GONE);
		indicator.setIndicatorOffset(null);
		frame.setBackgroundColor(Color.GREEN);
		
		// Indicator is on screen at a certain offset.
		
		if ( Math.abs(delta) < SCREEN_WIDTH_DEGREES) {
			indicator.setIndicatorOffset(delta/SCREEN_WIDTH_DEGREES);
			frame.setBackgroundColor(Color.RED);
		// Indicator is offscreen.
		} else if ( delta < 0 ) {
			leftIndicator.setVisibility(View.VISIBLE);
		} else if ( delta > 0 ) {
			rightIndicator.setVisibility(View.VISIBLE);
		}
	}

}
