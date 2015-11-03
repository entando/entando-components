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
package com.agiletec.plugins.jpavatar.apsadmin.avatar;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpavatar.apsadmin.ApsAdminPluginBaseTestCase;
import com.opensymphony.xwork2.Action;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.entando.entando.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;
import org.entando.entando.plugins.jpavatar.apsadmin.avatar.AvatarAction;

public class TestAvatarAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testEdit() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		String result = this.executeEdit();
		assertEquals(Action.SUCCESS, result);
		AvatarAction action = (AvatarAction) this.getAction();
		assertEquals(56, action.getImageHeight());
		assertEquals(56, action.getImageWidth());
		assertEquals(100, action.getImageMaxSize());
		assertEquals("png,jpg", action.getImageTypes());
	}

	public void testEditForGuest() throws Throwable {
		this.initAction(NS, "edit");
		String result = this.executeAction();
		assertEquals("apslogin", result);
	}

	public void testBin() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		String result = this.executeBin();
		assertEquals(Action.SUCCESS, result);
		AvatarAction action = (AvatarAction) this.getAction();
		List<String> actionMessages = (List<String>) action.getActionMessages();
		assertEquals(1, actionMessages.size());
		assertEquals(action.getText("jpavatar.message.confirmDelete"), actionMessages.get(0));
	}
	
	public void testDelete() throws Throwable {
		this.setAvatarStyle(AvatarConfig.STYLE_LOCAL);
		
		File file = new File("target/test/entando_logo.jpg");
		this._avatarManager.saveAvatar("admin", file, "entando_logo.jpg");
		String filename = this._avatarManager.getAvatarUrl("admin");
		assertEquals("/Entando/resources/plugins/jpavatar/avatar/admin.jpg", filename);
		String result = this.executeDelete();
		assertEquals(Action.SUCCESS, result);
		filename = this._avatarManager.getAvatarUrl("admin");
		assertEquals("/Entando/resources/plugins/jpavatar/avatar-default.png", filename);
		assertNull(this._avatarManager.getAvatarResource("admin"));		
	}
	
	public void testSave_1() throws Throwable {
		String result = this.executeSave();
		assertEquals(Action.INPUT, result);
	}

	private String executeEdit() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "edit");
		return this.executeAction();
	}
	
	private String executeBin() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "bin");
		return this.executeAction();
	}

	private String executeDelete() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "delete");
		return this.executeAction();
	}

	private String executeSave() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "save");
		return this.executeAction();
	}
	
	protected void setAvatarStyle(String style) throws ApsSystemException {
		AvatarConfig config = _avatarManager.getConfig();
		config.setStyle(style);
		this._avatarManager.updateConfig(config);
	}
	
	private void init() {
		_avatarManager = (IAvatarManager) this.getService(JpAvatarSystemConstants.AVATAR_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		FileUtils.cleanDirectory(new File(this._avatarManager.getAvatarDiskFolder() + "avatar"));
		super.tearDown();
	}
	
	private IAvatarManager _avatarManager;
	private static final String NS = "/do/jpavatar/Avatar";
	
}
