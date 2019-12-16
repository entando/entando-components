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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AttachAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;

/**
 * Test del servizio detentore delle operazioni sul motore di ricerca.
 *
 * @author E.Santoboni
 */
public class SearchEngineManagerIntegrationTest extends BaseTestCase {

    private IContentManager contentManager = null;
    private IResourceManager resourceManager = null;
    private ICmsSearchEngineManager searchEngineManager = null;
    private ICategoryManager categoryManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    @Override
    protected void tearDown() throws Exception {
        this.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
        super.tearDown();
    }
    
    public void testSearchAllContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            Set<String> allowedGroup = new HashSet<>();
            SearchEngineFilter[] filters = {};
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            List<String> freeContentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(freeContentsId);
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            List<String> allContentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(allContentsId);
            assertTrue(allContentsId.size() > freeContentsId.size());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testSearchContentsId_1() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            Content content_1 = this.createContent_1();
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);

            Content content_2 = this.createContent_2();
            this.searchEngineManager.deleteIndexedEntity(content_2.getId());
            this.searchEngineManager.addEntityToIndex(content_2);

            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", null);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
            contentsId = this.searchEngineManager.searchEntityId("en", "Petersburg wonderful", null);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
            contentsId = this.searchEngineManager.searchEntityId("en", "meravigliosa", null);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testSearchContentsId_2() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();

            Set<String> allowedGroup = new HashSet<>();
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "Corpo coach", allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains("ART104"));

            allowedGroup.add("coach");
            contentsId = this.searchEngineManager.searchEntityId("it", "testo coach", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("ART104"));//coach content

            contentsId = this.searchEngineManager.searchEntityId("it", "Titolo Evento 4", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("EVN194"));//free content

            Set<String> allowedGroup2 = new HashSet<>();
            allowedGroup2.add(Group.ADMINS_GROUP_NAME);
            contentsId = this.searchEngineManager.searchEntityId("it", "testo coach", allowedGroup2);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("ART104"));//coach content

        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchContentsId_3() throws Throwable {
        try {
            Content content_1 = this.createContent_1();
            content_1.setMainGroup(Group.ADMINS_GROUP_NAME);
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);

            Content content_2 = this.createContent_2();
            this.searchEngineManager.deleteIndexedEntity(content_2.getId());
            this.searchEngineManager.addEntityToIndex(content_2);

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains(content_1.getId()));
            allowedGroup.add("secondaryGroup");
            contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchContentsId_4() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            SearchEngineFilter filterByType = new SearchEngineFilter(IIndexerDAO.CONTENT_TYPE_FIELD_NAME, "ART");
            SearchEngineFilter[] filters = {filterByType};
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(contentsId);
            String[] expected1 = {"ART180", "ART1", "ART187", "ART121"};
            this.verify(contentsId, expected1);
            Category cat1 = this.categoryManager.getCategory("cat1");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(cat1);
            contentsId = sem.searchEntityId(filters, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected2 = {"ART180"};
            this.verify(contentsId, expected2);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchContentsId_5() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general_cat2 = this.categoryManager.getCategory("general_cat2");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general_cat2);
            List<String> allowedGroup = new ArrayList<String>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.isEmpty());
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            contentsId = sem.searchEntityId(null, categories, allowedGroup);
            String[] expected1 = {"ART111", "ART120", "EVN25"};
            this.verify(contentsId, expected1);
            Category general_cat1 = this.categoryManager.getCategory("general_cat1");
            categories.add(general_cat1);
            contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected2 = {"ART111", "EVN25"};
            this.verify(contentsId, expected2);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchContentsId_6() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general = this.categoryManager.getCategory("general");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected1 = {"ART122", "ART102", "ART111", "ART120", "EVN23", "EVN25"};
            this.verify(contentsId, expected1);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testSearchContentsId_7() throws Throwable {
        try {
            Content content_1 = this.createContent_1();
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);
            this.createContentsForTest();
            //San Pietroburgo è una città meravigliosa W3C-WAI
            //100
            //Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.
            //101
            //La vita è una cosa meravigliosa
            //103
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            SearchEngineFilter filter1 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1.setFullTextSearch(true);
            SearchEngineFilter[] filters1 = {filter1};
            List<String> contentsId = sem.searchEntityId(filters1, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains(content_1.getId()));
            
            SearchEngineFilter filter1_2 = new SearchEngineFilter("en", "San meravigliosa", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1_2.setFullTextSearch(true);
            SearchEngineFilter[] filters1_2 = {filter1_2};
            contentsId = sem.searchEntityId(filters1_2, null, allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.isEmpty());

            SearchEngineFilter filter2 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD);
            filter2.setFullTextSearch(true);
            SearchEngineFilter[] filters2 = {filter2};
            contentsId = sem.searchEntityId(filters2, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains("101"));
            assertTrue(contentsId.contains("103"));

            SearchEngineFilter filter3 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.EXACT);
            filter3.setFullTextSearch(true);
            SearchEngineFilter[] filters3 = {filter3};
            contentsId = sem.searchEntityId(filters3, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(0, contentsId.size());

            SearchEngineFilter filter4 = new SearchEngineFilter("it", "una cosa meravigliosa", SearchEngineFilter.TextSearchOption.EXACT);
            filter4.setFullTextSearch(true);
            SearchEngineFilter[] filters4 = {filter4};
            contentsId = sem.searchEntityId(filters4, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains("103"));
        } catch (Throwable t) {
            throw t;
        }
    }
    public void testSearchContentsId_7_like() throws Throwable {
        try {
            this.createContentsForTest();
            //San Pietroburgo è una città meravigliosa W3C-WAI
            //Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.
            //La vita è una cosa meravigliosa
            
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            SearchEngineFilter filter1 = new SearchEngineFilter("Articolo", true, "San viglios", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1.setLangCode("it");
            filter1.setLikeOption(true);
            SearchEngineFilter[] filters1 = {filter1};
            List<String> contentsId = sem.searchEntityId(filters1, null, allowedGroup);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains("101"));

            SearchEngineFilter filter2 = new SearchEngineFilter("Articolo", true, "San ravigl", SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD);
            filter2.setLangCode("it");
            filter2.setLikeOption(true);
            SearchEngineFilter[] filters2 = {filter2};
            contentsId = sem.searchEntityId(filters2, null, allowedGroup);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains("101"));
            assertTrue(contentsId.contains("103"));
            
            SearchEngineFilter filter3 = new SearchEngineFilter("Articolo", true, "meravig*");
            filter3.setLangCode("it");
            SearchEngineFilter[] filters3 = {filter3};
            contentsId = sem.searchEntityId(filters3, null, allowedGroup);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains("101"));
            assertTrue(contentsId.contains("103"));
            
            SearchEngineFilter filter4 = new SearchEngineFilter("en", "Accompany*");
            filter4.setFullTextSearch(true);
            SearchEngineFilter[] filters4 = {filter4};
            contentsId = sem.searchEntityId(filters4, null, allowedGroup);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains("102"));
            
            SearchEngineFilter filter5 = new SearchEngineFilter("en", "Accompany");
            filter5.setFullTextSearch(true);
            SearchEngineFilter[] filters5 = {filter5};
            contentsId = sem.searchEntityId(filters5, null, allowedGroup);
            assertTrue(contentsId.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testFacetedAllContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Set<String> allowedGroup = new HashSet<>();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            SearchEngineFilter[] filters = {};
            List<ITreeNode> categories = null;
            FacetedContentsResult result = sem.searchFacetedEntities(filters, categories, allowedGroup);
            assertNotNull(result);
            assertNotNull(result.getContentsId());
            assertNotNull(result.getOccurrences());
            assertTrue(result.getContentsId().size() > 0);
            assertTrue(result.getOccurrences().size() > 0);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchFacetedContents_1() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general = this.categoryManager.getCategory("general");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            SearchEngineFilter[] filters = {};
            FacetedContentsResult result = sem.searchFacetedEntities(filters, categories, allowedGroup);
            assertNotNull(result);
            String[] expected1 = {"ART122", "ART102", "ART111", "ART120", "EVN23", "EVN25"};
            this.verify(result.getContentsId(), expected1);
            assertEquals(4, result.getOccurrences().size());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    private void verify(List<String> contentsId, String[] array) {
        assertEquals(array.length, contentsId.size());
        for (int i = 0; i < array.length; i++) {
            assertTrue(contentsId.contains(array[i]));
        }
    }
    
    private void createContentsForTest() throws ApsSystemException {
        Content content_1 = this.createContent_1();
        this.searchEngineManager.deleteIndexedEntity(content_1.getId());
        this.searchEngineManager.addEntityToIndex(content_1);
        Content content_2 = this.createContent_2();
        this.searchEngineManager.deleteIndexedEntity(content_2.getId());
        this.searchEngineManager.addEntityToIndex(content_2);
        Content content_3 = this.createContent_3();
        this.searchEngineManager.deleteIndexedEntity(content_3.getId());
        this.searchEngineManager.addEntityToIndex(content_3);
    }

    private Content createContent_1() {
        Content content = new Content();
        content.setId("101");
        content.setDescription("Description content 1");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("secondaryGroup");
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("San Pietroburgo è una città meravigliosa W3C-WAI", "it");
        text.setText("St. Petersburg is a wonderful city", "en");
        content.addAttribute(text);
        Category category1 = this.categoryManager.getCategory("resCat2");
        Category category2 = this.categoryManager.getCategory("general_cat3");
        content.addCategory(category1);
        content.addCategory(category2);
        return content;
    }

    private Content createContent_2() {
        Content content = new Content();
        content.setId("102");
        content.setDescription("Description content 2");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("thirdGroup");
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.", "it");
        text.setText("Tourism had shot up more than 20 percent from 2011 to 2013, when Croatia joined the European Union. Accompanying that rise is a raft of modernized and recently built lodgings, including some three dozen hostels.", "en");
        content.addAttribute(text);
        Category category1 = this.categoryManager.getCategory("resCat1");
        Category category2 = this.categoryManager.getCategory("general_cat2");
        content.addCategory(category1);
        content.addCategory(category2);
        return content;
    }

    private Content createContent_3() {
        Content content = new Content();
        content.setId("103");
        content.setDescription("Description content 3");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("La vita è una cosa meravigliosa", "it");
        text.setText("Life is a wonderful thing", "en");
        content.addAttribute(text);
        Category category = this.categoryManager.getCategory("general_cat1");
        content.addCategory(category);
        return content;
    }
    
    public void testSearchContentByResource() throws Exception {
        Content contentForTest = this.contentManager.loadContent("ALL4", true);
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            contentForTest.setId(null);
            AttachAttribute attachAttribute = (AttachAttribute) contentForTest.getAttribute("Attach");
            ResourceInterface resource = this.resourceManager.loadResource("6");
            assertNotNull(resource);
            attachAttribute.setResource(resource, "it");
            this.contentManager.insertOnLineContent(contentForTest);
            assertNotNull(contentForTest.getId());
            super.waitNotifyingThread();
            super.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "accelerated development", allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains(contentForTest.getId()));
        } catch (Exception e) {
            throw e;
        } finally {
            Content contentToDelete = this.contentManager.loadContent(contentForTest.getId(), false);
            if (null != contentToDelete) {
                this.contentManager.removeOnLineContent(contentToDelete);
                this.contentManager.deleteContent(contentToDelete);
            }
        }
    }
    
    private void init() throws Exception {
        try {
            this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this.resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
            this.searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
            this.categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

}
