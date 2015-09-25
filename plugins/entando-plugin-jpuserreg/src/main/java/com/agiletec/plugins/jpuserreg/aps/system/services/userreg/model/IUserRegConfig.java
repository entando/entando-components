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

import java.util.Map;
import java.util.Set;

/**
 * Interface for container of plugin config 
 * */
public interface IUserRegConfig {
	
	public IUserRegConfig clone();
	
	public long getTokenValidityMinutes();
	
	public void setTokenValidityMinutes(long tokenValidityMinutes);
	
	public String getEMailSenderCode();
	
	public void setEMailSenderCode(String mailSenderCode);
	
	public String getActivationPageCode();
	
	public void setActivationPageCode(String activationPageCode);
	
	public String getReactivationPageCode();
	
	public void setReactivationPageCode(String reactivationPageCode);
	
	public void addDefaultCsvAuthorization(String csv);
	
	public void setDefaultCsvAuthorizations(Set<String> authorizations);
	
	public Set<String> getDefaultCsvAuthorizations();
	
	public Map<String, Template> getActivationTemplates();
	public void setActivationTemplates(Map<String, Template> activationTemplates);
	public void addActivationTemplate(String langCode, Template template);
	
	public Map<String, Template> getReactivationTemplates();
	public void setReactivationTemplates(Map<String, Template> reactivationTemplates);
	public void addReactivationTemplate(String langCode, Template template);
	
}