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
package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;
import java.io.File;

import org.apache.commons.io.FileUtils;

import com.agiletec.plugins.jpavatar.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import org.entando.entando.plugins.jpavatar.aps.system.JpAvatarSystemConstants;

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
