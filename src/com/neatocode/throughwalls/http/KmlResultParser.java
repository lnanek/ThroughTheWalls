package com.neatocode.throughwalls.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

public class KmlResultParser {

	private static final String LOG_TAG = "MapService";
	
	public static String inputStreamToString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * Retrieve navigation data set from either remote URL or String
	 * 
	 * @param url
	 * @return navigation set
	 */
	public static List<Placemark> getPlacemarks(String url) {

		// urlString = "http://192.168.1.100:80/test.kml";
		Log.d(LOG_TAG, "urlString -->> " + url);
		List<Placemark> navigationDataSet = null;
		try {
			final URL aUrl = new URL(url);
			final URLConnection conn = aUrl.openConnection();
			conn.setReadTimeout(15 * 1000); // timeout for reading the google
											// maps data: 15 secs
			conn.connect();

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Create a new ContentHandler and apply it to the XML-Reader */
			KmlElementHandler navSax2Handler = new KmlElementHandler();
			xr.setContentHandler(navSax2Handler);

			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(aUrl.openStream()));

			/* Our NavigationSaxHandler now provides the parsed data to us. */
			navigationDataSet = navSax2Handler.getParsedData();

			/* Set the result to be displayed in our GUI. */
			Log.d(LOG_TAG, "navigationDataSet: " + navigationDataSet.toString());

		} catch (Exception e) {
			Log.e(LOG_TAG, "error with kml xml", e);
			navigationDataSet = null;
		}

		return navigationDataSet;
	}

}
