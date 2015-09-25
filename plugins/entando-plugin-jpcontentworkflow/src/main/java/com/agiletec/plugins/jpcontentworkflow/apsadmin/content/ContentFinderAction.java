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
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.IContentSearcherManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;

/**
 * @author E.Santoboni
 */
public class ContentFinderAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction {
	
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			if (this.hasCurrentUserPermission(Permission.SUPERUSER)) {
				return super.getContents();
			}
			UserDetails user = this.getCurrentUser();
			List<WorkflowSearchFilter> workflowFilters = ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getWorkflowSearchFilters(user);
			if (workflowFilters.size() > 0) {
				List<String> allowedGroups = this.getContentGroupCodes();
				EntitySearchFilter[] filters = this.createFilters();
				String[] categories = null;
				Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
				if (null != category && !category.isRoot()) {
					categories = new String[]{this.getCategoryCode().trim()};
				}
				result = this.getContentSearcherManager().loadContentsId(workflowFilters, categories, filters, allowedGroups);
			} else {
				result = new ArrayList<String>();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContents");
			throw new RuntimeException("Error searching contents", t);
		}
		return result;
	}
	
	@Override
	public String insertOnLine() {
		try {
			if (null == this.getContentIds()) {
				return SUCCESS;
			}
			Iterator<String> iter = this.getContentIds().iterator();
			List<Content> publishedContents = new ArrayList<Content>();
			IContentManager contentManager = (IContentManager) this.getContentManager();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				Content contentToPublish = contentManager.loadContent(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToPublish) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToPublishNull", msgArg));
					continue;
				}
				msgArg[0] = contentToPublish.getDescr();
				if (!Content.STATUS_READY.equals(contentToPublish.getStatus())) {
					String nextStep = this.getNextStep(contentToPublish);
					if (null != nextStep && !Content.STATUS_READY.equals(nextStep)) {
						String[] args = {contentToPublish.getId(), contentToPublish.getDescr(), contentToPublish.getStatus()};
						this.addActionError(this.getText("error.content.publish.statusNotAllowed", args));
						continue;
					}
				}
				if (!this.isUserAllowed(contentToPublish)) {
					this.addActionError(this.getText("error.content.userNotAllowedToPublishContent", msgArg));
					continue;
				}
				this.getContentActionHelper().scanEntity(contentToPublish, this);
				if (this.getFieldErrors().size()>0) {
					this.addActionError(this.getText("error.content.publishingContentWithErrors", msgArg));
					continue;
				}
				contentManager.insertOnLineContent(contentToPublish);
				ApsSystemUtils.getLogger().info("Content '" + contentToPublish.getId() 
						+ "' published by user '" + this.getCurrentUser().getUsername() + "'");
				publishedContents.add(contentToPublish);
			}
			//RIVISITARE LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.publishedContents", publishedContents);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "insertOnLine");
			throw new RuntimeException("Error inserting online content", t);
		}
		return SUCCESS;
	}
	
	protected String getNextStep(Content content) {
		return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getNextStep(content.getStatus(), content.getTypeCode());
	}
	
	// Portare a protected in Action padre di jacms
	protected void addConfirmMessage(String key, List<Content> deletedContents) {
		if (deletedContents.size() > 0) {
			String confirm = this.getText(key);
			for (int i=0; i<deletedContents.size(); i++) {
				Content content = deletedContents.get(i);
				if (i>0) {
					confirm += " - ";
				}
				confirm += " '" + content.getDescr() + "'";
			}
			this.addActionMessage(confirm);
		}
	}
	
	@Override
	public List<SmallContentType> getContentTypes() {
		return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAllowedContentTypes(this.getCurrentUser());
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus() {
		if (null != this.getContentType() && this.getContentType().trim().length() > 0) {
			return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAvalaibleStatus(this.getCurrentUser(), this.getContentType());
		}
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT));
		if (super.hasCurrentUserPermission(Permission.SUPERVISOR)) {
			items.add(new SelectItem(Content.STATUS_READY, "name.contentStatus." + Content.STATUS_READY));
		}
		return items;
	}

	protected IContentSearcherManager getContentSearcherManager() {
		return _contentSearcherManager;
	}
	public void setContentSearcherManager(IContentSearcherManager contentSearcherManager) {
		this._contentSearcherManager = contentSearcherManager;
	}
	
	private IContentSearcherManager _contentSearcherManager;
	
}