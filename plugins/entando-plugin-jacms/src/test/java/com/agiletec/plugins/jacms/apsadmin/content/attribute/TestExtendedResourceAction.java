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
package com.agiletec.plugins.jacms.apsadmin.content.attribute;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ExtendedResourceAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ResourceAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestExtendedResourceAction extends AbstractBaseTestContentAction {

	public void testNewImageResource_1() throws Throwable {
		this.executeEdit("ART1", "admin");//Contenuto FREE
		String contentOnSessionMarker = super.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);

		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "Foto");
		session.setAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM, "Image");
		session.setAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM, "it");

		this.initContentAction("/do/jacms/Content/Resource", "new", contentOnSessionMarker);
		this.addParameter("resourceTypeCode", "Image");//per replicare il chain in occasione dei chooseResource da edit Contenuto.
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ExtendedResourceAction action = (ExtendedResourceAction) this.getAction();
		List<Group> allowedGroup = action.getAllowedGroups();
		assertEquals(1, allowedGroup.size());
	}

	public void testNewImageResource_2() throws Throwable {
		this.executeEdit("ART102", "admin");//Contenuto customers
		String contentOnSessionMarker = super.extractSessionMarker("ART102", ApsAdminSystemConstants.EDIT);

		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "Foto");
		session.setAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM, "Image");
		session.setAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM, "it");

		this.initContentAction("/do/jacms/Content/Resource", "new", contentOnSessionMarker);
		this.addParameter("resourceTypeCode", "Image");//per replicare il chain in occasione dei chooseResource da edit Contenuto.
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ExtendedResourceAction action = (ExtendedResourceAction) this.getAction();
		List<Group> allowedGroup = action.getAllowedGroups();
		assertEquals(2, allowedGroup.size());
	}

}