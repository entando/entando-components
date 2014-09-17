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