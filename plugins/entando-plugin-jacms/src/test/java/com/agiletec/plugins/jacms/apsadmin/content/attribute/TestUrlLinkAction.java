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
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestUrlLinkAction extends AbstractBaseTestContentAction {

	public void testFailureJoinContentLink_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("ART1", "VediAnche", "it");

		this.initContentAction("/do/jacms/Content/Link", "joinUrlLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("url");
		assertEquals(1, typeFieldErrors.size());
	}

	public void testJoinContentLink_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("ART1", "VediAnche", "it");

		this.initContentAction("/do/jacms/Content/Link", "joinUrlLink", contentOnSessionMarker);
		this.addParameter("url", "http://www.japsportal.org");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		Content content = this.getContentOnEdit(contentOnSessionMarker);
		LinkAttribute attribute = (LinkAttribute) content.getAttribute("VediAnche");
		SymbolicLink symbolicLink = attribute.getSymbolicLink();
		assertNotNull(symbolicLink);
		assertNull(symbolicLink.getPageDest());
		assertNull(symbolicLink.getContentDest());
		assertEquals("http://www.japsportal.org", symbolicLink.getUrlDest());
	}

	private String initJoinLinkTest(String contentId, String simpleLinkAttributeName, String langCode) throws Throwable {
		Content content = this.getContentManager().loadContent(contentId, false);
		this.executeEdit(contentId, "admin");
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, simpleLinkAttributeName);
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, langCode);
		return contentOnSessionMarker;
	}

}