/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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