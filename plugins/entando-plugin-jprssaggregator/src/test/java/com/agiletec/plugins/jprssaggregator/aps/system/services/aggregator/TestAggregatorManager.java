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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jprssaggregator.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jprssaggregator.aps.JpRssAggregatorSystemConstants;

public class TestAggregatorManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testInit() throws Throwable {
		assertNotNull(_aggregatorManager);
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		assertNotNull(items);
		assertEquals(0, items.size());
	}

	public void testAddItem() throws Throwable {
		ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr", "dummy_link");
		_aggregatorManager.addItem(item);
		assertTrue(item.getCode() > 0);
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		assertNotNull(items);
		assertEquals(1, items.size());
	}

	public void testGetItem() throws Throwable {
		ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr", "dummy_link");
		_aggregatorManager.addItem(item);
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		assertEquals(1, items.size());
		ApsAggregatorItem dummy = items.get(0);
		assertNotNull(dummy);
		assertEquals(item.getCode(), dummy.getCode());
		assertEquals(item.getDelay(), dummy.getDelay());
		assertEquals(item.getLink(), dummy.getLink());
		assertEquals(item.getCategories(), dummy.getCategories());
		assertEquals(item.getContentType(), dummy.getContentType());
		String todayString = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		String dummyLastUpdateString = DateConverter.getFormattedDate(dummy.getLastUpdate(), "dd/MM/yyyy");
		assertEquals(todayString, dummyLastUpdateString);
	}

	public void testGetItems() throws Throwable {
		for (int i = 0; i < 10; i++) {
			ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr_" + i, "dummy_link_" + i);
			_aggregatorManager.addItem(item);
		}
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		assertEquals(10, items.size());
	}

	public void testDeleteItem() throws Throwable {
		ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr", "dummy_link");
		_aggregatorManager.addItem(item);
		_aggregatorManager.deleteItem(item.getCode());
		List<ApsAggregatorItem> items = _aggregatorManager.getItems();
		assertEquals(0, items.size());
	}

	public void testUpdateItem() throws Throwable {
		ApsAggregatorItem item = TestAggregatorManagerHelper.createItem(3600, "dummy_descr", "dummy_link");
		_aggregatorManager.addItem(item);
		ApsAggregatorItem dummy = _aggregatorManager.getItem(item.getCode());
		dummy.setDelay(1000);
		dummy.setDescr("updated_descr");
		dummy.setLink("updated_link");
		ApsProperties categories = new ApsProperties();
		categories.put("code", "<categories />");
		_aggregatorManager.update(dummy);
		ApsAggregatorItem dummy2 = _aggregatorManager.getItem(item.getCode());
		assertNotNull(dummy2);
		assertEquals(dummy.getDelay(), dummy2.getDelay());
		assertEquals(dummy.getLink(), dummy2.getLink());
		assertEquals(dummy.getCategories(), dummy2.getCategories());
		assertTrue(dummy.getLastUpdate().equals(dummy2.getLastUpdate()));
	}

	private void init() {
		_aggregatorManager = (IAggregatorManager) this.getService(JpRssAggregatorSystemConstants.AGGREGATOR_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			List<ApsAggregatorItem> items = _aggregatorManager.getItems();
			Iterator<ApsAggregatorItem> it = items.iterator();
			while (it.hasNext()) {
				ApsAggregatorItem currentItem = it.next();
				this._aggregatorManager.deleteItem(currentItem.getCode());
			}
		} catch (Throwable t) {
			throw new Exception(t);
		} finally {
			super.tearDown();
		}
	}
	
	private IAggregatorManager _aggregatorManager;
	
}
