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
package org.entando.entando.plugins.jprss.aps.system.services.rss;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object for Channel Object. 
 * @author S.Puddu
 */
public class RssDAO extends AbstractDAO implements IRssDAO {
	
	private static final Logger _logger = LoggerFactory.getLogger(RssDAO.class);
	
	@Override
	public void addChannel(Channel channel) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_CHANNEL);
			stat.setInt(1, channel.getId());
			stat.setString(2, channel.getTitle());
			stat.setString(3, channel.getDescription());
			stat.setString(4, new Boolean(channel.isActive()).toString());
			stat.setString(5, channel.getContentType());
			stat.setString(6, channel.getCategory());
			if (null != channel.getFilters() && channel.getFilters().length() > 0) {
				stat.setString(7, channel.getFilters());
			} else {
				stat.setNull(7, Types.VARCHAR);
			}
			stat.setString(8, channel.getFeedType());
			if (channel.getMaxContentsSize() > 0) {
				stat.setInt(9, channel.getMaxContentsSize());
			} else {
				stat.setNull(9, Types.INTEGER);
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error adding a channel",  t);
			throw new RuntimeException("Error adding a channel", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateChannel(Channel channel) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_CHANNEL);
			//id, title, description, active, contentType, fileters, feedtype
			stat.setString(1, channel.getTitle());
			stat.setString(2, channel.getDescription());
			stat.setString(3, new Boolean(channel.isActive()).toString());
			stat.setString(4, channel.getContentType());
			if (null != channel.getCategory() && channel.getCategory().length() > 0 ) {
				stat.setString(5, channel.getCategory());
			} else {
				stat.setNull(5, Types.VARCHAR);
			}
			stat.setString(6, channel.getFilters());
			stat.setString(7, channel.getFeedType());
			if (channel.getMaxContentsSize() > 0) {
				stat.setInt(8, channel.getMaxContentsSize());
			} else {
				stat.setNull(8, Types.INTEGER);
			}
			stat.setInt(9, channel.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error updating a channel",  t);
			throw new RuntimeException("Error updating a channel", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteChannel(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_CHANNEL);
			stat.setInt(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error deleting a channel",  t);
			throw new RuntimeException("Error deleting a channel", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<Channel> getChannels(int status) throws Throwable {
		List<Channel> channels = new ArrayList<Channel>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			if (status == Channel.STATUS_ALL) {
				stat = conn.prepareStatement(LOAD_CHANNELS);
			} else {
				stat = conn.prepareStatement(LOAD_CHANNELS_BY_STATUS);
				stat.setString(1, new Boolean(status == Channel.STATUS_ACTIVE).toString());
			}
			res = stat.executeQuery();
			Channel channel;
			while (res.next()) {
				channel = this.getChannelFromResultSet(res);
				channels.add(channel);
			}
		} catch (Throwable t) {
			_logger.error("Error loading the channels list with status {}", status,  t);
			throw new RuntimeException("Error loading the channels list by status", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return channels;
	}
	
	@Override
	public Channel getChannel(int id) {
		Channel channel = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_CHANNEL_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				channel = this.getChannelFromResultSet(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading channel with id {}", id, t);
			throw new RuntimeException("Error loading channel by id", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return channel;
	}
	
	private Channel getChannelFromResultSet(ResultSet res) throws Throwable {
		Channel channel = new Channel();
		try {
			channel.setActive(res.getString("active").equalsIgnoreCase("true"));
			channel.setContentType(res.getString("contenttype"));
			channel.setCategory(res.getString("category"));
			channel.setDescription(res.getString("description"));
			channel.setFeedType(res.getString("feedtype"));
			channel.setFilters(res.getString("filters"));
			channel.setId(res.getInt("id"));
			channel.setTitle(res.getString("title"));
			int maxContentSize = res.getInt("maxcontentsize");
			if (maxContentSize > 0) {
				channel.setMaxContentsSize(maxContentSize);
			}
		} catch (Throwable t) {
			_logger.error("Error creating a channel from resultset");
			throw new Throwable("Error creating a channel from resultset", t);
		}
		return channel;
	}
	
	private static final String ADD_CHANNEL = 
		"INSERT INTO jprss_channel (id, title, description, active, contentType, category, filters, feedtype, maxcontentsize) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String DELETE_CHANNEL = "DELETE FROM jprss_channel WHERE id = ?";
	
	private static final String UPDATE_CHANNEL = 
		"UPDATE jprss_channel SET title =?, description=?, active=?, contentType=?, category=?, filters=?, feedtype=?, maxcontentsize=? WHERE id=?";
	
	private static final String LOAD_CHANNELS_BASE_BLOCK = 
		"SELECT id, title, description, active, contentType, category, filters, feedtype, maxcontentsize from jprss_channel ";
	
	private static final String LOAD_CHANNELS_ORDER_BLOCK = "ORDER BY description ";
	
	private static final String LOAD_CHANNELS = 
		LOAD_CHANNELS_BASE_BLOCK + LOAD_CHANNELS_ORDER_BLOCK;
	
	private static final String LOAD_CHANNELS_BY_STATUS = 
		LOAD_CHANNELS_BASE_BLOCK + " where active = ? " + LOAD_CHANNELS_ORDER_BLOCK;
	
	private static final String LOAD_CHANNEL_BY_ID = 
		LOAD_CHANNELS_BASE_BLOCK + " where id = ? " + LOAD_CHANNELS_ORDER_BLOCK;
	
}