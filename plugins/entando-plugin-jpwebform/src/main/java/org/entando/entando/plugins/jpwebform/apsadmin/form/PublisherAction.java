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
package org.entando.entando.plugins.jpwebform.apsadmin.form;

import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.entando.entando.plugins.jpwebform.apsadmin.AbstractPreviewAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class PublisherAction extends AbstractPreviewAction {

	private static final Logger _logger =  LoggerFactory.getLogger(PublisherAction.class);

	public String entry() {
		try {
			super.entryConfigStep();
			this.checkForm();
			if (this.hasActionErrors() || this.hasFieldErrors()) {
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in entry", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String publish() {
		try {
			this.checkForm();
			if (this.hasActionErrors() || this.hasFieldErrors()) {
				return INPUT;
			}
			this.getFormManager().generateNewVersionType(this.getEntityTypeCode());
		} catch (Throwable t) {
			_logger.error("error in publish", t);
			return FAILURE;
		}
		this.getRequest().getSession().removeAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM);
		return SUCCESS;
	}
	
	protected void checkForm() throws Throwable {
		try {
			StepsConfig config = super.getStepsConfig();
			if (null == config) {
				this.addActionError(this.getText("error.null.config"));//TODO user i18n
			} else {
				if (config.isConfirmGui() && !config.isBuiltConfirmGui()) {
					this.addActionError(this.getText("error.null.confirm.gui"));//TODO user i18n
				}
				if (!config.isBuiltEndPointGui()) {
					this.addActionError(this.getText("error.null.endpoint.gui"));//TODO user i18n
				}
				if(!this.getFormManager().checkStepGui(this.getEntityTypeCode())){
					this.addActionError(this.getText("error.null.steps.gui"));//TODO user i18n
				}
				List<Step> steps = config.getSteps();
				if (null == steps || steps.isEmpty()) {
					this.addActionError(this.getText("error.null.config"));//TODO ENPTY STEPS
				} else {
					IApsEntity prototype = super.getEntityPrototype(this.getEntityTypeCode());
					for (int i = 0; i < steps.size(); i++) {
						Step step = steps.get(i);
						List<String> attributeNames = step.getAttributeOrder();
						for (int j = 0; j < attributeNames.size(); j++) {
							String attributeName = attributeNames.get(j);
							if (null == prototype.getAttribute(attributeName)) {
								this.addActionError("STEP " + step.getCode() + " - Invalid attribute " + attributeName);//TODO user i18n
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in checkForm", t);
			throw new ApsSystemException("Error checking form config", t);
		}
	}
	
	
	
	
}
