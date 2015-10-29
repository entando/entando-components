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
public class ContentAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentAction {
	
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
						String[] args = { currentContent.getId() , currentContent.getDescription()};
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
						" - Descrizione: '" + currentContent.getDescription()+ "' - Utente: " + this.getCurrentUser().getUsername());
			} else {
				log.error("Tentativo Salvataggio/approvazione contenuto NULLO - Utente: " + this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Esegue l'azione di salvataggio contenuto con passaggio allo step precedente.
	 * @return Il codice del risultato dell'azione.
	 */
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
	
	/**
	 * Esegue l'azione di salvataggio contenuto con passaggio allo step successivo.
	 * @return Il codice del risultato dell'azione.
	 */
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