package com.neatocode.throughwalls.http;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ShelterElementHandler extends DefaultHandler {

	private boolean inDataTag;

	private boolean inRecordTag;

	private boolean isApproved;

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
		if (localName.equals("data")) {
			inDataTag = true;
			return;
		}
		if (!inDataTag) {
			return;
		}

		if (localName.equals("record")) {
			inRecordTag = true;
			currentPlacemark = new Placemark();
			currentPlacemark.setDescription("");

			for (int i = 0; i < atts.getLength(); i++) {
				final String attLocalName = atts.getLocalName(i);
				final String attValue = atts.getValue(i);

				if (attLocalName.equals("positionname")) {
					currentPlacemark.setName(attValue);
				} else if (attLocalName.equals("_sysmapper_latitude")) {
					try {
						currentPlacemark.setLat((int) (Float
								.parseFloat(attValue) * 1E6));
					} catch (Exception e) {
						// Ignore, usually a blank attribute value meaning no
						// value.
					}
				} else if (attLocalName.equals("_sysmapper_longitude")) {
					try {
						currentPlacemark.setLon((int) (Float
								.parseFloat(attValue) * 1E6));
					} catch (Exception e) {
						// Ignore, usually a blank attribute value meaning no
						// value.
					}
				} else if (attLocalName.equals("Public_Release")
						&& attValue.equals("APPROVED")) {
					isApproved = true;
				} else if (attLocalName.equals("tablename")
						|| attLocalName.equals("dataid")
						|| attLocalName.equals("subscribername")) {
					// Skip, not used/displayed.
				} else if (null != attValue && !"".equals(attValue.trim())) {
					currentPlacemark.setDescription(currentPlacemark
							.getDescription()
							+ "<br />"
							+ attLocalName
							+ ": "
							+ attValue);
				}

			}

			if (isApproved) {
				placemarks.add(currentPlacemark);
			}
			isApproved = false;
			inRecordTag = false;
			currentPlacemark = null;

			return;
		}
		if (!inRecordTag) {
			return;
		}

	}

}
