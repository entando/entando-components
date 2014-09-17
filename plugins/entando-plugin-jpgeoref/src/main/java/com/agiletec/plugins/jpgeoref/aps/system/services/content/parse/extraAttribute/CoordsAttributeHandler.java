/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpgeoref.aps.system.services.content.parse.extraAttribute;

import org.apache.commons.beanutils.BeanUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.AbstractAttributeHandler;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;
import com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute.CoordsAttribute;

/**
 * @author E.Santoboni
 */
public class CoordsAttributeHandler extends AbstractAttributeHandler {

	/**
	 * Start attribute analysis.
	 */
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_X)) {
			//nothing to do
		} else if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_Y)) {
			//nothing to do
		} else if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_Z)) {
			//nothing to do
		}
	}

	/**
	 * End attribute analysis.
	 */
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_X)) {
			this.endCoords(textBuffer, GeoRefSystemConstants.COORDS_ATTRIBUTE_X);
		} else if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_Y)) {
			this.endCoords(textBuffer, GeoRefSystemConstants.COORDS_ATTRIBUTE_Y);
		} else if (qName.equals(GeoRefSystemConstants.COORDS_ATTRIBUTE_Z)) {
			this.endCoords(textBuffer, GeoRefSystemConstants.COORDS_ATTRIBUTE_Z);
		}
	}
	
	/**
	 * Sets coordinate property
	 * @param textBuffer property value
	 * @param property property name
	 */
	private void endCoords(StringBuffer textBuffer, String property) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			String coordsString = textBuffer.toString();
			double coord = Double.parseDouble(coordsString);
			CoordsAttribute attribute = (CoordsAttribute) this.getCurrentAttr();
			try {
				BeanUtils.setProperty(attribute, property, new Double(coord));
			} catch (Throwable t) {
				throw new RuntimeException("Error setting '" + property + "' property", t);
			}
		}
	}

}