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
package org.entando.entando.plugins.jpavatar.aps.system.services.avatar;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import org.entando.entando.plugins.jpavatar.aps.system.utils.MD5Util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpavatar.aps.system.services.avatar.parse.AvatarConfigDOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Puddu
 */
@Aspect
public class AvatarManager extends AbstractService implements IAvatarManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(AvatarManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	/**
	 * Load the XML configuration containing service configuration.
	 * @throws ApsSystemException
	 */
	private void loadConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpAvatarSystemConstants.CONFIG_ITEM);
			if (xml == null) {
				_logger.error("Configuration item not present {}", JpAvatarSystemConstants.CONFIG_ITEM);
				throw new ApsSystemException("Configuration item not present: " + JpAvatarSystemConstants.CONFIG_ITEM);
			}
			AvatarConfigDOM configDOM = new AvatarConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error on AvatarManager startup", t);
			throw new ApsSystemException("Error on AvatarManager startup", t);
		}
	}

	@Override
	public void updateConfig(AvatarConfig config) throws ApsSystemException {
		try {
			String xml = new AvatarConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpAvatarSystemConstants.CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating jpavatar config", t);
			throw new ApsSystemException("Error updating jpavatar config", t);
		}
	}
	
	@Override
	public String getAvatar(String username) throws ApsSystemException {
		return this.getAvatarUrl(username);
	}

	@Override
	public String getAvatarUrl(String username) throws ApsSystemException {
		String url = null;
		try {
			if (this.getConfig().getStyle().equalsIgnoreCase(AvatarConfig.STYLE_LOCAL)) {
				String avatarFileName = null;
				StringBuilder urlBuffer = new StringBuilder();
				String sep = System.getProperty("file.separator");
				urlBuffer.append(this.getConfigManager().getParam(SystemConstants.PAR_RESOURCES_ROOT_URL));
				if (!urlBuffer.toString().endsWith(sep)) {
					urlBuffer.append(sep);
				}
				urlBuffer.append("plugins").append(sep).append("jpavatar").append(sep);
				File avatarResource = this.getAvatarResource(username);
				if (null != avatarResource) {
					avatarFileName = avatarResource.getName();
					url = urlBuffer.toString() + "avatar" + sep + avatarFileName;
				}
			} else if (this.getConfig().getStyle().equalsIgnoreCase(AvatarConfig.STYLE_GRAVATAR)) {
				url = this.getGravatarUrl() + this.getGravatarHash(username);
			}
		} catch (Throwable t) {
			_logger.error("Error getting avatar for user {}", username, t);
			throw new ApsSystemException("Error getting avatar for user " + username, t);
		}
		return url;
	}
	
	@Override
	public File getAvatarResource(String username) throws ApsSystemException {
		File avatarFile = null;
		try {
			if (StringUtils.isNotBlank(username)) {
				String basePath = this.getAvatarDiskFolder() + AVATAR_SUBFOLDER;
				File dir = new File(basePath);
				String[] files = dir.list(new PrefixFileFilter(username.toLowerCase() + "."));
				if (null != files && files.length > 0) {
					File resFile = new File(basePath + File.separator + files[0]);
					if (resFile.exists()) {
						avatarFile = resFile;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting avatar resource for user {}", username, t);
			throw new ApsSystemException("Error getting avatar resource for user " + username, t);
		}
		return avatarFile;
	}
	
	public String getGravatarHash(String username) throws ApsSystemException {
		String hash = null;
		try {
			if (null == username) {
				return null;
			}
			IUserProfile profile = (IUserProfile) this.getUserProfileManager().getProfile(username);
			if (null != profile) {
				String emailAttr = profile.getMailAttributeName();
				if (null == emailAttr) {
					return null;
				}
				String email = (String) profile.getValue(emailAttr);
				if (null != email) {
					hash = MD5Util.md5Hex(email);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting gravatar hash for user {}", username, t);
			throw new ApsSystemException("Error getting gravatar hash for user " + username, t);
		}
		return hash;
	}

	@Override
	public void saveAvatar(String username, File file, String filename) throws ApsSystemException {
		try {
			String path = this.createFullDiskPath(username.toLowerCase(), filename);
			if (null == path) {
				_logger.warn("Impossible to save avatar for user " + username + " . Wrong filename: " + file.getName());
				return;
			}
			_logger.debug("Saving avatar to position: " + path);
			File destFile = new File(path);
			FileUtils.copyFile(file, destFile);
		} catch (Throwable t) {
			_logger.error("Error saving avatar for user {}", username, t);
			throw new ApsSystemException("Error saving avatar for user " + username, t);
		}
	}

	private String createFullDiskPath(String username, String filename) {
		StringBuffer diskFolder = new StringBuffer(this.getAvatarDiskFolder()).append(AVATAR_SUBFOLDER).append(System.getProperty("file.separator"));
		int point = filename.lastIndexOf(".");
		if (point < 1) {
			return null;
		}
		diskFolder.append(username).append(filename.substring(point));
		String path = diskFolder.toString();
		return path;
	}
	
	@Override
	@AfterReturning(pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void removeAvatar(Object key) throws ApsSystemException {
		String username = null;
		try {
			if (key instanceof String) {
				username = key.toString();
			} else if (key instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) key;
				username = userDetails.getUsername();
			}
			username = username.toLowerCase();
			File fileToDelete = this.getAvatarResource(username);
			if (null != fileToDelete) {
				FileUtils.forceDelete(fileToDelete);
			}
		} catch (Throwable t) {
			_logger.error("Error deleting avatar for user {}", username, t);
			throw new ApsSystemException("Error deleting avatar for user " + username, t);
		}
	}
	
	public void setAvatarDiskFolder(String avatarDiskFolder) {
		this._avatarDiskFolder = avatarDiskFolder;
	}
	
	@Override
	public String getAvatarDiskFolder() {
		try {
			if (null == this._avatarDiskFolder) {
				this._avatarDiskFolder = this.createAvatarDiskFolderPath(SystemConstants.PAR_RESOURCES_DISK_ROOT, File.separator);
				File dir = new File(this._avatarDiskFolder);
				if (!dir.exists()) {
					FileUtils.forceMkdir(dir);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating Avatar Disk Folder", t);
			throw new RuntimeException("Error creating Avatar Disk Folder", t);
		}
		return _avatarDiskFolder;
	}
	
	private String createAvatarDiskFolderPath(String baseParamName, String separator) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.getConfigManager().getParam(baseParamName));
		if (!buffer.toString().endsWith(separator)) {
			buffer.append(separator);
		}
		buffer.append("plugins").append(separator).append("jpavatar").append(separator);
		return buffer.toString();
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	protected String getGravatarUrl() {
		return _gravatarUrl;
	}
	public void setGravatarUrl(String gravatarUrl) {
		this._gravatarUrl = gravatarUrl;
	}
	
	@Override
	public AvatarConfig getConfig() {
		return _config;
	}
	public void setConfig(AvatarConfig config) {
		this._config = config;
	}
	
	private String _avatarDiskFolder;
	
	private ConfigInterface _configManager;
	private IUserProfileManager _userProfileManager;
	
	private String _gravatarUrl;
	private AvatarConfig _config;
	
	public static final String AVATAR_SUBFOLDER = "avatar";
	
}
