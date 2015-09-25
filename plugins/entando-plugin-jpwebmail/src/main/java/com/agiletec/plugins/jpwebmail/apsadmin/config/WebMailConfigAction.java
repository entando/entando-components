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
package com.agiletec.plugins.jpwebmail.apsadmin.config;

import java.io.File;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;

public class WebMailConfigAction extends BaseAction implements IWebMailConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		String certificatePath = this.getConfig().getCertificatePath();
		if (certificatePath!=null && certificatePath.length()>0) {
			File file = new File(certificatePath);
			if (!file.exists() || !file.isDirectory()) {
				this.addFieldError("config.certificatePath", this.getText("Errors.certificatePath.notValid"));
			}
		}
	}
	
	@Override
	public String edit() {
		try {
			WebMailConfig config = this.getWebMailManager().loadConfig();
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			this.getWebMailManager().updateConfig(this.getConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public WebMailConfig getConfig() {
		return _config;
	}
	public void setConfig(WebMailConfig config) {
		this._config = config;
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private WebMailConfig _config;
	
	private IWebMailManager _webMailManager;
	
}