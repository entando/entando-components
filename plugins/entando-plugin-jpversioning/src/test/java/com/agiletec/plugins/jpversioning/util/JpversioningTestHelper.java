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
package com.agiletec.plugins.jpversioning.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;

public class JpversioningTestHelper extends AbstractDAO {

	private static final Logger _logger = LoggerFactory.getLogger(JpversioningTestHelper.class);
	
	public JpversioningTestHelper(DataSource dataSource, ApplicationContext context) {
		this.setDataSource(dataSource);
		this._contentManager = (IContentManager) context.getBean(JacmsSystemConstants.CONTENT_MANAGER);
	}
	
	public void initContentVersions() throws ApsSystemException {
		this.cleanContentVersions();
		ContentRecordVO record = this._contentManager.loadContentVO("ART1");
		ContentVersion version1 = this.createContentVersion(1, record.getId(), record.getTypeCode(), 
				"Articolo", Content.STATUS_DRAFT, record.getXmlWork(), DateConverter.parseDate("2005-02-13", "yyyy-MM-dd"), 
				"0.0", 0, true, "admin");
		this.addContentVersion(version1);
		ContentVersion version2 = this.createContentVersion(2, record.getId(), record.getTypeCode(), 
				"Articolo 2", Content.STATUS_DRAFT, record.getXmlWork(), DateConverter.parseDate("2005-02-14", "yyyy-MM-dd"), 
				"0.1", 0, false, "mainEditor");
		this.addContentVersion(version2);
		ContentVersion version3 = this.createContentVersion(3, record.getId(), record.getTypeCode(), 
				"Articolo 3", Content.STATUS_READY, record.getXmlWork(), DateConverter.parseDate("2005-02-15", "yyyy-MM-dd"), 
				"1.0", 1, true, "mainEditor");
		this.addContentVersion(version3);
	}
	
	public void cleanContentVersions() throws ApsSystemException {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(DELETE_VERSIONS);
		} catch (Throwable t) {
			throw new ApsSystemException("Error cleaning content versions", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void cleanTrashedResources() throws ApsSystemException {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(DELETE_TRASHED_RESOURCES);
		} catch (Throwable t) {
			throw new ApsSystemException("Error cleaning trashed resources", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void addContentVersion(ContentVersion contentVersion) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_VERSION_RECORD);
			stat.setLong(1, contentVersion.getId());
			stat.setString(2, contentVersion.getContentId());
			stat.setString(3, contentVersion.getContentType());
			stat.setString(4, contentVersion.getDescr());
			stat.setString(5, contentVersion.getStatus());
			stat.setString(6, contentVersion.getXml());
			stat.setTimestamp(7, new Timestamp(contentVersion.getVersionDate().getTime()));
			stat.setString(8, contentVersion.getVersion());
			stat.setInt(9, contentVersion.getOnlineVersion());
			int approved = contentVersion.isApproved() ? 1 : 0;
			stat.setInt(10, approved);
			stat.setString(11, contentVersion.getUsername());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding VersionRecord",  t);
			throw new RuntimeException("Error adding VersionRecord", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void addTrashedResource(String id, String type, String descr, String mainGroup, String xml, ResourceInterface resource) {
		Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = this.getConnection();
        	stat = conn.prepareStatement(ADD_RESOURCE);
			stat.setString(1, resource.getId());
			stat.setString(2, resource.getType());
			stat.setString(3, resource.getDescr());
			stat.setString(4, resource.getMainGroup());
			stat.setString(5, resource.getXML());
			stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error adding record for trashed resource",  t);
			throw new RuntimeException("Error adding record for trashed resource", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
	}
	
	public ContentVersion createContentVersion(long id, String contentId, String contentType, 
			String descr, String status, String xml, Date versionDate, String version, 
			int onlineVersion, boolean approved, String username) {
		ContentVersion contentVersion = new ContentVersion();
		contentVersion.setId(id);
		contentVersion.setContentId(contentId);
		contentVersion.setContentType(contentType);
		contentVersion.setDescr(descr);
		contentVersion.setStatus(status);
		contentVersion.setXml(xml);
		contentVersion.setVersionDate(versionDate);
		contentVersion.setVersion(version);
		contentVersion.setOnlineVersion(onlineVersion);
		contentVersion.setApproved(approved);
		contentVersion.setUsername(username);
		return contentVersion;
	}
	
	private IContentManager _contentManager;
	
	private static final String DELETE_VERSIONS = 
		"DELETE FROM jpversioning_versionedcontents";
	
	private static final String DELETE_TRASHED_RESOURCES = 
		"DELETE FROM jpversioning_trashedresources";
	
	private final String ADD_VERSION_RECORD = 
		"INSERT INTO jpversioning_versionedcontents ( id, contentid, contenttype, descr, " +
		"status, contentxml, versiondate, versioncode, onlineversion, approved, username ) " +
		" VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ? )";
	
	private final String ADD_RESOURCE = 
		"INSERT INTO jpversioning_trashedresources ( resid, restype, descr, maingroup, resxml ) VALUES ( ? , ? , ? , ? , ? )";
	
}