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
package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.agiletec.plugins.jpavatar.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;

public class TestAvatarManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	public void testSaveAvatar() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		File file = new File("target/test/entando_logo.jpg");
		String username = "admin";
		this._avatarManager.saveAvatar(username, file, "entando_logo.jpg");
		File avatarFile = this._avatarManager.getAvatarResource(username);
		assertTrue(avatarFile.exists());
		FileUtils.forceDelete(avatarFile);
		assertFalse(avatarFile.exists());
	}

	public void testGetAvatar() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		String username = "admin";
		File file = new File("target/test/entando_logo.jpg");
		this._avatarManager.saveAvatar(username, file, "entando_logo.jpg");
		String filename = this._avatarManager.getAvatarUrl(username);
		assertEquals("/Entando/resources/plugins/jpavatar/avatar/admin.jpg", filename);
		

	}

	public void testDeleteAvatar() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		String username = "admin";
		File file = new File("target/test/entando_logo.jpg");
		this._avatarManager.saveAvatar(username, file, "entando_logo.jpg");
		String filename = this._avatarManager.getAvatarUrl(username);
		assertEquals("/Entando/resources/plugins/jpavatar/avatar/admin.jpg", filename);
		this._avatarManager.removeAvatar(username);
		filename = this._avatarManager.getAvatarUrl(username);
		assertEquals("/Entando/resources/plugins/jpavatar/avatar-default.png", filename);
		assertNull(this._avatarManager.getAvatarResource(username));
	}

	public void testDeleteAOPAvatar() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		User user = new User();
		String username = "jpavatarTestUser".toLowerCase();
		user.setUsername(username);
		user.setPassword(username);
		_userManager.addUser(user);
		assertNotNull(_userManager.getUser(username));
		File file = new File("target/test/entando_logo.jpg");
		this._avatarManager.saveAvatar(username, file, "entando_logo.jpg");
		String filename = this._avatarManager.getAvatarUrl(username);
		assertEquals("/Entando/resources/plugins/jpavatar/avatar/" + username +".jpg", filename);
		this._userManager.removeUser(username);
		filename = this._avatarManager.getAvatarUrl(username);
		assertEquals("/Entando/resources/plugins/jpavatar/avatar-default.png", filename);
		assertNull(this._avatarManager.getAvatarResource(username));	
	}

	protected void setAvatarStyle(String style) throws ApsSystemException {
		AvatarConfig config = _avatarManager.getConfig();
		config.setStyle(style);
		this._avatarManager.updateConfig(config);
	}
	
	private void init() {
		_avatarManager = (IAvatarManager) this.getService(JpAvatarSystemConstants.AVATAR_MANAGER);
		_userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
	}

	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		FileUtils.cleanDirectory(new File(this._avatarManager.getAvatarDiskFolder() + "avatar"));
		this._userManager.removeUser("jpavatarTestUser");
	}

	private IAvatarManager _avatarManager;
	private IUserManager _userManager;

}
