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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestIntroNewContentAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.setWorkflowConfig();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.resetWorkflowConfig();
		super.tearDown();
	}
	
	public void testOpenNew() throws Throwable {
		String result = this.executeOpenNew("editorCoach");
		assertEquals(Action.SUCCESS, result);
		JpCwIntroNewContentAction action = (JpCwIntroNewContentAction) this.getAction();
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertEquals(3, contentTypes.size());
		for (int i=0; i<contentTypes.size(); i++) {
			assertFalse("EVN".equals(contentTypes.get(i).getCode()));
		}
		result = this.executeOpenNew("pageManagerCoach");
		assertEquals("userNotAllowed", result);
	}
	
	public void testCreateNewVoid() throws Throwable {
		String result = this.executeCreateNewVoid("admin", "ART", "descr", Content.STATUS_READY, "free");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeCreateNewVoid("editorCoach", "EVN", "descr", Content.STATUS_READY, "free");
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		List<String> descrErrors = fieldErrors.get("contentTypeCode");
		assertEquals(1, descrErrors.size());
	}
	
	private String executeOpenNew(String currentUserName) throws Throwable {
		this.initAction("/do/jacms/Content", "newJpCw");
		this.setUserOnSession(currentUserName);
		return this.executeAction();
	}
	
	private String executeCreateNewVoid(String username, String contentType, String descr, String status, String contentMainGroup) throws Throwable {
		//this.initAction("/do/jacms/Content", "createNewVoid");
		this.initAction("/do/jacms/Content", "createNewVoidJpCw");
		this.setUserOnSession(username);
		this.addParameter("contentTypeCode", contentType);
		this.addParameter("contentDescription", descr);
		this.addParameter("contentStatus", status);
		this.addParameter("contentMainGroup", contentMainGroup);
		return this.executeAction();
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._helper = new WorkflowTestHelper(workflowManager, configManager);
	}
	
	private WorkflowTestHelper _helper;
	
}