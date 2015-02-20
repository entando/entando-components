/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.system.services.checkin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import com.agiletec.aps.system.common.AbstractDAO;

public class ContentCheckinDAO extends AbstractDAO implements IContentCheckinDAO {
	
	@Override
	public void deleteContentCheckin(String contentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteContentCheckin(contentId, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error removing content from checkin list", "deleteContentCheckin");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteContentCheckin(String contentId, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_CONTENT_CHECKIN);
			stat.setString(1, contentId);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error removing content from checkin list", "deleteContentCheckin");
		} finally {
			closeDaoResources(null, stat, null);
		}
	}
	
	@Override
	public ContentCheckin loadContentChekin(String contentId) {
		ContentCheckin checkin = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_CONTENT_CHECKIN);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			if (res.next()) {
				checkin = new ContentCheckin();
				checkin.setContentId(res.getString(1));
				Timestamp checkinDate = res.getTimestamp(2);
				checkin.setCheckinDate(new Date(checkinDate.getTime()));
				checkin.setCheckinUser(res.getString(3));
			}
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error loading content checkin for content " + contentId, "loadContentChekin");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return checkin;
	}
	
	@Override
	public void insertContentChekin(ContentCheckin checkin) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			this.deleteContentCheckin(checkin.getContentId(), conn);
			
			stat = conn.prepareStatement(INSERT_CONTENT_CHECKIN);
			stat.setString(1, checkin.getContentId());
			Date checkinDate = checkin.getCheckinDate();
			if (null == checkinDate) checkinDate = new Date();
			stat.setTimestamp(2, new Timestamp(checkinDate.getTime()));
			stat.setString(3, checkin.getCheckinUser());
			stat.executeUpdate();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error on insert of a content checkin for content " + checkin.getContentId(), "insertContentChekin");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private final String DELETE_CONTENT_CHECKIN = "DELETE FROM jpsharedocs_checkin WHERE contentid = ?";
	
	private final String LOAD_CONTENT_CHECKIN = "SELECT contentid, checkin_date, checkin_editor FROM jpsharedocs_checkin WHERE contentid = ?";
	
	private final String INSERT_CONTENT_CHECKIN = "INSERT INTO jpsharedocs_checkin(contentid, checkin_date, checkin_editor) VALUES (?, ?, ?)";
	
}