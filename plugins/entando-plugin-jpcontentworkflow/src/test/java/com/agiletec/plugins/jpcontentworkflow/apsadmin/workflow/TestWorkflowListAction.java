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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow;

import java.util.List;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow.WorkflowListAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestWorkflowListAction extends ApsAdminPluginBaseTestCase {
	
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
	
	public void testListForAdminUser() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		WorkflowListAction action = (WorkflowListAction) this.getAction();
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertNotNull(contentTypes.get(0));
		assertNull(action.getRole("ART"));
		assertEquals("pageManager", action.getRole("EVN").getName());
	}
	
	public void testListForNotAllowedUser() throws Throwable {
		String result = this.executeList("editorCustomers");
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
	}
	
	protected String executeList(String currentUserName) throws Throwable {
		this.initAction("/do/jpcontentworkflow/Workflow", "list");
		this.setUserOnSession(currentUserName);
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._helper = new WorkflowTestHelper(workflowManager, configManager);
	}
	
	private WorkflowTestHelper _helper;
	
}