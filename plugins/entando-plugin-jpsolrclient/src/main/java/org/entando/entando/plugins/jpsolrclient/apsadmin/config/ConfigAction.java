/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsolrclient.apsadmin.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.entando.entando.plugins.jpsolrclient.aps.system.services.config.model.SolrConfig;
import org.entando.entando.plugins.jpsolrclient.aps.system.services.content.CmsSearchEngineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author S.Loru
 */
public class ConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ConfigAction.class);

	public String intro() {
		SolrConfig solrConfiguration = new SolrConfig();
		try {
			solrConfiguration = this.getSearchEngineManager().readConfiguration();
		} catch (Throwable t) {
			_logger.error("error in intro", t);
			return FAILURE;
		}
		this.setProviderUrl(solrConfiguration.getProviderUrl());
		this.setMaxResultSize(solrConfiguration.getMaxResultSize());
		return SUCCESS;
	}

	public String save() {
		SolrConfig solrConfiguration;
		try {
			solrConfiguration = this.getSearchEngineManager().readConfiguration();
			solrConfiguration.setProviderUrl(this.getProviderUrl());
			solrConfiguration.setMaxResultSize(this.getMaxResultSize());
			this.getSearchEngineManager().writeConfiguration(solrConfiguration);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		this.addActionMessage(this.getText("note.provider.saved"));
		return SUCCESS;
	}

	public String test() {
		try {
			if (StringUtils.isNotBlank(this.getProviderUrl())) {
				SolrServer httpSolrServer = new HttpSolrServer(this.getProviderUrl());
				SolrPingResponse ping = httpSolrServer.ping();
				String time = String.valueOf(ping.getElapsedTime());
				String[] args = {time};
				this.addActionMessage(this.getText("note.provider.success", args));
			}
		} catch (SolrServerException slr) {
			_logger.error("error in test", slr);
			this.addActionError(this.getText("error.connection"));
			return SUCCESS;
		} catch (Throwable t) {
			_logger.error("error in test", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getProviderUrl() {
		return _providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this._providerUrl = providerUrl;
	}

	public CmsSearchEngineManager getSearchEngineManager() {
		return _searchEngineManager;
	}

	public void setSearchEngineManager(CmsSearchEngineManager searchEngineManager) {
		this._searchEngineManager = searchEngineManager;
	}

	public String getMaxResultSize() {
		return _maxResultSize;
	}

	public void setMaxResultSize(String maxResultSize) {
		this._maxResultSize = maxResultSize;
	}
	private String _providerUrl;
	private CmsSearchEngineManager _searchEngineManager;
	private String _maxResultSize;
}
