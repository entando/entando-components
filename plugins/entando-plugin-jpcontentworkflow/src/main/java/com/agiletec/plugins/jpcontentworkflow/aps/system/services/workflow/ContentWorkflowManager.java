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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.parse.ContentWorkflowDOM;

/**
 * @author E.Santoboni
 */
public class ContentWorkflowManager extends AbstractService implements IContentWorkflowManager {

	private static final Logger _logger = LoggerFactory.getLogger(ContentWorkflowManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		ApsSystemUtils.getLogger().debug(this.getName() + ": initialized");
	}
	
	protected void loadConfig() {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
			}
			ContentWorkflowDOM configDOM = new ContentWorkflowDOM();
			this.setWorkflowConfig(configDOM.extractConfig(xml));
		} catch (Exception e) {
			_logger.error("error loading configs", e);
		}
	}
	
	@Override
	public String getRole(String typeCode) {
		Workflow workflow = this.getWorkflow(typeCode);
		return workflow.getRole();
	}
	
	@Override
	public void updateRole(String typeCode, String role) throws ApsSystemException {
		Workflow workflow = this.getWorkflow(typeCode);
		workflow.setRole(role);
		this.updateWorkflow(workflow);
	}
	
	@Override
	public List<Step> getSteps(String typeCode) {
		Workflow workflow = this.getWorkflow(typeCode);
		return workflow.getSteps();
	}
	
	@Override
	public void updateSteps(String typeCode, List<Step> steps) throws ApsSystemException {
		Workflow workflow = this.getWorkflow(typeCode);
		workflow.setSteps(steps);
		this.updateWorkflow(workflow);
	}
	
	@Override
	public Workflow getWorkflow(String typeCode) {
		Workflow workflow = this.getWorkflowConfig().get(typeCode);
		if (workflow == null) {
			workflow = new Workflow();
			workflow.setTypeCode(typeCode);
		}
		return workflow;
	}
	
	protected void updateWorkflow(Workflow workflow) throws ApsSystemException {
		Map<String, Workflow> config = this.getWorkflowConfig();
		config.put(workflow.getTypeCode(), workflow);
		try {
			String xml = new ContentWorkflowDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM, xml);
			this.setWorkflowConfig(config);
		} catch (Exception e) {
			this.loadConfig();
			_logger.error("Error updating workflow for content {}", workflow.getTypeCode(), e);
			throw new ApsSystemException("Error updating workflow for content " + workflow.getTypeCode(), e);
		}
	}
	
	@Override
	public List<String> searchUsedSteps(String typeCode) {
		return this.getWorkflowDAO().searchUsedSteps(typeCode);
	}
	
	@Override
	public List<WorkflowSearchFilter> getWorkflowSearchFilters(UserDetails user) throws ApsSystemException {
		List<WorkflowSearchFilter> filters = new ArrayList<WorkflowSearchFilter>();
		try {
			List<SmallContentType> contentTypes = this.getManagingContentTypes(user);
			boolean isSupervisor = this.getAuthorizationManager().isAuthOnPermission(user, Permission.CONTENT_SUPERVISOR);
			for (int i = 0; i < contentTypes.size(); i++) {
				SmallContentType type = contentTypes.get(i);
				Workflow workflow = this.getWorkflow(type.getCode());
				WorkflowSearchFilter filter = new WorkflowSearchFilter();
				filter.setTypeCode(type.getCode());
				List<String> allowedSteps = this.getAllowedStatus(user, isSupervisor, workflow);
				filter.setAllowedSteps(allowedSteps);
				filters.add(filter);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting workflow search filters by user {}", user, t);
			throw new ApsSystemException("Error extracting workflow search filters by user " + user, t);
		}
		return filters;
	}
	
	protected List<String> getAllowedStatus(UserDetails user, boolean isSupervisor, Workflow workflow) {
		List<String> allowedSteps = new ArrayList<String>();
		allowedSteps.add(Content.STATUS_NEW);
		allowedSteps.add(Content.STATUS_DRAFT);
		Iterator<Step> stepsIter = workflow.getSteps().iterator();
		while (stepsIter.hasNext()) {
			Step step = stepsIter.next();
			String stepRole = step.getRole();
			if (null == stepRole || stepRole.trim().length() == 0 
					|| this.getAuthorizationManager().isAuthOnRole(user, step.getRole())) {
				allowedSteps.add(step.getCode());
			}
		}
		if (isSupervisor) {
			allowedSteps.add(Content.STATUS_READY);
			allowedSteps.add(Content.STATUS_PUBLIC);
		}
		return allowedSteps;
	}
	
	@Override
	public List<SmallContentType> getManagingContentTypes(UserDetails user) throws ApsSystemException {
		List<SmallContentType> types = new ArrayList<SmallContentType>();
		try {
			List<SmallContentType> contentTypes = this.getContentManager().getSmallContentTypes();
			for (int i = 0; i < contentTypes.size(); i++) {
				SmallContentType contentType = contentTypes.get(i);
				Workflow workflow = this.getWorkflow(contentType.getCode());
				if (null != workflow && null != workflow.getRole()) {
					String roleName = workflow.getRole();
					if (null == roleName || roleName.trim().length() == 0 
							|| this.getAuthorizationManager().isAuthOnRole(user, roleName)) {
						types.add(contentType);
					}
				} else {
					types.add(contentType);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting types by user {}", user, t);
			throw new ApsSystemException("Error extracting types by user " + user, t);
		}
		return types;
	}
	
	protected Map<String, Workflow> getWorkflowConfig() {
		return _workflowConfig;
	}
	protected void setWorkflowConfig(Map<String, Workflow> workflowConfig) {
		this._workflowConfig = workflowConfig;
	}
	
	protected IContentWorkflowDAO getWorkflowDAO() {
		return _workflowDAO;
	}
	public void setWorkflowDAO(IContentWorkflowDAO workflowDAO) {
		this._workflowDAO = workflowDAO;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	private Map<String, Workflow> _workflowConfig;
	
	private IContentWorkflowDAO _workflowDAO;
	private ConfigInterface _configManager;
	
	private IContentManager _contentManager;
	private IAuthorizationManager _authorizationManager;
	
}