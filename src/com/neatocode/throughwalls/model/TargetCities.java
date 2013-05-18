package com.neatocode.throughwalls.model;

import java.util.LinkedList;
import java.util.List;

public class TargetCities {

	public static final Target BEIJING = 
			new Target(null, -122.049694f, 37.39771f, "Beijing");
	
	public static final Target NYC = 
			new Target(null, -73.982277f, 40.729568f, "NYC");

	public static final Target PALO_ALTO = 
			new Target(null, -122.146511f, 37.440519f, "Palo Alto");
	
	public static final List<Target> OTHER_CITIES = new LinkedList<Target>();
	static {
		OTHER_CITIES.add(TargetCities.BEIJING);
		OTHER_CITIES.add(TargetCities.NYC);
	}

}
