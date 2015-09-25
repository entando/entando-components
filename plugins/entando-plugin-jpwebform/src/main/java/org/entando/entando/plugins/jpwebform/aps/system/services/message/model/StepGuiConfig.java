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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class StepGuiConfig {
	
	public String getFormTypeCode() {
		return _formTypeCode;
	}
	public void setFormTypeCode(String formTypeCode) {
		this._formTypeCode = formTypeCode;
	}
	
	public String getStepCode() {
		return _stepCode;
	}
	public void setStepCode(String stepCode) {
		this._stepCode = stepCode;
	}
	
	public String getUserGui() {
		return _userGui;
	}
	public void setUserGui(String userGui) {
		this._userGui = userGui;
	}
	
	public String getUserCss() {
		return _userCss;
	}
	public void setUserCss(String userCss) {
		this._userCss = userCss;
	}

	public Map<String, String> getI18nLabels() {
		return _i18nLabels;
	}

	public void setI18nLabels(Map<String, String> i18nLabels) {
		this._i18nLabels = i18nLabels;
	}
	
	private String _formTypeCode;
	private String _stepCode;
	private String _userGui;
	private String _userCss;
	private Map<String, String> _i18nLabels = new HashMap<String, String>();
	
}
