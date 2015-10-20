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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.registration;

import java.util.Collection;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

import java.util.List;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Action to manage User Account Registration Requests
 * @author S.Puddu, E.Mezzano, G.Cocco
 */
public class UserRegistrationAction extends AbstractApsEntityAction {
	
	@Override
	public void validate() {
		try {
			if (this.getUserProfile()!=null) {
				super.validate();
				this.getUserRegManager().clearOldAccountRequests();// FIXME Verificare se è il caso di richiamarlo qui
				if (this.existsUser(this.getUsername())) {
					this.addFieldError("username", this.getText("jpuserreg.error.duplicateUser"));
				}
				if (!this.isPrivacyPolicyAgreement()) {
					this.addFieldError("privacyPolicyAgreement", this.getText("jpuserreg.error.privacyPolicyAgreement.required"));
				}
				this.checkEmailAddress();
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error validation of request for account activation" + this.getUsername(), t);
		}
	}
	
	@Override
	public String createNew() {
		String profileTypeCode = this.getProfileTypeCode();
		try {
			boolean allowed = false;
			if (profileTypeCode != null) {
				IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfileType(profileTypeCode);
				if (userProfile != null) {
					if (userProfile.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL) == null) {// Verifica che contenga l'attributo della mail
						ApsSystemUtils.getLogger().warn("Registration attempt with profile " + profileTypeCode + " missing email address");
					} else {
						userProfile.disableAttributes(JpUserRegSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_REGISTRATION);
						this.setUserProfile(userProfile);
						this.checkTypeLabels(userProfile);
						allowed = true;
					}
				}
			}
			if (!allowed) {
				this.addFieldError("profileTypeCode", this.getText("jpuserreg.error.profileType.required"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void checkTypeLabels(IUserProfile userProfile) {
		if (null == userProfile) {
			return;
		}
		try {
			List<AttributeInterface> attributes = userProfile.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				String attributeLabelKey = "userprofile_" + userProfile.getTypeCode() + "_" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					String attributeDescription = attribute.getDescription();
					String value = (null != attributeDescription && attributeDescription.trim().length() > 0) ?
							attributeDescription :
							attribute.getName();
					this.addLabelGroups(attributeLabelKey, value);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTypeLables");
			throw new RuntimeException("Error checking label types", t);
		}
	}

	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}

	@Override
	public String edit() {
		this.addActionError(this.getText("jpuserreg.error.operation.unsupported"));
		return FAILURE;
	}

	@Override
	public String view() {
		this.addActionError(this.getText("jpuserreg.error.operation.unsupported"));
		return FAILURE;
	}
	
	/**
	 * It Adds user account and profile in to the system,
	 * keeping disabled status until the end of registration process
	 * @return The result code
	 */
	@Override
	public String save() {
		try {
			IUserProfile userProfile = this.getUserProfile();
			if (userProfile!=null) {
				userProfile.setId(this.getUsername().trim());
				this._userRegManager.regAccount(userProfile);
				this.setUserProfile(null);
			} else {
				return "expired";
			}
		} catch (Throwable t) {
			this.addActionError(this.getText("jpuserreg.error.genericError"));
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Extract the typeCode from the current showlet.
	 * @return The type code extracted from the showlet.
	 */
	protected String extractTypeCode() {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		if (reqCtx != null) {
			Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (showlet != null) {
				ApsProperties config = showlet.getConfig();
				if (null != config) {
					String showletTypeCode = config.getProperty(JpUserRegSystemConstants.TYPECODE_SHOWLET_PARAM);
					if (showletTypeCode!=null && showletTypeCode.trim().length()>0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}
	
	/**
	 * check if user exist
	 * @param username
	 * @return true if exist a user with this username, false if user not exist.
	 * @throws Throwable In error case.
	 */
	protected boolean existsUser(String username) throws Throwable {
		boolean exists = (username!=null && username.trim().length()>=0 && this.getUserManager().getUser(username.trim())!=null);
		return exists;
	}

	private void checkEmailAddress() throws ApsSystemException {
		String emailAttrName = this.getEmailAttrName();
		String email = (String) this.getUserProfile().getValue(emailAttrName);
		if (null != email) {
			if (!email.equals(this.getEmailConfirm())) {
				this.addFieldError("emailConfirm", this.getText("jpuserreg.error.email.wrongConfirm"));
			} else if(this.verifyEmailAlreadyInUse(email)){
				this.addFieldError(emailAttrName, this.getText("jpuserreg.error.email.alreadyInUse"));
			}
		}
	}
	
	private boolean verifyEmailAlreadyInUse(String email) throws ApsSystemException {
		try {
			Collection<String> usernames = this.getUserRegManager().getUsernamesByEmail(email);
			if (usernames.size()>0) {
				return true;
			}
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "verifyEmailAlreadyInUse");
			throw e;
		}
		return false;
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return (IApsEntity) this.getRequest().getSession().getAttribute(SESSION_PARAM_NAME_REQ_PROFILE);
	}

	public IUserProfile getUserProfile() {
		return (IUserProfile) this.getRequest().getSession().getAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE);
	}

	protected void setUserProfile(IUserProfile userProfile) {
		if (userProfile!=null) {
			this.getRequest().getSession().setAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE, userProfile);
		} else {
			this.getRequest().getSession().removeAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE);
		}
	}

	public String getEmailAttrName() {
		if (this._emailAttrName==null) {
			AttributeInterface attribute = this.getUserProfile().getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
			if (attribute!=null) {
				this._emailAttrName = attribute.getName();
			}
		}
		return this._emailAttrName;
	}

	public String getProfileTypeCode() {
		if (null==this._profileTypeCode) {
			this._profileTypeCode = this.extractTypeCode();
			if (this._profileTypeCode==null) {
				this._profileTypeCode = SystemConstants.DEFAULT_PROFILE_TYPE_CODE;
			}
		}
		return _profileTypeCode;
	}

	public void setProfileTypeCode(String profileTypeCode) {
		String showletTypeCode = this.extractTypeCode();
		this._profileTypeCode = (null==showletTypeCode) ? profileTypeCode : showletTypeCode;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}

	public void setEmailConfirm(String emailConfirm) {
		this._emailConfirm = emailConfirm;
	}
	public String getEmailConfirm() {
		return _emailConfirm;
	}

	public void setPrivacyPolicyAgreement(boolean privacyPolicyAgreement) {
		this._privacyPolicyAgreement = privacyPolicyAgreement;
	}
	public boolean isPrivacyPolicyAgreement() {
		return _privacyPolicyAgreement;
	}

	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}
	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}

	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	private String _emailAttrName;
	private String _profileTypeCode;
	private String _username;
	private String _emailConfirm;
	private boolean _privacyPolicyAgreement = false;

	private IUserRegManager _userRegManager;
	private IUserManager _userManager;
	private IUserProfileManager _userProfileManager;
	private II18nManager _i18nManager;
	
	public static final String SESSION_PARAM_NAME_REQ_PROFILE = "jpuserreg_userRegSessionParam";

}