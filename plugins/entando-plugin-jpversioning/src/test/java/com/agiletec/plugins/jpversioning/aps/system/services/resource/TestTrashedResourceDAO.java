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
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpversioning.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceDAO;

/**
 * @author G.Cocco
 */
public class TestTrashedResourceDAO extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetTrashedResource () {
		String resourceId = "66";
		ResourceRecordVO resourceVO = _trashedResourceDAO.getTrashedResource(resourceId);
		assertNotNull(resourceVO);
		assertEquals(resourceId, resourceVO.getId());
		assertEquals("configurazione", resourceVO.getDescr());
		assertEquals("free", resourceVO.getMainGroup());
		assertEquals("Attach", resourceVO.getResourceType());
		assertNotNull(resourceVO.getXml());
		assertTrue(resourceVO.getXml().length() > 0 );
	}
	
	public void testAddGetDel_TrashedResource() throws ApsSystemException{
		String resourceId = "22";
		ResourceInterface resource = _resourceManager.loadResource(resourceId);
		
		assertNotNull(resource);
		
		_trashedResourceDAO.addTrashedResource(resource);
		
		ResourceRecordVO resourceVO = _trashedResourceDAO.getTrashedResource(resource.getId());
		assertNotNull(resourceVO);
		assertEquals( resource.getDescr(), resourceVO.getDescr());
		assertEquals( resource.getMainGroup(), resourceVO.getMainGroup());
		assertEquals( resource.getResourcePrototype().getType(), resourceVO.getResourceType());
		assertEquals( resource.getXML(), resourceVO.getXml());
		
		_trashedResourceDAO.delTrashedResource(resource.getId());
	}
	
	public void testSearchTrashedResourceIds(){
		String resourceTypeCode = "";
		List<String> resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, null, null);
		assertNotNull(resourceIds);
		assertEquals(6, resourceIds.size());
		
		resourceTypeCode = "Image";
		resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, null, null);
		assertNotNull(resourceIds);
		assertEquals(5, resourceIds.size());
		
		resourceTypeCode = "Attach";
		resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, null, null);
		assertNotNull(resourceIds);
		assertEquals(1, resourceIds.size());
		
		List<String> allowedGroups = new ArrayList<String>();
		allowedGroups.add("customers");
		resourceTypeCode = "Image";
		resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, null, allowedGroups);
		assertNotNull(resourceIds);
		assertEquals(1, resourceIds.size());
		
		allowedGroups = new ArrayList<String>();
		allowedGroups.add("free");
		resourceTypeCode = "Image";
		resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, null, allowedGroups);
		assertNotNull(resourceIds);
		assertEquals(4, resourceIds.size());
		
		String text = "220";
		resourceIds = _trashedResourceDAO.searchTrashedResourceIds(resourceTypeCode, text, allowedGroups);
		assertNotNull(resourceIds);
		assertEquals(1, resourceIds.size());
	}
	
	private void init() throws Exception {
		TrashedResourceDAO trashedResourceDAO = new TrashedResourceDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		trashedResourceDAO.setDataSource(dataSource);
		this._trashedResourceDAO = trashedResourceDAO;
		this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
	}
	
	private ITrashedResourceDAO _trashedResourceDAO;
	private IResourceManager _resourceManager;
	
}