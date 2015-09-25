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

import java.util.Arrays;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IGuiGeneratorManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.entando.entando.plugins.jpwebform.apsadmin.AbstractPreviewAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class GuiConfigAction extends AbstractPreviewAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GuiConfigAction.class);

	public String entryGui() {
		return super.entryConfigStep();
	}
	
	public String buildGui() {
		try {
			StepsConfig stepsConfig = super.getStepsConfig();
			if (null == stepsConfig) {
				//ERROR
				return INPUT;
			}
			Step step = stepsConfig.getStep(this.getStepCode());
			List<String> protectedCodes = Arrays.asList(JpwebformSystemConstants.RESERVED_STEP_CODES);
			if (null == step && !protectedCodes.contains(this.getStepCode())) {
				//ERROR
				return INPUT;
			}
			StepGuiConfig guiConfig = null;
			guiConfig = this.getFormManager().generateWorkStepUserGui(this.getEntityTypeCode(), this.getStepCode(), this.isOverwriteStepGui());
			this.setUserGui(guiConfig.getUserGui());
			this.setUserCss(guiConfig.getUserCss());
			super.entryConfigStep();
		} catch (Throwable t) {
			_logger.error("error in buildGui", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String generateGui() {
		try {
			StepsConfig stepsConfig = super.getStepsConfig();
			if (null == stepsConfig) {
				//ERROR
				return INPUT;
			}
			Step step = stepsConfig.getStep(this.getStepCode());
			List<String> protectedCodes = Arrays.asList(JpwebformSystemConstants.RESERVED_STEP_CODES);
			if (null == step && !protectedCodes.contains(this.getStepCode())) {
				//ERROR
				return INPUT;
			}
			StepGuiConfig guiConfig = this.getFormManager().generateWorkStepUserGui(this.getEntityTypeCode(), this.getStepCode(), isOverwriteStepGui());
			this.setUserGui(guiConfig.getUserGui());
			this.setUserCss(guiConfig.getUserCss());
			super.entryConfigStep();
		} catch (Throwable t) {
			_logger.error("error in generateGui", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String editGui() {
		try {
			StepsConfig stepsConfig = this.getFormManager().getStepsConfig(this.getEntityTypeCode());
			if (null != stepsConfig) {
				stepsConfig = stepsConfig.clone();
				this.getRequest().getSession().setAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM, stepsConfig);
			} else {
				this.setError("error.null.config.step");
				return INPUT;
			}
			Step step = stepsConfig.getStep(this.getStepCode());
			List<String> protectedCodes = Arrays.asList(JpwebformSystemConstants.RESERVED_STEP_CODES);
			if (null == step && !protectedCodes.contains(this.getStepCode())) {
				//ERROR
				return INPUT;
			}
			StepGuiConfig guiConfig = this.getFormManager().getWorkGuiConfig(this.getEntityTypeCode(), this.getStepCode());
			if(null != guiConfig && (null == this.getUserGui() || this.getUserGui().isEmpty())) {
				this.setUserGui(guiConfig.getUserGui());
			}
			if(null != guiConfig && (null == this.getUserCss() || this.getUserCss().isEmpty())) {
				this.setUserCss(guiConfig.getUserCss());
			}
		} catch (Throwable t) {
			_logger.error("error in editGui", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String saveGui() {
		try {
			StepsConfig stepsConfig = super.getStepsConfig();
			if (null == stepsConfig) {
				//ERROR
				return INPUT;
			}
			Step step = stepsConfig.getStep(this.getStepCode());
			List<String> protectedCodes = Arrays.asList(JpwebformSystemConstants.RESERVED_STEP_CODES);
			if (null == step && !protectedCodes.contains(this.getStepCode())) {
				//ERROR
				return INPUT;
			}
			StepGuiConfig guiConfig = this.getFormManager().getWorkGuiConfig(this.getEntityTypeCode(), this.getStepCode());
			if (null == guiConfig) {
				guiConfig = new StepGuiConfig();
				guiConfig.setFormTypeCode(this.getEntityTypeCode());
				guiConfig.setStepCode(this.getStepCode());
			}
			guiConfig.setUserGui(this.getUserGui());
			guiConfig.setUserCss(this.getUserCss());
			this.getFormManager().saveWorkGuiConfig(guiConfig);
		} catch (Throwable t) {
			_logger.error("error in saveGui", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	
	public String getUserCss() {
		return _userCss;
	}
	public void setUserCss(String userCss) {
		this._userCss = userCss;
	}
	
	public String getUserGui() {
		return _userGui;
	}
	public void setUserGui(String userGui) {
		this._userGui = userGui;
	}
	
	public IGuiGeneratorManager getGuiGeneratorManager() {
		return _guiGeneratorManager;
	}
	
	public void setGuiGeneratorManager(IGuiGeneratorManager guiGeneratorManager) {
		this._guiGeneratorManager = guiGeneratorManager;
	}
	
	public int getVersionType() {
		return _versionType;
	}
	
	public void setVersionType(int versionType) {
		this._versionType = versionType;
	}
	
	

	public boolean isOverwriteStepGui() {
		return _overwriteStepGui;
	}

	public void setOverwriteStepGui(boolean overwriteStepGui) {
		this._overwriteStepGui = overwriteStepGui;
	}

	public String getError() {
		return _error;
	}

	public void setError(String error) {
		this._error = error;
	}
	
	
	private boolean _overwriteStepGui;
	private String _error;
	private int _versionType;
	private IGuiGeneratorManager _guiGeneratorManager;
	private String _userGui;
	private String _userCss;

}
