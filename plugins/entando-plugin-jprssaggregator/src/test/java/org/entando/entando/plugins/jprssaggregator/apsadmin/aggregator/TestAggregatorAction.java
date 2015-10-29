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
package org.entando.entando.plugins.jprssaggregator.apsadmin.aggregator;

import org.entando.entando.plugins.jprssaggregator.apsadmin.aggregator.AggregatorAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.plugins.jprssaggregator.aps.JpRssAggregatorSystemConstants;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.IAggregatorManager;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.TestAggregatorManagerHelper;
import org.entando.entando.plugins.jprssaggregator.apsadmin.ApsAdminPluginBaseTestCase;

import com.opensymphony.xwork2.Action;

/**
 * Before running this test, copy the file /test/com/agiletec/plugins/jprssaggregator/test_rss.xml
 * inside an instance of Entando and run the server.
 * You must edit the constant TEST_URL inside com.agiletec.plugins.jprssaggregator.JpRssAggregatorConfigTestUtils
 */
public class TestAggregatorAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		List<String> contentsId = getRSSContents();
		assertEquals(0, contentsId.size());
	}
	
	private List<String> getRSSContents() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter[] filters = new EntitySearchFilter[1];
		
		filters[0] = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "RSS", false);
		List<String> contentsId = _contentManager.loadWorkContentsId(filters, groups);
		return contentsId;
	}
	
	public void testListAction() throws Throwable {
		_aggregatorManager.addItem(TestAggregatorManagerHelper.createItem(3600, "descr", "link"));
		this.setUserOnSession("admin");
		String result = this.executeList();
		assertEquals(Action.SUCCESS, result);
		AggregatorAction action = (AggregatorAction) this.getAction();
		assertEquals(1, action.getAggregatorItems().size());
	}

	public void testNewAction() throws Throwable {
		this.setUserOnSession("admin");
		String result = this.executeNew();
		assertEquals(Action.SUCCESS, result);
		AggregatorAction action = (AggregatorAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
	}
	/*
	public void testSaveNewOKAction() throws Throwable {
		this.setUserOnSession("admin");
		String result = this.executeSaveNewOK();
		assertEquals(Action.SUCCESS, result);
		IAggregatorAction action = (IAggregatorAction) this.getAction();
		assertEquals(1, action.getAggregatorItems().size());
		assertEquals(3, this.getRSSContents().size());
	}
	*/
	public void testSaveNewErrorsAction() throws Throwable {
		this.setUserOnSession("admin");
		String result = this.executeSaveNewErrors();
		assertEquals(Action.INPUT, result);
		Map fieldErrors = this.getAction().getFieldErrors();
		Collection actionErrors = this.getAction().getActionErrors();
		assertTrue(fieldErrors.containsKey("descr"));
		assertTrue(fieldErrors.containsKey("link"));
		assertTrue(fieldErrors.size() == 2);
		assertTrue(actionErrors.isEmpty());
	}
	
	public void testSaveNewWithDuplicate() throws Throwable {
		this.setUserOnSession("admin");
		_aggregatorManager.addItem(TestAggregatorManagerHelper.createItem(3600, "descr", "link"));
		String result = this.executeSaveNewWithDuplicate();
		assertEquals(Action.INPUT, result);
		Map fieldErrors = this.getAction().getFieldErrors();
		Collection actionErrors = this.getAction().getActionErrors();
		assertTrue(fieldErrors.containsKey("link"));
		assertTrue(fieldErrors.size() == 1);
		assertTrue(actionErrors.isEmpty());
	}
	
	public void testEditAction() throws Throwable {
		_aggregatorManager.addItem(TestAggregatorManagerHelper.createItem(3600, "descr", "link"));
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		int code = items.get(0).getCode();
		this.setUserOnSession("admin");
		String result = this.executeEdit(code);
		assertEquals(Action.SUCCESS, result);
		AggregatorAction action = (AggregatorAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		assertEquals(3600, action.getDelay());
		assertEquals("descr", action.getDescr());
		assertEquals("link", action.getLink());
	}
	
	public void testDeleteAction() throws Throwable {
		_aggregatorManager.addItem(TestAggregatorManagerHelper.createItem(3600, "descr", "link"));
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		int code = items.get(0).getCode();
		this.setUserOnSession("admin");
		String result = this.executeDelete(code);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testDoDeleteAction() throws Throwable {
		_aggregatorManager.addItem(TestAggregatorManagerHelper.createItem(3600, "descr", "link"));
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		int code = items.get(0).getCode();
		this.setUserOnSession("admin");
		String result = this.executeDoDelete(code);
		assertEquals(Action.SUCCESS, result);
		AggregatorAction action = (AggregatorAction) this.getAction();
		items  = action.getAggregatorItems();
		assertEquals(0, items.size());
	}
	
	private String executeList() throws Throwable {
		this.initAction(NS, "list");
		return this.executeAction();
	}

	private String executeNew() throws Throwable {
		this.initAction(NS, "new");
		return this.executeAction();
	}
	/*
	private String executeSaveNewOK() throws Throwable {
		this.initAction(NS, "save");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("contentType", "RSS");
		this.addParameter("descr", "descr");
		this.addParameter("delay", "3600");
		this.addParameter("link", PluginConfigTestUtils.TEST_URL);
		this.addParameter("code", "");
		this.addParameter("lastUpdate", "");
		this.addParameter("categories", "");
		return this.executeAction();
	}
	*/
	private String executeSaveNewErrors() throws Throwable {
		this.initAction(NS, "save");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("contentType", "RSS");
		this.addParameter("descr", "");
		this.addParameter("delay", "");
		this.addParameter("link", "");
		this.addParameter("code", "");
		this.addParameter("lastUpdate", "");
		this.addParameter("categories", "");
		return this.executeAction();
	}

	private String executeSaveNewWithDuplicate() throws Throwable {
		this.initAction(NS, "save");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("contentType", "RSS");
		this.addParameter("descr", "descr");
		this.addParameter("delay", "3600");
		this.addParameter("link", "link");
		return this.executeAction();
	}
	
	private String executeEdit(int code) throws Throwable {
		this.initAction(NS, "edit");
		this.addParameter("code", code);
		return this.executeAction();
	}

	private String executeDelete(int code) throws Throwable {
		this.initAction(NS, "delete");
		this.addParameter("code", code);
		return this.executeAction();
	}

	private String executeDoDelete(int code) throws Throwable {
		this.initAction(NS, "doDelete");
		this.addParameter("code", code);
		return this.executeAction();
	}

	private void init() {
		_aggregatorManager = (IAggregatorManager) this.getService(JpRssAggregatorSystemConstants.AGGREGATOR_MANAGER);
		_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		List<String> contents = this.getRSSContents();
		Iterator<String> contentIt = contents.iterator();
		while (contentIt.hasNext()) {
			String contentId = contentIt.next();
			Content content = _contentManager.loadContent(contentId, false);
			_contentManager.deleteContent(content);
		}
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		Iterator<ApsAggregatorItem> it = items.iterator();
		while (it.hasNext()) {
			ApsAggregatorItem currentItem = it.next();
			_aggregatorManager.deleteItem(currentItem.getCode());
		}
	}

	private IAggregatorManager _aggregatorManager;
	private IContentManager _contentManager;
	private static final String NS = "/do/jprssaggregator/Aggregator";
}
