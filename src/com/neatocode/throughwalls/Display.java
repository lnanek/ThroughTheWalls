package com.neatocode.throughwalls;

import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class Display {
	
	// TODO show page turn in corner if have camera image, etc.
	
	private static final String LOG_TAG = "ThroughWalls";
		
	private static final float SCREEN_WIDTH_DEGREES = 15f;

	private Float azimuth;

	private Float roll;

	private Float pitch;

	private Target target;

	private Double currentLat;

	private Double currentLon;

	private Float currentLocationAccuracy;

	private TextView text;

	private TextView locationText;

	private ViewGroup frame;
	
	private OffsetIndicatorView indicator;
	
	private View leftIndicator;
	
	private View rightIndicator;
	
	private WebView view;
	
	private MainActivity mActivity;
	
	private boolean isWebViewVisible;

	public Display(final MainActivity aActivity) {
		mActivity = aActivity;
		
		aActivity.getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		aActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		aActivity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		aActivity.setContentView(R.layout.activity_main);
		leftIndicator= (View) aActivity.findViewById(R.id.leftIndicator);
		rightIndicator= (View) aActivity.findViewById(R.id.rightIndicator);
		view = (WebView) aActivity.findViewById(R.id.web);
		WebSettings webSettings = view.getSettings();
		webSettings.setJavaScriptEnabled(true);
		view.setInitialScale(75);
		view.setFocusable(false);
		view.setFocusableInTouchMode(false);
		view.setClickable(false);
		indicator = (OffsetIndicatorView) aActivity.findViewById(R.id.indicator);
		frame = (ViewGroup) aActivity.findViewById(R.id.frame);
		text = (TextView) aActivity.findViewById(R.id.text);
		locationText = (TextView) aActivity.findViewById(R.id.location);
	}
	
	public boolean isWebViewVisible() {
		return isWebViewVisible;
	}
	
	public void showUrl(String url) {
		if ( null == url || 0 == url.trim().length() ) {
			isWebViewVisible = false;
			//view.setOnClickListener(null);
			view.setVisibility(View.GONE);
			return;
		}

		isWebViewVisible = true;
		Toast.makeText(mActivity, "Loading Camera...", Toast.LENGTH_LONG).show();	
		/*
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "Leaving Camera", Toast.LENGTH_LONG).show();
				showUrl(null);
			}
		});
		*/
		view.setVisibility(View.VISIBLE);
		view.loadUrl(url);
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

	public void showTarget(final Target aTarget) {
		showUrl(null);
		if (null == aTarget) {
			target = null;
			updateDisplay();
			return;
		}

		target = aTarget;
		locationText.setText(aTarget.name);
		updateDisplay();
	}

	public void setOrientation(final float aAzimuth, final float aRoll,
			final float aPitch) {
		azimuth = aAzimuth;
		roll = aRoll;
		pitch = aPitch;
		updateDisplay();
	}
	
	public float normalize(final float deg) {
		float result = deg;
		while(result > 360) {
			result -= 360;
		}
		while(result < -360) {
			result += 360;
		}
		if ( Math.abs(result - 360) < Math.abs(result) ) {
			return result - 360;
		}
		if ( Math.abs(result + 360) < Math.abs(result) ) {
			return result + 360;
		}
		return result;
	}

	public void updateDisplay() {
		if ( null == azimuth || null == roll || null == pitch ) {
			return;
		}
		
		if ( null == target ) {
			return;
		}
		final Location targetLocation = target.asLocation();

		if ( null == currentLat || null == currentLon ) {
			return;
		}
		final Location currentLocation = new Location("ThroughGlass");
		currentLocation.setLatitude(currentLat);
		currentLocation.setLongitude(currentLon);
		
		float bearingToAsEastOfNorthDegrees = currentLocation.bearingTo(targetLocation);
		float delta = normalize(bearingToAsEastOfNorthDegrees - azimuth);
		// Do something with these orientation angles.
		/*
		text.setText(
				  "a = " + azimuth + "\n"
				+ "p = " + pitch + "\n" 
				+ "r = " + roll + "\n" 
				+ "b = " + targetBearing + "\n"
				+ "d = " + delta
				);
		*/
		final String deltaString = 0 == delta ? "" : 
			delta > 0 ? (" right " + roundTenths(delta)) 
					: (" left " + roundTenths(Math.abs(delta)));
		text.setText(roundTenths(azimuth) + "° " + deltaString+ "°");
				
		leftIndicator.setVisibility(View.GONE);
		rightIndicator.setVisibility(View.GONE);
		indicator.setIndicatorOffset(null);
		//frame.setBackgroundColor(Color.GREEN);
		
		// Indicator is on screen at a certain offset.
		if ( Math.abs(delta) < (SCREEN_WIDTH_DEGREES/2)) {
			indicator.setIndicatorOffset(delta/SCREEN_WIDTH_DEGREES + 0.5f);
			//frame.setBackgroundColor(Color.RED);
		// Indicator is offscreen.
		} else if ( delta < 0 ) {
			leftIndicator.setVisibility(View.VISIBLE);
		} else if ( delta > 0 ) {
			rightIndicator.setVisibility(View.VISIBLE);
		}
		
		//final float distanceM = currentLocation.distanceTo(targetLocation);
		//locationText.setText(target.name + " (" + Math.round(distanceM) + "m)");
	}
	
	private String roundTenths(float input) {
		return "" + Math.round(input * 10) / 10.0;
	}

}
