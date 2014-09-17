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
package org.entando.entando.plugins.jpavatar.apsadmin.common;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

import java.io.File;
import java.io.FileInputStream;

import javax.activation.MimetypesFileTypeMap;

/**
 * @author E.Santoboni
 */
public class UserAvatarAction extends org.entando.entando.apsadmin.common.UserAvatarAction {
	
	@Override
	public String returnAvatarStream() {
		AvatarConfig config = this.getAvatarManager().getConfig();
		String stype = config.getStyle();
		if (null == stype || AvatarConfig.STYLE_GRAVATAR.equals(stype)) {
			return super.returnAvatarStream();
		}
		try {
			String url = this.getAvatarManager().getAvatarUrl(this.getUsername());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			this.setMimeType(mimeTypesMap.getContentType(url));
			File avatar = this.getAvatarManager().getAvatarResource(this.getUsername());
			if (null == avatar) {
				return this.extractDefaultAvatarStream();
			}
			this.setInputStream(new FileInputStream(avatar));
		} catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "returnAvatarStream");
			return this.extractDefaultAvatarStream();
        }
		return SUCCESS;
	}
	
	@Override
	public String getMimeType() {
		return this.mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public IAvatarManager getAvatarManager() {
		return _avatarManager;
	}
	public void setAvatarManager(IAvatarManager avatarManager) {
		this._avatarManager = avatarManager;
	}
	
	private String mimeType;
	
	private IAvatarManager _avatarManager;
	
}