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
import java.util.List;

import com.agiletec.plugins.jpaddressbook.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpaddressbook.util.JpaddressbookTestHelper;

import com.agiletec.apsadmin.system.entity.IApsEntityFinderAction;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.opensymphony.xwork2.Action;

public class TestContactFinderAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._addressBookManager = (IAddressBookManager) this.getService(JpaddressbookSystemConstants.ADDRESSBOOK_MANAGER);
		this._helper = new JpaddressbookTestHelper(this.getApplicationContext());
	}
	
	public void testList() throws Throwable {
		try {
			String result = this.executeList("editorCoach");
			assertEquals(Action.SUCCESS, result);
			List<String> contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { }, contacts);
			
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
			
			result = this.executeList("editorCustomers");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			result = this.executeList("editorCoach");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testSearch() throws Throwable {
		try {
			String result = this.executeSearch("editorCoach", "");
			assertEquals(Action.SUCCESS, result);
			List<String> contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { }, contacts);
			
			Date birthdate1 = this._helper.setBirthdate(1981, 10, 25);
			Date birthdate2 = this._helper.setBirthdate(1982, 10, 25);
			Date birthdate3 = this._helper.setBirthdate(1983, 10, 25);
			Date birthdate4 = this._helper.setBirthdate(1984, 10, 25);
			Date birthdate5 = this._helper.setBirthdate(1985, 10, 25);
			this._helper.addContact("1", "editorCoach", false, "name1 surname1", "email1@company.it", birthdate1, "it");
			this._helper.addContact("2", "editorCoach", false, "name2 surname2", "email2@company.it", birthdate2, "en");
			this._helper.addContact("3", "editorCustomers", false, "name3 surname3", "email3@company.it", birthdate3, "fr");
			this._helper.addContact("4", "editorCoach", true, "name4 surname4", "email2bis@company.it", birthdate4, "de");
			this._helper.addContact("5", "editorCustomers", false, "name5 surname5", "email5@company.it", birthdate5, "it");
			
			result = this.executeSearch("editorCustomers", "");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			result = this.executeSearch("editorCoach", "");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			result = this.executeSearch("editorCoach", "email");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			result = this.executeSearch("editorCoach", "email2");
			assertEquals(Action.SUCCESS, result);
			contacts = ((IApsEntityFinderAction) this.getAction()).getSearchResult();
			this.compareIds(new String[] { "2", "4" }, contacts);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testTrashWithErrors() throws Throwable {
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
			
			String result = this.executeTrash("editorCoach", "notExistantContact");
			assertEquals(Action.INPUT, result);
			List<String> fieldErrors = this.getAction().getFieldErrors().get("entityId");
			assertEquals(1, fieldErrors.size());
			
			result = this.executeTrash("editorCoach", "3");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			assertEquals("*CONTATTO NON PROPRIETARIO*", fieldErrors.get(0));
			assertNotNull(this._addressBookManager.getContact("3"));
			
			result = this.executeTrash("editorCustomers", "4");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			assertEquals("*CONTATTO NON PROPRIETARIO*", fieldErrors.get(0));
			assertNotNull(this._addressBookManager.getContact("4"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testTrashSuccessful() throws Throwable {
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

			String result = this.executeTrash("editorCoach", "1");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeTrash("editorCoach", "2");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeTrash("editorCustomers", "3");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeTrash("editorCoach", "4");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeTrash("editorCustomers", "5");
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testDeleteWithErrors() throws Throwable {
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
			
			String result = this.executeDelete("editorCoach", "notExistantContact");
			assertEquals(Action.INPUT, result);
			List<String> fieldErrors = this.getAction().getFieldErrors().get("entityId");
			assertEquals(1, fieldErrors.size());
			
			result = this.executeDelete("editorCoach", "3");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			assertNotNull(this._addressBookManager.getContact("3"));
			
			result = this.executeDelete("editorCustomers", "4");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors().get("contactKey");
			assertEquals(1, fieldErrors.size());
			assertNotNull(this._addressBookManager.getContact("4"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	public void testDeleteSuccessful() throws Throwable {
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

			String result = this.executeDelete("editorCoach", "1");
			assertEquals(Action.SUCCESS, result);
			assertNull(this._addressBookManager.getContact("1"));
			
			result = this.executeDelete("editorCoach", "2");
			assertEquals(Action.SUCCESS, result);
			assertNull(this._addressBookManager.getContact("2"));
			
			result = this.executeDelete("editorCustomers", "3");
			assertEquals(Action.SUCCESS, result);
			assertNull(this._addressBookManager.getContact("3"));
			
			result = this.executeDelete("editorCoach", "4");
			assertEquals(Action.SUCCESS, result);
			assertNull(this._addressBookManager.getContact("4"));
			
			result = this.executeDelete("editorCustomers", "5");
			assertEquals(Action.SUCCESS, result);
			assertNull(this._addressBookManager.getContact("5"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddressBook();
		}
	}
	
	private void compareIds(String[] expected, List<String> received) {
		assertEquals(expected.length, received.size());
		for (String id : expected) {
			if (!received.contains(id)) {
				throw new Error("Expected id \"" + id + "\" not found");
			}
		}
	}
	
	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "list");
		String result = this.executeAction();
		return result;
	}
	
	private String executeSearch(String username, String email) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "search");
		this.addParameter("email_textFieldName", email);
		String result = this.executeAction();
		return result;
	}
	
	private String executeTrash(String username, String entityId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "trash");
		this.addParameter("entityId", entityId);
		String result = this.executeAction();
		return result;
	}
	
	private String executeDelete(String username, String entityId) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpaddressbook/AddressBook", "delete");
		this.addParameter("entityId", entityId);
		String result = this.executeAction();
		return result;
	}
	
	private IAddressBookManager _addressBookManager;
	private JpaddressbookTestHelper _helper;
	
}