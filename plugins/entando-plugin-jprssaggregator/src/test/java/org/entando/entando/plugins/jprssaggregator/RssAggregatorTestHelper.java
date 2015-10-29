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
package org.entando.entando.plugins.jprssaggregator;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class RssAggregatorTestHelper extends AbstractDAO {

	public RssAggregatorTestHelper(ConfigInterface configManager) {
		this._configManager = configManager;
		this._config = this._configManager.getConfigItem("contentTypes");
	}
	
	public void addContentTypeToConfig() throws ApsSystemException {
		int insertPoint = this.getConfig().indexOf("</contenttypes>");
		StringBuffer sbBuffer = new StringBuffer(this.getConfig());
		sbBuffer.insert(insertPoint, RSS_TYPECODE);
		this.setModifiedConfig(sbBuffer.toString());
		this.getConfigManager().updateConfigItem("contentTypes", this.getModifiedConfig());
	}
	
	public void restoreConfig() throws ApsSystemException {
		this.getConfigManager().updateConfigItem("contenttypes", this.getConfig());
	}

	public String getModifiedConfig() {
		return _modifiedConfig;
	}
	public void setModifiedConfig(String modifiedConfig) {
		this._modifiedConfig = modifiedConfig;
	}
	
	public String getConfig() {
		return _config;
	}
	public void setConfig(String config) {
		this._config = config;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	private String _modifiedConfig;
	private String _config;
	
	private ConfigInterface _configManager;
	private static final String RSS_TYPECODE = "<contenttype typecode=\"RSS\" typedescr=\"Contenuto Rss\" viewpage=\"rssview\" listmodel=\"91\" defaultmodel=\"9\"><attributes><attribute name=\"Titolo\" attributetype=\"Text\"  searcheable=\"true\" indexingtype=\"text\" /><attribute name=\"Corpo\" attributetype=\"Hypertext\"/><attribute name=\"Link\" attributetype=\"Link\" searcheable=\"true\" indexingtype=\"text\" /><attribute name=\"Sorgente\" attributetype=\"Monotext\" required=\"true\" searcheable=\"true\" indexingtype=\"text\" /></attributes></contenttype>";
}
