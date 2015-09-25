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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;

/**
 * Return informations of content authorization
 * @author E.Santoboni
 */
public class ContentAuthorizationHelper extends com.agiletec.plugins.jacms.aps.system.services.content.helper.ContentAuthorizationHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentAuthorizationHelper.class);
	
	@Override
	public boolean isAuthToEdit(UserDetails user, Content content) throws ApsSystemException {
		boolean isAllowed = false;
		try {
			boolean firstCheck = (null != content.getMainGroup()) ? super.isAuthToEdit(user, content) : true;
			if (firstCheck) {
				if (this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)) {
					return true;
				}
				String typeCode = content.getTypeCode();
				Workflow workflow = this.getContentWorkflowManager().getWorkflow(typeCode);
				String requiredRole = workflow.getRole();
				if (requiredRole != null && !this.getAuthorizationManager().isAuthOnRole(user, requiredRole)) {
					return false;
				}
				String status = content.getStatus();
				if (!Content.STATUS_NEW.equals(status) && !Content.STATUS_DRAFT.equals(status)) {
					if (Content.STATUS_READY.equals(status) || Content.STATUS_PUBLIC.equals(status)) {
						isAllowed = this.getAuthorizationManager().isAuthOnPermission(user, Permission.CONTENT_SUPERVISOR);
					} else {
						Step step = workflow.getStep(status);
						if (step != null) {
							isAllowed = this.getAuthorizationManager().isAuthOnRole(user, step.getRole());
						}
					}
				} else {
					isAllowed = true;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error verifing content authority by user {}", user, t);
			throw new ApsSystemException("Error verifing content authority by user " + user, t);
		}
		return isAllowed;
	}
	
	@Override
	public boolean isAuthToEdit(UserDetails user, PublicContentAuthorizationInfo info) throws ApsSystemException {
		Content content = this.getContentManager().loadContent(info.getContentId(), true);
		return super.isAuthToEdit(user, content);
	}
	
	@Override
	public boolean isAuthToEdit(UserDetails user, String contentId, boolean publicVersion) throws ApsSystemException {
		Content content = this.getContentManager().loadContent(contentId, publicVersion);
		return super.isAuthToEdit(user, content);
	}
	
	protected IContentWorkflowManager getContentWorkflowManager() {
		return _contentWorkflowManager;
	}
	public void setContentWorkflowManager(IContentWorkflowManager contentWorkflowManager) {
		this._contentWorkflowManager = contentWorkflowManager;
	}
	
	private IContentWorkflowManager _contentWorkflowManager;
	
}