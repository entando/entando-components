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
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.*;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.ResourceAttributeInterface;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 */
public class IndexerDAO implements IIndexerDAO {

	private static final Logger _logger = LoggerFactory.getLogger(IndexerDAO.class);

	private Directory dir;
    
	private ILangManager langManager;
    
    private ICategoryManager categoryManager;

	/**
	 * Inizializzazione dell'indicizzatore.
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
	
    /**
     * Crea un oggetto Document pronto per l'indicizzazione da un oggetto Content.
     * @param entity Il contenuto dal quale ricavare il Document.
     * @return L'oggetto Document ricavato dal contenuto.
     * @throws ApsSystemException In caso di errore
     */
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
        try {
        	EntityAttributeIterator attributesIter = new EntityAttributeIterator(entity);
        	while (attributesIter.hasNext()) {
                AttributeInterface currentAttribute = (AttributeInterface) attributesIter.next();
                List<Lang> langs = this.getLangManager().getLangs();
            	for (int i=0; i<langs.size(); i++) {
            		Lang currentLang = (Lang) langs.get(i);
            		this.indexAttribute(document, currentAttribute, currentLang);
            	}
            }
			List<Category> categories = entity.getCategories();
			if (null != categories && !categories.isEmpty()) {
				for (int i = 0; i < categories.size(); i++) {
					Category category = categories.get(i);
					this.indexCategory(document, category);
				}
			}
        } catch (Throwable t) {
			_logger.error("Error creating document", t);
            throw new ApsSystemException("Error creating document", t);
        }
        return document;
    }
	
	private void indexCategory(Document document, Category categoryToIndex) {
		if (null == categoryToIndex || categoryToIndex.isRoot()) {
			return;
		}
		document.add(new StringField(CONTENT_CATEGORY_FIELD_NAME, 
				categoryToIndex.getPath(CONTENT_CATEGORY_SEPARATOR, false, this.getCategoryManager()), Field.Store.YES));
        Category parentCategory = this.getCategoryManager().getCategory(categoryToIndex.getParentCode());
		this.indexCategory(document, parentCategory);
	}
	
    private void indexAttribute(Document document,
            AttributeInterface attribute, Lang lang) throws ApsSystemException {
    	attribute.setRenderingLang(lang.getCode());
        if (attribute instanceof IndexableAttributeInterface) {
            String valueToIndex = ((IndexableAttributeInterface) attribute).getIndexeableFieldValue();
            String indexingType = attribute.getIndexingType();
			String fieldName = lang.getCode();
			if (attribute instanceof ResourceAttributeInterface) {
				fieldName += IIndexerDAO.ATTACHMENT_FIELD_SUFFIX;
			}
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_UNSTORED.equalsIgnoreCase(indexingType)) {
            	document.add(new TextField(fieldName, valueToIndex, Field.Store.NO));
            }
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) {
            	document.add(new TextField(fieldName, valueToIndex, Field.Store.YES));
            }
        }
		if (attribute.isSearchable()) {
			List<Lang> langs = new ArrayList<Lang>();
			langs.add(lang);
			AttributeTracer tracer = new AttributeTracer();
			tracer.setLang(lang);
			String name = tracer.getFormFieldName(attribute);
			List<AttributeSearchInfo> searchInfos = attribute.getSearchInfos(langs);
			if (null != searchInfos) {
				for (int i = 0; i < searchInfos.size(); i++) {
					AttributeSearchInfo info = searchInfos.get(i);
					Field field = null;
					if (null != info.getDate()) {
						field = new TextField(name, 
								DateTools.timeToString(info.getDate().getTime(), DateTools.Resolution.MINUTE), Field.Store.YES);
					} else if (null != info.getBigDecimal()) {
						int value = info.getBigDecimal().intValue();
						field = new IntPoint(name, value);

						//For int points a separate stored field must be added for future indexed search
						StoredField stored = new StoredField(name, value);
						document.add(stored);
					} else {
						field = new TextField(name, info.getString(), Field.Store.YES);
					}
					document.add(field);
				}
			}
		}
    }
	
    /**
     * Cancella un documento.
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

    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }
    @Override
    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}