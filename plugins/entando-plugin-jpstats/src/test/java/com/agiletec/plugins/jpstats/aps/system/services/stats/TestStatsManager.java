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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.util.CalendarConverter;
import com.agiletec.plugins.jpstats.aps.util.StatsDataBean;
import com.agiletec.plugins.jpstats.aps.util.TopContentsCDP;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

import de.laures.cewolf.DatasetProduceException;

public class TestStatsManager extends ApsPluginBaseTestCase {
	
	public void testAddStatsRecord() throws Throwable {
		String today = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		Calendar start = CalendarConverter.getCalendarDay(today, 0, 0, 0, 0);
		Calendar end = Calendar.getInstance();
		end.add(Calendar.DAY_OF_YEAR, 1);
		try {
			TimeSeries ts = _statsManager.getHitsByInterval(start, end);
			TimeSeriesDataItem timeSeriesDataItem = (TimeSeriesDataItem) ts.getItems().get(0);
			assertEquals(0, timeSeriesDataItem.getValue().intValue());
			this._statsManager.addStatsRecord(this.createRecordForTest());
			TestStatsUtils.waitStatsThread();
			ts = _statsManager.getHitsByInterval(start, end);
			timeSeriesDataItem = (TimeSeriesDataItem) ts.getItems().get(0);
			assertEquals(1, timeSeriesDataItem.getValue().intValue());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._statsManager.deleteStatsRecord(start.getTime(), end.getTime());
			TimeSeries ts = _statsManager.getHitsByInterval(start, end);
			TimeSeriesDataItem timeSeriesDataItem = (TimeSeriesDataItem) ts.getItems().get(0);
			assertEquals(0, timeSeriesDataItem.getValue().intValue());
		}
	}
	
	public void testGetHitsByInterval() throws Throwable {
		Calendar start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("07/03/2007", 23, 59, 59, 999);
		TimeSeries ts = _statsManager.getHitsByInterval(start, end);
		int days = ts.getItems().size();
		assertEquals(2, days);
		TimeSeriesDataItem timeSeriesDataItem = (TimeSeriesDataItem) ts.getItems().get(0);
		assertEquals(14,timeSeriesDataItem.getValue().intValue());
	}
	
	public void testGetAverageTimeSite() throws ApsSystemException, SQLException {
		Calendar start = CalendarConverter.getCalendarDay("10/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		String averageTimeSite = _statsManager.getAverageTimeSite(start, end);
		assertTrue(averageTimeSite.startsWith("0 days 00:00:0")); //derby results differ by 1 second from postgres results
	}
	
	public void testGetAverageTimePage() throws ApsSystemException, SQLException {
		Calendar start = CalendarConverter.getCalendarDay("10/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		String averageTimeSite = _statsManager.getAverageTimePage(start, end);
		assertTrue(averageTimeSite.startsWith("0 days 00:00:0")); //derby results differ by 1 second from postgres results
	}
	
	public void testGetNumPageSession() throws ApsSystemException, SQLException {
		Calendar start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		int numPageSession = _statsManager.getNumPageSession(start, end);
		assertEquals(3, numPageSession);
	}
	
	public void testGetPageVisitedAsc() throws ApsSystemException, SQLException {
		Calendar start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		Map<String, Integer> pageVisitedDesc = _statsManager.getTopPages(start, end);
		assertEquals(3, pageVisitedDesc.size());
		Object homepage = pageVisitedDesc.get("homepage");
		assertEquals(Integer.parseInt(homepage.toString()), 50);
		Object errorpage =pageVisitedDesc.get("errorpage");
		assertEquals(Integer.parseInt(errorpage.toString()), 4);
		Object risultati_della_ricerca =pageVisitedDesc.get("risultati_della_ricerca");
		assertEquals(Integer.parseInt(risultati_della_ricerca.toString()), 2);
	}
	
	public void testGetTopContents() throws ApsSystemException, SQLException, DatasetProduceException {
		Calendar start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		
		Map<String, Integer> topContents = _statsManager.getTopContents(start, end);
		assertNotNull(topContents);
		
		start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		end = CalendarConverter.getCalendarDay("18/03/2007", 23, 59, 59, 999);
		
		topContents = _statsManager.getTopContents(start, end);
		assertEquals(4, topContents.size());
		assertEquals("17", topContents.get("una descrizione particolare").toString());
		StatsDataBean bean = new StatsDataBean();
		start = CalendarConverter.getCalendarDay("05/03/2007", 23, 59, 59, 999);
		end = CalendarConverter.getCalendarDay("30/03/2007", 23, 59, 59, 999);
		bean.setStart(start);
		bean.setEnd(end);
		TopContentsCDP topContentsCDP = new TopContentsCDP(_statsManager, bean);
		Object produceDataset = topContentsCDP.produceDataset(null);
		assertNotNull(produceDataset);
	}
	
	public void testCountIPByDateInterval() throws ApsSystemException, SQLException {
		Calendar start = CalendarConverter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		
		int countIp =_statsManager.getIPByDateInterval(start, end);
		assertEquals(1, countIp);
	}
	
	public void testGetFirsrCalendarDay() {
		Calendar firstCalendarDay = _statsManager.getFirstCalendarDay();
		String now = DateConverter.getFormattedDate(firstCalendarDay.getTime(), JpStatsSystemConstants.DATE_FORMAT);
		assertEquals("26/02/2007", now);
	}
	
	private StatsRecord createRecordForTest() {
		StatsRecord statsRecord = new StatsRecord(Calendar.getInstance());
		statsRecord.setIp("127.0.0.1");
		statsRecord.setReferer("http://www.japsportal.org/japsportal/it/chi_siamo.wp");
		statsRecord.setSessionId("A3EFA0BC2E5FB24436609B5BE786EDCA");
		statsRecord.setRole("");
		statsRecord.setPageCode("chi_siamo");
		statsRecord.setLangcode("it");
		statsRecord.setUseragent("Mozilla/5.0 (X11; U; Linux i686; it; rv:1.9.0.7) Gecko/2009030422 Ubuntu/8.10 (intrepid) Firefox/3.0.7");
		statsRecord.setBrowserLang("it-it,it;q=0.8,en-us;q=0.5,en;q=0.3");
		statsRecord.setContentId("ART124");
		return statsRecord;
	}
	
}
