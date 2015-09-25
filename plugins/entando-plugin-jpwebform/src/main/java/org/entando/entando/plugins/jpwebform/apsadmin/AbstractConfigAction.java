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
package org.entando.entando.plugins.jpwebform.apsadmin;

import java.util.ArrayList;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.FormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public abstract class AbstractConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractConfigAction.class);

	protected String entryConfigStep() {
		try {
			StepsConfig config = this.getFormManager().getStepsConfig(this.getEntityTypeCode());
			if (null == config) {
				config = new StepsConfig();
				config.setFormTypeCode(this.getEntityTypeCode());
				config.setSteps(new ArrayList<Step>());
				this.getFormManager().saveStepsConfig(config);
			}
			this.getRequest().getSession().setAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM, config.clone());
		} catch (Throwable t) {
			this.getRequest().getSession().removeAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM);
			_logger.error("error in entry", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public StepsConfig getStepsConfig() {
		return (StepsConfig) this.getRequest().getSession().getAttribute(JpwebformSystemConstants.STEP_CONFIG_SESSION_PARAM);
	}
	
	public String getEntityManagerName(){
		return ((FormManager) this.getFormManager()).getName();
	}
	
	public IApsEntity getEntityPrototype(String typeCode) {
		return this.getFormManager().getEntityPrototype(typeCode);
	}
	
	public String getEntityTypeCode() {
		return _entityTypeCode;
	}
	public void setEntityTypeCode(String entityTypeCode) {
		this._entityTypeCode = entityTypeCode;
	}
	
	public String getStepCode() {
		return _stepCode;
	}
	public void setStepCode(String stepCode) {
		this._stepCode = stepCode;
	}
	
	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}
	
	public int getOrder() {
		return _order;
	}
	
	public void setOrder(int order) {
		this._order = order;
	}
	
	/**
	 * Return the default system lang.
	 * @return The default lang.
	 */
	public Lang getDefaultLang() {
		return this.getLangManager().getDefaultLang();
	}
	
	
	private int _order;
	
	private String _entityTypeCode;
	private String _stepCode;
	
	private IFormManager _formManager;
	
}
