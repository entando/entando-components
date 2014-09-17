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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook;

import java.util.Date;

import javax.sql.DataSource;

import com.agiletec.plugins.jpaddressbook.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpaddressbook.util.JpaddressbookTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.AddressBookDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.ContactRecord;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;

public class TestAddressBookDAO extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		AddressBookDAO addressBookDAO = new AddressBookDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		addressBookDAO.setDataSource(dataSource);
		ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
		addressBookDAO.setLangManager(langManager);
		this._addressBookDAO = addressBookDAO;
		
		this._helper = new JpaddressbookTestHelper(this.getApplicationContext());
	}
	
	public void testAddUpdateDeleteContact() throws Throwable {
		Date birthdate = this._helper.setBirthdate(1982, 10, 25);
		IUserProfile profile = this._helper.createUserProfile("mainEditor", 
				"name surname", "indirizzoemail@inesistente.itt", birthdate, "it");
		Contact contact = this._helper.createContact("1", "editorCoach", true, profile);
		try {
			// Aggiunta contatto
			this._addressBookDAO.addContact(contact);
			ContactRecord record = (ContactRecord) this._addressBookDAO.loadEntityRecord(contact.getId());
			this.compareContacts(contact, record);
			
			// Modifica contatto
			Date newBirthdate = this._helper.setBirthdate(1964, 07, 01);
			IUserProfile modifiedProfile = this._helper.createUserProfile("supervisorCoach", 
					"nameMOD surnameMOD", "indirizzoemailMOD@inesistente.itt", newBirthdate, "en");
			Contact modifiedContact = this._helper.createContact(contact.getId(), "pageManagerCoach", false, modifiedProfile);
			this._addressBookDAO.updateContact(modifiedContact);
			record = (ContactRecord) this._addressBookDAO.loadEntityRecord(contact.getId());
			modifiedContact.setOwner(contact.getOwner()); // FIXME ? Non modifica il proprietario del contatto
			this.compareContacts(modifiedContact, record);
			
			// Rimozione contatto
			this._addressBookDAO.deleteContact(contact.getId());
			record = (ContactRecord) this._addressBookDAO.loadEntityRecord(modifiedContact.getId());
			assertNull(record);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	protected void compareContacts(IContact expected, ContactRecord received) {
		assertEquals(expected.getId(), received.getId());
		assertEquals(expected.getOwner(), received.getOwner());
		assertEquals(expected.isPublicContact(), received.isPublicContact());
		String expectedXML = ((UserProfile) expected.getContactInfo()).getXML();
		assertEquals(expectedXML, received.getXml());
	}
	
	private IAddressBookDAO _addressBookDAO;
	private JpaddressbookTestHelper _helper;
	
}