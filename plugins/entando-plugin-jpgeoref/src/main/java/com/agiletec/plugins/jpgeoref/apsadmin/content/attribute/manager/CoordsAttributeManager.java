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
package com.agiletec.plugins.jpgeoref.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.apsadmin.system.entity.attribute.manager.AbstractMonoLangAttributeManager;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;
import com.agiletec.plugins.jpgeoref.aps.system.services.content.model.extraAttribute.CoordsAttribute;

/**
 * @author E.Santoboni
 */
public class CoordsAttributeManager extends AbstractMonoLangAttributeManager {
	
	/**
	 * Update attribute
	 * @param attribute Attribute to be updated.
	 * @param tracer Attribute tracer
	 * @param request Request 
	 */
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		CoordsAttribute coordsAttribute = (CoordsAttribute) attribute;
		String x = this.getValueFromForm(attribute, tracer, GeoRefSystemConstants.COORDS_ATTRIBUTE_X, request);
		if (x != null && x.trim().length()>0) {
			try {
				coordsAttribute.setX(Double.parseDouble(x));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Error in extracting coordinate x", e);
			}
		} else {
			coordsAttribute.setX(0);
		}
		String y = this.getValueFromForm(attribute, tracer, GeoRefSystemConstants.COORDS_ATTRIBUTE_Y, request);
		if (y != null && y.trim().length()>0) {
			try {
				coordsAttribute.setY(Double.parseDouble(y));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Error in extracting coordinate y", e);
			}
		} else {
			coordsAttribute.setY(0);
		}
		String z = this.getValueFromForm(attribute, tracer, GeoRefSystemConstants.COORDS_ATTRIBUTE_Z, request);
		if (z != null && z.trim().length()>0) {
			try {
				coordsAttribute.setZ(Double.parseDouble(z));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Error in extracting coordinate z", e);
			}
		} else {
			coordsAttribute.setZ(0);
		}
	}

	/**
	 * Returns the value returned by the form.
	 * @param attribute Attribute interface
	 * @param tracer Attribute tracer
	 * @param coordId Coordinate Id
	 * @param request Request
	 * @return the value returned by the form.
	 */
	protected String getValueFromForm(AttributeInterface attribute, AttributeTracer tracer, 
			String coordId, HttpServletRequest request) {
		String value = request.getParameter(coordId + "_" + attribute.getName());
		return value;
	}
	
	@Override
	protected String getInvalidAttributeMessage() {
		return "CoordsAttribute.incompleteAttribute";
	}
	
	@Override
	protected void setValue(AttributeInterface attribute, String value) {
		//do notning
	}
	
}