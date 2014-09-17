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