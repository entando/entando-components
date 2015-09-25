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
package org.entando.entando.plugins.jpfileattribute.aps.system.file;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public class FilePersistenceDAO extends AbstractSearcherDAO implements IFilePersistenceDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(FilePersistenceDAO.class);
	
	@Override
	public List<String> searchId(FieldSearchFilter[] filters) {
		return super.searchId(filters);
	}
	
	@Override
	public void updateEntityFiles(String entityId, List<Integer> fileIds) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			StringBuilder query = new StringBuilder(DELETE_FILES_BY_ENTITY_ID);
			for (int i = 0; i < fileIds.size(); i++) {
				query.append(" AND id != ? ");
			}
			stat = conn.prepareStatement(query.toString());
			int index = 1;
			stat.setString(index++, entityId);
			for (int i = 0; i < fileIds.size(); i++) {
				stat.setInt(index++, fileIds.get(i));
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting entity id",  t);
			throw new RuntimeException("Error while deleting entity id", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
		for (int i = 0; i < fileIds.size(); i++) {
			this.approve(fileIds.get(i), entityId);
		}
	}
	
	@Override
	public AttachedFile loadFile(Integer id) {
		Connection conn = null;
		AttachedFile file = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			//entityclass, filename, contenttype, base64
			stat = conn.prepareStatement(LOAD);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				file = new AttachedFile();
				file.setId(id);
				file.setEntityId(res.getString(1));
				file.setEntityClass(res.getString(2));
				file.setFilename(res.getString(3));
				file.setContentType(res.getString(4));
				//file.setBase64(res.getBytes(5));
				file.setUsername(res.getString(5));
				file.setDate(new Date(res.getDate(6).getTime()));
				int approved = (res.getInt(7));
				file.setApproved(approved == 1);
			}
		} catch (Throwable t) {
			_logger.error("Error loading file - id {}", id,  t);
			throw new RuntimeException("Error loading file - id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return file;
	}
	
	@Override
	public void approve(Integer id, String entityId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			//entityclass, filename, contenttype, base64
			stat = conn.prepareStatement(APPROVE);
			stat.setInt(1, 1);
			stat.setString(2, entityId);
			stat.setInt(3, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error approving file - id {}", id, t);
			throw new RuntimeException("Error approving file - id " + id, t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void addFile(AttachedFile file) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			//int id = this.extractNextId(conn);
			//file.setId(id);
			conn.setAutoCommit(false);
			//id, entityclass, filename, contenttype, base64
			stat = conn.prepareStatement(ADD);
			stat.setInt(1, file.getId());
			stat.setString(2, file.getEntityId());
			stat.setString(3, file.getEntityClass());
			stat.setString(4, file.getFilename());
			stat.setString(5, file.getContentType());
			//stat.setBytes(6, file.getBase64());
			stat.setString(6, file.getUsername());
			stat.setDate(7, new java.sql.Date(new Date().getTime()));
			int approved = (file.isApproved()) ? 1 : 0;
			stat.setInt(8, approved);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a file", t);
			throw new RuntimeException("Error while adding a file", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	/*
	@Override
	public void deleteFilesByEntityId(String entityId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_FILES_BY_ENTITY_ID);
			stat.setString(1, entityId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while deleting files by entity id", "deleteFilesByEntityId");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	*/
	@Override
	public void deleteFile(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_FILE);
			stat.setInt(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting file", t);
			throw new RuntimeException("Error while deleting file", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public int extractNexId() {
		int id = 0;
		Connection conn = null;
		try {
			conn = this.getConnection();
			id = this.extractNextId(conn);
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while extracting next id", t);
			throw new RuntimeException("Error while extracting next id", t);
		} finally {
			this.closeConnection(conn);
		}
		return id;
	}
	
	protected int extractNextId(Connection conn) {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery("SELECT MAX(" + this.getMasterTableIdFieldName() + ") FROM " + this.getMasterTableName());
			res.next();
			id = res.getInt(1) + 1; // N.B.: funziona anche per il primo record
		} catch (Throwable t) {
			_logger.error("Error extracting next id",  t);
			throw new RuntimeException("Error extracting next id", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpfileattribute_files";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}
	
	private static final String ADD = 
		"INSERT INTO jpfileattribute_files(id, entityid, entityclass, filename, contenttype, username, date, approved) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? )";
	
	private static final String LOAD = 
		"SELECT entityid, entityclass, filename, contenttype, username, date, approved FROM jpfileattribute_files WHERE id = ? ";
	
	private static final String APPROVE = 
		"UPDATE jpfileattribute_files SET approved = ? , entityid = ? WHERE id = ? ";
	
	private static final String DELETE_FILE = 
		"DELETE FROM jpfileattribute_files WHERE id = ? ";
	
	private static final String DELETE_FILES_BY_ENTITY_ID = 
		"DELETE FROM jpfileattribute_files WHERE entityid = ? ";
	
}

/*
CREATE TABLE jpfileattribute_files
(
  id integer NOT NULL,
  entityid character varying(20),
  entityclass text NOT NULL,
  filename character varying(100) NOT NULL,
  contenttype character varying(100) NOT NULL,
  username character varying(40) NOT NULL,
  date date NOT NULL,
  approved smallint NOT NULL,
  CONSTRAINT jpfileattribute_files_pkey PRIMARY KEY (id )
)
 */