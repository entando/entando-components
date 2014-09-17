/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
	
	public void addRole(String role);
	
	public void setRoles(Set<String> roles);
	
	public Set<String> getRoles();
	
	public void addGroup(String group);
	
	public void setGroups(Set<String> groups);
	
	public Set<String> getGroups();
	
	public Map<String, Template> getActivationTemplates();
	public void setActivationTemplates(Map<String, Template> activationTemplates);
	public void addActivationTemplate(String langCode, Template template);
	
	public Map<String, Template> getReactivationTemplates();
	public void setReactivationTemplates(Map<String, Template> reactivationTemplates);
	public void addReactivationTemplate(String langCode, Template template);
	
}