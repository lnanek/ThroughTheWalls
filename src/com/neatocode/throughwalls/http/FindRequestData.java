package com.neatocode.throughwalls.http;

import android.location.Location;

public class FindRequestData {
	

	public static final float CALIFORNIA_LAT = 37.544577f;
	
	public static final float CALIFORNIA_LON = -113.115234f;
	
	// If within this number of miles of California, offer to show CHP incidents.
	public static final int OFFER_CHP_RADIUS_MILES = 600;
	
	public static final float M_PER_MI = 1609.344f;
	
	Integer latE6;
	
	Integer lonE6;
		
	public FindRequestData() {
	}

	public FindRequestData(final Double lat, final Double lon) {
		setCoords(lat, lon);
	}
	
	public void setCoords(double lat, double lon) {
		latE6 = (int) (lat * 1E6);
		lonE6 = (int) (lon * 1E6);
	}
	
	public Float getLat() {
		if ( null == latE6 ) {
			return null;
		}
		return latE6 / 1E6f;
	}
	
	public Float getLon() {
		if ( null == lonE6 ) {
			return null;
		}
		return lonE6 / 1E6f;
	}

	public float getDistanceInMiles(double spotLat, double spotLon) {
		return getDistanceInMiles(spotLat, spotLon, getLat(), getLon());
	}

	public static float getDistanceInMiles(double spotLat, double spotLon, double destLat, double destLon) {
		final float[] distanceMeters = new float[1];
		Location.distanceBetween(destLat, destLon, spotLat, spotLon, distanceMeters);
		final float distanceMiles = distanceMeters[0] / M_PER_MI;	
		return distanceMiles;
	}

	public boolean hasCoordinates() {
		return null == lonE6 || null == latE6;
	}
	
}
