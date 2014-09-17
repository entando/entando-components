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
package com.agiletec.plugins.jpimagemap.apsadmin.content.attribute.action.link.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.ImageMapAttribute;

/**
 * Classe helper base per le action delegata 
 * alla gestione delle operazione sugli attributi link.
 * @author E.Santoboni
 */
public class LinkAttributeActionHelper extends com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.LinkAttributeActionHelper {
	
	@Override
	protected AttributeInterface getLinkAttribute(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			return getLinkAttribute(includedAttribute, request);
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			return getLinkAttribute(attributeElement, request);
		} else if (attribute instanceof ImageMapAttribute) {
			AttributeInterface attributeElement = this.getImageMapAttribute(attribute, session);
			return attributeElement;
		} else if (attribute instanceof LinkAttribute) {
			return attribute;
		} else {
			throw new RuntimeException("Caso non gestito : Atttributo tipo " + attribute.getClass());
		}
	}
	
	@Override
	protected void joinLink(AttributeInterface attribute, String[] destinations, int destType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			updateLink(includedAttribute, destinations, destType);
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			joinLink(attributeElement, destinations, destType, request);
		} else if (attribute instanceof ImageMapAttribute) {
			AttributeInterface attributeElement = this.getImageMapAttribute(attribute, session);
			updateLink(attributeElement, destinations, destType);
		} else if (attribute instanceof LinkAttribute) {
			updateLink(attribute, destinations, destType);
		}
	}
	
	@Override
	protected void removeLink(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			removeLink(includedAttribute, request);
		} else if (attribute instanceof LinkAttribute) {
			((LinkAttribute) attribute).setSymbolicLink(null);
			((LinkAttribute) attribute).getTextMap().clear();
		} else if (attribute instanceof ImageMapAttribute) {
			AttributeInterface attributeElement = this.getImageMapAttribute(attribute, session);
			removeLink(attributeElement, request);
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			removeLink(attributeElement, request);
		}
	}
	
	protected AttributeInterface getImageMapAttribute(AttributeInterface attribute, HttpSession session) {
		Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
		AttributeInterface attributeElement = ((ImageMapAttribute) attribute).getArea(elementIndex).getLink();
		return attributeElement;
	}
	
}