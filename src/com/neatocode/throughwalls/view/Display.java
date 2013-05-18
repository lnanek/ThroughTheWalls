package com.neatocode.throughwalls.view;

import com.neatocode.throughwalls.R;
import com.neatocode.throughwalls.R.id;
import com.neatocode.throughwalls.R.layout;
import com.neatocode.throughwalls.activity.TargetFinderActivity;
import com.neatocode.throughwalls.model.Target;

import android.location.Location;
import android.view.View;
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
	
	private OffsetIndicatorView indicator;
	
	private View leftIndicator;
	
	private View rightIndicator;
	
	private WebView view;
	
	private TargetFinderActivity mActivity;
	
	private boolean isWebViewVisible;

	public Display(final TargetFinderActivity aActivity) {
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
		//view.setInitialScale(75);
		view.setFocusable(false);
		view.setFocusableInTouchMode(false);
		view.setClickable(false);
		indicator = (OffsetIndicatorView) aActivity.findViewById(R.id.indicator);
		text = (TextView) aActivity.findViewById(R.id.text);
		locationText = (TextView) aActivity.findViewById(R.id.location);
	}
	
	public boolean isWebViewVisible() {
		return isWebViewVisible;
	}
	
	public void hideDetailsView() {
		isWebViewVisible = false;
		view.setVisibility(View.GONE);
	}
	
	public void showDetailsView() {

		// TODO Clear previous camera image somehow, flashing visible when loading new one.
		isWebViewVisible = true;
		Toast.makeText(mActivity, "Loading view...", Toast.LENGTH_LONG).show();	

		view.setVisibility(View.VISIBLE);
		
		if ( null != target.url ) {
			view.loadUrl(Target.getImageUrlFromD2(target.url));			
			return;
		}
		
		if ( null != target.description ) {
			view.loadData(target.description, "text/html", "UTF-8");
			return;
		}
		
		hideDetailsView();
		
	}

	public void setLocation(final Location aLocation) {
		if (null == aLocation) {
			// Keep previous point, if any.
			return;
		}

		// Accept new point if no previous or previous less accurate.
		if (null == currentLocationAccuracy
				|| 0 == currentLocationAccuracy 
				|| aLocation.getAccuracy() <= currentLocationAccuracy ) {
			// TODO throw out old points as well.

			currentLat = aLocation.getLatitude();
			currentLon = aLocation.getLongitude();
			currentLocationAccuracy = aLocation.getAccuracy();
		}
		updateDisplay();
	}

	public void showTarget(final Target aTarget) {
		hideDetailsView();
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
		indicator.setIndicatorDrawable(target.indicatorDrawableId);
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
