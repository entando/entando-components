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

import javax.sql.DataSource;

import com.agiletec.plugins.jpaddressbook.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpaddressbook.util.JpaddressbookTestHelper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.AddressBookSearcherDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookSearcherDAO;

public class TestAddressBookSearcherDAO extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		AddressBookSearcherDAO addressBookSearcherDAO = new AddressBookSearcherDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		addressBookSearcherDAO.setDataSource(dataSource);
		this._addressBookSearcherDAO = addressBookSearcherDAO;
		
		this._helper = new JpaddressbookTestHelper(this.getApplicationContext());
	}
	
	public void testSearchAllowedContacts() throws Throwable {
		try {
			Date birthdate1 = this._helper.setBirthdate(1981, 10, 25);
			Date birthdate2 = this._helper.setBirthdate(1982, 10, 25);
			Date birthdate3 = this._helper.setBirthdate(1983, 10, 25);
			Date birthdate4 = this._helper.setBirthdate(1984, 10, 25);
			Date birthdate5 = this._helper.setBirthdate(1985, 10, 25);
			this._helper.addContact("1", "editorCoach", false, "name1 surname1", "email1", birthdate1, "it");
			this._helper.addContact("2", "editorCoach", false, "name2 surname2", "email2", birthdate2, "en");
			this._helper.addContact("3", "editorCustomers", false, "name3 surname3", "email3", birthdate3,  "fr");
			this._helper.addContact("4", "editorCoach", true, "name4 surname4", "email2bis", birthdate4, "de");
			this._helper.addContact("5", "editorCustomers", false, "name5 surname5", "email5", birthdate5, "it");
			
			List<String> contacts = this._addressBookSearcherDAO.searchAllowedContacts("editorCustomers", null);
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			EntitySearchFilter[] filters = new EntitySearchFilter[0];
			contacts = this._addressBookSearcherDAO.searchAllowedContacts("editorCustomers", filters);
			this.compareIds(new String[] { "3", "4", "5" }, contacts);
			
			contacts = this._addressBookSearcherDAO.searchAllowedContacts("editorCoach", filters);
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			filters = new EntitySearchFilter[] {
					new EntitySearchFilter("email", true, "email", true), 
			};
			contacts = this._addressBookSearcherDAO.searchAllowedContacts("editorCoach", filters);
			this.compareIds(new String[] { "1", "2", "4" }, contacts);
			
			filters = new EntitySearchFilter[] {
					new EntitySearchFilter("email", true, "email2", true), 
			};
			contacts = this._addressBookSearcherDAO.searchAllowedContacts("editorCoach", filters);
			this.compareIds(new String[] { "2", "4" }, contacts);
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
	
	private IAddressBookSearcherDAO _addressBookSearcherDAO;
	private JpaddressbookTestHelper _helper;
	
}