package com.neatocode.throughwalls.http;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Placemark extends Location {

	private String name;
	
	private String snippet;
	
	private String address;
	
	private String coordinates;

	private String baloonText;
	
	private String description;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBaloonText() {
		return baloonText;
	}

	public void setBaloonText(String baloonText) {
		this.baloonText = baloonText;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
