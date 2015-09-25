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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.content;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * @author E.Santoboni
 */
public class Indexer {

	private static final Logger _logger =  LoggerFactory.getLogger(Indexer.class);

	public Indexer(String solrUrl, ILangManager langManager) {
		this.setSolrUrl(solrUrl);
		this.setLangManager(langManager);
	}
	
	public synchronized void delete(String id) throws ApsSystemException {
        try {
			HttpSolrServer server = new HttpSolrServer(this.getSolrUrl());
			server.deleteById(id);
			server.commit();
        } catch (Throwable e) {
            throw new ApsSystemException("Errore nella cancellazione di un indice", e);
        }
    }
	
	public void addContent(IApsEntity entity) throws ApsSystemException {
		try {
            SolrInputDocument document = this.createDocument(entity);
            this.addSolrDocument(document);
        } catch (Throwable t) {
        	_logger.error("Error adding content", t);
            throw new ApsSystemException("Error adding content", t);
        }
	}
	
    public synchronized void addSolrDocument(SolrInputDocument document) throws ApsSystemException {
        try {
			HttpSolrServer server = new HttpSolrServer(this.getSolrUrl());
            server.add(document);
            server.commit();
        } catch (Throwable t) {
        	_logger.error("Error adding document", t);
            throw new ApsSystemException("Error adding document", t);
        }
    }
	
	public SolrInputDocument createDocument(IApsEntity entity) throws ApsSystemException {
		SolrInputDocument document = new SolrInputDocument();
        document.addField(CmsSearchEngineManager.CONTENT_ID_FIELD_NAME, entity.getId());
        document.addField(CmsSearchEngineManager.CONTENT_GROUP_FIELD_NAME + "_txt", entity.getMainGroup());
        Iterator<String> iterGroups = entity.getGroups().iterator();
        while (iterGroups.hasNext()) {
        	String groupName = (String) iterGroups.next();
        	document.addField(CmsSearchEngineManager.CONTENT_GROUP_FIELD_NAME + "_txt", groupName);
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
        } catch (Throwable t) {
			throw new ApsSystemException("Error creating document", t);
        }
        return document;
    }
	
	private void indexAttribute(SolrInputDocument document, 
			AttributeInterface attribute, Lang lang) throws ApsSystemException {
    	attribute.setRenderingLang(lang.getCode());
        if (attribute instanceof IndexableAttributeInterface) {
			boolean isTitle = false;
			if (null != attribute.getRoles()) {
				List<String> roles = Arrays.asList(attribute.getRoles());
				isTitle = roles.contains(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
			}
            String valueToIndex = ((IndexableAttributeInterface) attribute).getIndexeableFieldValue();
            String indexingType = attribute.getIndexingType();
            if (null != indexingType && 
            		(IndexableAttributeInterface.INDEXING_TYPE_UNSTORED.equalsIgnoreCase(indexingType) 
					|| IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) ) {
				if (isTitle) {
					document.addField("title", valueToIndex);
				}
				document.addField("attr_" + lang.getCode(), valueToIndex);
            }
        }
    }
	
	protected String getSolrUrl() {
		return _solrUrl;
	}
	protected void setSolrUrl(String solrUrl) {
		this._solrUrl = solrUrl;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	protected void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	private String _solrUrl;
    private ILangManager _langManager;
	
}