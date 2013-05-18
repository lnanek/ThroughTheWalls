package com.neatocode.throughwalls.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.location.Location;

public class Target {
	
	public static final List<List<Target>> TARGET_LISTS = new LinkedList<List<Target>>();
	
	static {
		TARGET_LISTS.add(TargetCameras.CAMERAS);
		TARGET_LISTS.add(TargetCities.OTHER_CITIES);
	}

	public static String getImageUrlFromD2(final String url) {
		int idStartIndex = url.lastIndexOf('/');
		int idEndIndex = url.lastIndexOf(".");
		String id = url.substring(idStartIndex, idEndIndex);
		String result = "http://www.dot.ca.gov/cwwp2/data/d3/cctv/image/" + id + ".jpg?" + new Date().getTime();
		return result;
	}
	
	double lat;
	
	double lon;
	
	public String name;
	
	public String url;
	
	public String description;

	public Target(String url, double lon, double lat, String name) {
		this.url = url;
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}
	
	public Target(String url, double lon, double lat, String name, String videoUrl) {
		this.url = url;
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
