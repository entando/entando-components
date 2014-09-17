/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

import java.util.Date;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.ServletActionContext;

/**
 * @author E.Santoboni
 */
public class CaptchaAction extends UserNewMessageAction {
	
	@Override
	public void validate() {
		if (this.getRecaptchaAfterEnabled()) {
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
	
	public String captchaConfirm() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
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
			this.setMessageOnSession(null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
}
