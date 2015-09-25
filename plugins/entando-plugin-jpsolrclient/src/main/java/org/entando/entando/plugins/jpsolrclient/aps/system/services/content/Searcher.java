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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.IIndexerDAO;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public class Searcher {
	
	public Searcher(String solrUrl) {
		this.setSolrUrl(solrUrl);
	}
	
	public List<String> extractAllContentsId() throws ApsSystemException {
    	List<String> contentsId = null;
    	try {
			SolrQuery solrQuery = new SolrQuery("id:*");
			solrQuery.setRows(10000);
			contentsId = this.executeQuery(solrQuery);
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error extracting response", t);
    	}
    	return contentsId;
    }
	
	public List<String> searchContentsId(String langCode, 
    		String word, Collection<String> allowedGroups, int maxResultSize) throws ApsSystemException {
    	List<String> contentsId = null;
    	try {
			String queryString = this.createQueryString(langCode, word, allowedGroups);
			SolrQuery solrQuery = new SolrQuery(queryString);
			solrQuery.setRows(maxResultSize);
			contentsId = this.executeQuery(solrQuery);
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error extracting response", t);
    	}
    	return contentsId;
    }
    
	private String createQueryString(String langCode, String word, Collection<String> allowedGroups) {
		StringBuilder builder = new StringBuilder();
		String[] sections = word.split(" ");
		builder.append("(");
		for (int i = 0; i < sections.length; i++) {
			String section = sections[i];
			if (i>0) {
				builder.append(" AND ");
			}
			builder.append("attr_").append(langCode).append(":'").append(section).append("'");
		}
		builder.append(")");
		if (allowedGroups == null) {
			allowedGroups = new HashSet<String>();
		}
		if (!allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
			if (!allowedGroups.contains(Group.FREE_GROUP_NAME)) {
				allowedGroups.add(Group.FREE_GROUP_NAME);
			}
			builder.append(" AND (");
			boolean isFirstGroup = true;
			Iterator<String> iterGroups = allowedGroups.iterator();
			while (iterGroups.hasNext()) {
				String group = iterGroups.next();
				if (!isFirstGroup) {
					builder.append(" OR ");
				}
				builder.append(IIndexerDAO.CONTENT_GROUP_FIELD_NAME).append("_txt:").append(group);
				isFirstGroup = false;
			}
			builder.append(")");
		}
		return builder.toString();
	}
	
	protected List<String> executeQuery(SolrQuery solrQuery) throws ApsSystemException {
    	List<String> contentsId = new ArrayList<String>();
    	try {
			HttpClient httpClient = new DefaultHttpClient();
			SolrServer solr = new HttpSolrServer(this.getSolrUrl(), httpClient, new XMLResponseParser());
			QueryResponse rsp = solr.query(solrQuery);
            SolrDocumentList solrDocumentList = rsp.getResults();
            for (SolrDocument doc : solrDocumentList) {
				String id = (String) doc.getFieldValue(CmsSearchEngineManager.CONTENT_ID_FIELD_NAME); //id is the uniqueKey field
                contentsId.add(id);
            }
    	} catch (SolrServerException e) {
			throw new ApsSystemException("Error on solr server", e);
		} catch (Throwable t) {
    		throw new ApsSystemException("Error extracting response", t);
    	}
    	return contentsId;
    }
	
	protected String getSolrUrl() {
		return _solrUrl;
	}
	protected void setSolrUrl(String solrUrl) {
		this._solrUrl = solrUrl;
	}
	
	private String _solrUrl;
	
}