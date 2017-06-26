/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.apsadmin.config;

import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.ContentJobs;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.IContentSchedulerManager;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ConfigAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ConfigAction.class);

	private static final String THREAD_CONFIG_SESSION_PARAM = "threadConfig";

	public String viewItem() {
		try {
			ContentThreadConfig threadConfig = this.getContentSchedulerManager().getConfig();

			this.setSiteCode(threadConfig.getSitecode());
			this.setActive(threadConfig.isActive());
			this.setGlobalCat(threadConfig.getGlobalCat());
			this.setContentIdRepl(threadConfig.getContentIdRepl());
			this.setContentModelRepl(threadConfig.getContentModelRepl());

		} catch (Throwable t) {
			_logger.error("Error in viewItem", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String viewUsers() {
		try {
			this.setConfigOnSession();
		} catch (Throwable t) {
			_logger.error("Error in viewUsers", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String viewGroups() {
		try {
			this.setConfigOnSession();
		} catch (Throwable t) {
			_logger.error("Error in viewGroups", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String viewContentTypes() {
		try {
			this.setConfigOnSession();
		} catch (Throwable t) {
			_logger.error("Error in viewContentTypes", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String viewEmail() {
		try {
			this.setConfigOnSession();
		} catch (Throwable t) {
			_logger.error("Error in viewEmail", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String saveItem() {
		try {

			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();

			config.setActive(this.isActive());
			config.setGlobalCat(this.getGlobalCat());
			config.setSitecode(this.getSiteCode());
			config.setContentIdRepl(this.getContentIdRepl());
			config.setContentModelRepl(this.getContentModelRepl());

			this.getContentSchedulerManager().updateConfig(config);

			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving item", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String executeJob() {
		try {
			ContentJobs contentJobs = new ContentJobs();
			// this._contentJobs.setApplicationContext(apx);
			ICategoryManager categoryManager = (ICategoryManager) ApsWebApplicationUtils.getBean(SystemConstants.CATEGORY_MANAGER, this.getRequest());
			IContentSchedulerManager contentSchedulerManager = (IContentSchedulerManager) ApsWebApplicationUtils.getBean("jpcontentschedulerContentSchedulerManager",
					this.getRequest());
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.getRequest());
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.getRequest());
			IContentModelManager contentModelManager = (IContentModelManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MODEL_MANAGER, this.getRequest());
			contentJobs.setCategoryManager(categoryManager);
			contentJobs.setContentManager(contentManager);
			contentJobs.setContentModelManager(contentModelManager);
			contentJobs.setContentSchedulerManager(contentSchedulerManager);
			contentJobs.setPageManager(pageManager);
			ApplicationContext ap = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
			contentJobs.executeJob(ap);
		} catch (Throwable t) {
			_logger.error("Error executing job", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private void setConfigOnSession() {
		ContentThreadConfig threadConfig = this.getContentSchedulerManager().getConfig();
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM, threadConfig);
	}

	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}

	public ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}

	public ContentThreadConfig getThreadConfig() {
		return (ContentThreadConfig) this.getRequest().getSession().getAttribute(THREAD_CONFIG_SESSION_PARAM);
	}

	public IContentSchedulerManager getContentSchedulerManager() {
		return _contentSchedulerManager;
	}

	public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
		this._contentSchedulerManager = contentSchedulerManager;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getGlobalCat() {
		return globalCat;
	}

	public void setGlobalCat(String globalCat) {
		this.globalCat = globalCat;
	}

	public String getContentIdRepl() {
		return contentIdRepl;
	}

	public void setContentIdRepl(String contentIdRepl) {
		this.contentIdRepl = contentIdRepl;
	}

	public String getContentModelRepl() {
		return contentModelRepl;
	}

	public void setContentModelRepl(String contentModelRepl) {
		this.contentModelRepl = contentModelRepl;
	}

	private ConfigInterface _baseConfigManager;
	private IContentSchedulerManager _contentSchedulerManager;

	private String siteCode;
	private boolean active;
	private String globalCat;
	private String contentIdRepl;
	private String contentModelRepl;

}
