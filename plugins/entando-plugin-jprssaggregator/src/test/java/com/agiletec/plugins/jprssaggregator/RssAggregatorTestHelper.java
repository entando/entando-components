package com.agiletec.plugins.jprssaggregator;

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
