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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.List;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentInspectionAction extends AbstractBaseTestContentAction {
	
	public void testInspectContent_1() throws Throwable {
		String result = this.executeInspect("ART102", true, "admin");
		assertEquals(Action.SUCCESS, result);
		ContentInspectionAction action = (ContentInspectionAction) this.getAction();
		List<ContentRecordVO> referencedContents = action.getReferencedContents();
		assertNotNull(referencedContents);
		assertEquals(1, referencedContents.size());
		
		List<ContentRecordVO> referencingContents = action.getReferencingContents();
		assertNotNull(referencingContents);
		assertEquals(0, referencingContents.size());
		
		List<IPage> referencedPages = action.getReferencedPages();
		assertNotNull(referencedPages);
		assertEquals(0, referencedPages.size());
		
		List<IPage> referencingPages = action.getReferencingPages();
		assertNotNull(referencingPages);
		assertEquals(0, referencingPages.size());
	}
	
	public void testInspectContent_2() throws Throwable {
		String result = this.executeInspect("ART111", false, "admin");
		assertEquals(Action.SUCCESS, result);
		ContentInspectionAction action = (ContentInspectionAction) this.getAction();
		List<ContentRecordVO> referencedContents = action.getReferencedContents();
		assertNotNull(referencedContents);
		assertEquals(0, referencedContents.size());
		
		List<ContentRecordVO> referencingContents = action.getReferencingContents();
		assertNotNull(referencingContents);
		assertEquals(1, referencingContents.size());
		
		List<IPage> referencedPages = action.getReferencedPages();
		assertNotNull(referencedPages);
		assertEquals(0, referencedPages.size());
		
		List<IPage> referencingPages = action.getReferencingPages();
		assertNotNull(referencingPages);
		assertEquals(1, referencingPages.size());
	}
	
	private String executeInspect(String contentId, boolean publicVersion, String username) throws Throwable {
		this.initAction("/do/jacms/Content", "inspect");
		this.setUserOnSession(username);
		this.addParameter("contentId", contentId);
		this.addParameter("currentPublicVersion", String.valueOf(publicVersion));
		return this.executeAction();
	}
	
}