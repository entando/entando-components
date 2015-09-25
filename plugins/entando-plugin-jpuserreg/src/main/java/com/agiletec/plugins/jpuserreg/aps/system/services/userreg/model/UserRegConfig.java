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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Container of plugin config 
 * 
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 */
public class UserRegConfig implements IUserRegConfig, Cloneable {
	
	@Override
	public UserRegConfig clone() {
		UserRegConfig config = new UserRegConfig();
		config.setTokenValidityMinutes(this.getTokenValidityMinutes());
		config.setEMailSenderCode(this.getEMailSenderCode());
		config.setActivationPageCode(this.getActivationPageCode());
		config.setReactivationPageCode(this.getReactivationPageCode());
		config.setDefaultCsvAuthorizations(this.getDefaultCsvAuthorizations());
		Iterator<Entry<String, Template>> activationTemplates = this.getActivationTemplates().entrySet().iterator();
		while (activationTemplates.hasNext()) {
			Entry<String, Template> current = activationTemplates.next();
			config.addActivationTemplate(current.getKey(), current.getValue().clone());
		}
		Iterator<Entry<String, Template>> reactivationTemplates = this.getReactivationTemplates().entrySet().iterator();
		while (reactivationTemplates.hasNext()) {
			Entry<String, Template> current = reactivationTemplates.next();
			config.addReactivationTemplate(current.getKey(), current.getValue().clone());
		}
		return config;
	}
	
	@Override
	public long getTokenValidityMinutes() {
		return _tokenValidityMinutes;
	}
	@Override
	public void setTokenValidityMinutes(long tokenValidityMinutes) {
		this._tokenValidityMinutes = tokenValidityMinutes;
	}
	
	@Override
	public String getEMailSenderCode() {
		return _eMailSenderCode;
	}
	@Override
	public void setEMailSenderCode(String mailSenderCode) {
		_eMailSenderCode = mailSenderCode;
	}
	
	@Override
	public String getActivationPageCode() {
		return _activationPageCode;
	}
	@Override
	public void setActivationPageCode(String activationPageCode) {
		this._activationPageCode = activationPageCode;
	}
	
	@Override
	public String getReactivationPageCode() {
		return _reactivationPageCode;
	}
	@Override
	public void setReactivationPageCode(String reactivationPageCode) {
		this._reactivationPageCode = reactivationPageCode;
	}
	
	@Override
	public void addDefaultCsvAuthorization(String csv) {
		if (null == csv) return;
		if (null == this.getDefaultCsvAuthorizations()) {
			this.setDefaultCsvAuthorizations(new HashSet<String>());
		}
		this.getDefaultCsvAuthorizations().add(csv);
	}
	
	@Override
	public Set<String> getDefaultCsvAuthorizations() {
		return _defaultCsvAuthorizations;
	}
	@Override
	public void setDefaultCsvAuthorizations(Set<String> defaultCsvAuthorizations) {
		this._defaultCsvAuthorizations = defaultCsvAuthorizations;
	}
	
	@Override
	public Map<String, Template> getActivationTemplates() {
		return _activationTemplates;
	}
	@Override
	public void setActivationTemplates(Map<String, Template> activationTemplates) {
		this._activationTemplates = activationTemplates;
	}
	@Override
	public void addActivationTemplate(String langCode, Template template) {
		this._activationTemplates.put(langCode, template);
	}
	
	@Override
	public Map<String, Template> getReactivationTemplates() {
		return _reactivationTemplates;
	}
	@Override
	public void setReactivationTemplates(Map<String, Template> reactivationTemplates) {
		this._reactivationTemplates = reactivationTemplates;
	}
	@Override
	public void addReactivationTemplate(String langCode, Template template) {
		this._reactivationTemplates.put(langCode, template);
	}
	
	private long _tokenValidityMinutes;
	private String _eMailSenderCode;
	private String _activationPageCode;
	private String _reactivationPageCode;
	
	private Set<String> _defaultCsvAuthorizations = new HashSet<String>();
	
	private Map<String, Template> _activationTemplates = new HashMap<String, Template>();
	private Map<String, Template> _reactivationTemplates = new HashMap<String, Template>();
	
}