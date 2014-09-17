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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.ContactRecord;

/**
 * @author E.Santoboni
 */
public class AddressBookSearcherDAO extends AbstractEntitySearcherDAO implements IAddressBookSearcherDAO {

	private static final Logger _logger = LoggerFactory.getLogger(AddressBookSearcherDAO.class);
	
	@Override
	public List<String> searchAllowedContacts(String owner, EntitySearchFilter[] filters) {
		List<String> contactIds = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(owner, filters, conn);
			result = stat.executeQuery();
			this.flowResult(contactIds, filters, result);
		} catch (Throwable t) {
			_logger.error("Error on search contacts",  t);
			throw new RuntimeException("Error on search contacts", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return contactIds;
	}
	
	private PreparedStatement buildStatement(String owner, EntitySearchFilter[] filters, Connection conn) {
		String query = this.createQueryString(owner, filters);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = super.addAttributeFilterStatementBlock(filters, index, stat);
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
			if (null != owner) {
				stat.setString(++index, owner);
			}
		} catch (Throwable t) {
			_logger.error("Error creating statement",  t);
			throw new RuntimeException("Error creating statement", t);
		}
		return stat;
	}
	
	private String createQueryString(String owner, EntitySearchFilter[] filters) {
		StringBuffer query = this.createBaseQueryBlock(filters, false);
		
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
		hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
		
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		query.append(" (").append(this.getEntityMasterTableName()).append(".contactowner IS NULL ");
		if (owner != null) {
			query.append(" OR ").append(this.getEntityMasterTableName()).append(".contactowner = ? ");
			query.append(" OR ").append(this.getEntityMasterTableName()).append(".publiccontact = 1 ");
		}
		query.append(" ) ");
		
		boolean ordered = appendOrderQueryBlocks(filters, query, false);
		//System.out.println("********** " + query.toString());
		return query.toString();
	}
	
	@Override
	protected ApsEntityRecord createRecord(ResultSet result) throws Throwable {
		ContactRecord contact = new ContactRecord();
		contact.setId(result.getString("contactkey"));
		contact.setXml(result.getString("xml"));
		contact.setTypeCode(result.getString("profiletype"));
		contact.setOwner(result.getString("contactowner"));
		contact.setPublicContact(result.getInt("publiccontact") == 1);
		return contact;
	}
	
	@Override
	protected String getEntityMasterTableIdFieldName() {
		return "contactkey";
	}
	
	@Override
	protected String getEntityMasterTableIdTypeFieldName() {
		return "profiletype";
	}
	
	@Override
	protected String getEntityMasterTableName() {
		return "jpaddressbook_contacts";
	}
	
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "contactkey";
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "jpaddressbook_contactsearch";
	}
	
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "jpaddressbook_attroles";
	}
	
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "contactkey";
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		if (metadataFieldKey.equals(IEntityManager.ENTITY_ID_FILTER_KEY)) {
			return this.getEntityMasterTableIdTypeFieldName();
		} else if (metadataFieldKey.equals(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
			return this.getEntityMasterTableIdTypeFieldName();
		} else if (metadataFieldKey.equals(IAddressBookManager.CONTACT_OWNER_FILTER_KEY)) {
			return "contactowner";
		} else throw new RuntimeException("Search key '" + metadataFieldKey + "' not defined");
	}

}