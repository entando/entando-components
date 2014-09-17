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