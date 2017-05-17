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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
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
public class AdminAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(AdminAction.class);

	public String executeJob() {
		try {
			ContentJobs contentJobs = new ContentJobs();
			// this._contentJobs.setApplicationContext(apx);
			ICategoryManager categoryManager = (ICategoryManager) ApsWebApplicationUtils.getBean(SystemConstants.CATEGORY_MANAGER,
					this.getRequest());
			IContentSchedulerManager contentSchedulerManager = (IContentSchedulerManager) ApsWebApplicationUtils
					.getBean("jpcontentschedulerContentSchedulerManager", this.getRequest());
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER,
					this.getRequest());
			IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.getRequest());
			IContentModelManager contentModelManager = (IContentModelManager) ApsWebApplicationUtils
					.getBean(JacmsSystemConstants.CONTENT_MODEL_MANAGER, this.getRequest());
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

}
