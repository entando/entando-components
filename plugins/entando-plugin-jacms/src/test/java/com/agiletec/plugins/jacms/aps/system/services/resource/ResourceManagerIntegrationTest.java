/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.mock.MockResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author W.Ambu - E.Santoboni
 */
public class ResourceManagerIntegrationTest extends BaseTestCase {

    private IResourceManager resourceManager;
    private IGroupManager groupManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testLoadResource() throws Throwable {
        try {
            ResourceInterface resource = this.resourceManager.loadResource("44");
            assertTrue(resource instanceof ImageResource);
            assertTrue(resource.isMultiInstance());
            assertEquals(resource.getDescription(), "logo");
            assertEquals(resource.getCategories().size(), 2);
            assertNotNull(resource.getMetadata());
            assertEquals(23, resource.getMetadata().size());

            resource = this.resourceManager.loadResource("7");
            assertTrue(resource instanceof AttachResource);
            assertFalse(resource.isMultiInstance());
            assertEquals(resource.getDescription(), "configurazione");
            assertEquals(resource.getCategories().size(), 0);
            assertNotNull(resource.getMetadata());
            assertEquals(0, resource.getMetadata().size());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testUpdateResource() throws Throwable {
        String oldDescr = null;
        List<Category> oldCategories = null;
        try {
            ResourceInterface resource = this.resourceManager.loadResource("44");
            assertTrue(resource instanceof ImageResource);
            assertEquals(resource.getDescription(), "logo");
            assertEquals(resource.getCategories().size(), 2);
            assertTrue(resource.isMultiInstance());
            oldCategories = resource.getCategories();
            oldDescr = resource.getDescription();
            String newDescr = "New Description";
            resource.setDescription(newDescr);
            resource.setCategories(new ArrayList<>());
            this.resourceManager.updateResource(resource);
            resource = this.resourceManager.loadResource("44");
            assertEquals(resource.getDescription(), newDescr);
            assertEquals(resource.getCategories().size(), 0);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (oldCategories != null && oldDescr != null) {
                ResourceInterface resource = this.resourceManager.loadResource("44");
                resource.setCategories(oldCategories);
                resource.setDescription(oldDescr);
                this.resourceManager.updateResource(resource);
            }
        }
    }

    public void testSearchResources_1() throws Throwable {
        List<String> resourceIds = this.resourceManager.searchResourcesId("Image", "", null, this.getAllGroupCodes());
        assertEquals(3, resourceIds.size());

        resourceIds = resourceManager.searchResourcesId("Image", "Wrong descr", null, this.getAllGroupCodes());
        assertEquals(0, resourceIds.size());

        List<String> allowedGroups = new ArrayList<>();
        allowedGroups.add("customers");
        resourceIds = resourceManager.searchResourcesId("Image", "", null, allowedGroups);
        assertEquals(1, resourceIds.size());
    }

    public void testSearchResources_2() throws Throwable {
        List<String> resourceIds = this.resourceManager.searchResourcesId("Image", "", "jpg", null, this.getAllGroupCodes());
        assertEquals(3, resourceIds.size());

        resourceIds = this.resourceManager.searchResourcesId("Image", "", "and", null, this.getAllGroupCodes());
        assertEquals(2, resourceIds.size());

        resourceIds = this.resourceManager.searchResourcesId("Image", "", "ando.JPG", null, this.getAllGroupCodes());
        assertEquals(1, resourceIds.size());
    }

    public void testSearchResourcesForCategory() throws Throwable {
        List<String> resourceIds = resourceManager.searchResourcesId("Image", null, "resCat1", this.getAllGroupCodes());
        assertEquals(1, resourceIds.size());

        resourceIds = resourceManager.searchResourcesId("Image", null, "wrongCat", this.getAllGroupCodes());
        assertEquals(0, resourceIds.size());

        List<String> allowedGroups = new ArrayList<>();
        allowedGroups.add("customers");
        resourceIds = resourceManager.searchResourcesId("Image", "", "resCat1", allowedGroups);
        assertEquals(0, resourceIds.size());
    }

    public void testAddRemoveImageResource() throws Throwable {
        this.testAddRemoveImageResource(Group.FREE_GROUP_NAME);
        this.testAddRemoveImageResource(Group.ADMINS_GROUP_NAME);
    }

    private void testAddRemoveImageResource(String mainGroup) throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        ResourceInterface res = null;
        String resDescrToAdd = "Entando Logo";
        String resourceType = "Image";
        String categoryCodeToAdd = "resCat1";
        ResourceDataBean bean = this.getMockResource(resourceType, mainGroup, resDescrToAdd, categoryCodeToAdd);
        try {
            List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(1, resourcesId.size());

            this.resourceManager.addResource(bean);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);

            res = this.resourceManager.loadResource(resourcesId.get(0));
            assertTrue(res instanceof ImageResource);
            assertEquals(res.getCategories().size(), 1);
            assertEquals(res.getDescription(), resDescrToAdd);

            ResourceInstance instance0 = ((ImageResource) res).getInstance(0, null);
            assertEquals("entando_logo.jpg", res.getMasterFileName());
            assertEquals("image/jpeg", instance0.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 2);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (res != null) {
                this.resourceManager.deleteResource(res);
                List<String> resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
                assertEquals(resources.size(), 0);

                resources = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
                assertEquals(resources.size(), 1);
            }
        }
    }
    
    public void testAddRemoveImageResources() throws Throwable {
        this.testAddRemoveImageResources(Group.FREE_GROUP_NAME);
        this.testAddRemoveImageResources("customers");
        this.testAddRemoveImageResources(Group.ADMINS_GROUP_NAME);
    }


    private void testAddRemoveImageResources(String mainGroup) throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        String resDescrToAdd1 = "Entando Logo 1";
        String resDescrToAdd2 = "Entando Logo 2";
        String resDescrToAdd3 = "Entando Logo 3";
        String resDescrToAdd4 = "Entando Logo 4";
        String resourceType = "Image";
        String categoryCodeToAdd = "resCat1";
        
        List<BaseResourceDataBean> resourceList = new ArrayList<>();
        resourceList.add(this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd1, categoryCodeToAdd, "entando_logo.jpg"));
        resourceList.add(this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd2, categoryCodeToAdd, "entando_logo.jpg"));
        resourceList.add(this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd3, categoryCodeToAdd, "entando_logo.jpg"));
        resourceList.add(this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd4, categoryCodeToAdd, "&n%ndo logo.jpg"));
        List<ResourceInterface> resourceListAdded = null;
        try {
            List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(1, resourcesId.size());

            resourceListAdded = this.resourceManager.addResources(resourceList);
            assertEquals(4, resourceListAdded.size());
            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(5, resourcesId.size());
            FieldSearchFilter<String> filterType = new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, resourceType, false);
            FieldSearchFilter<String> filterOrder = new FieldSearchFilter(IResourceManager.RESOURCE_DESCR_FILTER_KEY);
            filterOrder.setOrder(FieldSearchFilter.ASC_ORDER);
            FieldSearchFilter pagerFilter = new FieldSearchFilter(2, 0);
            FieldSearchFilter[] filters = new FieldSearchFilter[]{filterType, filterOrder, pagerFilter};
            SearcherDaoPaginatedResult<String> result = this.resourceManager.getPaginatedResourcesId(filters, Arrays.asList(categoryCodeToAdd), allowedGroups);
            assertEquals(5, result.getCount().intValue());
            assertEquals(2, result.getList().size());
            assertEquals(resourceListAdded.get(0).getId(), result.getList().get(0));
            assertEquals(resourceListAdded.get(1).getId(), result.getList().get(1));
            
            // Image 1
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            ResourceInterface res1 = this.resourceManager.loadResource(resourceListAdded.get(0).getId());
            assertTrue(res1 instanceof ImageResource);
            assertEquals("entando_logo.jpg", res1.getMasterFileName());
            assertEquals(res1.getCategories().size(), 1);
            assertEquals(res1.getDescription(), resDescrToAdd1);
            assertTrue(res1.getMetadata().size() > 0);
            
            ResourceInstance instance01 = ((ImageResource) res1).getInstance(0, null);
            String fileNameInstance01 = instance01.getFileName();
            
            assertTrue(fileNameInstance01.startsWith("entando_logo"));
            assertEquals("image/jpeg", instance01.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(5, resourcesId.size());

            // Image 2
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            
            ResourceInterface res2 = this.resourceManager.loadResource(resourceListAdded.get(1).getId());
            assertTrue(res2 instanceof ImageResource);
            assertEquals(res2.getCategories().size(), 1);
            assertEquals(res2.getDescription(), resDescrToAdd2);
            assertEquals("entando_logo.jpg", res2.getMasterFileName());
            assertEquals(res1.getMasterFileName(), res2.getMasterFileName());
            
            ResourceInstance instance02 = ((ImageResource) res2).getInstance(0, null);
            String fileNameInstance02 = instance02.getFileName();
            
            assertTrue(fileNameInstance02.startsWith("entando_logo"));
            assertFalse(fileNameInstance02.equals(fileNameInstance01));
            assertEquals("image/jpeg", instance02.getMimeType());
            
            // Image 3
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            ResourceInterface res3 = this.resourceManager.loadResource(resourceListAdded.get(2).getId());
            assertEquals("entando_logo.jpg", res3.getMasterFileName());
            assertEquals(res1.getMasterFileName(), res3.getMasterFileName());
            assertTrue(res3 instanceof ImageResource);
            assertEquals(res3.getCategories().size(), 1);
            assertEquals(res3.getDescription(), resDescrToAdd3);

            ResourceInstance instance03 = ((ImageResource) res3).getInstance(0, null);
            String fileNameInstance03 = instance03.getFileName();
            assertTrue(fileNameInstance03.startsWith("entando_logo"));
            assertFalse(fileNameInstance03.equals(fileNameInstance01));
            assertEquals("image/jpeg", instance03.getMimeType());
            
            ResourceInterface res4 = this.resourceManager.loadResource(resourceListAdded.get(3).getId());
            assertEquals("&n%ndo logo.jpg", res4.getMasterFileName());
            assertTrue(res4 instanceof ImageResource);
            assertEquals(res4.getCategories().size(), 1);
            assertEquals(res4.getDescription(), resDescrToAdd4);

            ResourceInstance instance04 = ((ImageResource) res4).getInstance(0, null);
            String fileNameInstance04 = instance04.getFileName();
            assertFalse(fileNameInstance04.startsWith("entando_logo"));
            assertEquals("nndo_logo_d0.jpg", fileNameInstance04);
            assertEquals("image/jpeg", instance04.getMimeType());
        } catch (Throwable t) {
            throw t;
        } finally {
            if (resourceListAdded != null) {
                this.resourceManager.deleteResources(resourceListAdded);
                List<String> resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
                assertEquals(resources.size(), 1);
            }
        }
    }

    public void testLoadSvg() throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        String resDescrToAdd1 = "Svg1";
        String resourceType = "Image";
        String categoryCodeToAdd = "resCat1";
        String mainGroup = "customers";

        List<BaseResourceDataBean> resourceList = new ArrayList<>();
        resourceList.add(getMockSvgResource(resourceType, mainGroup, resDescrToAdd1, categoryCodeToAdd, "test.svg"));

        List<ResourceInterface> resourceListAdded =null;
        try {
            resourceListAdded = this.resourceManager.addResources(resourceList);
            ResourceInterface res1 = this.resourceManager.loadResource(resourceListAdded.get(0).getId());

            assertEquals("test.svg", res1.getMasterFileName());
            assertEquals(res1.getCategories().size(), 1);
            assertEquals(res1.getDescription(), resDescrToAdd1);


            ResourceInstance instance01 = ((ImageResource) res1).getInstance(0, null);
            String fileNameInstance01 = instance01.getFileName();

            assertTrue(fileNameInstance01.startsWith("test"));
            assertEquals("image/svg+xml", instance01.getMimeType());

        }catch(Throwable t) {
            throw t;
        }finally {
            this.resourceManager.deleteResources(resourceListAdded);
            List<String> resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, null, allowedGroups);
            assertEquals(resources.size(), 0);
        }
    }
    
    private ResourceDataBean getMockResource(String resourceType,
            String mainGroup, String resDescrToAdd, String categoryCodeToAdd) {
        File file = new File("target/test/entando_logo.jpg");
        MockResourceDataBean bean = new MockResourceDataBean();
        bean.setFile(file);
        bean.setDescr(resDescrToAdd);
        bean.setMainGroup(mainGroup);
        bean.setResourceType(resourceType);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("metadata1", "value1");
        metadata.put("metadata2", "value2");
        bean.setMetadata(metadata);
        bean.setMimeType("image/jpeg");
        List<Category> categories = new ArrayList<>();
        ICategoryManager catManager
                = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        Category cat = catManager.getCategory(categoryCodeToAdd);
        categories.add(cat);
        bean.setCategories(categories);
        return bean;
    }
    
    private BaseResourceDataBean getMockBaseResource(String resourceType,
            String mainGroup, String resDescrToAdd, String categoryCodeToAdd,
            String fileName) {
        File file = new File("target/test/entando_logo.jpg");
        BaseResourceDataBean bean = new BaseResourceDataBean();
        bean.setFile(file);
        bean.setFileName(fileName);
        bean.setDescr(resDescrToAdd);
        bean.setMainGroup(mainGroup);
        bean.setResourceType(resourceType);
        bean.setMimeType("image/jpeg");
        List<Category> categories = new ArrayList<>();
        ICategoryManager catManager
                = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        Category cat = catManager.getCategory(categoryCodeToAdd);
        categories.add(cat);
        bean.setCategories(categories);
        return bean;
    }

    private BaseResourceDataBean getMockSvgResource(String resourceType,
            String mainGroup, String resDescrToAdd, String categoryCodeToAdd,
            String fileName) {
        File file = new File("src/test/resources/images/test.svg");
        BaseResourceDataBean bean = new BaseResourceDataBean();
        bean.setFile(file);
        bean.setFileName(fileName);
        bean.setDescr(resDescrToAdd);
        bean.setMainGroup(mainGroup);
        bean.setResourceType(resourceType);
        bean.setMimeType("image/svg+xml");
        List<Category> categories = new ArrayList<>();
        ICategoryManager catManager
                = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        Category cat = catManager.getCategory(categoryCodeToAdd);
        categories.add(cat);
        bean.setCategories(categories);
        return bean;
    }
    
    public void testAddNullResource() throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        String resDescrToAdd = "Null Entando resource";
        String resourceType = "Attach";
        List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
        int initsize = resourcesId.size();
        ResourceDataBean bean = this.getNullMockResource(resourceType, resDescrToAdd);
        try {
            this.resourceManager.addResource(bean);
            fail();
        } catch (Throwable t) {
            //nothing to do
        } finally {
            this.verifyTestAddNullResource(resDescrToAdd, resourceType, initsize);
        }
    }
    
    private void verifyTestAddNullResource(String resDescrToAdd, String resourceType, int initsize) throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        List<String> resourcesId = null;
        try {
            resourcesId = this.resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
            assertEquals(initsize, resourcesId.size());
            resourcesId = this.resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            assertEquals(0, resourcesId.size());
        } catch (Throwable t) {
            resourcesId = this.resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            for (int i = 0; i < resourcesId.size(); i++) {
                ResourceInterface res = this.resourceManager.loadResource(resourcesId.get(i));
                this.resourceManager.deleteResource(res);
            }
            throw t;
        }
    }

    private ResourceDataBean getNullMockResource(String resourceType, String resDescrToAdd) {
        MockResourceDataBean bean = new MockResourceDataBean();
        bean.setDescr(resDescrToAdd);
        bean.setMainGroup(Group.FREE_GROUP_NAME);
        bean.setResourceType(resourceType);
        bean.setMimeType("text/plain");
        return bean;
    }
    
    public void testGetResourceType() {
        ResourceInterface imageResource = this.resourceManager.createResourceType("Image");
        assertEquals("", imageResource.getDescription());
        assertEquals("", imageResource.getId());
        assertEquals("Image", imageResource.getType());
    }

    public void testCreateResourceType() {
        ResourceInterface imageResource = this.resourceManager.createResourceType("Image");
        assertNotNull(imageResource);
        assertEquals("", imageResource.getDescription());
        assertEquals("", imageResource.getId());
        assertEquals("Image", imageResource.getType());
        assertNotSame("", imageResource.getXML());
    }

    public void testGetGroupUtilizers() throws Throwable {
        assertTrue(this.resourceManager instanceof GroupUtilizer);
        List utilizers = ((GroupUtilizer) this.resourceManager).getGroupUtilizers(Group.FREE_GROUP_NAME);
        assertEquals(4, utilizers.size());

        utilizers = ((GroupUtilizer) this.resourceManager).getGroupUtilizers("customers");
        assertEquals(2, utilizers.size());
        assertEquals("8", utilizers.get(0));
        assertEquals("82", utilizers.get(1));
    }
    
    private List<String> getAllGroupCodes() {
        List<String> groupCodes = new ArrayList<>();
        this.groupManager.getGroups().stream().forEach(group -> groupCodes.add(group.getName()));
        return groupCodes;
    }
    
    public void testGetMetadataMapping() throws Exception {
        Map<String, List<String>> mapping = this.resourceManager.getMetadataMapping();
        assertNotNull(mapping);
        assertEquals(4, mapping.size());
        assertEquals(5, mapping.get("alt").size());
        assertEquals(3, mapping.get("description").size());
        assertEquals(6, mapping.get("legend").size());
        assertEquals("metadatakey3", mapping.get("alt").get(2));
        assertEquals("metadataKeyA", mapping.get("description").get(0));
        assertEquals("YYYY", mapping.get("legend").get(3));
        assertEquals("metadataKeyK", mapping.get("title").get(1));
    }
    
    private void init() throws Exception {
        try {
            this.resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
            this.groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
