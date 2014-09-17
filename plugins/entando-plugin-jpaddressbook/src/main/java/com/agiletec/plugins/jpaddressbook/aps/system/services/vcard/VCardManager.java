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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.AddressBookDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.parse.IVCardDOM;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.util.VCardCreator;

/**
 * @author A.Cocco
 */
public class VCardManager extends AbstractService implements IVCardManager {

	private static final Logger _logger = LoggerFactory.getLogger(VCardManager.class);
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().debug(this.getName() + ": initialized ");
	}
	
	@Override
	public void updateVCardMapping(List<VCardContactField> fields) throws ApsSystemException {
		try {
			this.getVcardDOM().buildVcardMappingDOM(fields);
			String vcardMapping = this.getVcardDOM().getXMLDocument();
			this.getConfigManager().updateConfigItem(JpaddressbookSystemConstants.VCARD_MAPPING_ITEM, vcardMapping);
		} catch (Throwable t) {
			_logger.error("Error updating vcard mapping", t);
			throw new ApsSystemException("Error on updating vcard mapping", t);
		}
	}
	
	@Override
	public String getVCard(String contactId) throws ApsSystemException {
		String vcard = null;
		try {
			IContact contact = this.getAddressBookManager().getContact(contactId);
			if (null != contact) {
				String vcardMapping = this.getVcardCreator().createVCard(contact, this.getVCardFields());
				StringBuffer vcardBuffer = new StringBuffer(vcardMapping);
				vcard = vcardBuffer.toString();
			}
		} catch (Throwable t) {
			_logger.error("Error creating vcard mapping", t);
			throw new ApsSystemException("Error on creating vcard mapping", t);
		}
		return vcard;
	}
	
	/**
	 * Returns List VCard fields
	 * @return List VCard fields
	 */
	@Override
	public List<VCardContactField> getVCardFields() throws ApsSystemException {
		List<VCardContactField> vCardFields = new ArrayList<VCardContactField>();
		try {
			String mappingVCards = this.getConfigManager().getConfigItem(JpaddressbookSystemConstants.VCARD_MAPPING_ITEM);
			this.getVcardDOM().readVcardMapping(mappingVCards);
			vCardFields = this.getVcardDOM().parseVcardMapping();
		} catch (Throwable t) {
			_logger.error("Error loading VCard fields", t);
			throw new ApsSystemException("Error loading VCard fields", t);
		}
		return vCardFields;
	}
	
	protected IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	/**
	 * Sets vcard creator
	 * @param vcardCreator the vcardCreator to set
	 */
	public void setVcardCreator(VCardCreator vcardCreator) {
		this._vcardCreator = vcardCreator;
	}
	
	/**
	 * Returns vcard creator
	 * @return the vcardCreator
	 */
	protected VCardCreator getVcardCreator() {
		return _vcardCreator;
	}
	
	/**
	 * Sets VCard DOM
	 * @param vcardDOM the vcardDOM to set
	 */
	public void setVcardDOM(IVCardDOM vcardDOM) {
		this._vcardDOM = vcardDOM;
	}
	
	/**
	 * Returns VCard DOM
	 * @return the vcardDOM
	 */
	protected IVCardDOM getVcardDOM() {
		return _vcardDOM;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private IAddressBookManager _addressBookManager;
	private VCardCreator _vcardCreator;
	private IVCardDOM _vcardDOM;
	
	private ConfigInterface _configManager;
	
}