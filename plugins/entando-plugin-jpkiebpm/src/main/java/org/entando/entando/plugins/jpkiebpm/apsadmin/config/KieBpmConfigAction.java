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
package org.entando.entando.plugins.jpkiebpm.apsadmin.config;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;

public class KieBpmConfigAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(KieBpmConfigAction.class);

	public String edit() {
		try {
			KieBpmConfig config = this.getFormManager().getConfig().clone();
			this.configToModel(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			KieBpmConfig config = this.modelToConfig();
			this.getFormManager().updateConfig(config);
			this.addActionMessage(this.getText("message.config.savedConfirm"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected void configToModel(KieBpmConfig config) {
		this.setActive(config.getActive());
		this.setUserName(config.getUsername());
		this.setPassword(config.getPassword());
		this.setHostName(config.getHostname());
		this.setSchema(config.getSchema());
		this.setPort(config.getPort());
		this.setWebappName(config.getWebapp());
	}

	protected KieBpmConfig modelToConfig() {
		KieBpmConfig config = new KieBpmConfig();
		config.setActive(this.getActive());
		config.setUsername(this.getUserName());
		config.setPassword(this.getPassword());
		config.setHostname(this.getHostName());
		config.setSchema(this.getSchema());
		config.setPort(this.getPort());
		config.setWebapp(this.getWebappName());
		return config;
	}

	public IKieFormManager getFormManager() {
		return _formManager;
	}

	public void setFormManager(IKieFormManager formManager) {
		this._formManager = formManager;
	}

	public Boolean getActive() {
		return _active;
	}

	public void setActive(Boolean active) {
		this._active = active;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		this._userName = userName;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		this._password = password;
	}

	public String getHostName() {
		return _hostName;
	}

	public void setHostName(String hostName) {
		this._hostName = hostName;
	}

	public String getSchema() {
		return _schema;
	}

	public void setSchema(String schema) {
		this._schema = schema;
	}

	public Integer getPort() {
		return _port;
	}

	public void setPort(Integer port) {
		this._port = port;
	}

	public String getWebappName() {
		return _webappName;
	}

	public void setWebappName(String webappName) {
		this._webappName = webappName;
	}

	private IKieFormManager _formManager;

	private Boolean _active;
	private String _userName;
	private String _password;
	private String _hostName;
	private String _schema;
	private Integer _port;
	private String _webappName;

}
