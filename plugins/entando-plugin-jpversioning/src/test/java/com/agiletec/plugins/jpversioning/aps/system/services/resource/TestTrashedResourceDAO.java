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