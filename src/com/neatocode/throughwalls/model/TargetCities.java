package com.neatocode.throughwalls.model;

import java.util.LinkedList;
import java.util.List;

import com.neatocode.throughwalls.R;

public class TargetCities {

	public static final Target Tour_Eiffel = new Target(null, -122.082825f, 37.497742, "Tour Eiffel");	
	public static final Target NYC_Empire_State_Building = new Target(null, -73.985667f, 40.7482f, "NYC Empire State Building");
	public static final Target PALO_ALTO = new Target(null, -122.146511f, 37.440519f, "Palo Alto");
	public static final Target BEIJING = new Target(null, -73.985667f, 40.7482f, "Beijing");	
	public static final Target Sydney_Opera_House = new Target(null, 116.4077f, 39.898675f, "Sydney Opera House");
	public static final Target Shwedagon = new Target(null, 151.214731f, -33.857716f, "Shwedagon");	
	public static final Target Leaning_Tower_of_Pisa = new Target(null, 10.396693f, 43.722824f, "Leaning Tower of Pisa");

	public static final Target Forbidden_City = new Target(null, 0.021887f, 39.915316f, "Forbidden City");
	public static final Target Cologne_Cathedral = new Target(null, 6.95838f, 50.941299f, "Cologne Cathedral");
	public static final Target Brandenburg_Gate = new Target(null, 13.377914f, 52.516116f, "Brandenburg Gate");
	
	public static final List<Target> OTHER_CITIES = new LinkedList<Target>();
	static {
		
		
		OTHER_CITIES.add(TargetCities.Tour_Eiffel);
		Tour_Eiffel.localHtmlIndicator = "file:///android_asset/www/Tour_Eiffel.html";

		OTHER_CITIES.add(TargetCities.Sydney_Opera_House);
		Sydney_Opera_House.localHtmlIndicator = "file:///android_asset/www/Sydney_Opera_House.html";

		OTHER_CITIES.add(TargetCities.Shwedagon);
		Shwedagon.localHtmlIndicator = "file:///android_asset/www/Shwedagon.html";

		OTHER_CITIES.add(TargetCities.NYC_Empire_State_Building);
		NYC_Empire_State_Building.localHtmlIndicator = "file:///android_asset/www/NYC_Empire_State_Building.html";

		OTHER_CITIES.add(TargetCities.Leaning_Tower_of_Pisa);
		Leaning_Tower_of_Pisa.localHtmlIndicator = "file:///android_asset/www/Leaning_Tower_of_Pisa.html";

		OTHER_CITIES.add(TargetCities.BEIJING);
		BEIJING.localHtmlIndicator = "file:///android_asset/www/generic-city-icon.html";

		OTHER_CITIES.add(TargetCities.Forbidden_City);
		Forbidden_City.localHtmlIndicator = "file:///android_asset/www/Forbidden_City.html";

		OTHER_CITIES.add(TargetCities.Cologne_Cathedral);
		Cologne_Cathedral.localHtmlIndicator = "file:///android_asset/www/Cologne_Cathedral.html";

		OTHER_CITIES.add(TargetCities.Brandenburg_Gate);
		Brandenburg_Gate.localHtmlIndicator = "file:///android_asset/www/Brandenburg-Gate-342px.html";

		OTHER_CITIES.add(TargetCities.PALO_ALTO);
		PALO_ALTO.localHtmlIndicator = "file:///android_asset/www/generic-city-icon.html";

	
	}

}
