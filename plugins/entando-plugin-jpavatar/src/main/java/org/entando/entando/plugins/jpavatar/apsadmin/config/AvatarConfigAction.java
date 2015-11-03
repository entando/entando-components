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
package org.entando.entando.plugins.jpavatar.apsadmin.config;

import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvatarConfigAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(AvatarConfigAction.class);
	
	public String edit() {
		try {
			AvatarConfig config = this.getAvatarManager().getConfig();
			this.setAvatarConfig(config);
		} catch (Throwable t) {
			_logger.info("Error editing configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			AvatarConfig config = this.getAvatarConfig();
			this.getAvatarManager().updateConfig(config);
			this.addActionMessage(this.getText("message.config.savedConfirm"));
		} catch (Throwable t) {
			_logger.info("Error saving configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public AvatarConfig getAvatarConfig() {
		return _avatarConfig;
	}
	public void setAvatarConfig(AvatarConfig avatarConfig) {
		this._avatarConfig = avatarConfig;
	}
	
	protected IAvatarManager getAvatarManager() {
		return _avatarManager;
	}
	public void setAvatarManager(IAvatarManager avatarManager) {
		this._avatarManager = avatarManager;
	}
	
	private AvatarConfig _avatarConfig;
	private IAvatarManager _avatarManager;
	
}
