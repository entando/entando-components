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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;

public class ContentFeedbackManager extends AbstractService implements IContentFeedbackManager {

	private static final Logger _logger = LoggerFactory.getLogger(ContentFeedbackManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfigs();
		_logger.debug("{} ready", this.getClass().getName());
	}

	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM);
			if (xml == null) {
				_logger.error("Configuration item not present: {}", JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM);
				throw new ApsSystemException("Configuration item not present: " + JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM);
			}
			this.setConfig(new ContentFeedbackConfig(xml));
		} catch (Throwable t) {
			_logger.error("Errore loading configs", t);
			throw new ApsSystemException("Error loading config", t);
		}
	}

	public void updateConfig(IContentFeedbackConfig config) throws ApsSystemException {
		try {
			String xml = config.toXML();
			this.getConfigManager().updateConfigItem(JpcontentfeedbackSystemConstants.GLOBAL_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating ContentFeedback config", t);
			throw new ApsSystemException("Error updating ContentFeedback config", t);
		}
	}

	@Override
	public boolean isCommentActive() {
		if (null == this.getConfig()) {
			_logger.error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getComment();
		return null != value && value.equalsIgnoreCase("true");
	}

	@Override
	public boolean allowAnonymousComment() {
		if (null == this.getConfig()) {
			_logger.error("ContentFeedbackConfig is null");
			return false;
		}
		if (!this.isCommentActive()) return false;
		String value = this.getConfig().getAnonymousComment();
		return null != value && value.equalsIgnoreCase("true");
	}
	
	@Override
	public boolean isCommentModerationActive() {
		if (null == this.getConfig()) {
			_logger.error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getModeratedComment();
		return null != value && value.equalsIgnoreCase("true");
	}
	
	
	@Override
	public boolean isRateContentActive() {
		if (null == this.getConfig()) {
			_logger.error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getRateContent();
		return null != value && value.equalsIgnoreCase("true");
	}

	@Override
	public boolean isRateCommentActive() {
		if (null == this.getConfig()) {
			_logger.error("ContentFeedbackConfig is null");
			return false;
		}
		String value = this.getConfig().getRateComment();
		return isCommentActive() && null != value && value.equalsIgnoreCase("true");
	}
	
	public IContentFeedbackConfig getConfig() {
		return _config;
	}
	public void setConfig(IContentFeedbackConfig config) {
		this._config = config;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	private IContentFeedbackConfig _config;
	private ConfigInterface _configManager;

}
