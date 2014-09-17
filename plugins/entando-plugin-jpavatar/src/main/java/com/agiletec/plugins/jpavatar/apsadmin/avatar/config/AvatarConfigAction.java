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

package com.agiletec.plugins.jpavatar.apsadmin.avatar.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

public class AvatarConfigAction extends BaseAction {

	public String edit() {
		try {
			AvatarConfig config = this.getAvatarManager().getConfig();
			this.setAvatarConfig(config);

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
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
			ApsSystemUtils.logThrowable(t, this, "save");
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
