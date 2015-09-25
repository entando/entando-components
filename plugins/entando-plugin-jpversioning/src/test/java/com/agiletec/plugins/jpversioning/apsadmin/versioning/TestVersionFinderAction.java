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