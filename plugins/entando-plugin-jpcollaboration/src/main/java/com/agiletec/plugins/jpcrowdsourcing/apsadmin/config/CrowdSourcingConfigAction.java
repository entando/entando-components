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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.CrowdSourcingConfig;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class CrowdSourcingConfigAction extends BaseAction implements ICrowdSourcingConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CrowdSourcingConfigAction.class);

	@Override
	public String entryConfig() {
		try {
			CrowdSourcingConfig config = this.getIdeaManager().getConfig();
			if (null == config) {
				this.addActionError(this.getText("jpcrowdsourcing.error.message.null"));
				return INPUT;
			}
			this.setModerateEntries(config.isModerateEntries());
			this.setModerateComments(config.isModerateComments());
		} catch (Throwable t) {
			_logger.error("error in entryConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String saveConfig() {
		try {
			CrowdSourcingConfig config = new CrowdSourcingConfig();
			config.setModerateEntries(null != this.getModerateEntries() && this.getModerateEntries().booleanValue() == true);
			config.setModerateComments(null != this.getModerateComments() && this.getModerateComments().booleanValue() == true);
			this.getIdeaManager().updateConfig(config);
		} catch (Throwable t) {
			_logger.error("error in saveConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}
	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}

	public void setModerateEntries(Boolean moderateEntries) {
		this._moderateEntries = moderateEntries;
	}
	public Boolean getModerateEntries() {
		return _moderateEntries;
	}

	public void setModerateComments(Boolean moderateComments) {
		this._moderateComments = moderateComments;
	}
	public Boolean getModerateComments() {
		return _moderateComments;
	}

	//private CrowdSourcingConfig _config;
	private Boolean _moderateEntries;
	private Boolean _moderateComments;
	private IIdeaManager _ideaManager;
}
