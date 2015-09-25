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

import java.io.File;
import java.io.FileInputStream;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;
import com.agiletec.plugins.jpavatar.aps.system.utils.ImageInfo;

public class AvatarAction extends BaseAction implements IAvatarAction {

	@Override
	public void validate() {
		super.validate();
		this.checkAvatar();
	}
	
	private void checkAvatar() {
		File avatar = this.getAvatar();
		if (null != avatar && this.getAvatarFileName().trim().length() > 0) {
			String fileName = this.getAvatarFileName();
			String docType = fileName.substring(fileName.lastIndexOf('.')+1).trim();
			String[] types = this.getImageTypes().split(",");
			if (!isValidType(docType, types)) {
				String[] args = new String[1];
				args[0] = this.getImageTypes();
				this.addFieldError("Avatar", this.getText("jpavatar.avatar.invalidType",  args));
			}
			if (avatar.length() > this.getImageMaxSize() * 1024) {
				String[] args = new String [2];
				args[0] = new Long(avatar.length()).toString();
				args[1] = new Long(this.getImageMaxSize() * 1024).toString();
				this.addFieldError("Avatar", this.getText("jpavatar.avatar.sizeTooBig", args));
			}
			ImageInfo imageInfo = new ImageInfo();
			try {
				FileInputStream fis = new FileInputStream(this.getAvatar());
				imageInfo.setInput(fis);
				if (imageInfo.check()) {
					int width = imageInfo.getWidth();
					int heigth = imageInfo.getHeight();
					if (width != this.getImageWidth() || heigth != this.getImageHeight()) {
						String[] args = new String[4];
						args[0] = new Integer(this.getImageWidth()).toString();
						args[1] = new Integer(this.getImageHeight()).toString();
						args[2] = new Integer(width).toString();
						args[3] = new Integer(heigth).toString();
						this.addFieldError("Avatar", this.getText("jpavatar.avatar.wrongSize", args));
					}
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "checkAvatar");
				throw new RuntimeException("error in avatar validation for user " + this.getCurrentUser().getUsername(), t);
			}
		}
	}
	
	private boolean isValidType(String docType, String[] rightTypes) {
		boolean isValid = false;
		if (rightTypes.length > 0) {
			for (int i = 0; i < rightTypes.length; i++) {
				if (docType.toLowerCase().equals(rightTypes[i].toLowerCase())) {
					isValid = true;
					break;
				}
			}
		} else {
			isValid = true;
		}
		return isValid;
	}

	public String getAvatarResource() {
		String avatarRes = null;
		try {
			File aFile = this.getAvatarManager().getAvatarResource(this.getCurrentUser().getUsername());
			if (null != aFile) {
				avatarRes = aFile.getName();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatarResource");
			throw new RuntimeException("error in getAvatarRecource for " + this.getCurrentUser().getUsername(), t);
		}
		return avatarRes;
	}
	
	@Override
	public String edit() {
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			if (null != this.getAvatar()) {
				this.getAvatarManager().saveAvatar(this.getCurrentUser().getUsername(), this.getAvatar(), this.getAvatarFileName());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String bin() {
		try {
			this.addActionMessage(this.getText("jpavatar.message.confirmDelete"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "bin");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			this.getAvatarManager().removeAvatar(this.getCurrentUser().getUsername());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}

	public void setImageTypes(String imageTypes) {
		this._imageTypes = imageTypes.toLowerCase();
	}
	public String getImageTypes() {
		return _imageTypes;
	}

	public void setImageMaxSize(int imageMaxSize) {
		this._imageMaxSize = imageMaxSize;
	}
	public int getImageMaxSize() {
		return _imageMaxSize;
	}
	
	public void setImageWidth(int imageWidth) {
		this._imageWidth = imageWidth;
	}
	public int getImageWidth() {
		return _imageWidth;
	}
	
	public void setImageHeight(int imageHeight) {
		this._imageHeight = imageHeight;
	}
	public int getImageHeight() {
		return _imageHeight;
	}

	public void setAvatar(File avatar) {
		this._avatar = avatar;
	}
	public File getAvatar() {
		return _avatar;
	}

	public void setAvatarContentType(String avatarContentType) {
		this._avatarContentType = avatarContentType;
	}
	public String getAvatarContentType() {
		return _avatarContentType;
	}

	public void setAvatarFileName(String avatarFileName) {
		this._avatarFileName = avatarFileName;
	}
	public String getAvatarFileName() {
		return _avatarFileName;
	}

	public void setAvatarManager(IAvatarManager avatarManager) {
		this._avatarManager = avatarManager;
	}
	protected IAvatarManager getAvatarManager() {
		return _avatarManager;
	}

	private File _avatar;
	private String _avatarContentType;
	private String _avatarFileName;
	private String _imageTypes;
	private int _imageMaxSize;
	private int _imageWidth;
	private int _imageHeight;
	private IAvatarManager _avatarManager;
	
}
