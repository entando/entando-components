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

import com.agiletec.aps.system.common.entity.model.*;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.lang.*;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.slf4j.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import org.apache.lucene.util.BytesRef;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 */
public class IndexerDAO implements IIndexerDAO {

    private static final Logger _logger = LoggerFactory.getLogger(IndexerDAO.class);

    private Directory dir;

    private ILangManager langManager;

    private ITreeNodeManager treeNodeManager;

    /**
     * Inizializzazione dell'indicizzatore.
     *
     * @param dir La cartella locale contenitore dei dati persistenti.
     * @throws ApsSystemException In caso di errore
     */
    @Override
    public void init(File dir) throws ApsSystemException {
        try {
            this.dir = FSDirectory.open(dir.toPath(), SimpleFSLockFactory.INSTANCE);
        } catch (Throwable t) {
            _logger.error("Error creating directory", t);
            throw new ApsSystemException("Error creating directory", t);
        }
        _logger.debug("Indexer: search engine index ok.");
    }

    @Override
    public synchronized void add(IApsEntity entity) throws ApsSystemException {
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(this.dir, this.getIndexWriterConfig());
            Document document = this.createDocument(entity);
            writer.addDocument(document);
        } catch (Throwable t) {
            t.printStackTrace();
            _logger.error("Errore saving entity {}", entity.getId(), t);
            throw new ApsSystemException("Error saving entity", t);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    _logger.error("Error closing IndexWriter", ex);
                }
            }
        }
    }

    protected Document createDocument(IApsEntity entity) throws ApsSystemException {
        Document document = new Document();
        document.add(new StringField(CONTENT_ID_FIELD_NAME,
                entity.getId(), Field.Store.YES));
        document.add(new TextField(CONTENT_TYPE_FIELD_NAME,
                entity.getTypeCode(), Field.Store.YES));
        document.add(new StringField(CONTENT_GROUP_FIELD_NAME,
                entity.getMainGroup(), Field.Store.YES));
        Iterator<String> iterGroups = entity.getGroups().iterator();
        while (iterGroups.hasNext()) {
            String groupName = (String) iterGroups.next();
            document.add(new StringField(CONTENT_GROUP_FIELD_NAME,
                    groupName, Field.Store.YES));
        }
        if (entity instanceof Content) {
            if (null != entity.getDescription()) {
                document.add(new SortedDocValuesField(CONTENT_DESCRIPTION_FIELD_NAME, new BytesRef(entity.getDescription())));
                document.add(new TextField(CONTENT_DESCRIPTION_FIELD_NAME, entity.getDescription(), Field.Store.YES));
            }
            document.add(new TextField(CONTENT_TYPE_CODE_FIELD_NAME, entity.getTypeCode(), Field.Store.YES));
            document.add(new TextField(CONTENT_MAIN_GROUP_FIELD_NAME, entity.getMainGroup(), Field.Store.YES));
            Date creation = ((Content) entity).getCreated();
            Date lastModify = (null != ((Content) entity).getLastModified()) ? ((Content) entity).getLastModified() : creation;
            if (null != creation) {
                String value = DateTools.timeToString(creation.getTime(), DateTools.Resolution.MINUTE);
                document.add(new SortedDocValuesField(CONTENT_CREATION_FIELD_NAME, new BytesRef(value)));
            }
            if (null != lastModify) {
                String value = DateTools.timeToString(lastModify.getTime(), DateTools.Resolution.MINUTE);
                document.add(new SortedDocValuesField(CONTENT_LAST_MODIFY_FIELD_NAME, new BytesRef(value)));
            }
        }
        Iterator<AttributeInterface> iterAttribute = entity.getAttributeList().iterator();
        while (iterAttribute.hasNext()) {
            AttributeInterface currentAttribute = iterAttribute.next();
            Object value = currentAttribute.getValue();
            if (null == value) {
                continue;
            }
            List<Lang> langs = this.getLangManager().getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang currentLang = (Lang) langs.get(i);
                if (!currentAttribute.isMultilingual() && !currentLang.isDefault()) {
                    continue;
                }
                this.indexAttribute(document, currentAttribute, currentLang);
            }
        }
        List<Category> categories = entity.getCategories();
        if (null != categories && !categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                ITreeNode category = categories.get(i);
                this.indexCategory(document, category);
            }
        }
        return document;
    }

    protected void indexAttribute(Document document, AttributeInterface attribute, Lang lang) {
        attribute.setRenderingLang(lang.getCode());
        if (attribute instanceof IndexableAttributeInterface
                || ((attribute instanceof DateAttribute || attribute instanceof NumberAttribute) && attribute.isSearchable())) {
            String valueToIndex = null;
            Long number = null;
            if (attribute instanceof IndexableAttributeInterface) {
                valueToIndex = ((IndexableAttributeInterface) attribute).getIndexeableFieldValue();
            } else {
                if (attribute instanceof DateAttribute) {
                    Date date = ((DateAttribute) attribute).getDate();
                    number = (null != date) ? date.getTime() : null;
                } else {
                    BigDecimal value = ((NumberAttribute) attribute).getValue();
                    number = (null != value) ? value.longValue() : null;
                }
                valueToIndex = (null != number) ? DateTools.timeToString(number, DateTools.Resolution.MINUTE) : null;
            }
            if (null == valueToIndex) {
                return;
            }
            if (attribute instanceof IndexableAttributeInterface) {
                // full text search
                String indexingType = attribute.getIndexingType();
                if (null != indexingType
                        && IndexableAttributeInterface.INDEXING_TYPE_UNSTORED.equalsIgnoreCase(indexingType)) {
                    document.add(new TextField(lang.getCode(), valueToIndex, Field.Store.NO));
                }
                if (null != indexingType
                        && IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) {
                    document.add(new TextField(lang.getCode(), valueToIndex, Field.Store.YES));
                }
            }
            String fieldName = lang.getCode().toLowerCase() + "_" + attribute.getName();
            String sortableValue = (valueToIndex.length() > 100) ? valueToIndex.substring(0, 99) : valueToIndex;
            document.add(new SortedDocValuesField(fieldName, new BytesRef(sortableValue)));
            document.add(new TextField(fieldName, valueToIndex.toLowerCase(), Field.Store.YES));
            if (null != number) {
                document.add(new LongPoint(fieldName, number));
            }
            if (null == attribute.getRoles()) {
                return;
            }
            for (int i = 0; i < attribute.getRoles().length; i++) {
                String roleFieldName = lang.getCode().toLowerCase() + "_" + attribute.getRoles()[i];
                document.add(new SortedDocValuesField(roleFieldName, new BytesRef(sortableValue)));
                document.add(new TextField(roleFieldName, valueToIndex.toLowerCase(), Field.Store.YES));
                if (null != number) {
                    document.add(new LongPoint(roleFieldName, number));
                }
            }
        }
    }

    protected void indexCategory(Document document, ITreeNode categoryToIndex) {
        if (null == categoryToIndex || categoryToIndex.isRoot()) {
            return;
        }
        document.add(new StringField(CONTENT_CATEGORY_FIELD_NAME,
                categoryToIndex.getPath(CONTENT_CATEGORY_SEPARATOR, false, this.getTreeNodeManager()), Field.Store.YES));
        ITreeNode parentCategory = this.getTreeNodeManager().getNode(categoryToIndex.getParentCode());
        this.indexCategory(document, parentCategory);
    }

    /**
     * Cancella un documento.
     *
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale è stato indicizzato il documento.
     * @throws ApsSystemException In caso di errore
     */
    @Override
    public synchronized void delete(String name, String value) throws ApsSystemException {
        try {
            IndexWriter writer = new IndexWriter(this.dir, this.getIndexWriterConfig());
            writer.deleteDocuments(new Term(name, value));
            writer.close();
        } catch (IOException e) {
            _logger.error("Error deleting document", e);
            throw new ApsSystemException("Error deleting document", e);
        }
    }

    @Override
    public void close() {
        // nothing to do
    }

    private Analyzer getAnalyzer() {
        return new StandardAnalyzer();
    }

    private IndexWriterConfig getIndexWriterConfig() {
        return new IndexWriterConfig(this.getAnalyzer());
    }

    protected ILangManager getLangManager() {
        return langManager;
    }
    @Override
    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    public ITreeNodeManager getTreeNodeManager() {
        return treeNodeManager;
    }
    @Override
    public void setTreeNodeManager(ITreeNodeManager treeNodeManager) {
        this.treeNodeManager = treeNodeManager;
    }
    
}
