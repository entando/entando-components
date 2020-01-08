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

import static org.mockito.Mockito.when;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ITreeAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class SearchEngineManagerTest {

    @Mock
    private IContentManager contentManager;

    @Mock
    private ISearchEngineDAOFactory factory;

    @Mock
    private IIndexerDAO indexerDao;

    @Mock
    private ISearcherDAO searcherDao;

    @InjectMocks
    private SearchEngineManager searchEngineManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.factory.getIndexer()).thenReturn(indexerDao);
        when(this.factory.getSearcher()).thenReturn(searcherDao);
        this.searchEngineManager.init();
    }

    public void init() throws Exception {
        this.searchEngineManager.init();
        Mockito.verify(factory, Mockito.times(1)).getIndexer();
        Mockito.verify(factory, Mockito.times(1)).getSearcher();
    }

    @Test
    public void addContentNotify() throws Exception {
        when(this.factory.checkCurrentSubfolder()).thenReturn(false);
        Content content = Mockito.mock(Content.class);
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(PublicContentChangedEvent.INSERT_OPERATION_CODE);
        this.searchEngineManager.updateFromPublicContentChanged(event);
        Mockito.verify(indexerDao, Mockito.times(1)).add(content);
        Mockito.verify(indexerDao, Mockito.times(0)).delete(Mockito.anyString(), Mockito.anyString());
        Mockito.verifyZeroInteractions(searcherDao);
        Mockito.verify(factory, Mockito.times(1)).init();
    }

    @Test
    public void addContentNotify_withError() throws Exception {
        when(this.factory.checkCurrentSubfolder()).thenReturn(true);
        Mockito.doThrow(ApsSystemException.class).when(this.indexerDao).add(Mockito.any(Content.class));
        Content content = Mockito.mock(Content.class);
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(PublicContentChangedEvent.INSERT_OPERATION_CODE);
        this.searchEngineManager.updateFromPublicContentChanged(event);
        Mockito.verify(indexerDao, Mockito.times(1)).add(content);
        Mockito.verifyZeroInteractions(searcherDao);
        Mockito.verify(factory, Mockito.times(0)).init();
    }

    @Test
    public void updateContentNotify() throws Exception {
        when(this.factory.checkCurrentSubfolder()).thenReturn(false);
        Content content = Mockito.mock(Content.class);
        when(content.getId()).thenReturn("ART123");
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(PublicContentChangedEvent.UPDATE_OPERATION_CODE);
        this.searchEngineManager.updateFromPublicContentChanged(event);
        Mockito.verify(indexerDao, Mockito.times(1)).add(content);
        Mockito.verify(indexerDao, Mockito.times(1)).delete(IIndexerDAO.CONTENT_ID_FIELD_NAME, "ART123");
        Mockito.verifyZeroInteractions(searcherDao);
    }

    @Test
    public void updateContentNotify_withError() throws Exception {
        when(this.factory.checkCurrentSubfolder()).thenReturn(true);
        Mockito.doThrow(ApsSystemException.class).when(this.indexerDao).delete(Mockito.anyString(), Mockito.anyString());
        Content content = Mockito.mock(Content.class);
        when(content.getId()).thenReturn("ART124");
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(PublicContentChangedEvent.UPDATE_OPERATION_CODE);
        this.searchEngineManager.updateFromPublicContentChanged(event);
        Mockito.verify(indexerDao, Mockito.times(1)).delete(IIndexerDAO.CONTENT_ID_FIELD_NAME, "ART124");
        Mockito.verify(indexerDao, Mockito.times(0)).add(content);
        Mockito.verifyZeroInteractions(searcherDao);
    }

    @Test
    public void deleteContentNotify() throws Exception {
        when(this.factory.checkCurrentSubfolder()).thenReturn(false);
        Content content = Mockito.mock(Content.class);
        when(content.getId()).thenReturn("ART125");
        PublicContentChangedEvent event = new PublicContentChangedEvent();
        event.setContent(content);
        event.setOperationCode(PublicContentChangedEvent.REMOVE_OPERATION_CODE);
        this.searchEngineManager.updateFromPublicContentChanged(event);
        Mockito.verify(indexerDao, Mockito.times(0)).add(content);
        Mockito.verify(indexerDao, Mockito.times(1)).delete(IIndexerDAO.CONTENT_ID_FIELD_NAME, "ART125");
        Mockito.verifyZeroInteractions(searcherDao);
    }

    @Test
    public void addEntityTypeNotify() throws Exception {
        EntityTypesChangingEvent event = this.initEntityTypeNotify();
        event.setOperationCode(EntityTypesChangingEvent.INSERT_OPERATION_CODE);
        this.searchEngineManager.updateFromEntityTypesChanging(event);
        Assert.assertEquals(ICmsSearchEngineManager.STATUS_READY, this.searchEngineManager.getStatus());
    }

    @Test
    public void updateEntityTypeNotify() throws Exception {
        EntityTypesChangingEvent event = this.initEntityTypeNotify();
        event.setOperationCode(EntityTypesChangingEvent.UPDATE_OPERATION_CODE);
        this.searchEngineManager.updateFromEntityTypesChanging(event);
        Assert.assertEquals(ICmsSearchEngineManager.STATUS_NEED_TO_RELOAD_INDEXES, this.searchEngineManager.getStatus());
    }

    @Test
    public void removeEntityTypeNotify() throws Exception {
        EntityTypesChangingEvent event = this.initEntityTypeNotify();
        event.setOperationCode(EntityTypesChangingEvent.REMOVE_OPERATION_CODE);
        this.searchEngineManager.updateFromEntityTypesChanging(event);
        Assert.assertEquals(ICmsSearchEngineManager.STATUS_NEED_TO_RELOAD_INDEXES, this.searchEngineManager.getStatus());
    }

    private EntityTypesChangingEvent initEntityTypeNotify() {
        this.searchEngineManager.setStatus(ICmsSearchEngineManager.STATUS_READY);
        EntityTypesChangingEvent event = new EntityTypesChangingEvent();
        Content type1 = Mockito.mock(Content.class);
        Content type2 = Mockito.mock(Content.class);
        AttributeInterface attributeType1 = Mockito.mock(AttributeInterface.class);
        when(attributeType1.getIndexingType()).thenReturn(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        when(type1.getAttributeList()).thenReturn(new ArrayList<>(Arrays.asList(attributeType1)));
        AttributeInterface attributeType2 = Mockito.mock(AttributeInterface.class);
        when(attributeType2.getIndexingType()).thenReturn(null);
        when(type2.getAttributeList()).thenReturn(new ArrayList<>(Arrays.asList(attributeType2)));
        event.setOldEntityType(type1);
        event.setNewEntityType(type2);
        event.setEntityManagerName(JacmsSystemConstants.CONTENT_MANAGER);
        when(((IManager) this.contentManager).getName()).thenReturn(JacmsSystemConstants.CONTENT_MANAGER);
        return event;
    }

    @Test
    public void testSearchIds() throws Exception {
        when(this.searcherDao.searchContentsId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>(Arrays.asList("Art123", "Art456")));
        when(this.factory.checkCurrentSubfolder()).thenReturn(Boolean.TRUE);
        List<String> resources = this.searchEngineManager.searchEntityId("it", "test", Arrays.asList("group1", "group2"));
        Assert.assertEquals(2, resources.size());
    }

    @Test(expected = ApsSystemException.class)
    public void testSearchIds_withErrors() throws Exception {
        Mockito.doThrow(ApsSystemException.class).when(this.searcherDao).searchContentsId(Mockito.any(), Mockito.any(), Mockito.any());
        when(this.factory.checkCurrentSubfolder()).thenReturn(Boolean.TRUE);
        this.searchEngineManager.searchEntityId("it", "test", Arrays.asList("group1", "group2"));
    }

}
