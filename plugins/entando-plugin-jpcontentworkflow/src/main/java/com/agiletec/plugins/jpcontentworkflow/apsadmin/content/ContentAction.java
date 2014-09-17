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

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.IContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;
import org.slf4j.Logger;

/**
 * @author E.Santoboni
 */
public class ContentAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentAction implements IContentAction {
	
	@Override
	protected String saveContent(boolean approve) {
		Logger log = ApsSystemUtils.getLogger();
		try {
			Content currentContent = this.getContent();
			if (null != currentContent) {
				if (!this.getContentActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					log.info("Utente non abilitato al salvataggio del contenuto " + currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				if (approve) {
					if (!Content.STATUS_READY.equals(currentContent.getStatus()) && !Content.STATUS_PUBLIC.equals(currentContent.getStatus())) {
						String[] args = { currentContent.getId() , currentContent.getDescr()};
						this.addFieldError("status", this.getText("error.content.publish.notReadyStatus", args));
						return INPUT;
					}
					this.getContentManager().insertOnLineContent(currentContent);
				} else {
					this.getContentManager().saveContent(currentContent);
				}
				String sessionParamName = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker();
				this.getRequest().getSession().removeAttribute(sessionParamName);
				log.info("Salvato contenuto " + currentContent.getId() + 
						" - Descrizione: '" + currentContent.getDescr() + "' - Utente: " + this.getCurrentUser().getUsername());
			} else {
				log.error("Tentativo Salvataggio/approvazione contenuto NULLO - Utente: " + this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String previousStep() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			Content currentContent = this.updateContentOnSession();
			if (null != currentContent) {
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				String previousStep = this.getPreviousStep();
				if (previousStep != null) {
					currentContent.setStatus(previousStep);
					this.getContentManager().saveContent(currentContent);
					String sessionParamName = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker();
					this.getRequest().getSession().removeAttribute(sessionParamName);
				} else {
					this.addActionError(this.getText("error.content.save.statusNotAllowed"));
					return INPUT;
				}
			} else {
				log.error("Tentativo Salvataggio/approvazione contenuto NULLO - Utente: " + this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "prevStep");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String nextStep() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			Content currentContent = this.updateContentOnSession();
			if (null != currentContent) {
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				String nextStep = this.getNextStep();
				if (nextStep != null) {
					currentContent.setStatus(nextStep);
					this.getContentManager().saveContent(currentContent);
					String sessionParamName = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker();
					this.getRequest().getSession().removeAttribute(sessionParamName);
				} else {
					this.addActionError(this.getText("error.content.save.statusNotAllowed"));
					return INPUT;
				}
			} else {
				log.error("Tentativo Salvataggio/approvazione contenuto NULLO - Utente: " + this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "nextStep");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Verifica se l'utente corrente è abilitato all'accesso 
	 * del contenuto specificato.
	 * @param content Il contenuto su cui verificare il permesso di accesso.
	 * @return True se l'utente corrente è abilitato all'eccesso al contenuto,
	 * false in caso contrario.
	 */
	@Override
	protected boolean isUserAllowed(Content content) {
		UserDetails currentUser = this.getCurrentUser();
		boolean isAllowed = this.getContentActionHelper().isUserAllowed(content, currentUser);
		return isAllowed;
	}
	
	public String getPreviousStep() {
		if (this._previousStep == null) {
			Content content = this.getContent();
			this._previousStep = ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getPreviousStep(content.getStatus(), content.getTypeCode());
		}
		return this._previousStep;
	}
	
	public String getNextStep() {
		if (this._nextStep == null) {
			Content content = this.getContent();
			this._nextStep = ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getNextStep(content.getStatus(), content.getTypeCode());
		}
		return this._nextStep;
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus() {
		List<SelectItem> items;
		try {
			Content content = this.getContent();
			items = new ArrayList<SelectItem>(1);
			String statusDescrKey = "name.contentStatus." +content.getStatus();
			SelectItem item = null;
			if (statusDescrKey.equals(this.getText(statusDescrKey))) {
				String contentType = content.getTypeCode();
				Workflow workflow = this.getWorkflowManager().getWorkflow(contentType);
				if (null != workflow && null != workflow.getStep(content.getStatus())) {
					item = new SelectItem(content.getStatus(), workflow.getStep(content.getStatus()).getDescr());
				} else {
					item = new SelectItem(content.getStatus(), content.getStatus());
				}
			} else {
				item = new SelectItem(content.getStatus(), statusDescrKey);
			}
			items.add(item);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvalaibleStatus");
			throw new RuntimeException("Error in getAvalaibleStatus");
		}
		return items;
	}
	
	protected IContentWorkflowManager getWorkflowManager() {
		return _workflowManager;
	}
	public void setWorkflowManager(IContentWorkflowManager workflowManager) {
		this._workflowManager = workflowManager;
	}
	
	private String _previousStep;
	private String _nextStep;
	
	public IContentWorkflowManager _workflowManager;
	
}