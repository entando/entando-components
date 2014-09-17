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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import com.agiletec.ConfigTestUtils;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowNotifierTestHelper;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.agiletec.plugins.jpcontentworkflow.PluginConfigTestUtils;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 * @author G.Cocco
 */
public class TestContentAction extends AbstractBaseTestContentAction {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	public void testPreviousStep() throws Throwable {
		this._notifierHelper.deleteContentEvents();
		this._helper.setWorkflowConfig();
		String contentId = "ART179";
		Content content = this._contentManager.loadContent(contentId, false);
		assertEquals(Content.STATUS_DRAFT, content.getStatus());
		try {
			Content modifiedContent = this._contentManager.loadContent(contentId, false);
			modifiedContent.setStatus(Content.STATUS_READY);
			this._contentManager.saveContent(modifiedContent);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("descr", content.getDescr());
			params.put("mainGroup", content.getMainGroup());
			params.put("Text:it_Titolo", "titolo");
			String result = this.executePreviousStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step3", modifiedContent.getStatus());
			
			result = this.executePreviousStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step2", modifiedContent.getStatus());
			
			result = this.executePreviousStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step1", modifiedContent.getStatus());
			
			result = this.executePreviousStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals(Content.STATUS_DRAFT, modifiedContent.getStatus());
			
			result = this.executePreviousStep("admin", contentId, params);
			assertEquals(Action.INPUT, result);
		} catch(Throwable t) {
			throw t;
		} finally {
			this._contentManager.saveContent(content);
			this._helper.resetWorkflowConfig();
			this._notifierHelper.deleteContentEvents();
		}
	}
	
	public void testNextStep() throws Throwable {
		this._notifierHelper.deleteContentEvents();
		this._helper.setWorkflowConfig();
		String contentId = "ART179";
		Content content = this._contentManager.loadContent(contentId, false);
		assertEquals(Content.STATUS_DRAFT, content.getStatus());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("descr", content.getDescr());
			params.put("mainGroup", content.getMainGroup());
			params.put("Text:it_Titolo", "titolo");
			String result = this.executeNextStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			Content modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step1", modifiedContent.getStatus());
			
			result = this.executeNextStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step2", modifiedContent.getStatus());
			
			result = this.executeNextStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals("step3", modifiedContent.getStatus());
			
			result = this.executeNextStep("admin", contentId, params);
			assertEquals(Action.SUCCESS, result);
			modifiedContent = this._contentManager.loadContent(contentId, false);
			assertEquals(Content.STATUS_READY, modifiedContent.getStatus());
			
			result = this.executeNextStep("admin", contentId, params);
			assertEquals(Action.INPUT, result);
		} catch(Throwable t) {
			throw t;
		} finally {
			this._contentManager.saveContent(content);
			this._helper.resetWorkflowConfig();
			this._notifierHelper.deleteContentEvents();
		}
	}
	
	protected UserDetails getCurrentUser() {
		UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return currentUser;
	}
	
	private String executePreviousStep(String currentUserName, String contentId, Map<String, String> params) throws Throwable {
		Content content = this._contentManager.loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
		String contentOnSessionParam = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker;
		this.getRequest().getSession().setAttribute(contentOnSessionParam, content);
		this.setUserOnSession(currentUserName);
		this.initContentAction("/do/jacms/Content", "previousStep", contentOnSessionMarker);
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	private String executeNextStep(String currentUserName, String contentId, Map<String, String> params) throws Throwable {
		Content content = this._contentManager.loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
		String contentOnSessionParam = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker;
		this.getRequest().getSession().setAttribute(contentOnSessionParam, content);
		this.setUserOnSession(currentUserName);
		this.initContentAction("/do/jacms/Content", "nextStep", contentOnSessionMarker);
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		DataSource portDataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		this._helper = new WorkflowTestHelper(workflowManager, configManager, portDataSource);
		
		DataSource servDataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		this._notifierHelper = new WorkflowNotifierTestHelper();
		this._notifierHelper.setDataSource(servDataSource);
		
		_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
	}
	
	private WorkflowTestHelper _helper;
	private WorkflowNotifierTestHelper _notifierHelper;
	private IContentManager _contentManager = null;
	
}