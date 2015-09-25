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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook;

import java.util.Date;
import java.util.List;

import com.agiletec.plugins.jpaddressbook.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpaddressbook.util.JpaddressbookTestHelper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;

public class TestAddressBookManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._addressBookManager = (IAddressBookManager) this.getService(JpaddressbookSystemConstants.ADDRESSBOOK_MANAGER);
		this._helper = new JpaddressbookTestHelper(this.getApplicationContext());
	}
	
	public void testAddGetContact() throws Throwable {
		try {
			Date birthdate1 = this._helper.setBirthdate(1981, 10, 25);
			IUserProfile profile1 = this._helper.createUserProfile(null, 
					"name1 surname1", "indirizzoemail1@inesistente.itt", birthdate1, "it");
			Contact contact1 = this._helper.createContact(null, "editorCoach", true, profile1);
			// Aggiunta contatto1
			this._addressBookManager.addContact(contact1);
			IContact added1 = this._addressBookManager.getContact(contact1.getId());
			this.compareContacts(contact1, added1);
			assertNotNull(added1.getId());
			assertNotNull(added1.getContactInfo().getId());
			
			Date birthdate2 = this._helper.setBirthdate(1982, 10, 25);
			IUserProfile profile2 = this._helper.createUserProfile("pippo", 
					"name2 surname2", "indirizzoemail2@inesistente.itt", birthdate2, "it");
			Contact contact2 = this._helper.createContact("pippo", "editorCoach", true, profile2);
			// Aggiunta contatto2
			this._addressBookManager.addContact(contact2);
			IContact added2 = this._addressBookManager.getContact(contact2.getId());
			this.compareContacts(contact2, added2);
			assertEquals("pippo", added2.getId());
			assertEquals("pippo", added2.getContactInfo().getId());
			assertEquals("pippo", profile2.getId());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testUpdateDeleteContact() throws Throwable {
		try {
			Date birthdate = this._helper.setBirthdate(1982, 10, 25);
			IUserProfile profile = this._helper.createUserProfile(null, 
					"name surname", "indirizzoemail@inesistente.itt", birthdate, "it");
			Contact contact = this._helper.createContact(null, "editorCoach", true, profile);
			// Aggiunta contatto
			this._addressBookManager.addContact(contact);
			IContact added = this._addressBookManager.getContact(contact.getId());
			this.compareContacts(contact, added);
			
			// Modifica contatto
			Date newBirthdate = this._helper.setBirthdate(1985, 12, 02);
			IUserProfile modifiedProfile = this._helper.createUserProfile(contact.getId(), 
					"nameMOD surnameMOD", "indirizzoemailMOD@inesistente.itt", newBirthdate, "en");
			Contact modifiedContact = this._helper.createContact(contact.getId(), contact.getOwner(), false, modifiedProfile);
			this._addressBookManager.updateContact(contact.getOwner(), modifiedContact);
			IContact updated = this._addressBookManager.getContact(contact.getId());
			this.compareContacts(modifiedContact, updated);
			
			// Rimozione contatto
			this._addressBookManager.deleteContact(contact.getOwner(), contact.getId());
			IContact removed = this._addressBookManager.getContact(modifiedContact.getId());
			assertNull(removed);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testGetAllowedContacts() throws Throwable {
		try {
			Date birthdate1 = this._helper.setBirthdate(1981, 10, 25);
			Date birthdate2 = this._helper.setBirthdate(1982, 10, 25);
			Date birthdate3 = this._helper.setBirthdate(1983, 10, 25);
			Date birthdate4 = this._helper.setBirthdate(1984, 10, 25);
			Date birthdate5 = this._helper.setBirthdate(1985, 10, 25);
			this._helper.addContact("1", "editorCoach", false, "name1 surname1", "email1", birthdate1, "it");
			this._helper.addContact("2", "editorCoach", false, "name2 surname2", "email2", birthdate2, "en");
			this._helper.addContact("3", "editorCustomers", false, "name3 surname3", "email3", birthdate3, "fr");
			this._helper.addContact("4", "editorCoach", true, "name4 surname4", "email2bis", birthdate4, "de");
			this._helper.addContact("5", "editorCustomers", false, "name5 surname5", "email5", birthdate5, "it");
			
			List<String> contacts = this._addressBookManager.getAllowedContacts("editorCustomers", null);
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			EntitySearchFilter[] filters = new EntitySearchFilter[0];
			contacts = this._addressBookManager.getAllowedContacts("editorCustomers", filters);
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			contacts = this._addressBookManager.getAllowedContacts("editorCoach", filters);
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			EntitySearchFilter filter1 = new EntitySearchFilter("email", true, "email", true);
			EntitySearchFilter[] filters1 = new EntitySearchFilter[] {filter1};
			contacts = this._addressBookManager.getAllowedContacts("editorCoach", filters1);
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			EntitySearchFilter filter2 = new EntitySearchFilter("email", true, "email2", true);
			EntitySearchFilter[] filters2 = new EntitySearchFilter[] {filter2};
			contacts = this._addressBookManager.getAllowedContacts("editorCoach", filters2);
			this.compareIds(new String[] { "2", "4" }, contacts);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	protected void compareContacts(IContact expected, IContact received) {
		assertEquals(expected.getId(), received.getId());
		assertEquals(expected.getOwner(), received.getOwner());
		assertEquals(expected.isPublicContact(), received.isPublicContact());
		String expectedXML = ((UserProfile) expected.getContactInfo()).getXML();
		String receivedXML = ((UserProfile) received.getContactInfo()).getXML();
		assertEquals(expectedXML, receivedXML);
	}
	
	private void compareIds(String[] expected, List<String> received) {
		assertEquals(expected.length, received.size());
		for (String id : expected) {
			if (!received.contains(id)) {
				throw new Error("Expected id \"" + id + "\" not found");
			}
		}
	}
	
	private IAddressBookManager _addressBookManager;
	private JpaddressbookTestHelper _helper;
	
}