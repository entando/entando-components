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

import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.VersionAction;
import com.opensymphony.xwork2.Action;

/**
 * @author G.Cocco
 */
public class TestVersionAction extends ApsAdminPluginBaseTestCase {
	
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
	
	public void testHistory() throws Throwable {
		String result = this.executeHistory("admin", "ART1");
		assertEquals(Action.SUCCESS, result);
		List<Long> versions = ((VersionAction) this.getAction()).getContentVersions();
		assertEquals(3, versions.size());
		ContentVersion version = ((VersionAction) this.getAction()).getContentVersion(1);
		assertEquals("Articolo", version.getDescr());
		
		result = this.executeHistory("admin", "ART2");
		assertEquals(Action.SUCCESS, result);
		versions = ((VersionAction) this.getAction()).getContentVersions();
		assertNull(versions);
	}
	
	public void testTrash() throws Throwable {
		String result = this.executeTrash("admin", 3);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testDelete() throws Throwable {
		String result = this.executeDelete("admin", 300);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testPreview() throws Throwable {
		String result = this.executePreview("admin", 3);
		assertEquals(Action.SUCCESS, result);
		// TODO contenuto in sessione?
	}
	
	public void testEntryRecover() throws Throwable {
		String result = this.executeEntryRecover("admin", 3);
		assertEquals(Action.SUCCESS, result);
	}
	
	public String executeHistory(String username, String contentId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "history");
		this.addParameter("contentId", contentId);
		String result = this.executeAction();
		return result;
	}
	
	public String executeTrash(String username, long versionId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "trash");
		this.addParameter("versionId", String.valueOf(versionId));
		String result = this.executeAction();
		return result;
	}
	
	public String executeDelete(String username, long versionId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "delete");
		this.addParameter("versionId", String.valueOf(versionId));
		String result = this.executeAction();
		return result;
	}
	
	public String executePreview(String username, long versionId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "preview");
		this.addParameter("versionId", String.valueOf(versionId));
		String result = this.executeAction();
		return result;
	}
	
	public String executeEntryRecover(String username, long versionId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "entryRecover");
		this.addParameter("versionId", String.valueOf(versionId));
		String result = this.executeAction();
		return result;
	}
	
	public String executeRecover(String username, long versionId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpversioning/Content/Versioning", "recover");
		this.addParameter("versionId", String.valueOf(versionId));
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		this._helper = new JpversioningTestHelper(dataSource, this.getApplicationContext());
	}
	
	private JpversioningTestHelper _helper;
	
	private IContentManager _contentManager;
	private IResourceManager _resourceManager;
	
}