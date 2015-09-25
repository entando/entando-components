/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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