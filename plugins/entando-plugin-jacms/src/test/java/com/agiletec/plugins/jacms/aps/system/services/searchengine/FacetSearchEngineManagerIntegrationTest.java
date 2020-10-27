/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;

/**
 * @author eu
 */
public class FacetSearchEngineManagerIntegrationTest extends BaseTestCase {

    private IContentManager contentManager = null;
    private ICmsSearchEngineManager searchEngineManager = null;
    private ICategoryManager categoryManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this.searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
            this.categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

    public void testSearchAllContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            List<String> allowedGroup = new ArrayList<>();
            SearchEngineFilter[] filters = {};
            FacetedContentsResult freeResult = this.searchEngineManager.searchFacetedEntities(filters, filters, allowedGroup);
            assertNotNull(freeResult);
            List<String> freeContentsId = freeResult.getContentsId();
            assertNotNull(freeContentsId);
            assertEquals(2, freeResult.getOccurrences().size());
            assertEquals(2, freeResult.getOccurrences().get("evento").intValue());
            assertEquals(1, freeResult.getOccurrences().get("cat1").intValue());
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            FacetedContentsResult allResult = this.searchEngineManager.searchFacetedEntities(filters, filters, allowedGroup);
            assertNotNull(allResult);
            assertNotNull(allResult.getContentsId());
            assertTrue(allResult.getContentsId().size() > freeContentsId.size());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchOrderedContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            List<String> allowedGroup = new ArrayList<>();
            this.executeSearchOrderedContents(allowedGroup);
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            this.executeSearchOrderedContents(allowedGroup);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void executeSearchOrderedContents(List<String> allowedGroup) throws Exception {
        try {
            SearchEngineFilter[] categoriesFilters = {};
            SearchEngineFilter filterByType
                    = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", SearchEngineFilter.TextSearchOption.EXACT);
            SearchEngineFilter filterWithOrder
                    = new SearchEngineFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
            filterWithOrder.setOrder(FieldSearchFilter.Order.ASC);
            SearchEngineFilter[] filters1 = {filterByType, filterWithOrder};
            FacetedContentsResult freeResult1 = this.searchEngineManager.searchFacetedEntities(filters1, categoriesFilters, allowedGroup);
            assertNotNull(freeResult1);
            List<String> contentsId1 = freeResult1.getContentsId();
            if (allowedGroup.contains(Group.ADMINS_GROUP_NAME)) {
                assertEquals(10, contentsId1.size());
            } else {
                assertEquals(4, contentsId1.size());
            }
            Map<String, Integer> freeOccurrence1 = freeResult1.getOccurrences();
            filterWithOrder.setOrder(FieldSearchFilter.Order.DESC);
            SearchEngineFilter[] filters2 = {filterByType, filterWithOrder};
            FacetedContentsResult result2 = this.searchEngineManager.searchFacetedEntities(filters2, categoriesFilters, allowedGroup);
            assertEquals(freeOccurrence1, result2.getOccurrences());
            assertEquals(contentsId1.size(), result2.getContentsId().size());
            for (int i = 0; i < contentsId1.size(); i++) {
                assertEquals(contentsId1.get(i), result2.getContentsId().get(contentsId1.size() - 1 - i));
            }
        } catch (Exception t) {
            throw t;
        }
    }

    public void testSearchContents() throws Throwable {
        Thread thread = this.searchEngineManager.startReloadContentsReferences();
        thread.join();
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        SearchEngineFilter filterWithOrder
                = new SearchEngineFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
        filterWithOrder.setOrder(FieldSearchFilter.Order.DESC);
        SearchEngineFilter[] filters = {filterWithOrder};
        List<Category> categories_1 = new ArrayList<>();
        categories_1.add(this.categoryManager.getCategory("general_cat2"));
        FacetedContentsResult result1 = this.searchEngineManager.searchFacetedEntities(filters, this.extractCategoryFilters(categories_1), allowedGroup);
        List<String> contents = result1.getContentsId();
        String[] order_a = {"ART120", "ART111"};
        assertEquals(order_a.length, contents.size());
        this.verifyOrder(contents, order_a);

        List<Category> categories_2 = new ArrayList<>(categories_1);
        categories_2.add(this.categoryManager.getCategory("general_cat1"));
        FacetedContentsResult result2 = this.searchEngineManager.searchFacetedEntities(filters, this.extractCategoryFilters(categories_2), allowedGroup);
        contents = result2.getContentsId();
        String[] order_b = {"ART111"};
        assertEquals(order_b.length, contents.size());
        assertEquals(order_b[0], contents.get(0));

        Content newContent = this.contentManager.loadContent("ART120", false);
        newContent.setId(null);
        try {
            this.contentManager.insertOnLineContent(newContent);
            super.waitNotifyingThread();
            super.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
            FacetedContentsResult result3 = this.searchEngineManager.searchFacetedEntities(filters, this.extractCategoryFilters(categories_1), allowedGroup);
            contents = result3.getContentsId();
            String[] order_c = {newContent.getId(), "ART120", "ART111"};
            assertEquals(order_c.length, contents.size());
            this.verifyOrder(contents, order_c);

            newContent.addCategory(this.categoryManager.getCategory("general_cat1"));
            this.contentManager.insertOnLineContent(newContent);
            super.waitNotifyingThread();
            super.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
            FacetedContentsResult result4 = this.searchEngineManager.searchFacetedEntities(filters, this.extractCategoryFilters(categories_2), allowedGroup);
            contents = result4.getContentsId();
            String[] order_d = {newContent.getId(), "ART111"};
            assertEquals(order_d.length, contents.size());
            this.verifyOrder(contents, order_d);
        } catch (Throwable t) {
            throw t;
        } finally {
            this.contentManager.deleteContent(newContent);
            assertNull(this.contentManager.loadContent(newContent.getId(), false));
        }
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

    private void verifyOrder(List<String> contents, String[] order) {
        for (int i = 0; i < contents.size(); i++) {
            assertEquals(order[i], contents.get(i));
        }
    }

    public void testSearchContentsByRole_1() throws Throwable {
        Thread thread = this.searchEngineManager.startReloadContentsReferences();
        thread.join();
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        SearchEngineFilter[] categoriesFilters = {};
        SearchEngineFilter filterByRole
                = new SearchEngineFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, true);
        filterByRole.setOrder(FieldSearchFilter.Order.DESC);
        SearchEngineFilter filteryByType
                = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", SearchEngineFilter.TextSearchOption.EXACT);
        SearchEngineFilter[] filters = {filterByRole, filteryByType};
        FacetedContentsResult result1 = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, allowedGroup);
        Map<String, Integer> occurrence1 = result1.getOccurrences();
        List<String> contents1 = result1.getContentsId();
        filterByRole.setOrder(FieldSearchFilter.Order.ASC);

        FacetedContentsResult result2 = this.searchEngineManager.searchFacetedEntities(filters, categoriesFilters, allowedGroup);
        List<String> contents2 = result2.getContentsId();
        assertFalse(contents2.isEmpty());
        assertEquals(occurrence1, result2.getOccurrences());
        assertEquals(contents1.size(), contents2.size());
        for (int i = 0; i < contents1.size(); i++) {
            assertEquals(contents1.get(i), contents2.get(contents2.size() - 1 - i));
        }
    }

    public void testSearchContentsByRole_2() throws Exception {
        Thread thread = this.searchEngineManager.startReloadContentsReferences();
        thread.join();
        Content newContent = this.contentManager.loadContent("EVN25", false);
        newContent.setId(null);
        try {
            ITextAttribute title = (ITextAttribute) newContent.getAttribute("Titolo");
            title.setText("AAA Titolo in italiano", "it");
            title.setText("ZZZ English Title", "en");
            this.contentManager.insertOnLineContent(newContent);
            super.waitNotifyingThread();
            super.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            SearchEngineFilter[] categoriesFilters = {};
            SearchEngineFilter filterByRole
                    = new SearchEngineFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, true);
            filterByRole.setOrder(FieldSearchFilter.Order.DESC);
            filterByRole.setLangCode("it");
            SearchEngineFilter filteryByType
                    = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", SearchEngineFilter.TextSearchOption.EXACT);
            SearchEngineFilter[] filters1 = {filterByRole, filteryByType};
            FacetedContentsResult resultItSearch = this.searchEngineManager.searchFacetedEntities(filters1, categoriesFilters, allowedGroup);
            Map<String, Integer> occurrenceIt = resultItSearch.getOccurrences();
            List<String> contentsIt = resultItSearch.getContentsId();
            assertFalse(occurrenceIt.isEmpty());
            assertFalse(contentsIt.isEmpty());

            filterByRole.setLangCode("en");
            SearchEngineFilter[] filters2 = {filterByRole, filteryByType};
            FacetedContentsResult resultEnSearch = this.searchEngineManager.searchFacetedEntities(filters2, categoriesFilters, allowedGroup);
            Map<String, Integer> occurrenceEn = resultEnSearch.getOccurrences();
            List<String> contentsEn = resultEnSearch.getContentsId();
            assertFalse(contentsEn.isEmpty());
            assertEquals(occurrenceIt, occurrenceEn);
            assertEquals(contentsIt.size(), contentsEn.size());
            boolean equals = true;
            for (int i = 0; i < contentsIt.size(); i++) {
                assertTrue(contentsEn.contains(contentsIt.get(i)));
                if (!contentsIt.get(i).equals(contentsEn.get(i))) {
                    equals = false;
                    break;
                }
            }
            assertFalse(equals);
        } catch (Exception t) {
            throw t;
        } finally {
            this.contentManager.deleteContent(newContent);
            assertNull(this.contentManager.loadContent(newContent.getId(), false));
        }

    }

}
