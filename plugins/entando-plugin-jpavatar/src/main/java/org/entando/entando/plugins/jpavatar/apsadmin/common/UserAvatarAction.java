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
package org.entando.entando.plugins.jpavatar.apsadmin.common;

import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

import java.io.File;
import java.io.FileInputStream;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class UserAvatarAction extends org.entando.entando.apsadmin.common.UserAvatarAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(UserAvatarAction.class);
	
	@Override
	public String returnAvatarStream() {
		AvatarConfig config = this.getAvatarManager().getConfig();
		String stype = config.getStyle();
		if (null == stype || AvatarConfig.STYLE_DEFAULT.equals(stype)) {
			return super.returnAvatarStream();
		} else if (AvatarConfig.STYLE_GRAVATAR.equals(stype)) {
			return super.extractGravatar();
		}
		try {
			String url = this.getAvatarManager().getAvatarUrl(this.getUsername());
			if (null == url) {
				return this.extractDefaultAvatarStream();
			}
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			this.setMimeType(mimeTypesMap.getContentType(url));
			File avatar = this.getAvatarManager().getAvatarResource(this.getUsername());
			if (null == avatar) {
				return this.extractDefaultAvatarStream();
			}
			this.setInputStream(new FileInputStream(avatar));
		} catch (Throwable t) {
			_logger.info("local avatar not available", t);
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