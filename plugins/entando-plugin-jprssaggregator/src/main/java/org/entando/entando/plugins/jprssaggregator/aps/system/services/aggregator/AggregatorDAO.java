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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.util.ApsProperties;

/**
 * This class is the Data Access Object for the ApsAggregatorItem Object
 */
public class AggregatorDAO extends AbstractDAO implements IAggregatorDAO {

	private static final Logger _logger = LoggerFactory.getLogger(AggregatorDAO.class);
	
	@Override
	public void addItem(ApsAggregatorItem item) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_ITEM);
			stat.setInt(1, item.getCode());
			stat.setString(2, item.getContentType());
			stat.setString(3, item.getDescr());
			stat.setString(4, item.getLink());
			stat.setLong(5, item.getDelay());
			stat.setTimestamp(6, new Timestamp(item.getLastUpdate().getTime()));
			if (null != item.getCategories() && item.getCategories().size() > 0) {
				stat.setString(7, item.getCategories().toXml());
			} else {
				stat.setNull(7, Types.VARCHAR);
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error inserting a new ApsAggregatorItem",  t);
			throw new RuntimeException("Error inserting a new ApsAggregatorItem", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public ApsAggregatorItem getItem(int code) {
		Connection conn = null;
		ApsAggregatorItem item = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_AGGREGATOR_ITEM);
			stat.setInt(1, code);
			res = stat.executeQuery();
			item = this.createItemFromRecord(res);
		} catch (Throwable t) {
			_logger.error("Error getting the ApsAggregatorItem with code {}", code,  t);
			throw new RuntimeException("Error getting the ApsAggregatorItem", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return item;
	}

	@Override
	public void deleteItem(int code) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_ITEM);
			stat.setInt(1, code);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing the ApsAggregatorItem with code {}", code,  t);
			throw new RuntimeException("Error removing the ApsAggregatorItem", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public List<ApsAggregatorItem> getItems() {
		Connection conn = null;
		List<ApsAggregatorItem> items = new ArrayList<ApsAggregatorItem>();
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_AGGREGATOR_ITEMS);
			items = this.loadItems(res);
		} catch (Throwable t) {
			_logger.error("Errore loading items",  t);
			throw new RuntimeException("Errore loading items", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return items;
	}

	@Override
	public void update(ApsAggregatorItem item) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_ITEM);
			stat.setString(1, item.getDescr());
			stat.setString(2, item.getLink());
			stat.setLong(3, item.getDelay());
			stat.setTimestamp(4, new Timestamp(item.getLastUpdate().getTime()));
			if (null != item.getCategories() && item.getCategories().size() > 0) {
				stat.setString(5, item.getCategories().toXml());
			} else {
				stat.setNull(5, Types.VARCHAR);
			}
			stat.setInt(6, item.getCode());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating item",  t);
			throw new RuntimeException("Error updating item", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	protected ApsAggregatorItem createItemFromRecord(ResultSet res) throws SQLException, IOException {
		ApsAggregatorItem item = null;
		if (res.next()) {
			item = new ApsAggregatorItem();
			item.setCode(res.getInt("code"));
			item.setDescr(res.getString("descr"));
			item.setLink(res.getString("link"));
			item.setDelay(res.getInt("delay"));
			Date lastUpdate = new Date(res.getTimestamp("lastupdate").getTime());
			item.setLastUpdate(lastUpdate);
			item.setContentType(res.getString("contenttype"));
			String cat = res.getString("categories");
			if (null != cat && cat.trim().length() > 0) {
				ApsProperties categories = new ApsProperties();
				categories.loadFromXml(cat);
				item.setCategories(categories);
			}
		}
		return item;
	}

	protected List<ApsAggregatorItem> loadItems(ResultSet res) throws SQLException, IOException {
		List<ApsAggregatorItem> items = new ArrayList<ApsAggregatorItem>();
		ApsAggregatorItem item = null;
		while (res.next()) {
			item = new ApsAggregatorItem();
			item.setCode(res.getInt("code"));
			item.setDescr(res.getString("descr"));
			item.setLink(res.getString("link"));
			item.setDelay(res.getInt("delay"));
			item.setContentType(res.getString("contenttype"));
			Date lastUpdate = new Date(res.getTimestamp("lastupdate").getTime());
			item.setLastUpdate(lastUpdate);
			String cat = res.getString("categories");
			if (null != cat && cat.trim().length() > 0) {
				ApsProperties categories = new ApsProperties();
				categories.loadFromXml(cat);
				item.setCategories(categories);
			}
			items.add(item);
		}
		return items;
	}

	protected static final String ADD_ITEM = 
		"INSERT INTO jprssaggregator_aggregatoritems (code, contenttype, descr, link, delay, lastupdate, categories) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	protected static final String LOAD_AGGREGATOR_ITEM = 
		"SELECT code, contenttype, descr, link, delay, lastupdate, categories FROM jprssaggregator_aggregatoritems WHERE code = ?";
	
	protected static final String DELETE_ITEM = 
		"DELETE FROM jprssaggregator_aggregatoritems WHERE code = ?";


	protected static final String LOAD_AGGREGATOR_ITEMS = 
		"SELECT code, contenttype, descr, link, delay, lastupdate, categories FROM jprssaggregator_aggregatoritems";

	protected static final String UPDATE_ITEM = 
		"UPDATE jprssaggregator_aggregatoritems SET descr = ?, link = ?, delay = ?, lastupdate = ?, categories = ? WHERE code = ?";
}
