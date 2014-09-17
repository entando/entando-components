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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
//		config.setProfileEMailAttr(this.getProfileEMailAttr());
		
		config.setEMailSenderCode(this.getEMailSenderCode());
		config.setActivationPageCode(this.getActivationPageCode());
		config.setReactivationPageCode(this.getReactivationPageCode());
		
		config.setRoles(new TreeSet<String>(this.getRoles()));
		config.setGroups(new TreeSet<String>(this.getGroups()));
		
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
	public void addRole(String role) {
		if (null == this._roles ) {
			this._roles = new HashSet<String>();
		}
		this._roles.add(role);
	}
	
	@Override
	public void setRoles(Set<String> roles) {
		this._roles = roles;
	}
	
	@Override
	public Set<String> getRoles() {
		return _roles;
	}
	
	@Override
	public void addGroup(String group) {
		if (null == this._groups ) {
			this._groups = new HashSet<String>();
		}
		this._groups.add(group);
	}
	
	@Override
	public void setGroups(Set<String> groups) {
		this._groups = groups;
	}
	
	@Override
	public Set<String> getGroups() {
		return _groups;
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
	private Set<String> _roles = new TreeSet<String>();
	private Set<String> _groups = new TreeSet<String>();
	
	private Map<String, Template> _activationTemplates = new HashMap<String, Template>();
	private Map<String, Template> _reactivationTemplates = new HashMap<String, Template>();
	
}