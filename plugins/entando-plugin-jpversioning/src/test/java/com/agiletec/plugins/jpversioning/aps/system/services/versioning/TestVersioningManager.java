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

import com.agiletec.plugins.jpversioning.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpversioning.util.JpversioningTestHelper;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;

/**
 * @author G.Cocco
 */
public class TestVersioningManager extends ApsPluginBaseTestCase {
	
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
		List<Long> versions = this._versioningManager.getVersions("CNG12");
		assertNull(versions);
		
		versions = this._versioningManager.getVersions("ART1");
		this.checkVersionIds(new long[] { 1, 2, 3 }, versions);
	}
	
	public void testGetLastVersions() throws Throwable {
		List<Long> versions = this._versioningManager.getLastVersions("CNG", null);
		assertTrue(versions.isEmpty());
		
		versions = this._versioningManager.getLastVersions("ART", null);
		this.checkVersionIds(new long[] { 3 }, versions);
	}
	
	public void testGetVersion() throws Throwable {
		ContentVersion contentVersion = this._versioningManager.getVersion(10000);
		assertNull(contentVersion);	
		long id = 1;
		contentVersion = this._versioningManager.getVersion(id);
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
	
	public void testGetLastVersion() throws Throwable {
		ContentVersion contentVersion = this._versioningManager.getLastVersion("CNG12");
		assertNull(contentVersion);
		contentVersion = this._versioningManager.getLastVersion("ART1");
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
	
	public void testSaveGetDeleteVersion() throws Throwable {
		((VersioningManager) this._versioningManager).saveContentVersion("ART102");
		ContentVersion contentVersion = this._versioningManager.getLastVersion("ART102");
		assertEquals(4, contentVersion.getId());
		assertEquals("ART102", contentVersion.getContentId());
		assertEquals("ART", contentVersion.getContentType());
		assertEquals("Contenuto 2 Customers", contentVersion.getDescr());
		assertEquals(Content.STATUS_DRAFT, contentVersion.getStatus());
		assertNotNull(contentVersion.getXml());
		assertEquals("15/12/2007", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
		assertEquals("1.0", contentVersion.getVersion());
		assertEquals(1, contentVersion.getOnlineVersion());
		assertTrue(contentVersion.isApproved());
		assertEquals("admin", contentVersion.getUsername());
		
		this._versioningManager.deleteVersion(contentVersion.getId());
		assertNull(this._versioningManager.getLastVersion("ART102"));
	}
	
	public void deleteWorkVersions() throws Throwable {
		List<Long> versions = this._versioningManager.getVersions("ART1");
		this.checkVersionIds(new long[] { 1, 2, 3 }, versions);
		
		this._versioningManager.deleteWorkVersions("ART1", 0);
		versions = this._versioningManager.getVersions("ART1");
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
		this._versioningManager = (IVersioningManager) this.getService(JpversioningSystemConstants.VERSIONING_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		this._helper = new JpversioningTestHelper(dataSource, this.getApplicationContext());
	}
	
    private IVersioningManager _versioningManager;
	private JpversioningTestHelper _helper;
	
}