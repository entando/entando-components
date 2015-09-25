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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.plugins.jpversioning.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import java.io.InputStream;

/**
 * @author G.Cocco
 */
public class TestTrashedResourceManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
    }
	
	public void testSearchTrashedResourceIds() throws ApsSystemException {
		String resourceTypeCode = null;
		String text = null;
		List<String> allowedGroups = new ArrayList<String>();
		List<String> resourceIds = this._trashedResourceManager.searchTrashedResourceIds(resourceTypeCode, text, allowedGroups);
		assertNotNull(resourceIds);
		assertEquals(6, resourceIds.size());
		assertEquals("67", resourceIds.get(0));
		assertEquals("68", resourceIds.get(1));
		
		resourceTypeCode = "Image";
		text = "qq";
		allowedGroups = new ArrayList<String>();
		allowedGroups.add("customers");
		resourceIds = this._trashedResourceManager.searchTrashedResourceIds(resourceTypeCode, text, allowedGroups);
		assertNotNull(resourceIds);
		assertEquals(1, resourceIds.size());
		assertEquals("69", resourceIds.get(0));
	}
	
	public void testAdd_Trash_LoadFromTrash_RemoveFromTrash() throws Throwable {
		String mainGroup = Group.FREE_GROUP_NAME;
		String resDescrToAdd = "Versioning test 1";
		String resourceType = "Image";
		String categoryCodeToAdd = "resCat1";
		List<String> groups = new ArrayList<String>();
		groups.add(Group.FREE_GROUP_NAME);
		try {
			ResourceDataBean bean = this.getMockResource(resourceType, mainGroup, resDescrToAdd, categoryCodeToAdd);

			// add the resource
			this._resourceManager.addResource(bean);
			List<String> resources = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			assertEquals(1, resources.size());
			
			ResourceInterface resource = _resourceManager.loadResource(resources.get(0));
			assertNotNull(resource);

			// move into the trash
			_resourceManager.deleteResource(resource);

			// load from Trash
			ResourceInterface resourceLoadedFromTrash = this._trashedResourceManager.loadTrashedResource(resource.getId());
			assertNotNull(resourceLoadedFromTrash);
			assertEquals(resDescrToAdd, resourceLoadedFromTrash.getDescr());
			assertEquals(mainGroup, resourceLoadedFromTrash.getMainGroup());
			assertEquals(resourceType, resourceLoadedFromTrash.getType());
			
			// del from trash
			this._trashedResourceManager.removeFromTrash(resource.getId());
		} catch (Throwable t) {
			List<String> resources = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			if (null != resources && resources.size() > 0) {
				for (int i = 0; i < resources.size(); i++) {
					String id = resources.get(i);
					this._trashedResourceManager.removeFromTrash(id);
					ResourceInterface resource = this._resourceManager.loadResource(id);
					this._resourceManager.deleteResource(resource);
				}
			}
			throw t;
		}
	}
	
	public void testAdd_Trash_RestoreResource_DeleteFromArchive() throws Throwable {
		String mainGroup = Group.FREE_GROUP_NAME;
		String resDescrToAdd = "Versioning test 2";
		String resourceType = "Image";
		String categoryCodeToAdd = "resCat1";
		ResourceDataBean bean = this.getMockResource(resourceType, mainGroup, resDescrToAdd, categoryCodeToAdd);
		List<String> groups = new ArrayList<String>();
		groups.add(Group.FREE_GROUP_NAME);
		try {
			// add the resource
			this._resourceManager.addResource(bean);
			List<String> resources = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			assertEquals(1, resources.size());
			
			String resourceId = resources.get(0);
			ResourceInterface resource = this._resourceManager.loadResource(resourceId);
			assertNotNull(resource);
			
			// move into the trash
			this._resourceManager.deleteResource(resource);
			resources = _resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			assertEquals(0, resources.size());
			
			// restore from Trash
			this._trashedResourceManager.restoreResource(resourceId);
			resources = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			assertEquals(1, resources.size());
			resource = this._resourceManager.loadResource(resources.get(0));
			// delete from archive and from trash
			_resourceManager.deleteResource(resource);
			resources = _resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			assertEquals(0, resources.size());
			this._trashedResourceManager.removeFromTrash(resource.getId());
		} catch (Throwable t) {
			List<String> resources = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, groups);
			if (null != resources && resources.size() > 0) {
				for (int i = 0; i < resources.size(); i++) {
					String id = resources.get(i);
					this._trashedResourceManager.removeFromTrash(id);
					ResourceInterface resource = this._resourceManager.loadResource(id);
					this._resourceManager.deleteResource(resource);
				}
			}
			throw t;
		}
	}
	
	private ResourceDataBean getMockResource(String resourceType, String mainGroup, String resDescrToAdd, String categoryCodeToAdd) {
		File file = new File("target/test/entando_logo.jpg");
		BaseResourceDataBean bean = new BaseResourceDataBean(file);
		bean.setDescr(resDescrToAdd);
		bean.setMainGroup(mainGroup);
		bean.setResourceType(resourceType);
		bean.setMimeType("image/jpeg");
		List<Category> categories = new ArrayList<Category>();
		ICategoryManager catManager = 
			(ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		Category cat = catManager.getCategory(categoryCodeToAdd);
		categories.add(cat);
		bean.setCategories(categories);
		return bean;
    }
	
	private void init() throws Exception {
    	try {
    		this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
    		this._trashedResourceManager = (TrashedResourceManager) this.getService(JpversioningSystemConstants.TRASHED_RESOURCE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
	
	private IResourceManager _resourceManager;
	private TrashedResourceManager _trashedResourceManager;
	
}