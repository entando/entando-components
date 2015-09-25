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
package com.agiletec.plugins.jpversioning.apsadmin.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.plugins.jpversioning.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceDAO;
import com.agiletec.plugins.jpversioning.apsadmin.resource.ITrashedResourceAction;
import com.opensymphony.xwork2.Action;

/**
 * @author G.Cocco
 * */
public class TestTrashedResourceAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testInit() throws Throwable{
		this.setUserOnSession("admin");
		this.initAction("/do/jpversioning/Resource/Trash", "list");
		this.addParameter("resourceTypeCode", "Image");
		String result = this.executeAction();
		assertEquals("success", result);
		ITrashedResourceAction action = (ITrashedResourceAction) this.getAction();
		assertNotNull(action);
		assertTrue(action instanceof com.agiletec.plugins.jpversioning.apsadmin.resource.ITrashedResourceAction);
	}
	
	public void testGetTrashedResources() throws Throwable{
		this.setUserOnSession("admin");
		this.initAction("/do/jpversioning/Resource/Trash", "list");
		this.addParameter("resourceTypeCode", "Image");
		String result = this.executeAction();
		assertEquals("success", result);
		ITrashedResourceAction action = (ITrashedResourceAction) this.getAction();
		List<String> trashedResources = action.getTrashedResources();
		assertNotNull(trashedResources);
		assertEquals(5, trashedResources.size());
		assertEquals("67", trashedResources.get(0));
		
		
		this.setUserOnSession("admin");
		this.initAction("/do/jpversioning/Resource/Trash", "list");
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("text", "tux");
		result = this.executeAction();
		assertEquals("success", result);
		action = (ITrashedResourceAction) this.getAction();
		trashedResources = action.getTrashedResources();
		assertNotNull(trashedResources);
		assertEquals(1, trashedResources.size());
		assertEquals("70", trashedResources.get(0));
		
		this.initAction("/do/jpversioning/Resource/Trash", "list");
		this.addParameter("resourceTypeCode", "Attach");
		result = this.executeAction();
		assertEquals("success", result);
		action = (ITrashedResourceAction) this.getAction();
		trashedResources = action.getTrashedResources();
		assertNotNull(trashedResources);
		assertEquals(1, trashedResources.size());
		assertEquals("66", trashedResources.get(0));
	}
	
	public void testList() throws Throwable{
		String result = this.executeList("admin", "Image");
		assertEquals(Action.SUCCESS, result);
		List<String> trashedResources = ((ITrashedResourceAction) this.getAction()).getTrashedResources();
		assertEquals(5, trashedResources.size());
		assertEquals("67", trashedResources.get(0));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceTypeCode", "Image");
		params.put("text", "tux");
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		trashedResources = ((ITrashedResourceAction) this.getAction()).getTrashedResources();
		assertEquals(1, trashedResources.size());
		assertEquals("70", trashedResources.get(0));
		
		params.put("resourceTypeCode", "Attach");
		params.remove("text");
		result = this.executeSearch("admin", params);
		assertEquals(Action.SUCCESS, result);
		trashedResources = ((ITrashedResourceAction) this.getAction()).getTrashedResources();
		assertEquals(1, trashedResources.size());
		assertEquals("66", trashedResources.get(0));
		
		ResourceInterface trashedResource = ((ITrashedResourceAction) this.getAction()).getTrashedResource("70");
		assertEquals("tux", trashedResource.getDescr());
		assertEquals("Image", trashedResource.getType());
		assertEquals("free", trashedResource.getMainGroup());
		assertNotNull(trashedResource.getXML());

	}
	
	public void testRemoveFromTrash() throws Throwable{
		String resourceId = "22";
		try {
			ResourceInterface resource = this._resourceManager.loadResource(resourceId);
			this._trashedResourceDAO.addTrashedResource(resource);
			
			String result = this.executeList("admin", "Image");
			assertEquals(Action.SUCCESS, result);
			List<String> trashedResources = ((ITrashedResourceAction) this.getAction()).getTrashedResources();
			assertTrue(trashedResources.contains(resourceId));
			
			result = this.executeRemove("admin", "Image", resourceId);
			assertEquals(Action.SUCCESS, result);
			assertNull(this._trashedResourceDAO.getTrashedResource(resourceId));
		} catch (Throwable t) {
			this._trashedResourceDAO.delTrashedResource(resourceId);
			throw t;
		}
	}
	
	public void testRestoreFromTrash() throws Throwable{
		String resourceId = "70";
		ResourceInterface originaryResourceInterface = this._trashedResourceManager.loadTrashedResource(resourceId);
		assertNotNull(originaryResourceInterface);
		try {
			String result = this.executeRestore("admin", "Image", resourceId);
			assertEquals(Action.SUCCESS, result);
			ResourceInterface resourceInterface = this._resourceManager.loadResource(resourceId);
			assertEquals("tux", resourceInterface.getDescr());
			assertEquals("Image", resourceInterface.getType());
			assertEquals(resourceId, resourceInterface.getId());
			
			this._resourceManager.deleteResource(resourceInterface);
			
			ResourceRecordVO recordVO = _trashedResourceDAO.getTrashedResource(resourceId);
			assertNotNull(recordVO);
			assertEquals(resourceId, recordVO.getId());
			assertEquals("tux", recordVO.getDescr());
		} catch (Throwable t) {
			if (this._resourceManager.loadResource(resourceId) != null) {
				this._resourceManager.deleteResource(originaryResourceInterface);
			}
			if (this._trashedResourceDAO.getTrashedResource(resourceId) == null) {
				this._trashedResourceDAO.addTrashedResource(originaryResourceInterface);
			}
			throw t;
		}
	}
	
	private String executeList(String username, String resourceTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Resource/Trash", "list");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		String result = this.executeAction();
		return result;
	}
	
	private String executeSearch(String username, Map<String, Object> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Resource/Trash", "search");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	private String executeRemove(String username, String resourceTypeCode, String resourceId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Resource/Trash", "remove");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		this.addParameter("resourceId", resourceId);
		String result = this.executeAction();
		return result;
	}
	
	private String executeRestore(String username, String resourceTypeCode, String resourceId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Resource/Trash", "restore");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		this.addParameter("resourceId", resourceId);
		String result = this.executeAction();
		return result;
	}
	
	private void init() throws Exception {
		try {
			TrashedResourceDAO trashedResourceDAO = new TrashedResourceDAO();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			trashedResourceDAO.setDataSource(dataSource);
			this._trashedResourceDAO = trashedResourceDAO;
			this._trashedResourceManager = (ITrashedResourceManager) this.getService(JpversioningSystemConstants.TRASHED_RESOURCE_MANAGER);
			this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	private ITrashedResourceDAO _trashedResourceDAO;
	private ITrashedResourceManager _trashedResourceManager;
	private IResourceManager _resourceManager;
	
}