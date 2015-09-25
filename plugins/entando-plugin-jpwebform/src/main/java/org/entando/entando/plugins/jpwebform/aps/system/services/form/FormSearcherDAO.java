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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Implementation of Data Access Object delegated for the Message searching operations.
 * @author E.Santoboni
 */
public class FormSearcherDAO extends AbstractEntitySearcherDAO implements IFormSearcherDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(FormSearcherDAO.class);

	@Override
	protected ApsEntityRecord createRecord(ResultSet result) throws Throwable {
		MessageRecordVO record = new MessageRecordVO();
		record.setId(result.getString("messageid"));
		record.setXml(result.getString("messagexml"));
		record.setTypeCode(result.getString("messagetype"));
		record.setUsername(result.getString("username"));
		record.setLangCode(result.getString("langcode"));
		record.setCreationDate(result.getTimestamp("creationdate"));
		record.setVersionType(result.getInt("versiontype"));
		return record;
	}
	
	@Override
	public List<String> searchId(EntitySearchFilter[] filters, boolean answered) throws ApsSystemException {
		Connection conn = null;
		List<String> idList = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, false, answered, conn);
			result = stat.executeQuery();
			this.flowResult(idList, filters, result);
		} catch (Throwable t) {
			_logger.error("Error loading list ",  t);
			throw new RuntimeException("Error loading list ", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return idList;
	}

	protected PreparedStatement buildStatement(EntitySearchFilter[] filters, boolean selectAll, boolean answered, Connection conn) {
		String query = this.createQueryString(filters, selectAll, answered);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = this.addAttributeFilterStatementBlock(filters, index, stat);
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
		} catch (Throwable t) {
			_logger.error("Error creating statement",  t);
			throw new RuntimeException("Error creating statement", t);
		}
		return stat;
	}

	protected String createQueryString(EntitySearchFilter[] filters, boolean selectAll, boolean answered) {
		StringBuffer query = this.createBaseQueryBlock(filters, selectAll);
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
		hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);

		this.appendAnsweredFilterQueryBlocks(answered, query, hasAppendWhereClause);

		appendOrderQueryBlocks(filters, query, false);
		return query.toString();
	}

	protected boolean appendAnsweredFilterQueryBlocks(boolean answered, StringBuffer query, boolean hasAppendWhereClause) {
		String masterTableName = this.getEntityMasterTableName();
		String masterTableIdFieldName = this.getEntityMasterTableIdFieldName();
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		if (answered) {
			query.append(masterTableName).append(".").append(masterTableIdFieldName).append(
					" IN ( SELECT messageid FROM jpwebform_messageanswers )");
		} else {
			query.append(masterTableName).append(".").append(masterTableIdFieldName).append(
			" NOT IN ( SELECT messageid FROM jpwebform_messageanswers )");
		}
		return hasAppendWhereClause;
	}
	
	@Override
	protected String getEntityMasterTableName() {
		return "jpwebform_messages";
	}

	@Override
	protected String getEntityMasterTableIdFieldName() {
		return "messageid";
	}

	@Override
	protected String getEntityMasterTableIdTypeFieldName() {
		return "messagetype";
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "jpwebform_messagesearch";
	}

	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "messageid";
	}
	
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "jpwebform_attributeroles";
	}
	
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "messageid";
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		if (metadataFieldKey.equals(IEntityManager.ENTITY_ID_FILTER_KEY)) {
			return this.getEntityMasterTableIdFieldName();
		} else if (metadataFieldKey.equals(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
			return this.getEntityMasterTableIdTypeFieldName();
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_USERNAME)) {
			return "username";
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_CREATION_DATE)) {
			return "creationdate";
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_SEND_DATE)) {
			return "senddate";
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_VERSION_TYPE)) {
			return "versiontype";
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_MESSAGE_TYPE)) {
			return "messagetype";
		} else if (metadataFieldKey.equals(IFormManager.FILTER_KEY_DONE)) {
			return "done";
		} else throw new RuntimeException("Chiave di ricerca '" + metadataFieldKey + "' non riconosciuta");
	}
	
}