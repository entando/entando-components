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

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;

/**
 * @author E.Santoboni
 */
public class WorkflowStepAction extends AbstractWorkflowAction implements IWorkflowStepAction {
	
	@Override
	public void validate() {
		super.validate();
		this.updateSteps();
		this.checkSteps();
	}
	
	protected void checkSteps() {
		List<String> usedSteps = this.getWorkflowManager().searchUsedSteps(this.getTypeCode());
		usedSteps.remove(Content.STATUS_NEW);
		usedSteps.remove(Content.STATUS_DRAFT);
		usedSteps.remove(Content.STATUS_READY);
		usedSteps.remove(Content.STATUS_PUBLIC);
		List<Step> steps = this.getSteps();
		for (Step step : steps) {
			usedSteps.remove(step.getCode());
			String descr = step.getDescr();
			if (descr==null || (descr=descr.trim()).length()==0) {
				String[] args = { step.getCode() };
				this.addFieldError("descr", this.getText("error.saveSteps.requiredDescr", args));
			}
			String roleName = step.getRole();
			if (roleName!=null && roleName.length()>0) {
				if (this.getRoleManager().getRole(roleName)==null) {
					String[] args = { roleName };
					this.addFieldError("descr", this.getText("error.save.unknowRole", args));
				}
			}
		}
		for (String stepCode : usedSteps) {
			String[] args = { stepCode };
			this.addActionError(this.getText("error.saveSteps.removeUsedStep", args));
		}
	}
	
	@Override
	public String edit() {
		try {
			String typeCode = this.getTypeCode();
			List<Step> steps = this.getWorkflowManager().getSteps(typeCode);
			this.setSteps(steps);
			this.updateStepCodes();
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addStep() {
		try {
			this.updateSteps();
			Step step = this.createNewStep();
			if (this.validateNewStep(step)) {
				this.getSteps().add(step);
				String stepCodes = this.getStepCodes() + "," + step.getCode();
				this.setStepCodes(stepCodes);
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addStep");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String moveStep() {
		try {
			this.updateSteps();
			this.moveStepElement();
			this.updateStepCodes();
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "moveStep");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeStep() {
		try {
			this.updateSteps();
			String code = this.getStepCode();
			if (code!=null && code.length()>0) {
				List<String> usedSteps = this.getWorkflowManager().searchUsedSteps(this.getTypeCode());
				if (usedSteps.contains(code)) {
					String[] args = { code };
					this.addActionError(this.getText("error.saveSteps.removeUsedStep", args));
					return INPUT;
				}
				Iterator<Step> stepsIter = this.getSteps().iterator();
				for (int i=0; stepsIter.hasNext(); i++) {
					Step step = stepsIter.next();
					if (code.equals(step.getCode())) {
						this.getSteps().remove(i);
						this.updateStepCodes();
						break;
					}
				}
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeStep");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			this.updateSteps();
			String typeCode = this.getTypeCode();
			List<Step> steps = this.getSteps();
			this.getWorkflowManager().updateSteps(typeCode, steps);
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected boolean validateNewStep(Step step) {
		boolean validated = true;
		String code = step.getCode();
		String descr = step.getDescr();
		if (code==null || (code=code.trim()).length()==0) {
			this.addFieldError("code", this.getText("error.newStep.requiredCode"));
			validated = false;
		} else if (code.trim().length() > 12) {
			this.addFieldError("code", this.getText("error.newStep.wrongMaxLength"));
			validated = false;
		} else if (this.getExtractedStepCodes().contains(code)) {
			this.addFieldError("code", this.getText("error.newStep.duplicatedCode"));
			validated = false;
		}
		if (descr==null || (descr=descr.trim()).length()==0) {
			this.addFieldError("descr", this.getText("error.newStep.requiredDescr"));
			validated = false;
		}
		return validated;
	}
	
	protected Step createNewStep() {
		Step step = new Step();
		if (null != this.getStepCode()) {
			step.setCode(this.getStepCode().trim());
		}
		if (null != this.getStepDescr()) {
			step.setDescr(this.getStepDescr().trim());
		}
		if (null != this.getStepRole()) {
			step.setRole(this.getStepRole().trim());
		}
		return step;
	}
	
	protected void updateSteps() {
		List<Step> steps = new ArrayList<Step>();
		List<String> codes = this.extractStepCodes();
		HttpServletRequest request = this.getRequest();
		Iterator<String> codesIter = codes.iterator();
		while (codesIter.hasNext()) {
			Step step = new Step();
			String code = codesIter.next();
			step.setCode(code);
			String descr = request.getParameter(code+"_SEP_descr");
			step.setDescr(descr);
			String role = request.getParameter(code+"_SEP_role");
			step.setRole(role);
			steps.add(step);
		}
		this.setSteps(steps);
		this.updateStepCodes();
	}
	
	protected List<String> extractStepCodes() {
		List<String> extractedCodes = new ArrayList<String>();
		String stepCodes = this.getStepCodes();
		if (stepCodes!=null) {
			String[] array = stepCodes.trim().split(",");
			for (int i=0; i<array.length; i++) {
				String code = array[i].trim();
				if (code.length()>0) {
					if (!extractedCodes.contains(code)) {
						extractedCodes.add(code);
					}
				}
			}
		}
		this.setExtractedStepCodes(extractedCodes);
		return extractedCodes;
	}
	
	protected void updateStepCodes() {
		StringBuffer stepCodes = new StringBuffer();
		Iterator<Step> stepsIter = this.getSteps().iterator();
		while (stepsIter.hasNext()) {
			Step step = stepsIter.next();
			stepCodes.append(step.getCode());
			if (stepsIter.hasNext()) {
				stepCodes.append(",");
			}
		}
		this.setStepCodes(stepCodes.toString());
	}
	
	/**
	 * Sposta un'elemento della lista in funzione del movimento richiesto 
	 * ed impedisce i movimenti non consentiti.
	 * @param list La lista su cui effettuare lo spostamento di un'elemento
	 * @param elementIndex L'indice dell'elemento da spostare.
	 * @param movement Il codice del movimento richiesto.
	 */
	protected void moveStepElement() {
		List<Step> steps = this.getSteps();
		int elements = steps.size();
		int elementIndex = this.getElementIndex();
		String movement = this.getMovement();
		boolean isUp = IWorkflowStepAction.MOVEMENT_UP_CODE.equals(movement);
		boolean isDown = IWorkflowStepAction.MOVEMENT_DOWN_CODE.equals(movement);
		if ((isUp || isDown) && elementIndex>=0 && elementIndex<elements) {
			if (isUp && elementIndex>0) {
				Step step = steps.remove(elementIndex);
				steps.add(elementIndex-1, step);
			}
			if (isDown && elementIndex<elements-1) {
				Step step = steps.remove(elementIndex);
				steps.add(elementIndex+1, step);
			}
		}
	}
	
	public SmallContentType getContentType() {
		if (this._contentType==null) {
			String typeCode = this.getTypeCode();
			this._contentType = (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(typeCode);
		}
		return this._contentType;
	}
	
	public List<Role> getRoles() {
		if (this._roles==null) {
			this._roles = this.getRoleManager().getRoles();
		}
		return this._roles;
	}
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	public String getStepCodes() {
		return _stepCodes;
	}
	public void setStepCodes(String stepCodes) {
		this._stepCodes = stepCodes;
	}
	protected List<String> getExtractedStepCodes() {
		return _extractedStepCodes;
	}
	protected void setExtractedStepCodes(List<String> extractedStepCodes) {
		this._extractedStepCodes = extractedStepCodes;
	}
	
	public List<Step> getSteps() {
		return _steps;
	}
	protected void setSteps(List<Step> steps) {
		this._steps = steps;
	}
	
	public String getStepCode() {
		return _stepCode;
	}
	public void setStepCode(String stepCode) {
		this._stepCode = stepCode;
	}
	public String getStepDescr() {
		return _stepDescr;
	}
	public void setStepDescr(String stepDescr) {
		this._stepDescr = stepDescr;
	}
	public String getStepRole() {
		return _stepRole;
	}
	public void setStepRole(String stepRole) {
		this._stepRole = stepRole;
	}
	
	public int getElementIndex() {
		return _elementIndex;
	}
	public void setElementIndex(int elementIndex) {
		this._elementIndex = elementIndex;
	}
	
	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}
	
	private String _typeCode;
	private String _stepCodes;
	private List<String> _extractedStepCodes;
	private List<Step> _steps;
	private List<Role> _roles;
	
	/* Parametri Nuovo Step*/
	private String _stepCode;
	private String _stepDescr;
	private String _stepRole;
	
	/* Parametri Nuovo Step*/
	private int _elementIndex;
	private String _movement;
	
	private SmallContentType _contentType;
	
}