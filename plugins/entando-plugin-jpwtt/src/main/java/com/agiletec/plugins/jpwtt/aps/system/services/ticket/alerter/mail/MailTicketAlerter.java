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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.ITicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.AlerterThread;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.AlerterThreadListener;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.ITicketAlerter;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.MailTemplate;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.WttMailConfig;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.parse.WttMailConfigDOM;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

/**
 * 
 * @version 1.0
 * @author E.Mezzano
 */
public class MailTicketAlerter implements AlerterThreadListener, ITicketAlerter {
	
	public void init() throws ApsSystemException {
		this.loadConfigs();
	}
	
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = (ConfigInterface) this.getConfigManager();
			String xml = configManager.getConfigItem("jpwttMailConfig");
			if (xml == null) {
				throw new ApsSystemException("Missing config item: jpwttMailConfig");
			}
			WttMailConfigDOM configDOM = new WttMailConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Error loading config", t);
		}
	}
	
	@Override
	public void notifyAddedTicket(Ticket ticket, InterventionType interventionType) {
		AlerterThread thread = new AlerterThread();
		String threadName = WTT_MAIL_ALERTER_THREAD_NAME + ticket.getCode();
		thread.startAddedTicketThread(this, ticket, interventionType, threadName);
	}
	
	@Override
	public void notifyTicketOperation(Ticket ticket, TicketOperation operation, InterventionType interventionType) {
		AlerterThread thread = new AlerterThread();
		String threadName = WTT_MAIL_ALERTER_THREAD_NAME + ticket.getCode();
		thread.startTicketOperationThread(this, ticket, operation, interventionType, threadName);
	}
	
	@Override
	public void sendAlertOnAddedTicket(Ticket ticket, InterventionType interventionType) {
		MailTemplate template = this.getConfig().getTemplates().get(new Integer(0));
		try {
			this.sendAlert(ticket, null, interventionType, template);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendAlertOnAddedTicket");
		}
	}
	
	@Override
	public void sendAlertOnTicketOperation(Ticket ticket, TicketOperation operation, InterventionType interventionType) {
		Integer operationCode = new Integer(operation.getOperationCode());
		MailTemplate template = this.getConfig().getTemplates().get(operationCode);
		try {
			this.sendAlert(ticket, operation, interventionType, template);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendAlertOnTicketOperation");
		}
	}
	
	private void sendAlert(Ticket ticket, TicketOperation operation, InterventionType interventionType, MailTemplate template) throws ApsSystemException {
		Map<String, String> params = this.prepareParams(ticket, interventionType, operation);
		
		Map<String, String> bodies = template.getTemplateBodies();
		String userTemplate = bodies.get(MailTemplate.TEMPLATE_TYPE_USER);
		if (userTemplate!=null && userTemplate.length()>0) {
			this.sendMailToUser(ticket.getEmail(), userTemplate, params);
		}
		/*
		String operatorTemplate = bodies.get(MailTemplate.TEMPLATE_TYPE_OPERATOR);
		if (operatorTemplate!=null && operatorTemplate.length()>0) {
			this.sendMailToUser(operation.getOperator(), userTemplate, params);
		}
		//*/
		String allOperatorsTemplate = bodies.get(MailTemplate.TEMPLATE_TYPE_ALLOPERATORS);
		if (allOperatorsTemplate!=null && allOperatorsTemplate.length()>0) {
			String[] addresses = this.extractAllOperatorsMailAddresses(ticket);
			this.sendMailToAddresses(addresses, allOperatorsTemplate, params);
		}
		String adminTemplate = bodies.get(MailTemplate.TEMPLATE_TYPE_ADMIN);
		if (adminTemplate!=null && adminTemplate.length()>0) {
			String[] addresses = this.extractAdminMailAddresses(ticket);
			this.sendMailToAddresses(addresses, adminTemplate, params);
		}
	}
	
	private void sendMailToUser(String eMail, String template, Map<String, String> params) throws ApsSystemException {
		WttMailConfig mailConfig = this.getConfig();
		if (eMail!=null) {
			String text = this.prepareBody(template, params);
			String[] addresses = { eMail };
			this.getMailManager().sendMail(text, mailConfig.getSubject(), addresses, null, null, mailConfig.getSenderCode());
		}
	}
	
	private void sendMailToAddresses(String[] addresses, String template, Map<String, String> params) throws ApsSystemException {
		if (addresses!=null && addresses.length>0) {
			WttMailConfig config = this.getConfig();
			String text = this.prepareBody(template, params);
			String subject = config.getSubject();
			String senderCode = config.getSenderCode();
			if (config.isUniqueMail()) {
				this.getMailManager().sendMail(text, subject, addresses, null, null, senderCode);
			} else {
				for (String eMail : addresses) {
					this.getMailManager().sendMail(text, subject, new String[] { eMail }, null, null, senderCode);
				}
			}
		}
	}
	
	private String[] extractAdminMailAddresses(Ticket ticket) throws ApsSystemException {
		WttMailConfig config = this.getConfig();
		List<String> commonAddresses = config.getCommonAdminAddresses();
		List<String> intervTypeAddresses = config.getIntervTypesAdminAddresses().get(ticket.getUserInterventionType());
		/*
		String mailAttrName = config.getMailAttrName();
		Collection<String> usernames = this.extractUsersForPermission(JpWttSystemConstants.WTT_ADMIN_PERMISSION);
		int size = usernames.size() + commonAddresses.size();
		//*/
		int size = commonAddresses.size();
		if (intervTypeAddresses!=null) size += intervTypeAddresses.size();
		
		List<String> mailAddresses = new ArrayList<String>(size);
		mailAddresses.addAll(commonAddresses);
		if (intervTypeAddresses!=null) mailAddresses.addAll(intervTypeAddresses);
		/*
		for (String username : usernames) {
			String eMail = this.extractMailAddress(username, mailAttrName);
			if (eMail != null) mailAddresses.add(eMail);
		}
		//*/
		return (String[]) mailAddresses.toArray(new String[mailAddresses.size()]);
	}
	
	private String[] extractAllOperatorsMailAddresses(Ticket ticket) throws ApsSystemException {
		WttMailConfig config = this.getConfig();
		List<String> commonAddresses = config.getCommonOperatorAddresses();
		List<String> intervTypeAddresses = config.getIntervTypesOperatorAddresses().get(ticket.getUserInterventionType());
		/*
		String mailAttrName = config.getMailAttrName();
//		Collection<String> usernames = this.extractUsersForPermission(JpWttSystemConstants.WTT_OPERATOR_PERMISSION);
		int size = usernames.size() + commonAddresses.size();
		//*/
		int size = commonAddresses.size();
		if (intervTypeAddresses!=null) size += intervTypeAddresses.size();
		
		List<String> mailAddresses = new ArrayList<String>(size);
		mailAddresses.addAll(commonAddresses);
		if (intervTypeAddresses!=null) mailAddresses.addAll(intervTypeAddresses);
		/*
		for (String username : usernames) {
			String eMail = this.extractMailAddress(username, mailAttrName);
			if (eMail != null) mailAddresses.add(eMail);
		}
		//*/
		return (String[]) mailAddresses.toArray(new String[mailAddresses.size()]);
	}
	/*
	private String extractMailAddress(String username, String mailAttrName) throws ApsSystemException {
		String eMail = null;
		UserProfile profile = this.getProfileManager().getProfile(username);
		if (profile!=null) {
			ITextAttribute mailAttribute = (ITextAttribute) profile.getAttribute(mailAttrName);
			if (mailAttribute!=null) {
				eMail = mailAttribute.getText();
			}
		}
		return eMail;
	}
	
	private Collection<String> extractUsersForPermission(String permissionName) throws ApsSystemException {
		Set<String> usernames = new TreeSet<String>();
		IApsAuthorityManager authorityManager = (IApsAuthorityManager) this.getRoleManager();
		List<Role> roles = this.getRoleManager().getRolesWithPermission(permissionName);
		for (Role role : roles) {
			List<UserDetails> roleUsers = authorityManager.getUsersByAuthority(role);
			for (UserDetails user : roleUsers) {
				if(!user.isDisabled()) {
					usernames.add(user.getUsername());
				}
			}
		}
		return usernames;
	}
	//*/
	
	private Map<String, String> prepareParams(Ticket ticket, InterventionType interventionType, TicketOperation operation) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", ticket.getCode());
		params.put("creationDate", DateConverter.getFormattedDate(ticket.getCreationDate(), "dd/MM/yyyy"));
		params.put("author", ticket.getAuthor());
		params.put("nome", ticket.getNome());
		params.put("cognome", ticket.getCognome());
		params.put("codFisc", ticket.getCodFisc());
		params.put("comune", ticket.getComune());
		params.put("localita", ticket.getLocalita());
		params.put("tipoIndirizzo", ticket.getTipoIndirizzo());
		params.put("indirizzo", ticket.getIndirizzo());
		params.put("numeroIndirizzo", ticket.getNumeroIndirizzo());
		params.put("telefono", ticket.getTelefono());
		params.put("email", ticket.getEmail());
		params.put("message", ticket.getMessage());
		
		ITicketManager ticketManager = this.getTicketManager();
		String userInterventionTypeDescr = null;
		if (ticket.getUserInterventionType()>0) {
			InterventionType userInterventionType = ticketManager.getInterventionType(ticket.getUserInterventionType());
			if (null!=userInterventionType) {
				userInterventionTypeDescr = userInterventionType.getDescr();
			}
		}
		if (null==userInterventionTypeDescr) {
			userInterventionTypeDescr = "Generico";
		}
		params.put("userInterventionType", userInterventionTypeDescr);
		
		String assignedInterventionTypeDescr = null;
		if (ticket.getOpInterventionType()>0) {
			InterventionType opInterventionType = ticketManager.getInterventionType(ticket.getOpInterventionType());
			if (null!=opInterventionType) {
				assignedInterventionTypeDescr = opInterventionType.getDescr();
			}
		}
		if (null==assignedInterventionTypeDescr) {
			assignedInterventionTypeDescr = "Generico";
		}
		params.put("assignedInterventionType", assignedInterventionTypeDescr);
		
		String priority = ticketManager.getPriorities().get(ticket.getPriority());
		if (priority==null) priority = "Normale";
		params.put("priority", priority);
		
		Date closingDate = ticket.getClosingDate();
		if (closingDate!=null) {
			params.put("creationDate", DateConverter.getFormattedDate(closingDate, "dd/MM/yyyy"));
		}
		
		//	TODO gestire messaggio esito positivo?
		if (ticket.isResolved()) {
			params.put("resolved", "risolta");
		} else {
			params.put("resolved", "non risolta");
		}
		
		if (operation!=null) {
			params.put("wttRole", operation.getWttRole());
			params.put("operator", operation.getOperator());
			params.put("note", operation.getNote());
			params.put("operationDate", DateConverter.getFormattedDate(operation.getDate(), "dd/MM/yyyy"));
		}
		return params;
	}
	
	private String prepareBody(String defaultText, Map<String, String> params) {
		String body = defaultText;
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String field = "{" + pairs.getKey() + "}";
			int start = body.indexOf(field);
			if (start >= 0) {
				int end = start + field.length();
				body = body.substring(0, start) + pairs.getValue()
						+ body.substring(end);
			}
		}
		return body;
	}
	
	public WttMailConfig getConfig() {
		return _config;
	}
	protected void setConfig(WttMailConfig config) {
		this._config = config;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	protected BaseConfigManager getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(BaseConfigManager configManager) {
		this._configManager = configManager;
	}
	
	public IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	
	public ITicketManager getTicketManager() {
		return ticketManager;
	}
	public void setTicketManager(ITicketManager ticketManager) {
		this.ticketManager = ticketManager;
	}
	
	private WttMailConfig _config;
	
	private IMailManager _mailManager;
	private BaseConfigManager _configManager;
	private IRoleManager _roleManager;
	private ITicketManager ticketManager;
	
	public static final String WTT_MAIL_ALERTER_THREAD_NAME = "WttMailAlerterThread";
	
}