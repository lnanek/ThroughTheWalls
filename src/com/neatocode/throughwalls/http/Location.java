package com.neatocode.throughwalls.http;

import org.apache.commons.lang3.builder.CompareToBuilder;

import flexjson.JSON;

public class Location implements Comparable<Location> {

	private Float distanceFromDestination;

	protected int lat;
	
	protected int lon;

	@JSON(include = false)
	public Float getDistanceFromDestination() {
		return distanceFromDestination;
	}

	public void setDistanceFromDestination(Float distance) {
		this.distanceFromDestination = distance;
	}
	
	public void setLat(int latE6) {
		this.lat = latE6;
	}

	public void setLon(int lonE6) {
		this.lon = lonE6;
	}

	public int getLat() {
		return lat;
	}

	public int getLon() {
		return lon;
	}

	public int compareTo(Location rhs) {
		return new CompareToBuilder()
			.append(distanceFromDestination, rhs.distanceFromDestination)
			.toComparison();
	}
	
}
