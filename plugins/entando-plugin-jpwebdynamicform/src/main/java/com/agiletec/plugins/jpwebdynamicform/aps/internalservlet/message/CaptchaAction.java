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
