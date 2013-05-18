package com.neatocode.throughwalls;

import android.location.Location;

public class Target {

	public static final Target BEIJING = 
			new Target(37.39771f, -122.049694f, "Beijing");
	
	public static final Target NYC = 
			new Target(40.729568f, -73.982277f, "NYC");

	public static final Target PALO_ALTO = 
			new Target(37.440519f, -122.146511f, "Palo Alto");

	double lat;
	
	double lon;
	
	String name;

	public Target(double lat, double lon, String name) {
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}
	
	public Location asLocation() {
		final Location targetLocation = new Location("ThroughGlass");
		targetLocation.setLatitude(lat);
		targetLocation.setLongitude(lon);
		return targetLocation;
	}

}
