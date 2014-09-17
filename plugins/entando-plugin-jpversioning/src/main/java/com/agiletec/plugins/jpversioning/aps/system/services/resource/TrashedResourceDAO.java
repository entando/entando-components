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
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;

/**
 * Data Access Object per gli oggetti risorsa cestinati.
 * @author G.Cocco
 */
public class TrashedResourceDAO extends AbstractDAO implements ITrashedResourceDAO {

	private static final Logger _logger = LoggerFactory.getLogger(TrashedResourceDAO.class);
	
	@Override
	public ResourceRecordVO getTrashedResource(String id) {
		ResourceRecordVO resourceVo = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_TRASHED_RESOURCE);
			stat.setString(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				resourceVo = new ResourceRecordVO();
				resourceVo.setId(id);
				resourceVo.setResourceType(res.getString(1));
				resourceVo.setDescr(res.getString(2));
				resourceVo.setMainGroup(res.getString(3));
				resourceVo.setXml(res.getString(4));
			}
		} catch (Throwable t) {
			_logger.error("Error loading record for trashed resource",  t);
			throw new RuntimeException("Error loading record for trashed resource", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return resourceVo;
	}
	
	@Override
	public void addTrashedResource(ResourceInterface resource) {
		Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = this.getConnection();
        	conn.setAutoCommit(false);
        	stat = conn.prepareStatement(ADD_RESOURCE);
			stat.setString(1, resource.getId());
			stat.setString(2, resource.getType());
			stat.setString(3, resource.getDescr());
			stat.setString(4, resource.getMainGroup());
			stat.setString(5, resource.getXML());
			stat.executeUpdate();
			conn.commit();
        } catch (Throwable t) {
        	this.executeRollback(conn);
            _logger.error("Error adding record for trashed resource",  t);
			throw new RuntimeException("Error adding record for trashed resource", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
	}
	
	@Override
	public void delTrashedResource(String id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_RESOURCE);
			stat.setString(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing record for trashed resource",  t);
			throw new RuntimeException("Error removing record for trashed resource", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<String> searchTrashedResourceIds(String resourceTypeCode, String text, List<String> allowedGroups) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<String> resources = new ArrayList<String>();
		try {
			conn = this.getConnection();
			stat = this.buildStatement(resourceTypeCode, allowedGroups, conn);
			res = stat.executeQuery();
			while (res.next()) {
				String descr = res.getString(3);
				if (this.checkText(text, descr)) {
					resources.add(res.getString(1));
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento id risorse", t);
			throw new RuntimeException("Errore in caricamento id risorse", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return resources;
	}
	
	/**
	 * Metodo di servizio. 
	 * Serve a controllare se il testo immesso testo (insertedText) 
	 * è contenuto dentro un'altro (descr), ed in caso di esito 
	 * affermativo restituisce true.
	 */
	private boolean checkText(String insertedText, String descr) {
		boolean validate = false;
		if (insertedText == null || insertedText.trim().length() == 0 || 
				descr.toLowerCase().indexOf(insertedText.trim().toLowerCase()) != -1) {
			validate = true;
		}
		return validate;
	}
	
	private PreparedStatement buildStatement(String type, Collection<String> groupCodes, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		String query = this.createQuery(type, groupCodes);
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			if (null != type && type.trim().length() > 0) {
				stat.setString(++index, type);
			}
			if (groupCodes != null && groupCodes.size()>0) {
				Iterator<String> groupIter = groupCodes.iterator();
				while (groupIter.hasNext()) {
					String groupName = (String) groupIter.next();
					stat.setString(++index, groupName);
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in fase di creazione statement", t);
			throw new RuntimeException("Errore in fase di creazione statement", t);
		}
		return stat;
	}
	
	private String createQuery(String type, Collection<String> groupCodes) {
		StringBuffer query = new StringBuffer(LOAD_RESOURCES_START_BLOCK);
		boolean check = false;
		if (null != type && type.trim().length() > 0) {
			query.append(APPEND_STRING_WHERE).append(LOAD_RESOURCES_TYPE_SEARCH_BLOCK);
			check = true;
		}
		if (groupCodes != null && groupCodes.size()>0) {
			if (!check) {
				query.append(APPEND_STRING_WHERE);
				check = true;
			} else {
				query.append(APPEND_STRING_AND);
			}
			query.append(APPEND_STRING_GROUP_SEARCH_START);
			int size = groupCodes.size();
			for (int i=0; i<size; i++) {
				if (i!=0) query.append(APPEND_STRING_OR);
				query.append(APPEND_STRING_GROUP_SEARCH);
			}
			query.append(APPEND_STRING_GROUP_SEARCH_END);
		}
		query.append(LOAD_RESOURCES_ORDER_STRING_BLOCK);
		return query.toString();
	}
	
	private final String LOAD_RESOURCES_START_BLOCK = 
		"SELECT resid, restype, descr, maingroup, resxml FROM jpversioning_trashedresources ";
	private final String APPEND_STRING_WHERE = "WHERE ";
	private final String LOAD_RESOURCES_TYPE_SEARCH_BLOCK = "restype = ? ";
	private final String APPEND_STRING_AND = "AND ";
	private final String APPEND_STRING_GROUP_SEARCH_START = "( ";
	private final String APPEND_STRING_GROUP_SEARCH = "maingroup = ? ";
	private final String APPEND_STRING_OR = "OR ";
	private final String APPEND_STRING_GROUP_SEARCH_END = ") ";
	private final String LOAD_RESOURCES_ORDER_STRING_BLOCK = "ORDER BY descr ";
	
	private final String SELECT_TRASHED_RESOURCE = 
		"SELECT restype, descr, maingroup, resxml FROM jpversioning_trashedresources WHERE resid = ? ";
	
	private final String ADD_RESOURCE = 
		"INSERT INTO jpversioning_trashedresources ( resid, restype, descr, maingroup, resxml ) VALUES ( ? , ? , ? , ? , ? )";
	
	private final String DELETE_RESOURCE = 
		"DELETE FROM jpversioning_trashedresources WHERE resid = ? ";
	
}