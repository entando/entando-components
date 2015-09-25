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

import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class TypeVersionGuiConfig {
	
	public Integer getCode() {
		return _code;
	}
	public void setCode(Integer code) {
		this._code = code;
	}
	
	public String getFormTypeCode() {
		return _formTypeCode;
	}
	public void setFormTypeCode(String formTypeCode) {
		this._formTypeCode = formTypeCode;
	}
	
	public Integer getVersion() {
		return _version;
	}
	public void setVersion(Integer version) {
		this._version = version;
	}
	
	public Message getPrototype() {
		return _prototype;
	}
	public void setPrototype(Message prototype) {
		this._prototype = prototype;
	}
	
	public String getPrototypeXml() {
		return _prototypeXml;
	}
	public void setPrototypeXml(String prototypeXml) {
		this._prototypeXml = prototypeXml;
	}
	
	public StepsConfig getStepsConfig() {
		return _stepsConfig;
	}
	public void setStepsConfig(StepsConfig stepsConfig) {
		this._stepsConfig = stepsConfig;
	}
	
	public String getStepsConfigXml() {
		return _stepsConfigXml;
	}
	public void setStepsConfigXml(String stepsConfigXml) {
		this._stepsConfigXml = stepsConfigXml;
	}
	
	public List<StepGuiConfig> getGuiConfigs() {
		return _guiConfigs;
	}
	public void setGuiConfigs(List<StepGuiConfig> guiConfigs) {
		this._guiConfigs = guiConfigs;
	}
	
	private Integer _code;
	private String _formTypeCode;
	private Integer _version;
	private Message _prototype;
	private String _prototypeXml;
	private StepsConfig _stepsConfig;
	private String _stepsConfigXml;
	private List<StepGuiConfig> _guiConfigs = new ArrayList<StepGuiConfig>();
	
}
