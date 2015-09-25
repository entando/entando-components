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
package org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import java.util.List;

import org.entando.entando.plugins.jpfileattribute.aps.system.entity.model.FileAttribute;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.AttachedFile;

/**
 * Classe helper base per le action delegata 
 * alla gestione delle operazione sugli attributi risorsa.
 * @author E.Santoboni
 */
public abstract class FileAttributeActionHelper {
	
	public static void initSessionParams(FileAttributeAction action, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (null != action.getParentAttributeName()) {
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getParentAttributeName());
			session.setAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM, action.getAttributeName());
		} else {
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getAttributeName());
		}
		if (action.getElementIndex()>=0) {
			session.setAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM, new Integer(action.getElementIndex()));
		}
	}
	
	/**
	 * Associa la risorsa all'attributo del contenuto o all'elemento dell'attributo lista
	 * o all'elemento dell'attributo Composito (sia semplice che in lista).
	 */
	public static void joinResource(IApsEntity currentEntity, AttachedFile file, HttpServletRequest request) {
		HttpSession session = request.getSession();
		//IApsEntity currentContent = FileAttributeActionHelper.getContent(request);
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) currentEntity.getAttribute(attributeName);
		joinResource(attribute, file, request);
		removeSessionParams(session);
	}
	
	/**
	 * Associa la risorsa all'attributo del contenuto o all'elemento dell'attributo lista
	 * o all'elemento dell'attributo Composito (sia semplice che in lista).
	 */
	private static void joinResource(AttributeInterface attribute, AttachedFile file, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			joinResource(includedAttribute, file, request);
		} else if (attribute instanceof FileAttribute) {
			((FileAttribute) attribute).setAttachedFile(file);
			//TODO
			//String langCode = (String) session.getAttribute(RESOURCE_LANG_CODE_SESSION_PARAM);
			//langCode = (langCode!=null && !"".equals(langCode)) ? langCode : null;
			//((ResourceAttributeInterface) attribute).setResource(resource, langCode);
		} else if (attribute instanceof MonoListAttribute) {
			int elementIndex = ((Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM)).intValue();
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex);
			joinResource(attributeElement, file, request);
		} else if (attribute instanceof ListAttribute) {
			int elementIndex = ((Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM)).intValue();
			ListAttribute listAttribute = (ListAttribute) attribute;
			String defaultLang = listAttribute.getDefaultLangCode();
			List<AttributeInterface> attributeList = listAttribute.getAttributeList(defaultLang);
			AttributeInterface attributeElement = attributeList.get(elementIndex);
			joinResource(attributeElement, file, request);
		}
	}
	
	public static void removeSessionParams(HttpSession session) {
		session.removeAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		session.removeAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
		session.removeAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
	}
	
	public static void removeResource(IApsEntity entity, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) entity.getAttribute(attributeName);
		removeResource(attribute, request);
		removeSessionParams(session);
	}
	
	private static void removeResource(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			removeResource(includedAttribute, request);
		} else if (attribute instanceof FileAttribute) {
			FileAttribute fileAttribute = (FileAttribute) attribute;
			fileAttribute.setAttachedFile(null);
		} else if (attribute instanceof MonoListAttribute) {
			int elementIndex = ((Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM)).intValue();
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex);
			removeResource(attributeElement, request);
		}
	}
	
	public static final String ATTRIBUTE_NAME_SESSION_PARAM = "fileAttribute_contentAttributeName";
	public static final String LIST_ELEMENT_INDEX_SESSION_PARAM = "fileAttribute_listElementIndex";
	public static final String INCLUDED_ELEMENT_NAME_SESSION_PARAM = "fileAttribute_includedElementName";
	
}
