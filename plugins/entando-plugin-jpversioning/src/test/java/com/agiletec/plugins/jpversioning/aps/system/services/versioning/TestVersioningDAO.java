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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.aps.util.DateConverter;

import com.agiletec.plugins.jpversioning.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpversioning.util.JpversioningTestHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author G.Cocco
 */
public class TestVersioningDAO extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.initContentVersions();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanContentVersions();
		super.tearDown();
	}
	
	public void testGetVersions() throws Throwable {
		List<Long> versions = this._versioningDAO.getVersions("CNG12");
		assertNull(versions);
		
		versions = this._versioningDAO.getVersions("ART1");
		this.checkVersionIds(new long[] { 1, 2, 3 }, versions);
	}
	
	public void testGetLastVersions() throws Throwable {
		List<Long> versions = this._versioningDAO.getLastVersions("CNG", null);
		assertTrue(versions.isEmpty());
		
		versions = this._versioningDAO.getLastVersions("ART", null);
		this.checkVersionIds(new long[] { 3 }, versions);
	}
	
	public void testGetVersion() {
		ContentVersion contentVersion = this._versioningDAO.getVersion(10000);
		assertNull(contentVersion);	
		long id = 1;
		contentVersion = this._versioningDAO.getVersion(id);
		assertEquals(id, contentVersion.getId());
		assertEquals("ART1", contentVersion.getContentId());
		assertEquals("ART", contentVersion.getContentType());
		assertEquals("Articolo", contentVersion.getDescr());
		assertEquals(Content.STATUS_DRAFT, contentVersion.getStatus());
		assertNotNull(contentVersion.getXml());
		assertEquals("13/02/2005", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
		assertEquals("0.0", contentVersion.getVersion());
		assertEquals(0, contentVersion.getOnlineVersion());
		assertTrue(contentVersion.isApproved());
		assertEquals("admin", contentVersion.getUsername());
	}
	
	public void testGetLastVersion() {
		ContentVersion contentVersion = this._versioningDAO.getLastVersion("CNG12");
		assertNull(contentVersion);
		contentVersion = this._versioningDAO.getLastVersion("ART1");
		assertEquals(3, contentVersion.getId());
		assertEquals("ART1", contentVersion.getContentId());
		assertEquals("ART", contentVersion.getContentType());
		assertEquals("Articolo 3", contentVersion.getDescr());
		assertEquals(Content.STATUS_READY, contentVersion.getStatus());
		assertNotNull(contentVersion.getXml());
		assertEquals("15/02/2005", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
		assertEquals("1.0", contentVersion.getVersion());
		assertEquals(1, contentVersion.getOnlineVersion());
		assertTrue(contentVersion.isApproved());
		assertEquals("mainEditor", contentVersion.getUsername());
	}
	
	public void testAddGetDeleteVersion() {
		ContentVersion versionRecord = this._versioningDAO.getVersion(1);
		assertNotNull(versionRecord);
		versionRecord.setVersion("1.1");
		versionRecord.setOnlineVersion(1);
		this._versioningDAO.addContentVersion(versionRecord);
		ContentVersion newVersionRecord = this._versioningDAO.getVersion(versionRecord.getId());
		assertNotNull(newVersionRecord);
		this._versioningDAO.deleteVersion(versionRecord.getId());
		newVersionRecord = this._versioningDAO.getVersion(versionRecord.getId());
		assertNull(newVersionRecord);		
	}
	
	public void deleteWorkVersions() {
		List<Long> versions = this._versioningDAO.getVersions("ART1");
		this.checkVersionIds(new long[] { 1, 2, 3 }, versions);
		
		this._versioningDAO.deleteWorkVersions("ART1", 0);
		versions = this._versioningDAO.getVersions("ART1");
		this.checkVersionIds(new long[] { 1, 3 }, versions);
	}
	
	private void checkVersionIds(long[] expected, List<Long> received) {
		assertEquals(expected.length, received.size());
		for (long current : expected) {
			if (!received.contains(new Long(current))) {
				fail("Expected " + current + " - Not found");
			}
		}
	}
	
	private void init() throws Exception {
		VersioningDAO versioningDAO = new VersioningDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		versioningDAO.setDataSource(dataSource);
		this._versioningDAO = versioningDAO;
		this._helper = new JpversioningTestHelper(dataSource, this.getApplicationContext());
	}
	
	private IVersioningDAO _versioningDAO;
	private JpversioningTestHelper _helper;
	
}