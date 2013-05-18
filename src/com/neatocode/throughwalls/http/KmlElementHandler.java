package com.neatocode.throughwalls.http;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class KmlElementHandler extends DefaultHandler {

	private boolean inKmlTag;

	private boolean inPlacemarkTag;
	
	private boolean inNameTag;
	
	private boolean inSnippetTag;
	
	private boolean inAddressTag;
	
	private boolean inPointTag;
	
	private boolean inCoordinatesTag;
	
	private boolean inExtendedData;
	
	private boolean inBalloonText;
	
	private boolean inBalloonTextValue;
	
	private boolean inDescriptionTag;
	
	private StringBuffer characterBuffer;

	private List<Placemark> placemarks = new ArrayList<Placemark>();
	
	private Placemark currentPlacemark;
	
	public List<Placemark> getParsedData() {
		return placemarks;
	}

	@Override
	public void startDocument() throws SAXException {
		placemarks = new ArrayList<Placemark>();
		characterBuffer = null;
		currentPlacemark = null;
	}

	@Override
	public void endDocument() throws SAXException {
		characterBuffer = null;
		currentPlacemark = null;
	}

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if ( localName.equals("kml") ) {
			inKmlTag = true;
			return;
		}
		if ( !inKmlTag ) {
			return;
		}
			
		if ( localName.equals("Placemark") ) {
			inPlacemarkTag = true;
			currentPlacemark = new Placemark();
			return;
		} 
		if ( !inPlacemarkTag ) {
			return;
		}
				
		if ( localName.equals("name") ) {
			characterBuffer = new StringBuffer();
			inNameTag = true;
		
		} else if ( localName.equals("description") ) {
			characterBuffer = new StringBuffer();
			inDescriptionTag = true;
		
		} else if ( localName.equals("Snippet") ) {
			characterBuffer = new StringBuffer();
			inSnippetTag = true;
		
		} else if ( localName.equals("address") ) {
			characterBuffer = new StringBuffer();
			inAddressTag = true;
			
		} else if ( localName.equals("Point") ) {
			inPointTag = true;
			
		} else if ( inPointTag && localName.equals("coordinates") ) {
			characterBuffer = new StringBuffer();
			inCoordinatesTag = true;
			
		} else if ( localName.equals("ExtendedData") ) {
			inExtendedData = true;
			
		} else if ( inExtendedData && localName.equals("Data") && containsName(atts, "balloon_text") ) {
			inBalloonText = true;
			
		} else if ( inBalloonText && localName.equals("value") ) {
			characterBuffer = new StringBuffer();
			inBalloonTextValue = true;
		}
	}
	
	private static boolean containsName(final Attributes atts, final String checkForValue) {
		for ( int i = 0 ; i < atts.getLength(); i++ ) {
			if ( "name".equals(atts.getLocalName(i)) && checkForValue.equals(atts.getValue(i)) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("kml")) {
			inKmlTag = false;
			return;
		} 		
		if ( !inKmlTag ) {
			return;
		}
		
		if (localName.equals("Placemark")) {
			inPlacemarkTag = false;
			placemarks.add(currentPlacemark);
			currentPlacemark = null;
			return;
		} 		
		if ( !inPlacemarkTag ) {
			return;
		}
		
		if ( inNameTag && localName.equals("name") ) {
			inNameTag = false;
			currentPlacemark.setName(characterBuffer.toString());
			characterBuffer = null;
		
		} else if ( inSnippetTag && localName.equals("Snippet") ) {
			inSnippetTag = false;
			currentPlacemark.setSnippet(characterBuffer.toString());
			characterBuffer = null;
		
		} else if ( inDescriptionTag && localName.equals("description") ) {
			inDescriptionTag = false;
			currentPlacemark.setDescription(characterBuffer.toString());
			characterBuffer = null;
		
		} else if ( inAddressTag && localName.equals("address") ) {
			inAddressTag = false;			
			currentPlacemark.setAddress(characterBuffer.toString());
			characterBuffer = null;
		
		} else if ( inPointTag && localName.equals("Point") ) {
			inPointTag = false;			
		
		} else if ( inPointTag && inCoordinatesTag && localName.equals("coordinates") ) {
			inCoordinatesTag = false;			

			String location = characterBuffer.toString();
			characterBuffer = null;
			currentPlacemark.setCoordinates(location);
			
			int commaLocation = location.indexOf(',');
			if ( -1 != commaLocation ) {
				String lon = location.substring(0, commaLocation);
				if ( location.length() > commaLocation ) {
					String remainingCoords = location.substring(commaLocation + 1, location.length());
					int remainingCoordsCommaLocation = remainingCoords.indexOf(',');
					if ( -1 != remainingCoordsCommaLocation ) {
						String lat = remainingCoords.substring(0, remainingCoordsCommaLocation);
						
						try {
							int latE6 = (int) (Float.parseFloat(lat) * 1E6);
							int lonE6 = (int) (Float.parseFloat(lon) * 1E6);
							currentPlacemark.setLat(latE6);
							currentPlacemark.setLon(lonE6);
						} catch (NumberFormatException e) {
							// Do nothing. Fields not set.
						}						
					}
				}
			}			
		} else if ( inExtendedData && localName.equals("ExtendedData") ) {
			inExtendedData = false;
			
		} else if ( inExtendedData && inBalloonText && localName.equals("Data") ) {
			inBalloonText = false;
			
		} else if ( inExtendedData && inBalloonText && inBalloonTextValue && localName.equals("value") ) {
			inBalloonTextValue = false;
			currentPlacemark.setBaloonText(characterBuffer.toString());
			characterBuffer = null;			
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		if ( null == currentPlacemark ) {
			return;
		}
		
		if ( null == characterBuffer ) {
			return;
		}
		
		characterBuffer.append(ch, start, length);
	}
}
