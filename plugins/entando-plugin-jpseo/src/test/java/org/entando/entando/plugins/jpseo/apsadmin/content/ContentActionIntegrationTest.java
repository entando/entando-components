/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.apsadmin.content;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.opensymphony.xwork2.Action;

import org.entando.entando.plugins.jpseo.apsadmin.ApsAdminPluginBaseTestCase;

public class ContentActionIntegrationTest extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testSaveNewContent() throws Throwable {
		String contentId = "ART1";
		Content master = this.getContentManager().loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(master, ApsAdminSystemConstants.ADD);
		master.setId(null);
		String descr = "Contenuto di prova per testSave";
		try {
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, master);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("descr", "");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(descr);
		}
	}

	private void removeTestContent(String descr) throws Throwable {
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, false);
		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, descr, false);
		EntitySearchFilter[] filters = {filter1, filter2};
		List<String> contentsId = this.getContentManager().searchId(filters);
		assertNotNull(contentsId);
		if (contentsId.size() == 1) {
			String id = contentsId.get(0);
			Content extractContent = this.getContentManager().loadContent(id, false);
			this.getContentManager().deleteContent(extractContent);
		}
	}

	//	public void test() {
	//		assertTrue(true);
	//	}
	//
	//	public void testValidateContent() {
	//
	//	}

	//	private void testSuccesfullEdit(String contentId, String currentUserName) throws Throwable {
	//		Content content = this.getContentManager().loadContent(contentId, false);
	//		String result = this.executeEdit(contentId, currentUserName);
	//		assertEquals(Action.SUCCESS, result);
	//		try {
	//			String contentSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
	//			Content currentContent = this.getContentOnEdit(contentSessionMarker);
	//			assertEquals(content.getId(), currentContent.getId());
	//			assertEquals(content.getTypeCode(), currentContent.getTypeCode());
	//			assertEquals(content.getDescr(), currentContent.getDescr());
	//			assertEquals(content.getMainGroup(), currentContent.getMainGroup());
	//		} catch (Throwable t) {
	//			throw t;
	//		}
	//	}

	//	protected String executeEdit(String contentId, String currentUserName) throws Throwable {
	//		this.initAction("/do/jacms/Content", "edit");
	//		this.setUserOnSession(currentUserName);
	//		this.addParameter("contentId", contentId);
	//		String result = this.executeAction();
	//		return result;
	//	}

	//	protected Content getContentOnEdit(String contentMarker) {
	//		return (Content) this.getRequest().getSession().getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentMarker);
	//	}

	//	protected void removeTestContent(String descr) throws Throwable {
	//		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, false);
	//		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
	//		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, descr, false);
	//		EntitySearchFilter[] filters = {filter1, filter2};
	//		List<String> contentsId = this.getContentManager().searchId(filters);
	//		assertNotNull(contentsId);
	//		if (contentsId.size() == 1) {
	//			String id = contentsId.get(0);
	//			Content extractContent = this.getContentManager().loadContent(id, false);
	//			this.getContentManager().deleteContent(extractContent);
	//		}
	//	}

	protected void initContentAction(String namespace, String name, String contentOnSessionMarker) throws Exception {
		this.initAction(namespace, name);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
	}

	private void init() throws Exception {
		try {
			_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	protected IContentManager getContentManager() {
		return this._contentManager;
	}

	private IContentManager _contentManager = null;
}
