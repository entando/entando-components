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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.ITicketAlerter;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.WttConfig;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.parse.WttConfigDOM;
import com.agiletec.plugins.jpwtt.aps.util.KeyGenerator;

/**
 * @author S.Didaci, E.Mezzano
 */
public class TicketManager extends AbstractService implements ITicketManager {
	
	/**
	 * Inizializzazione del servizio Ticket.
	 * @param ctx Il contesto del sistema.
	 * @param properties Le proprietà di inizializzazione.
	 * @throws com.agiletec.aps.system.exception.ApsSystemException 
	 */
	public void init() throws ApsSystemException {
		this.loadConfigs();
	}
	
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem("jpwttConfig");
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: jpwttConfig");
			}
			WttConfigDOM configDOM = new WttConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}
	
	@Override
	public List<Ticket> getTickets() throws ApsSystemException {
		List<Ticket> tickets = null;
		try {
			tickets = this.getTicketDAO().loadTickets();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTickets");
			throw new ApsSystemException("Error loading tickets", t);
		}
		return tickets;
	}
	
	@Override
	public List<String> searchTicketIds(ITicketSearchBean searchBean) throws ApsSystemException {
		List<String> ticketIds = null;
		try {
			ticketIds = this.getTicketDAO().searchTicketIds(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchTicketIds");
			throw new ApsSystemException("Error searching ticket ids", t);
		}
		return ticketIds;
	}
	
	@Override
	public Ticket getTicket(String code) throws ApsSystemException {
		Ticket ticket = null;
		try {
			ticket = this.getTicketDAO().loadTicket(code);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTicket");
			throw new ApsSystemException("Error loading ticket", t);
		}
		return ticket;
	}
	
	public List<TicketOperation> getTicketOperations(String code) throws ApsSystemException {
		 List<TicketOperation> ticketOperations = null;
		try {
			ticketOperations = this.getTicketDAO().loadTicketOperations(code);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTicketOperations");
			throw new ApsSystemException("Error loading ticket operations", t);
		}
		return ticketOperations;
	}
	
	@Override
	public void addTicket(Ticket ticket) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			String id =  KeyGenerator.generateTicketKey(String.valueOf(key)); // TODO Modificare?
			ticket.setCode(id.trim());
			this.getTicketDAO().addTicket(ticket);
			this.sendNotify(ticket);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addTicket");
			throw new ApsSystemException("Error adding ticket", t);
		}
	}
	
	@Override
	public void updateTicketWithOperation(Ticket ticket, TicketOperation operation) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			operation.setId(key);
			this.getTicketDAO().updateTicketWithOperation(ticket, operation);
			this.sendNotify(ticket, operation);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateTicketWithOperation");
			throw new ApsSystemException("Error updating ticket with operation", t);
		}
	}
	
	/**
	 * restituisce la lista delle priorità di intervento.
	 * @return La lista delle priorità di intervento.
	 */
	@Override
	public Map<Integer, String> getPriorities() {
	    return this.getConfig().getPriorities();
	}
	@Override
	public String getPriority(int priority) {
	    return this.getConfig().getPriorities().get(new Integer(priority));
	}
	
	protected void sendNotify(Ticket ticket) {
		Integer intervTypeId = ticket.getUserInterventionType();
		InterventionType interventionType = intervTypeId!=null ? this.getInterventionType(intervTypeId) : null;
		
		List<ITicketAlerter> alerters = this.getTicketAlerters();
		if (alerters!=null && !alerters.isEmpty()) {
			for (ITicketAlerter ticketAlerter : alerters) {
				ticketAlerter.notifyAddedTicket(ticket, interventionType);
			}
		}
	}
	
	protected void sendNotify(Ticket ticket, TicketOperation operation) {
		Integer intervTypeId = ticket.getUserInterventionType();
		InterventionType interventionType = intervTypeId!=null ? this.getInterventionType(intervTypeId) : null;
		
		List<ITicketAlerter> alerters = this.getTicketAlerters();
		if (alerters!=null && !alerters.isEmpty()) {
			for (ITicketAlerter ticketAlerter : alerters) {
				ticketAlerter.notifyTicketOperation(ticket, operation, interventionType);
			}
		}
	}
	
	@Override
	public Map<Integer, InterventionType> getInterventionTypes() {
	    return this.getConfig().getInterventionTypes();
	}
	@Override
	public InterventionType getInterventionType(Integer intervTypeId) {
	    return this.getConfig().getInterventionType(intervTypeId);
	}
	
	protected List<ITicketAlerter> getTicketAlerters() {
		return _ticketAlerters;
	}
	public void setTicketAlerters(List<ITicketAlerter> ticketAlerters) {
		this._ticketAlerters = ticketAlerters;
	}
	
	public WttConfig getConfig() {
		return _config;
	}
	protected void setConfig(WttConfig config) {
		this._config = config;
	}
	
	public ITicketDAO getTicketDAO() {
		return _ticketDAO;
	}
	public void setTicketDAO(ITicketDAO ticketDAO) {
		this._ticketDAO = ticketDAO;
	}
	
	public IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	/**
	 * Returns the configuration manager.
	 * @return The Configuration manager.
	 */
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	/**
	 * Set method for Spring bean injection.<br /> Set the Configuration manager.
	 * @param configManager The Configuration manager.
	 */
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private WttConfig _config;
	
	private ITicketDAO _ticketDAO;
	private List<ITicketAlerter> _ticketAlerters = new ArrayList<ITicketAlerter>();
	private IKeyGeneratorManager _keyGeneratorManager;
	private ConfigInterface _configManager;
	
}