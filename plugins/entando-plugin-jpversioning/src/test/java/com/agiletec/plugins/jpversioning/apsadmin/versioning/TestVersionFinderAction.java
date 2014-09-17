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
package com.agiletec.plugins.jpversioning.apsadmin.versioning;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpversioning.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpversioning.util.JpversioningTestHelper;

import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.VersionFinderAction;
import com.opensymphony.xwork2.Action;

/**
 * @author G.Cocco
 */
public class TestVersionFinderAction  extends ApsAdminPluginBaseTestCase {
	
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
	
	public void testList() throws Throwable{
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		
		VersionFinderAction action = (VersionFinderAction) this.getAction();
		List<Long> lastVersionsId = action.getLatestVersions();
		assertEquals(1, lastVersionsId.size());
		assertEquals(3, lastVersionsId.get(0).longValue());
		
		ContentVersion contentVersion = action.getContentVersion(3);
		assertEquals(3, contentVersion.getId());
		assertEquals("1.0", contentVersion.getVersion());
		assertEquals(1, contentVersion.getOnlineVersion());
		assertEquals("ART1", contentVersion.getContentId());
		assertEquals("ART", contentVersion.getContentType());
		assertEquals("Articolo 3", contentVersion.getDescr());
		assertNotNull(contentVersion.getVersionDate());
		assertNotNull(contentVersion.getXml());
	}
	
	public void testSearch() throws Throwable{
		String result = this.executeSearch("admin", null, null);
		assertEquals(Action.SUCCESS, result);
		
		VersionFinderAction action = (VersionFinderAction) this.getAction();
		List<Long> lastVersionsId = action.getLatestVersions();
		assertEquals(1, lastVersionsId.size());
		assertEquals(3, lastVersionsId.get(0).longValue());
		
		result = this.executeSearch("admin", "No descr", null);
		assertEquals(Action.SUCCESS, result);
		lastVersionsId = ((VersionFinderAction) this.getAction()).getLatestVersions();
		assertEquals(0, lastVersionsId.size());
		
		result = this.executeSearch("admin", "Articolo 2", null);
		assertEquals(Action.SUCCESS, result);
		lastVersionsId = ((VersionFinderAction) this.getAction()).getLatestVersions();
		assertEquals(0, lastVersionsId.size());
		
		result = this.executeSearch("admin", "Articolo 3", null);
		assertEquals(Action.SUCCESS, result);
		lastVersionsId = ((VersionFinderAction) this.getAction()).getLatestVersions();
		assertEquals(1, lastVersionsId.size());
		assertEquals(3, lastVersionsId.get(0).longValue());
		
		result = this.executeSearch("admin", "Articolo 3", "ART");
		assertEquals(Action.SUCCESS, result);
		lastVersionsId = ((VersionFinderAction) this.getAction()).getLatestVersions();
		assertEquals(1, lastVersionsId.size());
		assertEquals(3, lastVersionsId.get(0).longValue());
		
		result = this.executeSearch("admin", "Articolo 3", "CNG");
		assertEquals(Action.SUCCESS, result);
		lastVersionsId = ((VersionFinderAction) this.getAction()).getLatestVersions();
		assertEquals(0, lastVersionsId.size());
	}
	
	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "list");
		String result = this.executeAction();
		return result;
	}
	
	private String executeSearch(String username, String descr, String contentType) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "search");
		if (contentType != null) this.addParameter("contentType", contentType);
		if (descr != null) this.addParameter("descr", descr);
		String result = this.executeAction();
		return result;
	}
	
	private void init() throws Exception {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		this._helper = new JpversioningTestHelper(dataSource, this.getApplicationContext());
	}
	
	private JpversioningTestHelper _helper;
	
}