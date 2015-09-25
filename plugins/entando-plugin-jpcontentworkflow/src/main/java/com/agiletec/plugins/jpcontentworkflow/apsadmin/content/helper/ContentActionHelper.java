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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public class ContentActionHelper extends com.agiletec.plugins.jacms.apsadmin.content.helper.ContentActionHelper 
		implements IContentWorkFlowActionHelper {
	
	@Override
	public void updateEntity(IApsEntity entity, HttpServletRequest request) {
		Content content = (Content) entity;
		try {
            if (null != content) {
            	String descr = request.getParameter("descr");
            	if (descr != null) content.setDescr(descr.trim());
            	// Non consente di modificare lo stato del contenuto
				String status = content.getStatus();
				if (status==null || status.equals(Content.STATUS_NEW)) {
					content.setStatus(Content.STATUS_DRAFT);
				}
	            if (null == content.getId()) {
	            	String mainGroup = request.getParameter("mainGroup");
	            	if (mainGroup != null) content.setMainGroup(mainGroup);
	            }
	            super.updateEntity(content, request);
            }
        } catch (Throwable t) {
        	ApsSystemUtils.logThrowable(t, this, "updateContent");
        	throw new RuntimeException("Errore in updateContent", t);
        }
	}
	
	@Override
	public List<WorkflowSearchFilter> getWorkflowSearchFilters(UserDetails currentUser) {
		try {
			return this.getWorkflowManager().getWorkflowSearchFilters(currentUser);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getWorkflowSearchFilters", "Error extracting filters");
        	throw new RuntimeException("Error extracting filters", t);
		}
	}
	
	@Override
	public String getPreviousStep(String currentStep, String typeCode) {
		String previousStep = null;
		if (!currentStep.equals(Content.STATUS_NEW) && !currentStep.equals(Content.STATUS_DRAFT)) {
			List<Step> steps = this.getWorkflowManager().getSteps(typeCode);
			previousStep = Content.STATUS_DRAFT;
			if (Content.STATUS_READY.equals(currentStep)) {
				if (steps.size() > 0) {
					previousStep = steps.get(steps.size()-1).getCode();
				}
			} else {
				for (int i = 0; i < steps.size(); i++) {
					Step step = steps.get(i);
					if (step.getCode().equals(currentStep)) {
						break;
					}
					previousStep = step.getCode();
				}
			}
		}
		return previousStep;
	}
	
	@Override
	public String getNextStep(String currentStep, String typeCode) {
		String nextStep = null;
		if (!currentStep.equals(Content.STATUS_READY) && !currentStep.equals(Content.STATUS_PUBLIC)) {
			List<Step> steps = this.getWorkflowManager().getSteps(typeCode);
			Iterator<Step> stepsIter = steps.iterator();
			if (!currentStep.equals(Content.STATUS_NEW) && !currentStep.equals(Content.STATUS_DRAFT)) {
				while (stepsIter.hasNext()) {
					if (currentStep.equals(stepsIter.next().getCode())) {
						break;
					}
				}
			}
			if (stepsIter.hasNext()) {
				nextStep = stepsIter.next().getCode();
			} else {
				nextStep = Content.STATUS_READY;
			}
		}
		return nextStep;
	}
	
	@Override
	public boolean isUserAllowed(Content content, UserDetails currentUser) {
		try {
			if (!super.isUserAllowed(content, currentUser)) {
				return false;
			}
			boolean allowedType = false;
			List<SmallContentType> allowedContentTypes = this.getAllowedContentTypes(currentUser);
			for (int i = 0; i < allowedContentTypes.size(); i++) {
				SmallContentType smallContentType = allowedContentTypes.get(i);
				if (smallContentType.getCode().equals(content.getTypeCode())) {
					allowedType = true;
					break;
				}
			}
			if (!allowedType) {
				return false;
			}
			String status = content.getStatus();
			if (status != null && !status.equals(Content.STATUS_NEW) && !status.equals(Content.STATUS_DRAFT)) {
				if (status.equals(Content.STATUS_READY) || status.equals(Content.STATUS_PUBLIC)) {
					boolean isSupervisor = this.getAuthorizationManager().isAuthOnPermission(currentUser, Permission.SUPERVISOR);
					if (!isSupervisor) {
						return false;
					}
				} else {
					List<Step> steps = this.getWorkflowManager().getSteps(content.getTypeCode());
					boolean auth = false;
					for (int i = 0; i < steps.size(); i++) {
						Step step = steps.get(i);
						if (step.getCode().equals(status)) {
							if (step.getRole() != null || this.checkRole(step.getRole(), currentUser)) {
								auth = true;
								break;
							}
						}
					}
					if (!auth) {
						return false;
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isUserAllowed");
			throw new RuntimeException("Error checking user authority", t);
		}
		return true;
	}
	
	@Override
	public List<SmallContentType> getAllowedContentTypes(UserDetails currentUser) {
		try {
			return this.getWorkflowManager().getManagingContentTypes(currentUser);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedContentTypes", "Error extracting allowed content types");
        	throw new RuntimeException("Error extracting allowed content types", t);
		}
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus(UserDetails user, String contentTypeCode) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		String roleName = this.getWorkflowManager().getRole(contentTypeCode);
		if (!this.checkRole(roleName, user)) {
			return items;
		}
		Workflow workflow = this.getWorkflowManager().getWorkflow(contentTypeCode);
		items.add(new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT));
		if (null != workflow) {
			Iterator<Step> stepsIter = workflow.getSteps().iterator();
			while (stepsIter.hasNext()) {
				Step step = stepsIter.next();
				if (this.checkRole(step.getRole(), user)) {
					items.add(new SelectItem(step.getCode(), step.getDescr()));
				}
			}
		}
		if (this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERVISOR)) {
			items.add(new SelectItem(Content.STATUS_READY, "name.contentStatus." + Content.STATUS_READY));
		}
		return items;
	}
	
	protected boolean checkRole(String roleName, UserDetails user) {
		boolean isAllowed = true;
		if (roleName != null && roleName.length() > 0) {
			boolean isSuperuser = this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER);
			if (!isSuperuser) {
				isAllowed = this.getAuthorizationManager().isAuthOnRole(user, roleName);
			}
		}
		return isAllowed;
	}
	
	protected IContentWorkflowManager getWorkflowManager() {
		return _workflowManager;
	}
	public void setWorkflowManager(IContentWorkflowManager workflowManager) {
		this._workflowManager = workflowManager;
	}
	
	public IContentWorkflowManager _workflowManager;
	
}