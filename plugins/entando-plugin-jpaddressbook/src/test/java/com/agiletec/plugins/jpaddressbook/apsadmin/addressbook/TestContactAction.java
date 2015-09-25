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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpaddressbook.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpaddressbook.util.JpaddressbookTestHelper;

import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.ContactAction;

import com.opensymphony.xwork2.Action;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;

public class TestContactAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._addressBookManager = (IAddressBookManager) this.getService(JpaddressbookSystemConstants.ADDRESSBOOK_MANAGER);
		this._helper = new JpaddressbookTestHelper(this.getApplicationContext());
	}
	
	public void testView() throws Throwable {
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
			
			String result = this.executeView("editorCoach", "notExistantContact");
			assertEquals(Action.INPUT, result);
			List<String> fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			
			result = this.executeView("editorCoach", "3");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			
			result = this.executeView("editorCoach", "1");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("editorCoach", "2");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("editorCustomers", "3");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("editorCoach", "4");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("editorCustomers", "4");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("editorCustomers", "5");
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testNew() throws Throwable {
		try {
			String result = this.executeNew("editorCoach");
			assertEquals(Action.SUCCESS, result);
			IContact contact = ((ContactAction) this.getAction()).getContact();
			assertEquals("editorCoach", contact.getOwner());
			
			// Test entryContact
			this.executeEntryContact("editorCoach");
			assertEquals(Action.SUCCESS, result);
			IContact actionContact = ((ContactAction) this.getAction()).getContact();
			this.compareContacts(contact, actionContact);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testEdit() throws Throwable {
		try {
			Date birthdate1 = this._helper.setBirthdate(1982, 10, 25);
			IUserProfile profile = this._helper.createUserProfile("1", "name1 surname1", "email1", birthdate1, "it");
			IContact contact = this._helper.createContact(profile.getId(), "editorCoach", true, profile);
			this._addressBookManager.addContact(contact);
			
			String result = this.executeEdit("editorCoach", "notExistantContact");
			assertEquals(Action.INPUT, result);
			List<String> fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			assertEquals("*CONTATTO NULLO*", fieldErrors.get(0));
			
			result = this.executeEdit("editorCoach", contact.getId());
			assertEquals(Action.SUCCESS, result);
			IContact actionContact = ((ContactAction) this.getAction()).getContact();
			this.compareContacts(contact, actionContact);
			
			result = this.executeEdit("editorCustomers", contact.getId());
			assertEquals(Action.SUCCESS, result);
			actionContact = ((ContactAction) this.getAction()).getContact();
			this.compareContacts(contact, actionContact);
			
			// Test entryContact
			this.executeEntryContact("editorCoach");
			assertEquals(Action.SUCCESS, result);
			actionContact = ((ContactAction) this.getAction()).getContact();
			this.compareContacts(contact, actionContact);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testSaveWithErrors() throws Throwable {
		try {
			String result = this.executeNew("editorCoach");
			assertEquals(Action.SUCCESS, result);
			
			Map<String, Object> params = new HashMap<String, Object>();
			result = this.executeSave("editorCoach", params);
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(4, fieldErrors.size());
			assertEquals(1, fieldErrors.get("Monotext:fullname").size());
			assertEquals(1, fieldErrors.get("Monotext:email").size());
			assertEquals(1, fieldErrors.get("Date:birthdate").size());
			assertEquals(1, fieldErrors.get("Monotext:language").size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testSaveNewSuccessful() throws Throwable {
		try {
			String username = "editorCoach";
			String result = this.executeNew(username);
			assertEquals(Action.SUCCESS, result);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Monotext:fullname", "name surname");
			params.put("Monotext:email", "email@eeeeeeemail.it");
			params.put("Date:birthdate", "10/09/1986");
			params.put("Monotext:language", "lang");
			result = this.executeSave(username, params);
			assertEquals(Action.SUCCESS, result);
			
			List<String> contactIds = this._addressBookManager.getAllowedContacts(username, null);
			assertEquals(1, contactIds.size());
			IContact contact = this._addressBookManager.getContact(contactIds.get(0));
			assertEquals(username, contact.getOwner());
			assertEquals(false, contact.isPublicContact());
			IUserProfile profile = contact.getContactInfo();
			assertEquals("name surname", ((ITextAttribute) profile.getAttribute("fullname")).getText());
			assertEquals("email@eeeeeeemail.it", ((ITextAttribute) profile.getAttribute("email")).getText());
			assertEquals("10/09/1986", ((DateAttribute) profile.getAttribute("birthdate")).getFormattedDate("dd/MM/yyyy"));
			assertEquals("lang", ((ITextAttribute) profile.getAttribute("language")).getText());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testSaveEditSuccessful() throws Throwable {
		try {
			String username = "editorCoach";
			Date birthdate = this._helper.setBirthdate(1982, 10, 25);
			IUserProfile profile = this._helper.createUserProfile("1", "name surname", "email", birthdate, "it");
			IContact contact = this._helper.createContact(profile.getId(), username, true, profile);
			this._addressBookManager.addContact(contact);
			
			String result = this.executeEdit(username, contact.getId());
			assertEquals(Action.SUCCESS, result);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Monotext:fullname", "nameMOD surnameMOD");
			params.put("Monotext:email", "email@eeeeeeemail.it");
			params.put("Date:birthdate", "10/09/1986");
			params.put("Monotext:language", "langMOD");
			result = this.executeSave(username, params);
			assertEquals(Action.SUCCESS, result);
			
			List<String> contactIds = this._addressBookManager.getAllowedContacts(username, null);
			assertEquals(1, contactIds.size());
			contact = this._addressBookManager.getContact(contactIds.get(0));
			assertEquals(username, contact.getOwner());
			assertEquals(true, contact.isPublicContact());
			profile = contact.getContactInfo();
			assertEquals("nameMOD surnameMOD", ((ITextAttribute) profile.getAttribute("fullname")).getText());
			assertEquals("email@eeeeeeemail.it", ((ITextAttribute) profile.getAttribute("email")).getText());
			assertEquals("10/09/1986", ((DateAttribute) profile.getAttribute("birthdate")).getFormattedDate("dd/MM/yyyy"));
			assertEquals("langMOD", ((ITextAttribute) profile.getAttribute("language")).getText());
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
	
	private String executeView(String username, String entityId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "view");
		this.addParameter("entityId", entityId);
		return this.executeAction();
	}
	
	private String executeNew(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "new");
		return this.executeAction();
	}
	
	private String executeEdit(String username, String entityId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "edit");
		this.addParameter("entityId", entityId);
		return this.executeAction();
	}
	
	private String executeSave(String username, Map<String, Object> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "save");
		this.addParameters(params);
		return this.executeAction();
	}
	
	private String executeEntryContact(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "entryContact");
		return this.executeAction();
	}
	
	private IAddressBookManager _addressBookManager;
	private JpaddressbookTestHelper _helper;
	
}