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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import com.agiletec.aps.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;

/**
 * Rewriting of some default test for content manager
 * @author E.Santoboni
 */
public class AdvContentSearchTest extends BaseTestCase {

    private IContentManager contentManager = null;
    private ICmsSearchEngineManager searchEngineManager = null;
    private ICategoryManager categoryManager;
    
    private List<String> allowedGroup = new ArrayList<>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this.searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
            this.categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void testSearchContents_main() throws Throwable {
        this.executeSearchContents_main("ciliegia");
        this.executeSearchContents_main("Sagra della ciliegia");
    }
    
    protected void executeSearchContents_main(String text) throws Throwable {
        SearchEngineFilter descrFilter = new SearchEngineFilter("it", text, SearchEngineFilter.TextSearchOption.EXACT);
        descrFilter.setFullTextSearch(true);
        SearchEngineFilter[] filters = {descrFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expected = {"EVN41"};
        assertEquals(expected.length, contentIds.size());
        for (int i = 0; i < expected.length; i++) {
            String contentId = expected[i];
            assertTrue(contentIds.contains(contentId));
        }
    }
    
    public void testSearchContents_1() throws Throwable {
        SearchEngineFilter groupFilter = new SearchEngineFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, false, "coach", SearchEngineFilter.TextSearchOption.EXACT);
        SearchEngineFilter[] filters = {groupFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expected = {"EVN103", "ART104", "ART111", "ART112", "EVN25", "EVN41"};
        assertEquals(expected.length, contentIds.size());
        for (int i = 0; i < expected.length; i++) {
            String contentId = expected[i];
            assertTrue(contentIds.contains(contentId));
        }
    }
    
    public void testSearchContents_2() throws Throwable {
        SearchEngineFilter descrFilter = new SearchEngineFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Mostra della ciliegia", SearchEngineFilter.TextSearchOption.EXACT);
        SearchEngineFilter[] filters = {descrFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expected = {"EVN41"};
        assertEquals(expected.length, contentIds.size());
        for (int i = 0; i < expected.length; i++) {
            String contentId = expected[i];
            assertTrue(contentIds.contains(contentId));
        }
    }
    
    public void testSearchContents_3() throws Throwable {
        SearchEngineFilter attributeFilter = new SearchEngineFilter("Titolo", true, "Sagra della ciliegia", SearchEngineFilter.TextSearchOption.EXACT);
        attributeFilter.setLangCode("it");
        SearchEngineFilter[] filters = {attributeFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expected = {"EVN41"};
        assertEquals(expected.length, contentIds.size());
        for (int i = 0; i < expected.length; i++) {
            String contentId = expected[i];
            assertTrue(contentIds.contains(contentId));
        }
    }
    
    public void testSearchContents_4() throws Throwable {
        SearchEngineFilter creationOrder = new SearchEngineFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter groupFilter = new SearchEngineFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, false, "coach");
        SearchEngineFilter[] filters = {creationOrder, groupFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expected = {"EVN103", "ART104", "ART111", "ART112", "EVN25", "EVN41"};
        assertEquals(expected.length, contentIds.size());
        this.verifyOrder(contentIds, expected);
    }
    
    public void testLoadPublicEvents_1() throws ApsSystemException {
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {typeFilter};
        SearchEngineFilter[] categoriesFilters = {};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        assertNotNull(result);
        List<String> contentIds = result.getContentsId();
        String[] expectedFreeContentsId = {"EVN194", "EVN193",
            "EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedFreeContentsId.length, contentIds.size());
        for (int i = 0; i < expectedFreeContentsId.length; i++) {
            assertTrue(contentIds.contains(expectedFreeContentsId[i]));
        }
        assertFalse(contentIds.contains("EVN103"));

        List<String> groups = new ArrayList<>();
        groups.add("coach");
        result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, groups);
        contentIds = result.getContentsId();
        assertEquals(expectedFreeContentsId.length + 2, contentIds.size());
        for (int i = 0; i < expectedFreeContentsId.length; i++) {
            assertTrue(contentIds.contains(expectedFreeContentsId[i]));
        }
        assertTrue(contentIds.contains("EVN103"));
        assertTrue(contentIds.contains("EVN41"));
    }
    
    public void testLoadPublicEvents_2() throws Exception {
        SearchEngineFilter[] categoriesFilters = {};
        Date start = DateConverter.parseDate("2007-01-10", "yyyy-MM-dd");
        Date end = DateConverter.parseDate("2008-12-19", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, start, end);
        filter.setOrder(SearchEngineFilter.Order.ASC);
        SearchEngineFilter[] filters = {filter};
        
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        List<String> contentIds = result.getContentsId();
        String[] expectedContentsIds = {"EVN25", "EVN41", "EVN23"};
        assertEquals(expectedContentsIds.length, contentIds.size());
        this.verifyOrder(contentIds, expectedContentsIds);
        
        filter.setOrder(SearchEngineFilter.Order.DESC);
        SearchEngineFilter[] filters2 = {filter};
        result = this.searchEngineManager.searchFacetedEntities(filters2, categoriesFilters, this.allowedGroup);
        contentIds = result.getContentsId();
        assertEquals(expectedContentsIds.length, contentIds.size());
        for (int i = 0; i < contentIds.size(); i++) {
            assertEquals(expectedContentsIds[expectedContentsIds.length-i-1], contentIds.get(i));
        }
    }
    
    public void testLoadPublicEvents_3() throws Exception {
        SearchEngineFilter[] categoriesFilters = {};
        Date end = DateConverter.parseDate("2000-01-01", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, null, end);
        filter.setOrder(SearchEngineFilter.Order.ASC);
        SearchEngineFilter[] filters = {filter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        List<String> contentIds = result.getContentsId();
        String[] expectedContentsIds = {"EVN191", "EVN192", "EVN103"};
        assertEquals(expectedContentsIds.length, contentIds.size());
        this.verifyOrder(contentIds, expectedContentsIds);
    }
    
    private void verifyOrder(List<String> contents, String[] order) {
        for (int i = 0; i < contents.size(); i++) {
            assertEquals(order[i], contents.get(i));
        }
    }
    
    public void testLoadPublicEvents_7() throws ApsSystemException {
        this.testLoadPublicEvents_7(true);
        this.testLoadPublicEvents_7(false);
    }
    
    protected void testLoadPublicEvents_7(boolean useRoleFilter) throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        List<String> allowedDescription = new ArrayList<>();
        allowedDescription.add("Castello dei bambini");//EVN24
        allowedDescription.add("Mostra Zootecnica");//EVN20
        SearchEngineFilter filter1 = (useRoleFilter)
                ? new SearchEngineFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, true, allowedDescription, SearchEngineFilter.TextSearchOption.EXACT)
                : new SearchEngineFilter("Titolo", true, allowedDescription, SearchEngineFilter.TextSearchOption.EXACT);
        filter1.setLangCode("it");
        SearchEngineFilter filter2 = new SearchEngineFilter("DataInizio", true);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter1 , filter2, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, this.allowedGroup);
        List<String> contents = result.getContentsId();
        System.out.println("contents -> " + contents);
        String[] expectedOrderedContentsId2 = {"EVN20", "EVN24"};
        assertEquals(expectedOrderedContentsId2.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId2.length; i++) {
            assertEquals(expectedOrderedContentsId2[i], contents.get(i));
        }
    }
    
    public void testLoadOrderedPublicEvents_1() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        SearchEngineFilter filterForDescr = new SearchEngineFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false);
        filterForDescr.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filterForDescr, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedFreeContentsId = {"EVN24", "EVN23", "EVN191",
            "EVN192", "EVN193", "EVN194", "EVN20", "EVN21", "EVN25"};
        assertEquals(expectedFreeContentsId.length, contents.size());
        for (int i = 0; i < expectedFreeContentsId.length; i++) {
            assertEquals(expectedFreeContentsId[i], contents.get(i));
        }
        filterForDescr.setOrder(EntitySearchFilter.DESC_ORDER);
        result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        contents = result.getContentsId();
        assertEquals(expectedFreeContentsId.length, contents.size());
        for (int i = 0; i < expectedFreeContentsId.length; i++) {
            assertEquals(expectedFreeContentsId[expectedFreeContentsId.length - i - 1], contents.get(i));
        }
    }
    
    public void testLoadOrderedPublicEvents_2() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        SearchEngineFilter filterForCreation = new SearchEngineFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
        filterForCreation.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {typeFilter, filterForCreation};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedFreeOrderedContentsId = {"EVN191", "EVN192", "EVN193", "EVN194",
            "EVN20", "EVN23", "EVN24", "EVN25", "EVN21"};
        assertEquals(expectedFreeOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedFreeOrderedContentsId.length; i++) {
            assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
        }
        filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
        result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        contents = result.getContentsId();
        assertEquals(expectedFreeOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedFreeOrderedContentsId.length; i++) {
            assertEquals(expectedFreeOrderedContentsId[expectedFreeOrderedContentsId.length - i - 1], contents.get(i));
        }
    }
    
    public void testLoadOrderedPublicEvents_3() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        SearchEngineFilter filterForCreation = new SearchEngineFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
        filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
        SearchEngineFilter filterForDate = new SearchEngineFilter("DataInizio", true);
        filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filterForCreation, filterForDate, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedFreeOrderedContentsId = {"EVN21", "EVN25", "EVN24", "EVN23",
            "EVN20", "EVN194", "EVN193", "EVN192", "EVN191"};
        assertEquals(expectedFreeOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedFreeOrderedContentsId.length; i++) {
            assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
        }
        
        SearchEngineFilter[] filters2 = {filterForDate, filterForCreation, typeFilter};
        FacetedContentsResult result2 = this.searchEngineManager.searchFacetedEntities(filters2, categoriesFilters, null);
        List<String> contents2 = result2.getContentsId();
        String[] expectedFreeOrderedContentsId2 = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedFreeOrderedContentsId2.length, contents2.size());
        for (int i = 0; i < expectedFreeOrderedContentsId2.length; i++) {
            assertEquals(expectedFreeOrderedContentsId2[i], contents2.get(i));
        }
    }
    
    public void testLoadOrderedPublicEvents_4() throws Throwable {
        SearchEngineFilter[] categoriesFilters = {};
        Content masterContent = this.contentManager.loadContent("EVN193", true);
        masterContent.setId(null);
        DateAttribute dateAttribute = (DateAttribute) masterContent.getAttribute("DataInizio");
        dateAttribute.setDate(DateConverter.parseDate("17/06/2019", "dd/MM/yyyy"));
        try {
            this.contentManager.saveContent(masterContent);
            this.contentManager.insertOnLineContent(masterContent);
            this.waitNotifyingThread();
            
            SearchEngineFilter filterForDate = new SearchEngineFilter("DataInizio", true);
            filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
            SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
            SearchEngineFilter[] filters = {filterForDate, typeFilter};
            FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
            List<String> contents = result.getContentsId();
            String[] expectedFreeOrderedContentsId = {"EVN194", masterContent.getId(), "EVN193", "EVN24",
                "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
            assertEquals(expectedFreeOrderedContentsId.length, contents.size());
            for (int i = 0; i < expectedFreeOrderedContentsId.length; i++) {
                assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != masterContent.getId() && !"EVN193".equals(masterContent.getId())) {
                this.contentManager.removeOnLineContent(masterContent);
                this.contentManager.deleteContent(masterContent);
            }
        }
    }
    
    public void testLoadFutureEvents_1() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN21", "EVN20", "EVN25", "EVN23", "EVN24", "EVN193", "EVN194"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }
    
    public void testLoadFutureEvents_2() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN25", "EVN20", "EVN21"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }

    public void testLoadFutureEvents_3() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, groups);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN41", "EVN25", "EVN20", "EVN21"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }

    public void testLoadPastEvents_1() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN191", "EVN192",
            "EVN21", "EVN20", "EVN25", "EVN23"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }

    public void testLoadPastEvents_2() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, null);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN23", "EVN25",
            "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }

    public void testLoadPastEvents_3() throws ApsSystemException {
        SearchEngineFilter[] categoriesFilters = {};
        Date start = null;
        Date today = DateConverter.parseDate("2008-02-13", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {filter, typeFilter};
        List<String> groups = new ArrayList<>();
        groups.add("coach");
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, groups);
        List<String> contents = result.getContentsId();
        String[] expectedOrderedContentsId = {"EVN191", "EVN192", "EVN103",
            "EVN21", "EVN20", "EVN25", "EVN41", "EVN23"};
        assertEquals(expectedOrderedContentsId.length, contents.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], contents.get(i));
        }
    }

    public void testLoadPublicContentsForCategory() throws ApsSystemException {
        List<Category> categories1 = new ArrayList<>();
        categories1.add(this.categoryManager.getCategory("evento"));
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(null, this.extractCategoryFilters(categories1), null);
        List<String> contents = result.getContentsId();
        assertEquals(2, contents.size());
        assertTrue(contents.contains("EVN192"));
        assertTrue(contents.contains("EVN193"));
        
        List<Category> categories2 = new ArrayList<>();
        categories2.add(this.categoryManager.getCategory("cat1"));
        result = this.searchEngineManager.searchFacetedEntities(null, this.extractCategoryFilters(categories2), null);
        contents = result.getContentsId();
        assertEquals(1, contents.size());
        assertTrue(contents.contains("ART180"));
    }
    
    public void testLoadPublicEventsForCategory_1() throws ApsSystemException {
        List<Category> categories1 = new ArrayList<>();
        categories1.add(this.categoryManager.getCategory("evento"));
        SearchEngineFilter typeFilter = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN");
        SearchEngineFilter[] filters = {typeFilter};
        FacetedContentsResult result = this.searchEngineManager.searchFacetedEntities(filters, this.extractCategoryFilters(categories1), null);
        List<String> contents = result.getContentsId();
        assertEquals(2, contents.size());
        assertTrue(contents.contains("EVN192"));
        assertTrue(contents.contains("EVN193"));

        Date today = DateConverter.parseDate("2005-02-13", "yyyy-MM-dd");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        SearchEngineFilter[] filters2 = {typeFilter, filter};
        result = this.searchEngineManager.searchFacetedEntities(filters2, this.extractCategoryFilters(categories1), null);
        contents = result.getContentsId();
        assertEquals(1, contents.size());
        assertTrue(contents.contains("EVN192"));
    }
    
    private SearchEngineFilter[] extractCategoryFilters(Collection<Category> categories) {
        SearchEngineFilter[] categoryFilterArray = null;
        if (null != categories) {
            List<SearchEngineFilter> categoryFilters = categories.stream().filter(c -> c != null)
                    .map(c -> new SearchEngineFilter("category", false, c.getCode())).collect(Collectors.toList());
            categoryFilterArray = categoryFilters.toArray(new SearchEngineFilter[categoryFilters.size()]);
        }
        return categoryFilterArray;
    }
    
}
