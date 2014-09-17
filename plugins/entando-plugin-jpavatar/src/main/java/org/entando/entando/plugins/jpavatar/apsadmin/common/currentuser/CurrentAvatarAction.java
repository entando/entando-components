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
package org.entando.entando.plugins.jpavatar.apsadmin.common.currentuser;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

import org.entando.entando.plugins.jpavatar.apsadmin.common.UserAvatarAction;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class CurrentAvatarAction extends UserAvatarAction {
	
	@Override
	protected IUserProfile getUserProfile() throws ApsSystemException {
		UserDetails currentUser = super.getCurrentUser();
		IUserProfile profile = (null != currentUser && null != currentUser.getProfile()) 
				? (IUserProfile) currentUser.getProfile() 
				: null;
		return profile;
	}
	
	@Override
	public String getUsername() {
		return super.getCurrentUser().getUsername();
	}
	
	/*
	@Override
	public String returnAvatarStream() {
		AvatarConfig config = this.getAvatarManager().getConfig();
		String stype = config.getStyle();
		if (null == stype || AvatarConfig.STYLE_GRAVATAR.equals(stype)) {
			return super.returnAvatarStream();
		}
		try {
			UserDetails currentUser = super.getCurrentUser();
			String url = this.getAvatarManager().getAvatarUrl(currentUser.getUsername());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			this.setMimeType(mimeTypesMap.getContentType(url));
			File avatar = this.getAvatarManager().getAvatarResource(currentUser.getUsername());
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
	*/
}