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
package org.entando.entando.plugins.jpwebform.aps.internalservlet.form;

import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IGuiGeneratorManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.TypeVersionGuiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;

/**
 * Implementation for action managing Message entity editing operations.
 *
 * @author E.Mezzano
 */
public class UserFormAction extends AbstractApsEntityAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserFormAction.class);
	
	@Override
	public void validate() {
		if (this.getMessage() != null) {
			super.validate();
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
			TypeVersionGuiConfig configType = null;
			Message resumedMessage = resumeMessage();
			if (resumedMessage != null) {
				configType = this.getMessageManager().getTypeVersionGui(typeCode, resumedMessage.getVersionType());
			} else {
				configType = this.getMessageManager().getTypeVersionGui(typeCode);
			}
			if (null == configType) {
				_logger.error("configuration GUI is null");
				return FAILURE;
			}
			// create GUI
			this.getGuiGeneratorManager().checkUserGuis(configType);
			Message prototype = (Message) configType.getPrototype().getEntityPrototype();
			Message prototyConfig = (Message) this.getMessageManager().getEntityPrototype(typeCode);
			Message existAlready = this.getMessageManager().resumeMessageByUserAndType(this.getCurrentUser().getUsername(), typeCode, new Integer(1));
			// if the form exist and was fully completed
			if ((null != existAlready && existAlready.isCompleted())
					// and it cannot be submitted multiple times
					&& (!prototyConfig.isRepeatable()
					// and the current is not guest
					&& !isGuest())) {
				return "notRepeatable";
			}
			this.setVersionConfigOnSession(configType);
			if (null == resumedMessage
					|| resumedMessage.isCompleted()
					|| isGuest()) {
				this.setVersionType(configType.getVersion());
				StepsConfig stepsConfig = configType.getStepsConfig();
				Step firstStep = stepsConfig.getFirstStep();
				this.setCurrentStepCode(firstStep.getCode());
				String username = this.getCurrentUser().getUsername();
				prototype.setUsername(username);
				prototype.setCreationDate(new Date());
				prototype.setLangCode(this.getCurrentLang().getCode());
				prototype.toggleAttribute(firstStep.getCode());
				this.setMessageOnSession(prototype);
			} else {
				// load latest version of the form
				TypeVersionGuiConfig latestType = this.getMessageManager().getTypeVersionGui(typeCode);
				// check whether we have to update to the latest version
				if (!prototyConfig.isIgnoreVersionEdit()
						// version is not changed
						|| (prototyConfig.isIgnoreVersionEdit()
						&& resumedMessage.getVersionType() == latestType.getVersion())) {
					this.setVersionType(resumedMessage.getVersionType());
					this.setMessageOnSession(resumedMessage);
					Step currentStep = configType.getStepsConfig().getNextStep(resumedMessage.getLastCompletedStep());
					while (!ognlValidation(currentStep)) {
						currentStep = configType.getStepsConfig().getNextStep(currentStep.getCode());
					}
					this.setCurrentStepCode(currentStep.getCode());
				} else {
					// create a new form and left the older AS IS
					_logger.debug("webform: updating form {} from version {} to {}", resumedMessage.getId(), resumedMessage.getVersionType(),latestType.getVersion());
					// get the last version of the form
					latestType = this.getMessageManager().getTypeVersionGui(typeCode);
					// update step
					StepsConfig stepsConfig = latestType.getStepsConfig();
					Step firstStep = stepsConfig.getFirstStep();
					this.setCurrentStepCode(firstStep.getCode());
					// copy original values into the new 
					Message updatedMessage = getMessageManager().getMessage(resumedMessage.getId(), latestType);
					// update version
					updatedMessage.setVersionType(latestType.getVersion());
					// update entity
					this.getMessageManager().addUpdateMessage(updatedMessage);
					// put it on session
					this.setMessageOnSession(updatedMessage);
					// update version type
					this.setVersionType(updatedMessage.getVersionType());
					// create GUI
					this.getGuiGeneratorManager().checkUserGuis(latestType);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in createNew", t);
			return FAILURE;
		}
		return SUCCESS;
	}
//
//	private Step ognlValidation(NAVIGATE navigate) throws ApsSystemException {
//		Message currentMessage = this.getMessage();
//		StepsConfig stepsConfig = this.getMessageManager().getStepsConfig(currentMessage.getTypeCode());
//		List<Step> steps = stepsConfig.getSteps();
//		Iterator<Step> iterator = steps.iterator();
//		try {
//			Step step = stepsConfig.getStep(this.getCurrentStepCode());
//			while (iterator.hasNext() && !ognlValidationHelper(step)) {
//				step = iterator.next();
//			}
//			this.setCurrentStepCode(step.getCode());
//			return step;
//		} catch (Throwable t) {
//			ApsSystemUtils.logThrowable(t, this, "ognlValidation");
//			throw new ApsSystemException("ognlValidation", t.getCause());
//		}
//	}

	private Boolean ognlValidation(Step step) throws ApsSystemException {
		return this.getMessageManager().validateOgnl(step.getOgnlExpression(), this.getMessage(), this.getCurrentUser().getUsername());
	}

	/**
	 * Get the message on hold for the current user
	 *
	 * @return
	 * @throws ApsSystemException
	 */
	private Message resumeMessage() throws ApsSystemException {
		if (isGuest()) {
			return null;
		}
		return this.getMessageManager().resumeMessageByUserAndType(this.getCurrentUser().getUsername(), this.getTypeCode(), new Integer(0));
	}

	public String submitStep() {
		return navigateForm(NAVIGATE.FORWARD);
	}

	public String previousStep() {
		this.getEntityActionHelper().updateEntity(this.getApsEntity(), this.getRequest());
		return navigateForm(NAVIGATE.BACK);
	}

	@Override
	public String edit() {
		// Operation Not Allowed
		return null;
	}

	public String entryForm() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
		} catch (Throwable t) {
			_logger.error("error in entryForm", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String save() {
		return SUCCESS;
	}

	public String resume() {
		try {
			Message currentMessage = this.getMessage();
			if (currentMessage != null) {
				TypeVersionGuiConfig config = this.getMessageManager().getTypeVersionGui(currentMessage.getTypeCode(), currentMessage.getVersionType());
				this.setVersionType(currentMessage.getVersionType());
				this.setTypeCode(currentMessage.getTypeCode());
			}
		} catch (Throwable t) {
			_logger.error("error in Error resuming message", t);
			throw new RuntimeException("Error resuming message", t);
		}

		return SUCCESS;
	}

	public TypeVersionGuiConfig getVersionConfigFromSession() {
		return (TypeVersionGuiConfig) this.getRequest().getSession().getAttribute(JpwebformSystemConstants.SESSION_PARAM_NAME_CURRENT_VERSION_CONFIG);
	}

	public void setVersionConfigOnSession(TypeVersionGuiConfig config) {
		this.getRequest().getSession().setAttribute(JpwebformSystemConstants.SESSION_PARAM_NAME_CURRENT_VERSION_CONFIG, config);
	}

	public Message getMessage() {
		if (this._message == null) {
			try {
				String sessionParamName = this.getSessionParamName();
				this._message = (Message) this.getRequest().getSession().getAttribute(sessionParamName);
			} catch (Throwable t) {
				_logger.error("Error finding message", t);
				throw new RuntimeException("Error finding message", t);
			}
		}
		return _message;
	}

	protected void setMessageOnSession(Message message) {
		String sessionParamName = this.getSessionParamName();
		if (message == null) {
			this.getRequest().getSession().removeAttribute(sessionParamName);
		} else {
			this.getRequest().getSession().setAttribute(sessionParamName, message);
		}
		this._message = message;
	}

	/**
	 * Returns the name of the session parameter containing the current Message.
	 *
	 * @return The name of the session parameter containing the current Message.
	 */
	protected String getSessionParamName() {
		String typeCode = this.getTypeCode();
		return JpwebformSystemConstants.SESSION_PARAM_NAME_CURRENT_FORM + typeCode;
	}

	/**
	 * Returns the current language from front-end context.
	 *
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

	private String navigateForm(NAVIGATE navigate) {
		try {
			String typeCode = this.getTypeCode();
			TypeVersionGuiConfig config = this.getVersionConfigFromSession();
			boolean isPreview = (config != null && config.getVersion() == 0);
			if (null == config) {
				//MANAGE ERROR
				//return FAILURE;
				return "voidTypeCode";
			}
			if (!config.getFormTypeCode().equals(typeCode)) {
				//MANAGE ERROR
				return FAILURE;
			}
			this.setVersionType(config.getVersion());
			Message message = this.getMessage();
			if (!isPreview && (isGuest() && message.isCompleted() || !isGuest())) {
				message.setSendDate(new Date());
				this.getMessageManager().addUpdateMessage(message);
			}
			String previsiousStepCode = this.getCurrentStepCode();
			StepsConfig stepsConfig = config.getStepsConfig();
			Step nextStep = null;
			switch (navigate) {
				case BACK:
					nextStep = stepsConfig.getPreviousStep(previsiousStepCode);
					while (!ognlValidation(nextStep)) {
						nextStep = stepsConfig.getPreviousStep(nextStep.getCode());
					}
					if (null != nextStep) {
						message.setLastCompletedStep(nextStep.getCode());
					}
					break;
				case FORWARD:
					message.setLastCompletedStep(this.getCurrentStepCode());
					nextStep = stepsConfig.getNextStep(previsiousStepCode);
					while (!ognlValidation(nextStep)) {
						nextStep = stepsConfig.getNextStep(nextStep.getCode());
					}
					break;
			}

			if (null != nextStep && nextStep.getCode().equals(JpwebformSystemConstants.COMPLETED_STEP_CODE)) {
				Step lastStep = stepsConfig.getLastStep();
				if (previsiousStepCode.equals(JpwebformSystemConstants.CONFIRM_STEP_CODE)
						|| !stepsConfig.isConfirmGui()) {
					this.setPreviousStepCode(lastStep.getCode());
					this.setCurrentStepCode(JpwebformSystemConstants.COMPLETED_STEP_CODE);
					if (!isPreview) {
						message.setCompleted(true);
						message.setSendDate(new Date());
						this.getMessageManager().addUpdateMessage(message);
						this.getMessageManager().sendMessage(message);
						if (this.isSendMail() && (null != this.getCustomEmail() && !this.getCustomEmail().isEmpty())) {
							this.getMessageManager().sendMessage(message, this.getCustomEmail());
						}
					}
				}
			}
			Step previousStep = stepsConfig.getPreviousStep(this.getCurrentStepCode());
			if (null != previousStep) {
				while (!ognlValidation(previousStep)) {
					previousStep = stepsConfig.getPreviousStep(previousStep.getCode());
				}
			}
			this.setPreviousStepCode((previousStep != null) ? previousStep.getCode() : null);
			this.setCurrentStepCode(nextStep.getCode());
			if (!isPreview) {
				message.activateAttributes();
				message.toggleAttribute(nextStep.getCode());
				if (isGuest() && message.isCompleted() || !isGuest()) {
					this.getMessageManager().addUpdateMessage(message);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in submitStep", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Extract the typeCode from the current showlet.
	 *
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
					String showletTypeCode = config.getProperty(JpwebformSystemConstants.WIDGET_PARAM_TYPECODE);
					if (showletTypeCode != null && showletTypeCode.trim().length() > 0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}

	public String getCurrentTypeCode() {
		return this.getTypeCode();
	}

	public String getTypeCode() {
		if (null == this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}

	public void setTypeCode(String typeCode) {
		String showletTypeCode = this.extractTypeCode();
		this._typeCode = (null == showletTypeCode) ? typeCode : showletTypeCode;
	}

	public Integer getVersionType() {
		return _versionType;
	}

	public void setVersionType(Integer versionType) {
		this._versionType = versionType;
	}

	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	public String getCurrentStepCode() {
		return _currentStepCode;
	}

	public void setCurrentStepCode(String currentStepCode) {
		this._currentStepCode = currentStepCode;
	}

	public String getPreviousStepCode() {
		return _previousStepCode;
	}

	public void setPreviousStepCode(String previousStepCode) {
		this._previousStepCode = previousStepCode;
	}

	protected IGuiGeneratorManager getGuiGeneratorManager() {
		return _guiGeneratorManager;
	}

	public void setGuiGeneratorManager(IGuiGeneratorManager guiGeneratorManager) {
		this._guiGeneratorManager = guiGeneratorManager;
	}

	protected IFormManager getMessageManager() {
		return _messageManager;
	}

	public void setMessageManager(IFormManager messageManager) {
		this._messageManager = messageManager;
	}

	public IMailManager getMailManager() {
		return _mailManager;
	}

	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}

	public String getCurrentUserEmail() {
		IApsEntity profile = (IApsEntity) this.getCurrentUser().getProfile();
		if (profile != null) {
			MonoTextAttribute email = (MonoTextAttribute) profile.getAttribute("email");
			return email.getText();
		}
		return "";
	}

	public void setCurrentUserEmail(String currentUserEmail) {
		this._currentUserEmail = currentUserEmail;
	}

	public String getCustomEmail() {
		return _customEmail;
	}

	public void setCustomEmail(String customEmail) {
		this._customEmail = customEmail;
	}

	public boolean isSendMail() {
		return _sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this._sendMail = sendMail;
	}

	private boolean isGuest() {
		return "guest".equals(this.getCurrentUser().getUsername());
	}

	private boolean _sendMail;

	private String _currentUserEmail;
	private String _customEmail;

	private String _typeCode;
	private Integer _versionType;
	private Lang _currentLang;
	private Message _message;

	private String _currentStepCode;
	private String _previousStepCode;

	private IFormManager _messageManager;
	private IGuiGeneratorManager _guiGeneratorManager;

	private IMailManager _mailManager;

	public enum NAVIGATE {

		BACK, FORWARD
	}

}
