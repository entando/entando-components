/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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