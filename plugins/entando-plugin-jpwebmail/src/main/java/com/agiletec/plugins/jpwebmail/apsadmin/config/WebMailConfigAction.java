/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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