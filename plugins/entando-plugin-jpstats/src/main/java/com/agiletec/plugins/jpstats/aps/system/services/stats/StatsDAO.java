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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.ContentStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.DateStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.PageStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.VisitsStat;


/**
 * Data Access Object for the Statistics Manager
 * @version 1.2
 * @author M.Lisci - E.Santoboni
 */
public class StatsDAO extends AbstractDAO implements IStatsDAO {

	private static final Logger _logger = LoggerFactory.getLogger(StatsDAO.class);
	
	protected String getDriverName() throws Throwable {
		String driverName = null;
		Method method = this.getDataSource().getClass().getDeclaredMethod("getDriverClassName");
		String className = (String) method.invoke(this.getDataSource());
		if (StringUtils.isNotBlank(className)) {
			Iterator<Entry<Object, Object>> it = this.getDatabaseTypeDrivers().entrySet().iterator();
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				List<String> values = (List<String>) entry.getValue();
				if (null != values && !values.isEmpty()) {
					if (values.contains(className)) {
						driverName = (String) entry.getKey();
						break;
					}
				}
			}
		}
		return driverName;
	}

	private String convertSecondsToInterval(int seconds) {
		int day = (int)TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		return day + " days " + String.format("%02d",hours)+ ":" + String.format("%02d",minute) + ":" + String.format("%02d",second);
	}

	@Override
	public List<StatsRecord> loadStatsRecord(Date from, Date to) {
		List<StatsRecord> records = new ArrayList<StatsRecord>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String startString = new Timestamp(from.getTime()).toString();
		String endString = new Timestamp(to.getTime()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_RECORDS);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				StatsRecord record = this.createStatsRecord(res);
				records.add(record);
			}
		} catch (Throwable t) {
			_logger.error("Error getting Ip address ",  t);
			throw new RuntimeException("Error getting Ip address ", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return records;
	}

	/**
	 * Adds a record to the statistic table
	 * @param statsRecord
	 */
	@Override
	public void addStatsRecord(StatsRecord statsRecord) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			prepStat = conn.prepareStatement(ADD_RECORD);
			prepStat.setString(1, statsRecord.getIp());
			prepStat.setString(2, statsRecord.getReferer());
			prepStat.setString(3, statsRecord.getSessionId());
			prepStat.setString(4, statsRecord.getRole());
			prepStat.setString(5, statsRecord.getTimestamp());
			prepStat.setString(6, statsRecord.getYear());
			prepStat.setString(7, statsRecord.getMonth());
			prepStat.setString(8, statsRecord.getDay());
			prepStat.setString(9, statsRecord.getHour());
			prepStat.setString(10, statsRecord.getPageCode());
			prepStat.setString(11, statsRecord.getLangcode());
			prepStat.setString(12, statsRecord.getUseragent());
			prepStat.setString(13, statsRecord.getBrowserLang());
			prepStat.setString(14, statsRecord.getContentId());
			prepStat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error adding a statistic record",  t);
			throw new RuntimeException("Error adding a statistic record", t);
		} finally {
			closeDaoResources(null, prepStat, conn);
		}
	}

	@Override
	public void deleteStatsRecord(Date from, Date to) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			prepStat = conn.prepareStatement(REMOVE_RECORDS);
			prepStat.setString(1, (new java.sql.Timestamp(from.getTime())).toString());
			prepStat.setString(2, (new java.sql.Timestamp(to.getTime())).toString());
			prepStat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error removing statistic records",  t);
			throw new RuntimeException("Error removing statistic records", t);
		} finally {
			closeDaoResources(null, prepStat, conn);
		}
	}

	@Override
	public List<VisitsStat> searchVisitsForDate(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_DAILY_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			Calendar calendar = Calendar.getInstance();
			while (res.next()) {
				DateStatistic statistic = new DateStatistic();
				int hit = res.getInt(1);
				calendar.set(res.getInt(2), res.getInt(3)-1, res.getInt(4), 0, 0, 0);
				Date day = calendar.getTime();
				statistic.setDate(day);
				statistic.setVisits(new Integer(hit));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			_logger.error("Error searching visits for date",  t);
			throw new RuntimeException("Error searching visits for date", t);
			//processDaoException(t, "Error searching visits for date", "searchVisitsForDate");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}

	@Override
	public List<VisitsStat> searchVisitsForPages(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_PAGE_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			IPageManager pageManager = this.getPageManager();
			String langCode = this.getLangManager().getDefaultLang().getCode();
			while (res.next()) {
				PageStatistic statistic = new PageStatistic();
				String code = res.getString(1);
				statistic.setCode(code);
				IPage page = pageManager.getPage(code);
				String descr = (page!=null) ? page.getTitle(langCode) : code;
				statistic.setDescr(descr);
				statistic.setVisits(new Integer(res.getInt(2)));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			_logger.error("Error searching visits for pages",  t);
			throw new RuntimeException("Error searching visits for pages", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}

	@Override
	public List<VisitsStat> searchVisitsForContents(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_CONTENT_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			Map<String, SmallContentType> contentTypes = this.getContentManager().getSmallContentTypesMap();
			while (res.next()) {
				ContentStatistic statistic = new ContentStatistic();
				String id = res.getString(1);
				statistic.setId(id);
				ContentRecordVO content = this.getContentManager().loadContentVO(id);
				if (content == null) {
					statistic.setDescr(id);
				} else {
					SmallContentType contentType = contentTypes.get(content.getTypeCode());
					statistic.setDescr(content.getDescr());
					statistic.setType(contentType.getDescr());
				}
				statistic.setVisits(new Integer(res.getInt(2)));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			_logger.error("Error searching visits for contents",  t);
			throw new RuntimeException("Error searching visits for contents", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}

	/**
	 * Gets the hits between two dates
	 * @param start Calendar
	 * @param end Calendar
	 * @return a TimeSeries object, used to render the chart
	 */
	@Override
	public TimeSeries getHitsByInterval(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		TimeSeries hitsPage = new TimeSeries("Entando_Chart_v0.0", Day.class);
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(HITS_BY_INTERVAL);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			Day initDay = new Day(start.getTime());
			Day endDay = new Day(end.getTime());
			while (res.next()) {
				Day day = new Day(res.getInt("day_value"),res.getInt("month_value"),res.getInt("year_value"));
				hitsPage.add(day,res.getInt("hits"));
			}
			try {
				hitsPage.add(initDay,0);
			} catch (Throwable t) {}
			try {
				hitsPage.add(endDay,0);
			} catch (Throwable t) {}
		} catch (Throwable t) {
			_logger.error("Error getting hits by interval ",  t);
			throw new RuntimeException("Error getting hits by interval ", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return hitsPage;
	}

	/**
	 * Gets the average time spent on the site by session
	 * @param start Calendar
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 */
	@Override
	public String getAverageTimeSite(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String mediaSessioni = null;
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			String queryName = this.GetAVERAGE_TIME_SITE(this.getDriverName());
			stat = conn.prepareStatement(queryName);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			String media = null;
			while (res.next()) {
				int seconds = res.getInt(1);
				media = this.convertSecondsToInterval(seconds);

			}
			mediaSessioni = media;
		} catch (Throwable t) {
			_logger.error("Error getting  average time site",  t);
			throw new RuntimeException("Error getting  average time site", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return this.roundInterval(mediaSessioni);
	}

	/**
	 * Gets the average time spent on a page by pagecode and by session
	 * @param start Calendar
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 */
	@Override
	public String getAverageTimePage(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String mediaTimePage = new String();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			String queryName = this.GetAVERAGE_TIME_PAGE(this.getDriverName());
			stat = conn.prepareStatement(queryName);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				int seconds = res.getInt("media");
				mediaTimePage = this.convertSecondsToInterval(seconds);
			}
		} catch (Throwable t) {
			_logger.error("Error getting average time page", t);
			throw new RuntimeException("Error getting average time page", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return roundInterval(mediaTimePage);
	}

	/**
	 * Gets the average amount of pages visited in each session
	 * @param start Calendar
	 * @param end Calendar
	 * @return int the average amount of pages visited in each session
	 */
	@Override
	public int getNumPageSession(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int mediaPage = 0;
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(AVERAGE_PAGE);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				mediaPage = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("Error getting average num page session ",  t);
			throw new RuntimeException("Error getting average num page session ", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mediaPage;
	}

	/**
	 * Gets the ten most visited pages
	 * @param start Calendar
	 * @param end Calendar
	 * @return a map (pagecode:hits) used to render the chart
	 */
	@Override
	public Map<String, Integer> getTopPages(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> hitsPage = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			String queryName = this.GetGET_TOP_PAGES(this.getDriverName());
			stat = conn.prepareStatement(queryName);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				int count = res.getInt("hits");
				hitsPage.put(res.getString("pagecode"), new Integer(count));
			}
		} catch (Throwable t) {
			_logger.error("Error getting the most visited pages ", t);
			throw new RuntimeException("Error getting the most visited pages ", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return hitsPage;
	}

	/**
	 * Gets the ten most visited contents
	 * If the content does not exists anymore the function
	 * prints [DELETED] instead of the description
	 * @param start Calendar
	 * @param end Calendar
	 * @param contentManager
	 * @return a map (content_descr:hits) used to render the chart
	 */
	@Override
	public Map<String, Integer> getTopContents(Calendar start, Calendar end) {
		IContentManager contentManager = this.getContentManager();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> topContents = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			String queryName = this.GetGET_TOP_CONTENTS(this.getDriverName());
			stat = conn.prepareStatement(queryName);
			stat.setString(1,  startString);
			stat.setString(2,  endString);
			res = stat.executeQuery();
			while (res.next()) {
				String contentId = res.getString("content");
				String contentDescr = null;
				ContentRecordVO content = contentManager.loadContentVO(contentId);
				if (null == content) {
					contentDescr = "[DELETED]";
				} else {
					contentDescr = content.getDescr();
				}
				int count = res.getInt("hits");
				topContents.put(contentDescr, new Integer(count));
			}
		} catch (Throwable t) {
			_logger.error("Error getting the most visited contents",  t);
			throw new RuntimeException("Error getting the most visited contents", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return topContents;
	}

	/**
	 *
	 * @return Calendar the first date stored in the statistic table
	 * If the table is empty returns the current date
	 */
	@Override
	public Calendar getFirstCalendarDay() {
		Connection conn = null;
		Calendar firstDay = Calendar.getInstance();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			String queryName = this.GetGET_FIRST_DATE(this.getDriverName());
			stat = conn.prepareStatement(queryName);
			res = stat.executeQuery();
			while (res.next()) {
				int year = Integer.parseInt(res.getString("year_value"));
				int month = Integer.parseInt(res.getString("month_value"));
				int day = Integer.parseInt(res.getString("day_value"));
				firstDay.set(year,month-1, day,0,0,0);
				firstDay.set(Calendar.MILLISECOND, 0);
			}
		} catch (Throwable t) {
			_logger.error("Error getting the first day",  t);
			throw new RuntimeException("Error getting the first day", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return firstDay;
	}

	/**
	 * Gets a map of Ip Address (ip,hits)
	 * @param start Calendar
	 * @param end Calendar
	 * @return a map of Ip (ip,hits)
	 */
	@Override
	public Map<String, Integer> getIPByDateInterval(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> statsRecord = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_IP);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				String ip = res.getString(1);
				int count = res.getInt(2);
				statsRecord.put(ip, new Integer(count));
			}
		} catch (Throwable t) {
			_logger.error("Error getting Ip address",  t);
			throw new RuntimeException("Error getting Ip address", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return statsRecord;
	}

	/**
	 * Rounds a string cutting the milliseconds
	 * Queries the gets average time can return null values
	 * This function transform null values in 00:00:00
	 * @param interval String
	 * @return
	 */
	private String roundInterval(String interval) {
		if(interval==null) interval = "00:00:00";
		int length = interval.length();
		if (interval.indexOf(".")!=-1) {
			length=interval.indexOf(".");
		}
		return interval.substring(0,length);
	}

	private StatsRecord createStatsRecord(ResultSet res) throws Throwable {
		//ip, referer, session_id, role, timestamp, year, month, day, hour, pagecode, langcode, useragent, browserlang, content
		Calendar calendar = this.extractRecordDate(res);
		StatsRecord record = new StatsRecord(calendar);
		record.setIp(res.getString("ip"));
		record.setReferer(res.getString("referer"));
		record.setSessionId(res.getString("session_id"));
		record.setRole(res.getString("role"));
		record.setPageCode(res.getString("pagecode"));
		record.setLangcode(res.getString("langcode"));
		record.setUseragent(res.getString("useragent"));
		record.setBrowserLang(res.getString("browserlang"));
		record.setContentId(res.getString("content"));
		return record;
	}

	private Calendar extractRecordDate(ResultSet res) throws SQLException {
		Calendar calendar = Calendar.getInstance();
		String year = res.getString("year_value");
		String month = res.getString("month_value");
		String day = res.getString("day_value");
		String hour = res.getString("hour_value");
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		String[] array = hour.split(":");
		if (array.length == 3) {
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(array[0].trim()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(array[1].trim()));
			calendar.set(Calendar.SECOND, Integer.parseInt(array[2].trim()));
		}
		return calendar;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	private IContentManager _contentManager;
	private IPageManager _pageManager;
	private ILangManager _langManager;


	private  String GetAVERAGE_TIME_PAGE(String driver) {
		String q = AVERAGE_TIME_PAGE_postgres;
		if (driver.equalsIgnoreCase("postgres")) {
			q = AVERAGE_TIME_PAGE_postgres;
		} else if (driver.equalsIgnoreCase("mysql")) {
			return AVERAGE_TIME_PAGE_mysql;
		}else if (driver.equalsIgnoreCase("derby")) {
			return AVERAGE_TIME_PAGE_derby;
		}
		return q;
	}

	private  String GetAVERAGE_TIME_SITE(String driver) {
		String q = AVERAGE_TIME_SITE_postgres;
		if (driver.equalsIgnoreCase("postgres")) {
			q = AVERAGE_TIME_SITE_postgres;
		} else if (driver.equalsIgnoreCase("mysql")) {
			return AVERAGE_TIME_SITE_mysql;
		} else if (driver.equalsIgnoreCase("derby")) {
			return AVERAGE_TIME_SITE_derby;
		}
		return q;
	}

	private  String GetGET_FIRST_DATE(String driver) {
		String q = GET_FIRST_DATE;
		if (driver.equalsIgnoreCase("derby")) {
			q = GET_FIRST_DATE_derby;
		}
		return q;
	}

	private  String GetGET_TOP_CONTENTS(String driver) {
		String q = GET_TOP_CONTENTS;
		if (driver.equalsIgnoreCase("derby")) {
			q = GET_TOP_CONTENTS_derby;
		}
		return q;
	}

	private  String GetGET_TOP_PAGES(String driver) {
		String q = GET_TOP_PAGES;
		if (driver.equalsIgnoreCase("derby")) {
			q = GET_TOP_PAGES_derby;
		}
		return q;
	}

	private final String ADD_RECORD =
			"INSERT INTO jpstats_statistics (ip, referer, session_id, role, timestamp, year_value, month_value, day_value, hour_value, pagecode, langcode, useragent, browserlang, content) "
					+ "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

	private final String REMOVE_RECORDS =
			"DELETE FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? ";

	private final String LOAD_RECORDS =
			"SELECT * FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";

	private final String SEARCH_DAILY_VISITS =
			"SELECT count(*) as hits, year_value, month_value, day_value FROM jpstats_statistics " +
					"WHERE timestamp >= ? AND timestamp <= ? GROUP BY year_value, month_value, day_value ORDER BY hits DESC";

	private final String SEARCH_PAGE_VISITS =
			"SELECT pagecode, COUNT(*) AS hits FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? " +
					"GROUP BY pagecode ORDER BY hits DESC";

	private final String SEARCH_CONTENT_VISITS =
			"SELECT content, COUNT(*) AS hits FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? " +
					"AND content IS NOT NULL GROUP BY content ORDER BY hits DESC";

	private final String HITS_BY_INTERVAL =
			"SELECT count(*) as hits, day_value, month_value, year_value FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ? " +
					" GROUP BY year_value, month_value, day_value ORDER BY year_value, month_value, day_value ASC";


	private final String AVERAGE_TIME_SITE_postgres =
			"SELECT avg(x) AS media " +
					" FROM( SELECT session_id, extract(EPOCH FROM MAX(timestamp)::TIMESTAMP - MIN(timestamp)::TIMESTAMP) as x " +
					" FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ?" +
					" GROUP BY session_id " +
					" HAVING count(session_id)>1 )AS SUBQUERY";

	private final String AVERAGE_TIME_SITE_mysql =
			"SELECT avg(x) AS media " +
					" FROM( SELECT session_id, TIMESTAMPDIFF(second, MIN(timestamp(timestamp)), MAX(timestamp(timestamp))) AS x" +
					" FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ?" +
					" GROUP BY session_id " +
					" HAVING count(session_id)>1 )AS SUBQUERY";

	private final String AVERAGE_TIME_SITE_derby =
			"SELECT avg(x) AS media " +
					" FROM( SELECT session_id, {fn TIMESTAMPDIFF(SQL_TSI_SECOND, MIN(timestamp(timestamp)), MAX(timestamp(timestamp)))} AS x" +
					" FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ?" +
					" GROUP BY session_id " +
					" HAVING count(session_id)>1 )AS SUBQUERY";


	private final String AVERAGE_TIME_PAGE_postgres =
			"SELECT AVG(x) AS media" +
					" FROM( SELECT session_id as s, pagecode as p, extract(EPOCH FROM MAX(timestamp)::TIMESTAMP - MIN(timestamp)::TIMESTAMP) as x " +
					" FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? GROUP BY p, s )AS SUBQUERY ";

	private final String AVERAGE_TIME_PAGE_mysql =
			"SELECT AVG(x) AS media" +
					" FROM( SELECT session_id as s, pagecode as p, TIMESTAMPDIFF(second, MIN(timestamp(timestamp)), MAX(timestamp(timestamp))) AS x  " +
					" FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? GROUP BY p, s )AS SUBQUERY ";

	private final String AVERAGE_TIME_PAGE_derby =
			"SELECT AVG(x) AS media" +
					" FROM( SELECT session_id as s, pagecode as p, {fn TIMESTAMPDIFF(SQL_TSI_SECOND, MIN(timestamp(timestamp)), MAX(timestamp(timestamp)))} AS x  " +
					" FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? GROUP BY pagecode, session_id )AS SUBQUERY ";



	private final String AVERAGE_PAGE=
			"SELECT AVG(x) AS media " +
					" FROM(SELECT session_id, COUNT(pagecode) AS x " +
					" FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ? " +
					" GROUP BY session_id )AS SUBQUERY";

	private final String GET_TOP_PAGES =
			"SELECT pagecode,COUNT(*) AS hits FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ? " +
					" GROUP BY pagecode " +
					" ORDER BY hits DESC" +
					" LIMIT 10;";

	private final String GET_TOP_PAGES_derby =
			"SELECT * FROM ( SELECT ROW_NUMBER() OVER() AS rownum,  pagecode,COUNT(*) AS hits FROM jpstats_statistics  " +
					" WHERE timestamp >= ? AND timestamp <= ? " +
					" GROUP BY pagecode " +
					" ORDER BY hits DESC) as tmp  WHERE rownum <= 10";


	private final String GET_TOP_CONTENTS =
			"SELECT content, COUNT(content) AS hits FROM jpstats_statistics " +
					"WHERE timestamp >= ? AND timestamp <= ? and content IS NOT NULL " +
					"GROUP BY content " +
					"ORDER BY hits " +
					"DESC LIMIT 10";

	private final String GET_TOP_CONTENTS_derby =
			"SELECT * FROM ( SELECT ROW_NUMBER() OVER() AS rownum,  content, COUNT(content) AS hits FROM jpstats_statistics " +
					"WHERE timestamp >= ? AND timestamp <= ? and content IS NOT NULL " +
					"GROUP BY content " +
					"ORDER BY hits " +
					"DESC) as tmp  WHERE rownum <= 10";

	private final String GET_FIRST_DATE =
			"SELECT year_value, month_value, day_value FROM jpstats_statistics ORDER BY timestamp ASC LIMIT 1";

	private final String GET_FIRST_DATE_derby =
			"SELECT * FROM ( SELECT ROW_NUMBER() OVER() AS rownum, year_value, month_value, day_value FROM jpstats_statistics ORDER BY timestamp ASC) as tmp WHERE rownum <= 1";

	private final String GET_IP =
			"SELECT DISTINCT ip, count(*) as count " +
					"FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? GROUP BY ip";

	protected Properties getDatabaseTypeDrivers() {
		return _databaseTypeDrivers;
	}
	public void setDatabaseTypeDrivers(Properties databaseTypeDrivers) {
		this._databaseTypeDrivers = databaseTypeDrivers;
	}

	private Properties _databaseTypeDrivers;
}