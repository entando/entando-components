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
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.INewMessageAction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.ServletActionContext;

/**
 * Implementation for action managing Message entity editing operations.
 * @author E.Santoboni - E.Mezzano
 */
public class UserNewMessageAction extends AbstractApsEntityAction implements INewMessageAction {
	
	@Override
	public void validate() {
		Message message = this.getMessage();
		if (message == null) {
			return;
		}
		super.validate();
		if (this.getRecaptchaEnabled()) {
			String remoteAddr = ServletActionContext.getRequest().getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			String privateKey = this.getConfigManager().getParam(JpwebdynamicformSystemConstants.RECAPTCHA_PRIVATEKEY_PARAM_NAME);
			reCaptcha.setPrivateKey(privateKey);
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
					this.getRecaptcha_challenge_field(), this.getRecaptcha_response_field());
			if (!reCaptchaResponse.isValid()) {
				this.addFieldError("recaptcha_response_field", this.getText("Errors.webdynamicform.captcha.notValid"));
			}
		}
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return this.getMessage();
	}
	
	@Override
	public String view() {
		//Operation not allowed
		return null;
	}
	
	@Override
	public String createNew() {
		try {
			String typeCode = this.getTypeCode();
			Message message = null;
			if (typeCode != null && typeCode.length() > 0) {
				message = this.getMessageManager().createMessageType(typeCode);
			}
			if (message == null) {
				return "voidTypeCode";
			}
			this.checkTypeLabels(message);
			if (this.getHoneypotEnabled()) {
				String honeypotCode = this.buildHoneypotCode(message);
				String sessionParamName = SESSION_PARAM_NAME_HONEYPOT + this.getTypeCode();
				this.getRequest().getSession().setAttribute(sessionParamName, honeypotCode);
			}
			this.setMessageOnSession(message);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}

	private String buildHoneypotCode(Message message) throws ApsSystemException {
		String uniqueCode = null;
		try {
			uniqueCode = message.getTypeCode() + "_text";
			if (null != message.getAttribute(uniqueCode)) {
				int index = 0;
				String currentCode = null;
				do {
					index++;
					currentCode = uniqueCode + "_" + index;
				} while (null != message.getAttribute(uniqueCode));
				uniqueCode = currentCode;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "buildHoneypotCode");
			throw new ApsSystemException("Errore creating honeypot code", t);
		}
		return uniqueCode;
	}

	protected void checkTypeLabels(Message messageType) {
		if (null == messageType) {
			return;
		}
		try {
			String typeLabelKey = "jpwebdynamicform_TITLE_" + messageType.getTypeCode();
			if (null == this.getI18nManager().getLabelGroup(typeLabelKey)) {
				this.addLabelGroups(typeLabelKey, messageType.getTypeDescr());
			}
			List<AttributeInterface> attributes = messageType.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				String attributeLabelKey = "jpwebdynamicform_" + messageType.getTypeCode() + "_" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					String attributeDescription = attribute.getDescription();
					String value = (null != attributeDescription && attributeDescription.trim().length() > 0)
							? attributeDescription
							: attribute.getName();
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
		// Operation Not Allowed
		return null;
	}

	@Override
	public String entryMessage() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "entryMessage");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
			if (this.getRecaptchaAfterEnabled()) {
				return "captchaPage";
			}
			String honeypotFieldName = null;
			String honeypotValue = null;
			if (this.getHoneypotEnabled()) {
				honeypotFieldName = this.getHoneypotParamName();
				honeypotValue = this.getRequest().getParameter(honeypotFieldName);
			}
			if (!this.getHoneypotEnabled() || (null == honeypotValue || honeypotValue.length() == 0)) {
				String username = this.getCurrentUser().getUsername();
				message.setUsername(username);
				message.setCreationDate(new Date());
				message.setLangCode(this.getCurrentLang().getCode());
				try {
					this.getMessageManager().sendMessage(message);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "save");
					this.addActionError(this.getText("Errors.webdynamicform.sendingError"));
					return INPUT;
				}
			}
			if (this.getHoneypotEnabled()) {
				String honeypotSessionParamName = SESSION_PARAM_NAME_HONEYPOT + message.getTypeCode();
				this.getRequest().getSession().removeAttribute(honeypotSessionParamName);
			}
			this.setMessageOnSession(null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Returns the current session Message.
	 * @return The current session Message.
	 */
	public Message getMessage() {
		if (this._message == null) {
			try {
				String sessionParamName = this.getSessionParamName();
				HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
				this._message = (Message) request.getSession().getAttribute(sessionParamName);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getMessage");
				throw new RuntimeException("Error finding message", t);
			}
		}
		return _message;
	}
	
	/**
	 * Sets the Message into the session.
	 * @param message The Message to set into the session.
	 */
	protected void setMessageOnSession(Message message) {
		String sessionParamName = this.getSessionParamName();
		if (null == message) {
			this.getRequest().getSession().removeAttribute(sessionParamName);
		} else {
			this.getRequest().getSession().setAttribute(sessionParamName, message);
		}
		this._message = message;
	}
	
	/**
	 * Returns the name of the session parameter containing the current Message.
	 * @return The name of the session parameter containing the current Message.
	 */
	protected String getSessionParamName() {
		String typeCode = this.getTypeCode();
		return SESSION_PARAM_NAME_CURRENT_MESSAGE + typeCode;
	}
	
	public String getHoneypotParamName() {
		String sessionParamName = SESSION_PARAM_NAME_HONEYPOT + this.getTypeCode();
		return (String) this.getRequest().getSession().getAttribute(sessionParamName);
	}
	
	/**
	 * Returns the current language from front-end context.
	 * @return The current language.
	 */
	@Override
	public Lang getCurrentLang() {
		super.getCurrentLang();
		if (null == this._currentLang) {
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			if (null != reqCtx) {
				this._currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			}
			if (null == this._currentLang) {
				this._currentLang = this.getLangManager().getDefaultLang();
			}
		}
		return this._currentLang;
	}
	
	/**
	 * Extract the typeCode from the current widget.
	 * @return The type code extracted from the widget.
	 */
	protected String extractTypeCode() {
		return this.extractWidgetConfig(JpwebdynamicformSystemConstants.TYPECODE_WIDGET_PARAM);
	}
	
	protected String extractProtectionType() {
		String protectionType = this.extractWidgetConfig(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_WIDGET_PARAM);
		if (null != protectionType) {
			List<String> protectionTypes = Arrays.asList(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES);
			if (!protectionTypes.contains(protectionType)) {
				protectionType = null;
			}
		}
		return protectionType;
	}
	
	protected String extractWidgetConfig(String paramName) {
		String value = null;
		try {
			HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			if (null != reqCtx) {
				Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
				if (null != widget) {
					ApsProperties config = widget.getConfig();
					if (null != config) {
						String widgetParam = config.getProperty(paramName);
						if (widgetParam != null && widgetParam.trim().length() > 0) {
							value = widgetParam.trim();
						}
					}
				}
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error extracting param " + paramName, t);
		}
		return value;
	}
	
	/**
	 * Returns the message type search filter.
	 * @return The message type search filter.
	 */
	@Override
	public String getTypeCode() {
		if (null == this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}
	
	/**
	 * Sets the message type search filter.
	 * @param typeCode The message type search filter.
	 */
	public void setTypeCode(String typeCode) {
		String extractedTypeCode = this.extractTypeCode();
		this._typeCode = (null == extractedTypeCode) ? typeCode : extractedTypeCode;
	}
	
	/**
	 * Returns the list of system languages.
	 * @return The list of system languages.
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}
	
	public Boolean getRecaptchaEnabled() {
		if (null == this._recaptchaEnabled) {
			this.setProtectionTypeProperties();
		}
		return _recaptchaEnabled;
	}

	public Boolean getRecaptchaAfterEnabled() {
		if (null == this._recaptchaAfterEnabled) {
			this.setProtectionTypeProperties();
		}
		return _recaptchaAfterEnabled;
	}

	public Boolean getHoneypotEnabled() {
		if (null == this._honeypotEnabled) {
			this.setProtectionTypeProperties();
		}
		return _honeypotEnabled;
	}

	private void setProtectionTypeProperties() {
		String protectionType = this.extractProtectionType();
		if (null == protectionType) {
			protectionType = JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_NONE;
		}
		this._recaptchaEnabled = protectionType.equalsIgnoreCase(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_RECAPTCHA);
		this._recaptchaAfterEnabled = protectionType.equalsIgnoreCase(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_RECAPTCHA_AFTER);
		this._honeypotEnabled = protectionType.equalsIgnoreCase(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_HONEYPOT);
	}
	
	public String getJCaptchaResponse() {
		return _jCaptchaResponse;
	}
	public void setJCaptchaResponse(String jCaptchaResponse) {
		this._jCaptchaResponse = jCaptchaResponse;
	}
	
	public String getRecaptcha_challenge_field() {
		return recaptcha_challenge_field;
	}
	public void setRecaptcha_challenge_field(String recaptcha_challenge_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
	}
	
	public String getRecaptcha_response_field() {
		return recaptcha_response_field;
	}
	public void setRecaptcha_response_field(String recaptcha_response_field) {
		this.recaptcha_response_field = recaptcha_response_field;
	}
	
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private String _typeCode;
	private Lang _currentLang;
	private Message _message;
	
	private Boolean _recaptchaEnabled;
	private Boolean _recaptchaAfterEnabled;
	private Boolean _honeypotEnabled;
	
	private String _jCaptchaResponse;
	private String recaptcha_challenge_field;
	private String recaptcha_response_field;
	private II18nManager _i18nManager;
	private IMessageManager _messageManager;
	
}