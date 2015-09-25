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
package com.agiletec.plugins.jpavatar.apsadmin.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

public class AvatarTag extends TagSupport {

	public int doEndTag() throws JspException {
		try {
			IAvatarManager avatarManager = (IAvatarManager) ApsWebApplicationUtils.getBean(JpAvatarSystemConstants.AVATAR_MANAGER, pageContext);
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);

			boolean isCurrentUserGuest = null == currentUser || currentUser.getUsername().trim().length() == 0 || currentUser.getUsername().equalsIgnoreCase(SystemConstants.GUEST_USER_NAME);
			if (StringUtils.isBlank(this.getUsername()) && isCurrentUserGuest) {
				this.doOut(this.getNullAvatar(avatarManager));
			} else {
				String username = this.getUsername();
				if (StringUtils.isBlank(username)) {
					username = currentUser.getUsername();
				}
				String avatarName = avatarManager.getAvatarUrl(username);
				if (null != avatarName) {
					this.doOut(avatarName);
				} else {
					this.doOut(this.getNullAvatar(avatarManager));
				}
			}
			if (StringUtils.isNotBlank(this.getAvatarStyleVar())) {
				this.pageContext.getRequest().setAttribute(this.getAvatarStyleVar(), avatarManager.getConfig().getStyle());
			}
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "doEndTag");
			throw new JspException("Error in AvatarTag", e);
		}
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		this.setReturnDefaultAvatar(null);
		this.setVar(null);
		this.setUsername(null);
		this.setAvatarStyleVar(null);
	}

	/**
	 * String returned when no avatar is found
	 * @param avatarManager
	 * @return
	 * @throws ApsSystemException 
	 */
	private String getNullAvatar(IAvatarManager avatarManager) throws ApsSystemException {
		String nullAvatar = null;
		if (null != this.getReturnDefaultAvatar() && this.getReturnDefaultAvatar().equalsIgnoreCase("true")) {
			nullAvatar = avatarManager.getAvatarUrl(null);// + JpAvatarSystemConstants.DEFAULT_AVATAR_NAME;
		}
		return nullAvatar;
	}

	private void doOut(String value) throws Throwable {
		if (null != this.getVar() && this.getVar().trim().length() > 0) {
			this.pageContext.getRequest().setAttribute(this.getVar(), value);
		} else {
			pageContext.getOut().print(value);
		}
	}

	public void setReturnDefaultAvatar(String returnDefaultAvatar) {
		this._returnDefaultAvatar = returnDefaultAvatar;
	}
	public String getReturnDefaultAvatar() {
		return _returnDefaultAvatar;
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}

	public String getAvatarStyleVar() {
		return _avatarStyleVar;
	}
	public void setAvatarStyleVar(String avatarStyleVar) {
		this._avatarStyleVar = avatarStyleVar;
	}

	private String _returnDefaultAvatar;
	private String _var;
	private String _username;
	private String _avatarStyleVar;
}
