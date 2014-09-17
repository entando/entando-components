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
package com.agiletec.plugins.jpldap.apsadmin.user;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

import com.agiletec.plugins.jpldap.aps.system.services.user.ILdapUserManager;
import java.util.Date;

/**
 * @author E.Santoboni
 */
public class UserAction extends org.entando.entando.apsadmin.user.UserAction {
	
	@Override
	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			String result = this.checkUserForEdit();
			if (null != result) return result;
			String username = this.getUsername();
			UserDetails user = this.getUserManager().getUser(username);
			if (!user.isEntandoUser() && !this.isWriteUserEnable()) {
				this.addActionError(this.getText("error.user.notLocal"));
				return "userList";
			}
			this.setRemoteUser(!user.isEntandoUser());
			this.setActive(!user.isDisabled());
			this.setUser(user);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	protected String checkUserForEdit() throws Throwable {
		if (!this.existsUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		}
		if (!this.getUserManager().isWriteUserEnable() && !this.isJapsUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notLocal"));
			return "userList";
		}
		return null;
	}
	
	@Override
	protected String checkUserForDelete() throws Throwable {
		if (!this.existsUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		} else if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotDeleteAdminUser"));
			return "userList";
		} else if (this.isCurrentUser()) {
			this.addActionError(this.getText("error.user.cannotDeleteCurrentUser"));
			return "userList";
		} else if (!this.getUserManager().isWriteUserEnable() && !this.isEntandoUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotDeleteNotLocalUser"));
			return "userList";
		}
		return null;
	}
	
	@Override
	public String save() {
		AbstractUser user = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				user = new User();
				user.setUsername(this.getUsername());
				user.setPassword(this.getPassword());
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				user = (AbstractUser) this.getUserManager().getUser(this.getUsername());
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					user.setPassword(this.getPassword());
				}
			}
			if (user instanceof User) {
				((User) user).setDisabled(!this.isActive());
				if (this.isReset()) {
					((User) user).setLastAccess(new Date());
					((User) user).setLastPasswordChange(new Date());
				}
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getUserManager().addUser(user);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getUserManager().updateUser(user);
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					this.getUserManager().changePassword(this.getUsername(), this.getPassword());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	protected ILdapUserManager getUserManager() {
		return (ILdapUserManager) super.getUserManager();
	}
	
	public boolean isWriteUserEnable() {
		return this.getUserManager().isWriteUserEnable();
	}
	
	public boolean isRemoteUser() {
		return _remoteUser;
	}
	public void setRemoteUser(boolean remoteUser) {
		this._remoteUser = remoteUser;
	}
	
	private boolean _remoteUser;
	
}
