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
package com.agiletec.plugins.jpmassiveresourceloader.apsadmin.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.opensymphony.xwork2.Action;

public class TestMassiveResourceLoaderAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testSaveAction() throws Throwable {
		String type = "Image";
		String descr = "testMRL";
		List<String> resources = _resourceManager.searchResourcesId(type, descr, null, null);
		assertEquals(0, resources.size());
		try {
			File dir = new File("target/test/");
			assertTrue(dir.isDirectory());
			String folder = dir.getAbsolutePath();
			
			String result = this.executeLoadAction(type, folder, descr, "");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			List<String> mainGroupErrors = fieldErrors.get("mainGroup");
			assertEquals(1, mainGroupErrors.size());
			assertTrue(mainGroupErrors.get(0).contains(this.getAction().getText("requiredstring")));

			result = this.executeLoadAction(type, folder, descr, "free");
			assertEquals(Action.SUCCESS, result);
			
			List<String> groups = new ArrayList<String>();
			groups.add(Group.FREE_GROUP_NAME);
			resources = _resourceManager.searchResourcesId(type, descr, null, groups);
			assertEquals(1, resources.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			resources = _resourceManager.searchResourcesId(type, descr, null, null);
			if (null != resources && resources.size() > 0) {
				for (int i = 0; i < resources.size(); i++) {
					ResourceInterface resource = _resourceManager.loadResource(resources.get(i));
					_resourceManager.deleteResource(resource);
				}
			}
		}
	}
	
	private String executeLoadAction(String resourceTypeCode, String folder, String descr, String mainGroup) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmassiveresourceloader/Resource/Massive", "save");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		this.addParameter("descr", descr);
		this.addParameter("mainGroup", mainGroup);
		this.addParameter("folder", folder);
		return this.executeAction();
	}
	
	private void init() {
		_resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
	}
	
	private IResourceManager _resourceManager;
	
}