/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WttMailConfig {
	
	public boolean isUniqueMail() {
		return _uniqueMail;
	}
	public void setUniqueMail(boolean uniqueMail) {
		this._uniqueMail = uniqueMail;
	}
	
	public String getSenderCode() {
		return _senderCode;
	}
	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}
	
	public String getMailAttrName() {
		return _mailAttrName;
	}
	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}
	
	public String getSubject() {
		return _subject;
	}
	public void setSubject(String subject) {
		this._subject = subject;
	}
	
	public Map<Integer, MailTemplate> getTemplates() {
		return _templates;
	}
	public void setTemplates(Map<Integer, MailTemplate> templates) {
		this._templates = templates;
	}
	public void addTemplate(Integer operation, MailTemplate template) {
		this._templates.put(operation, template);
	}
	
	public List<String> getCommonAdminAddresses() {
		return _commonAdminAddresses;
	}
	public void setCommonAdminAddresses(List<String> commonAdminAddresses) {
		this._commonAdminAddresses = commonAdminAddresses;
	}
	
	public List<String> getCommonOperatorAddresses() {
		return _commonOperatorAddresses;
	}
	public void setCommonOperatorAddresses(List<String> commonOperatorAddresses) {
		this._commonOperatorAddresses = commonOperatorAddresses;
	}
	
	public Map<Integer, List<String>> getIntervTypesAdminAddresses() {
		return _intervTypesAdminAddresses;
	}
	public void setIntervTypesAdminAddresses(Map<Integer, List<String>> intervTypesAdminAddresses) {
		this._intervTypesAdminAddresses = intervTypesAdminAddresses;
	}
	public void addIntervTypeAdminAddresses(Integer intervType, List<String> addresses) {
		this._intervTypesAdminAddresses.put(intervType, addresses);
	}
	
	public Map<Integer, List<String>> getIntervTypesOperatorAddresses() {
		return _intervTypesOperatorAddresses;
	}
	public void setIntervTypesOperatorAddresses(Map<Integer, List<String>> intervTypesOperatorAddresses) {
		this._intervTypesOperatorAddresses = intervTypesOperatorAddresses;
	}
	public void addIntervTypeOperatorAddresses(Integer intervType, List<String> addresses) {
		this._intervTypesOperatorAddresses.put(intervType, addresses);
	}
	
	private boolean _uniqueMail = false;
	private String _senderCode;
	private String _mailAttrName;
	private String _subject;
	
	private Map<Integer, MailTemplate> _templates = new HashMap<Integer, MailTemplate>();
	
	private List<String> _commonAdminAddresses;
	private List<String> _commonOperatorAddresses;
	private Map<Integer, List<String>> _intervTypesAdminAddresses = new HashMap<Integer, List<String>>();
	private Map<Integer, List<String>> _intervTypesOperatorAddresses = new HashMap<Integer, List<String>>();
	
}