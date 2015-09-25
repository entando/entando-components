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
