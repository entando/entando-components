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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow.WorkflowAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestWorkflowAction extends ApsAdminPluginBaseTestCase {
	
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
	
	public void testEdit() throws Throwable {
		String typeCode = "ART";
		String result = this.executeEdit("admin", typeCode);
		assertEquals(Action.SUCCESS, result);
		WorkflowAction action = (WorkflowAction) this.getAction();
		String role = this._workflowManager.getRole(typeCode);
		assertEquals(role, action.getRole());
	}
	
	public void testSave() throws Throwable {
		String typeCode = "ART";
		String originaryRole = this._workflowManager.getRole(typeCode);
		String result = this.executeSave("admin", typeCode, "supervisor");
		assertEquals(Action.SUCCESS, result);
		String updatedRole = this._workflowManager.getRole(typeCode);
		assertEquals("supervisor", updatedRole);
		
		result = this.executeSave("admin", typeCode, originaryRole);
		assertEquals(Action.SUCCESS, result);
		updatedRole = this._workflowManager.getRole(typeCode);
		assertEquals(originaryRole, updatedRole);
	}
	
	protected String executeEdit(String currentUserName, String typeCode) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "editRole");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		String result = this.executeAction();
		return result;
	}
	
	protected String executeSave(String currentUserName, String typeCode, String role) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "saveRole");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		if (role!=null) {
			this.addParameter("role", role);
		}
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		this._workflowManager = workflowManager;
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._helper = new WorkflowTestHelper(workflowManager, configManager);
	}
	
	private IContentWorkflowManager _workflowManager;
	private WorkflowTestHelper _helper;
	
}