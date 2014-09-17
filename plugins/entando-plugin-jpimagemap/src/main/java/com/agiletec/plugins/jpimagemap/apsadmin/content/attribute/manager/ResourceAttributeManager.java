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
package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;

/**
 * Classe manager degli attributi tipo risorsa Image(per ImageMap attribute).
 * @author E.Santoboni - G.Cocco
 */
public class ResourceAttributeManager extends com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.ResourceAttributeManager {
	
	@Override
	protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
		super.updateAttribute(attribute, tracer, request);
	}
	
}