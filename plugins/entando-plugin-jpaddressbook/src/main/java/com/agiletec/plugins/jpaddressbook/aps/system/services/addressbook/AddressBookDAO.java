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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.ContactRecord;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;

/**
 * @author E.Santoboni
 */
public class AddressBookDAO extends AbstractEntityDAO implements IAddressBookDAO {

	private static final Logger _logger = LoggerFactory.getLogger(AddressBookDAO.class);
	
	@Override
	public String getLoadEntityRecordQuery() {
		return GET_CONTACT_VO;
	}
	
	@Override
	public ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		ContactRecord contact = new ContactRecord();
		contact.setId(res.getString("contactkey"));
		contact.setTypeCode(res.getString("profiletype"));
		contact.setXml(res.getString("contactxml"));
		contact.setOwner(res.getString("contactowner"));
		contact.setPublicContact(res.getInt("publiccontact") == 1);
		return contact;
	}
	
	@Override
	public void addContact(IContact contact) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_CONTACT);
			stat.setString(1, contact.getId());
			stat.setString(2, contact.getContactInfo().getTypeCode());
			stat.setString(3, contact.getContactInfo().getXML());
			stat.setString(4, contact.getOwner());
			if (contact.isPublicContact()) {
				stat.setInt(5, 1);
			} else {
				stat.setInt(5, 0);
			}
			stat.executeUpdate();
			this.addEntitySearchRecord(contact.getId(), contact.getContactInfo(), conn);
			this.addEntityAttributeRoleRecord(contact.getId(), contact.getContactInfo(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on adding Contact",  t);
			throw new RuntimeException("Error on adding Contact", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteContact(String contactKey) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteEntitySearchRecord(contactKey, conn);
			this.deleteRecordsByEntityId(contactKey, this.getRemovingAttributeRoleRecordQuery(), conn);
			stat = conn.prepareStatement(DELETE_CONTACT_BY_KEY);
			stat.setString(1, contactKey);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting contact by id {}", contactKey, t);
			throw new RuntimeException("Error on deleting contact by id " + contactKey, t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateContact(IContact contact) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteEntitySearchRecord(contact.getId(), conn);
			this.deleteRecordsByEntityId(contact.getId(), this.getRemovingAttributeRoleRecordQuery(), conn);
			stat = conn.prepareStatement(UPDATE_CONTACT);
			stat.setString(1, contact.getContactInfo().getTypeCode());
			stat.setString(2, contact.getContactInfo().getXML());
			if (contact.isPublicContact()) {
				stat.setInt(3, 1);
			} else {
				stat.setInt(3, 0);
			}
			stat.setString(4, contact.getId());
			stat.executeUpdate();
			this.addEntitySearchRecord(contact.getId(), contact.getContactInfo(), conn);
			this.addEntityAttributeRoleRecord(contact.getId(), contact.getContactInfo(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on updating contact",  t);
			throw new RuntimeException("Error on updating contact", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		throw new RuntimeException("Method not supported");
	}
	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		throw new RuntimeException("Method not supported");
	}
	@Override
	protected String getAddEntityRecordQuery() {
		throw new RuntimeException("Method not supported");
	}
	@Override
	protected String getDeleteEntityRecordQuery() {
		throw new RuntimeException("Method not supported");
	}
	@Override
	protected String getUpdateEntityRecordQuery() {
		throw new RuntimeException("Method not supported");
	}
	
	@Override
	protected String getAddingSearchRecordQuery() {
		return ADD_CONTACT_SEARCH_RECORD;
	}
	
	@Override
	protected String getExtractingAllEntityIdQuery() {
		return GET_ALL_ENTITY_ID;
	}
	
	@Override
	protected String getRemovingSearchRecordQuery() {
		return DELETE_CONTACT_SEARCH_RECORD;
	}
	
	@Override
	protected String getAddingAttributeRoleRecordQuery() {
		return ADD_ATTRIBUTE_ROLE_RECORD;
	}
	
	@Override
	protected String getRemovingAttributeRoleRecordQuery() {
		return DELETE_ATTRIBUTE_ROLE_RECORD;
	}
	
	private final String GET_CONTACT_VO = 
		"SELECT contactkey, profiletype, contactxml, contactowner, publiccontact FROM jpaddressbook_contacts WHERE contactkey = ? ";
	
	private static final String INSERT_CONTACT = 
		"INSERT INTO jpaddressbook_contacts(contactkey, profiletype, contactxml, contactowner, publiccontact) VALUES (?, ?, ?, ?, ?) ";
	
	private final String DELETE_CONTACT_BY_KEY = 
		"DELETE FROM jpaddressbook_contacts WHERE contactkey = ? ";
	
	private final String UPDATE_CONTACT = 
		"UPDATE jpaddressbook_contacts SET profiletype = ? , contactxml = ? , publiccontact = ? WHERE contactkey = ? ";
	
	private final String ADD_CONTACT_SEARCH_RECORD =
		"INSERT INTO jpaddressbook_contactsearch(contactkey, attrname, textvalue, datevalue, numvalue, langcode) VALUES (?, ?, ?, ?, ?, ?) ";
	
	private final String DELETE_CONTACT_SEARCH_RECORD =
		"DELETE FROM jpaddressbook_contactsearch WHERE contactkey = ? ";
	
	private final String GET_ALL_ENTITY_ID = 
		"SELECT contactkey FROM jpaddressbook_contacts";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO jpaddressbook_attroles (contactkey, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM jpaddressbook_attroles WHERE contactkey = ? ";
	
}