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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;

/**
 * @author E.Santoboni
 */
public class TestContentWorkflowManager extends ApsPluginBaseTestCase {
	
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
	
	public void testGetWorkflow() {
		Workflow workflow = this._workflowManager.getWorkflow("ART");
		assertEquals("ART", workflow.getTypeCode());
		assertNull(workflow.getRole());
		List<Step> steps = workflow.getSteps();
		assertEquals(3, steps.size());
		Step step1 = workflow.getStep("step1");
		assertEquals("step1", step1.getCode());
		assertEquals("Step 1", step1.getDescr());
		assertEquals("pageManager", step1.getRole());
		
		Step step2 = workflow.getStep("step2");
		assertEquals("step2", step2.getCode());
		assertEquals("Step 2", step2.getDescr());
		assertNull(step2.getRole());
		
		Step step3 = workflow.getStep("step3");
		assertEquals("step3", step3.getCode());
		assertEquals("Step 3", step3.getDescr());
		assertEquals("supervisor", step3.getRole());
		
		Workflow workflow2 = this._workflowManager.getWorkflow("EVN");
		assertEquals("pageManager", workflow2.getRole());
		assertEquals(0, workflow2.getSteps().size());
		
		assertNotNull(this._workflowManager.getWorkflow("RAH"));
	}
	
	public void testGetRole() {
		assertNull(this._workflowManager.getRole("ART"));
		
		String role = this._workflowManager.getRole("EVN");
		assertEquals("pageManager", role);
	}
	
	public void testUpdateRole() throws ApsSystemException {
		String typeCode = "ART";
		String xml = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
		assertNull(this._workflowManager.getRole(typeCode));
		
		this._workflowManager.updateRole(typeCode, "pageManager");
		String updatedXml = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
		assertEquals("pageManager", this._workflowManager.getRole(typeCode));
		assertFalse(xml.equals(updatedXml));
		
		this._workflowManager.updateRole(typeCode, null);
		assertNull(this._workflowManager.getRole(typeCode));
	}
	
	public void testGetSteps() {
		List<Step> steps = this._workflowManager.getSteps("ART");
		assertEquals(3, steps.size());
		
		steps = this._workflowManager.getSteps("EVN");
		assertEquals(0, steps.size());
		
		steps = this._workflowManager.getSteps("RAH");
		assertEquals(0, steps.size());
	}
	
	public void testUpdateSteps() throws ApsSystemException {
		String typeCode = "EVN";
		String xml = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
		assertEquals(0, this._workflowManager.getSteps(typeCode).size());
		
		List<Step> steps = this.createSteps();
		this._workflowManager.updateSteps(typeCode, steps);
		String updatedXml = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
		assertEquals(2, this._workflowManager.getSteps(typeCode).size());
		assertFalse(xml.equals(updatedXml));
		
		this._workflowManager.updateSteps(typeCode, new ArrayList<Step>());
		assertEquals(0, this._workflowManager.getSteps(typeCode).size());
	}
	
	private List<Step> createSteps() {
		List<Step> steps = new ArrayList<Step>();
		
		Step step1 = new Step();
		step1.setCode("step_1");
		step1.setDescr("step_1 descr");
		step1.setRole("role1");
		steps.add(step1);
		
		Step step2 = new Step();
		step2.setCode("step_2");
		step2.setDescr("step_2 descr");
		step2.setRole("role2");
		steps.add(step2);
		
		return steps;
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		this._workflowManager = workflowManager;
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._helper = new WorkflowTestHelper(workflowManager, this._configManager);
	}
	
	private IContentWorkflowManager _workflowManager;
	private ConfigInterface _configManager;
	private WorkflowTestHelper _helper;
	
}