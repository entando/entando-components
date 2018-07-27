/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.content.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;

import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.IReferenceableAttribute;

/**
 * Represents the authorization information of a content.
 * The enhanced object is cached by alphanumeric identifier produced by the identifier of the content.
 * @author E.Santoboni
 */
public class PublicContentAuthorizationInfo implements Serializable {
	
	private static final long serialVersionUID = -5241592759371755368L;
	
	@Deprecated
	public PublicContentAuthorizationInfo(Content content) {
		this(content, null);
	}
	
	public PublicContentAuthorizationInfo(Content content, List<Lang> langs) {
		this._contentId = content.getId();
		this._contentType = content.getTypeCode();
		this._mainGroup = content.getMainGroup();
		String[] allowedGroups = new String[1+content.getGroups().size()];
		allowedGroups[0] = content.getMainGroup();
		int index = 1;
		Iterator<String> iterGroup = content.getGroups().iterator();
		while (iterGroup.hasNext()) {
			allowedGroups[index++] = iterGroup.next();
		}
		this.setAllowedGroups(allowedGroups);
		if (null == langs) {
			return;
		}
		List<CmsAttributeReference> references = new ArrayList<CmsAttributeReference>();
		EntityAttributeIterator attributeIter = new EntityAttributeIterator(content);
		while (attributeIter.hasNext()) {
			AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
			if (currAttribute instanceof IReferenceableAttribute) {
				IReferenceableAttribute referenceableAttr = (IReferenceableAttribute) currAttribute;
				List<CmsAttributeReference> attributeReferences = referenceableAttr.getReferences(langs);
				if (null != attributeReferences) {
					references.addAll(attributeReferences);
				}
			}
		}
		for (int i = 0; i < references.size(); i++) {
			CmsAttributeReference cmsAttributeReference = references.get(i);
			if (null != cmsAttributeReference.getRefResource()) {
				this.addProtectedResourceId(cmsAttributeReference.getRefResource());
			}
		}
	}
	
	/**
	 * Setta l'array dei codici dei gruppi 
	 * autorizzati alla visualizzazione del contenuto.
	 * @param allowedGroups  L'array dei codici dei gruppi autorizzati.
	 */
	protected void setAllowedGroups(String[] allowedGroups) {
		this._allowedGroups = allowedGroups;
	}
	
	public boolean isUserAllowed(Collection<String> userGroupCodes) {
		if (null == userGroupCodes) {
			userGroupCodes = new ArrayList<String>();
		}
		if (userGroupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			return true;
		}
    	for (int i=0; i<_allowedGroups.length; i++) {
			String allowedGroup = _allowedGroups[i];
			if (Group.FREE_GROUP_NAME.equals(allowedGroup) || 
					userGroupCodes.contains(allowedGroup)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica i permessi dell'utente in accesso al contenuto.
	 * Restituisce true se l'utente specificato è abilitato 
	 * ad accedere al contenuto, false in caso contrario.
	 * @param userGroups I gruppi dell'utente di cui verificarne l'abilitazione.
	 * @return true se l'utente specificato è abilitato ad accedere 
	 * al contenuto, false in caso contrario.
	 */
	public boolean isUserAllowed(List<Group> userGroups) {
		if (null == userGroups) {
			userGroups = new ArrayList<Group>();
		}
		Set<String> codes = new HashSet<String>();
		for (int i = 0; i < userGroups.size(); i++) {
			Group group = userGroups.get(i);
			codes.add(group.getAuthority());
		}
		return this.isUserAllowed(codes);
	}
	
	/**
	 * Aggiunge un identificativo di risorsa protetta nella lista 
	 * di risorse protette referenziato dal contenuto.
	 * @param resourceId L'identificativo della risorsa protetta 
	 * da aggiungere nella lista.
	 */
	protected void addProtectedResourceId(String resourceId) {
		int len = this._protectedResourcesId.length;
		String[] newArray = new String[len + 1];
		for (int i=0; i < len; i++){
			newArray[i] = this._protectedResourcesId[i];
		}
		newArray[len] = resourceId;
		this._protectedResourcesId = newArray;
	}
	
	/**
	 * Verifica che una risorsa protetta sia referenziata nel contenuto gestito.
	 * @param resourceId L'identificativo della risorsa del quale 
	 * verificare se referenziato.
	 * @return True se la risorsa è referenziata nel contenuto, 
	 * false in caso contrario.
	 */
	public boolean isProtectedResourceReference(String resourceId) {
		for (int i=0; i<this._protectedResourcesId.length; i++) {
			if (this._protectedResourcesId[i].equals(resourceId)) {
				return true;
			}
		}
		return false;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public String getContentType() {
		return _contentType;
	}
	public String getMainGroup() {
		return _mainGroup;
	}

	private String _contentId;
	private String _contentType;
	
	private String _mainGroup;
	
	private String[] _allowedGroups = new String[0];
	private String[] _protectedResourcesId = new String[0];
	
}
