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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content;

import org.entando.entando.plugins.jpcontentscheduler.aps.ApsPluginBaseTestCase;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * @author E.Santoboni
 */
public class TestContentJobs extends ApsPluginBaseTestCase {

	private ContentJobs _contentJobs;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ApplicationContext apx = super.getApplicationContext();
		IContentSchedulerManager contentSchedulerManager = (IContentSchedulerManager) apx
				.getBean("jpcontentschedulerContentSchedulerManager");
		IContentManager contentManager = (IContentManager) apx.getBean(JacmsSystemConstants.CONTENT_MANAGER);
		ICategoryManager categoryManager = (ICategoryManager) apx.getBean(SystemConstants.CATEGORY_MANAGER);
		IPageManager pageManager = (IPageManager) apx.getBean(SystemConstants.PAGE_MANAGER);
		IContentModelManager contentModelManager = (IContentModelManager) apx.getBean(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
		this._contentJobs = new ContentJobs();
		// this._contentJobs.setApplicationContext(apx);
		this._contentJobs.setCategoryManager(categoryManager);
		this._contentJobs.setContentManager(contentManager);
		this._contentJobs.setContentModelManager(contentModelManager);
		this._contentJobs.setContentSchedulerManager(contentSchedulerManager);
		this._contentJobs.setPageManager(pageManager);
	}

	public void testExecuteJob() {
		try {
			this._contentJobs.executeJob(super.getApplicationContext());
		} catch (JobExecutionException ex) {
			fail();
		}
	}

}
