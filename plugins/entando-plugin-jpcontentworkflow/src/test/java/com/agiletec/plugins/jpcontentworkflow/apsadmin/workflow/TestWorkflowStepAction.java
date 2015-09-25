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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestWorkflowStepAction extends ApsAdminPluginBaseTestCase {
	
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
	
	public void testEditSteps() throws Throwable {
		String typeCode = "ART";
		String result = this.executeEditSteps("admin", typeCode);
		assertEquals(Action.SUCCESS, result);
		WorkflowStepAction action = (WorkflowStepAction) this.getAction();
		SmallContentType contentType = action.getContentType();
		assertEquals(typeCode, contentType.getCode());
		assertEquals(typeCode, action.getTypeCode());
		assertEquals(3, action.getSteps().size());
	}
	
	public void testMoveStep() throws Throwable {
		String typeCode = "ART";
		WorkflowStepAction action = (WorkflowStepAction) this.executeSuccessfulEditSteps(typeCode);
		
		String result = this.executeMoveStep("admin", typeCode, IWorkflowStepAction.MOVEMENT_DOWN_CODE, "0", action.getSteps());
		assertEquals(Action.SUCCESS, result);
		action = (WorkflowStepAction) this.getAction();
		SmallContentType contentType = action.getContentType();
		assertEquals(typeCode, contentType.getCode());
		assertEquals(typeCode, action.getTypeCode());
		List<Step> steps = action.getSteps();
		assertEquals(3, steps.size());
		assertEquals("step2", steps.get(0).getCode());
		assertEquals("step1", steps.get(1).getCode());
		assertEquals("step3", steps.get(2).getCode());
		
		result = this.executeMoveStep("admin", typeCode, IWorkflowStepAction.MOVEMENT_UP_CODE, "1", steps);
		assertEquals(Action.SUCCESS, result);
		action = (WorkflowStepAction) this.getAction();
		steps = action.getSteps();
		assertEquals(3, steps.size());
		assertEquals("step1", steps.get(0).getCode());
		assertEquals("step2", steps.get(1).getCode());
		assertEquals("step3", steps.get(2).getCode());
		
		result = this.executeMoveStep("admin", typeCode, IWorkflowStepAction.MOVEMENT_UP_CODE, "0", steps);
		assertEquals(Action.SUCCESS, result);
		action = (WorkflowStepAction) this.getAction();
		steps = action.getSteps();
		assertEquals(3, steps.size());
		assertEquals("step1", steps.get(0).getCode());
		assertEquals("step2", steps.get(1).getCode());
		assertEquals("step3", steps.get(2).getCode());
		
		result = this.executeMoveStep("admin", typeCode, IWorkflowStepAction.MOVEMENT_DOWN_CODE, "2", steps);
		assertEquals(Action.SUCCESS, result);
		action = (WorkflowStepAction) this.getAction();
		steps = action.getSteps();
		assertEquals(3, steps.size());
		assertEquals("step1", steps.get(0).getCode());
		assertEquals("step2", steps.get(1).getCode());
		assertEquals("step3", steps.get(2).getCode());
	}
	
	public void testRemoveStep() throws Throwable {
		String typeCode = "ART";
		WorkflowStepAction updateAction = (WorkflowStepAction) this.executeSuccessfulEditSteps(typeCode);
		assertEquals(3, updateAction.getSteps().size());
		
		String result = this.executeRemoveStep("admin", typeCode, "step1", updateAction.getSteps());
		assertEquals(Action.SUCCESS, result);
		WorkflowStepAction action = (WorkflowStepAction) this.getAction();
		SmallContentType contentType = action.getContentType();
		assertEquals(typeCode, contentType.getCode());
		assertEquals(typeCode, action.getTypeCode());
		List<Step> steps = action.getSteps();
		assertEquals(2, steps.size());
		assertEquals("step2", steps.get(0).getCode());
		assertEquals("Step 2", steps.get(0).getDescr());
		assertEquals(null, steps.get(0).getRole());
		assertEquals("step3", steps.get(1).getCode());
		assertEquals("Step 3", steps.get(1).getDescr());
		assertEquals("supervisor", steps.get(1).getRole());
	}
	
	public void testAddStep() throws Throwable {
		String typeCode = "ART";
		WorkflowStepAction action = (WorkflowStepAction) this.executeSuccessfulEditSteps(typeCode);
		assertEquals(3, action.getSteps().size());
		
		String result = this.executeAddStep("admin", typeCode, "step4", "Step 4", "supervisor", action.getSteps());
		assertEquals(Action.SUCCESS, result);
		action = (WorkflowStepAction) this.getAction();
		List<Step> steps = action.getSteps();
		assertEquals(4, steps.size());
		assertEquals("step1", steps.get(0).getCode());
		assertEquals("step2", steps.get(1).getCode());
		assertEquals("step3", steps.get(2).getCode());
		Step step = steps.get(3);
		assertEquals("step4", step.getCode());
		assertEquals("step4", step.getCode());
		assertEquals("Step 4", step.getDescr());
		assertEquals("supervisor", step.getRole());
	}
	
	public void testSave() throws Throwable {
		String typeCode = "ART";
		List<Step> originarySteps = this._workflowManager.getSteps(typeCode);
		List<Step> steps = this.prepareSteps(5);
		
		String result = this.executeSave("admin", typeCode, steps);
		assertEquals(Action.SUCCESS, result);
		List<Step> savedSteps = this._workflowManager.getSteps(typeCode);
		this.compareSteps(steps, savedSteps);
		
		result = this.executeSave("admin", typeCode, originarySteps);
		assertEquals(Action.SUCCESS, result);
		savedSteps = this._workflowManager.getSteps(typeCode);
		this.compareSteps(originarySteps, savedSteps);
	}
	
	protected String executeEditSteps(String currentUserName, String typeCode) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "editSteps");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		String result = this.executeAction();
		return result;
	}
	
	protected Action executeSuccessfulEditSteps(String typeCode) throws Throwable {
		String result = this.executeEditSteps("admin", typeCode);
		assertEquals(Action.SUCCESS, result);
		WorkflowStepAction action = (WorkflowStepAction) this.getAction();
		return action;
	}
	
	protected String executeMoveStep(String currentUserName, String typeCode, String movement, String elementIndex, List<Step> steps) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "moveStep");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		if (movement!=null) {
			this.addParameter("movement", movement);
		}
		if (elementIndex!=null) {
			this.addParameter("elementIndex", elementIndex);
		}
		this.addSteps(steps);
		String result = this.executeAction();
		return result;
	}
	
	protected String executeRemoveStep(String currentUserName, String typeCode, String stepCode, List<Step> steps) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "removeStep");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		if (stepCode!=null) {
			this.addParameter("stepCode", stepCode);
		}
		this.addSteps(steps);
		String result = this.executeAction();
		return result;
	}
	
	protected String executeAddStep(String currentUserName, String typeCode, String stepCode, 
			String stepDescr, String stepRole, List<Step> steps) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "addStep");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		if (stepCode!=null) {
			this.addParameter("stepCode", stepCode);
		}
		if (stepDescr!=null) {
			this.addParameter("stepDescr", stepDescr);
		}
		if (stepRole!=null) {
			this.addParameter("stepRole", stepRole);
		}
		this.addSteps(steps);
		String result = this.executeAction();
		return result;
	}
	
	protected String executeSave(String currentUserName, String typeCode, List<Step> steps) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jpcontentworkflow/Workflow", "saveSteps");
		if (typeCode!=null) {
			this.addParameter("typeCode", typeCode);
		}
		this.addSteps(steps);
		String result = this.executeAction();
		return result;
	}
	
	protected void addSteps(List<Step> steps) {
		if (steps!=null) {
			Iterator<Step> stepsIter = steps.iterator();
			while (stepsIter.hasNext()) {
				Step step = stepsIter.next();
				String code = step.getCode();
				if (step.getDescr()!=null) {
					this.addParameter(code+"_SEP_descr", step.getDescr());
				}
				if (step.getRole()!=null) {
					this.addParameter(code+"_SEP_role", step.getRole());
				}
			}
			String codes = this.concatCodes(steps);
			this.addParameter("stepCodes", codes);
		}
	}
	
	protected String concatCodes(List<Step> steps) {
		String codes = "";
		if (steps!=null) {
			Iterator<Step> stepsIter = steps.iterator();
			while (stepsIter.hasNext()) {
				Step step = stepsIter.next();
				codes += step.getCode();
				if (stepsIter.hasNext()) {
					codes += ",";
				}
			}
		}
		return codes;
	}
	
	protected List<Step> prepareSteps(int size) {
		List<Step> steps = new ArrayList<Step>(size);
		for (int i=0; i<size; i++) {
			Step step = new Step();
			step.setCode("step" + i);
			step.setDescr("Step " + i);
			step.setRole("supervisor");
			steps.add(step);
		}
		return steps;
	}
	
	protected void compareSteps(List<Step> steps1, List<Step> steps2) {
		assertEquals(steps1.size(), steps2.size());
		Iterator<Step> steps1Iter = steps1.iterator();
		Iterator<Step> steps2Iter = steps2.iterator();
		while (steps1Iter.hasNext()) {
			Step step1 = steps1Iter.next();
			Step step2 = steps2Iter.next();
			assertEquals(step1.getCode(), step2.getCode());
			assertEquals(step1.getDescr(), step2.getDescr());
			assertEquals(step1.getRole(), step2.getRole());
		}
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