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
package com.agiletec.plugins.jpstats.apsadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.data.category.DefaultIntervalCategoryDataset;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.util.TopContentsCDP;
import com.opensymphony.xwork2.Action;

import de.laures.cewolf.DatasetProducer;

public class TestStatisticsAction extends ApsAdminPluginBaseTestCase {
	
	public void testPerformFirstView() throws Throwable {
		String start = "";
		String end = "";
		this.setUserOnSession("admin");
		String result = this.executeViewStats(start, end);
		assertEquals(Action.SUCCESS, result);
		StatisticsAction action = (StatisticsAction) this.getAction();
		DatasetProducer accessChart = (DatasetProducer) action.getHitsTimeData();
		DatasetProducer mostVisitedPagesChart = (DatasetProducer) action.getMostVisitedPagestimeData();
		
		assertNotNull(accessChart);
		assertNotNull(mostVisitedPagesChart);
		
		Calendar today = Calendar.getInstance();
		today.add(Calendar.WEEK_OF_YEAR, -1);
		String startString = DateConverter.getFormattedDate(today.getTime(), JpStatsSystemConstants.DATE_FORMAT);
		String extractedStringStart = DateConverter.getFormattedDate(action.getStartDate(), JpStatsSystemConstants.DATE_FORMAT);
		assertEquals(startString, extractedStringStart);
	}
	
	public void testPerformWithDataView() throws Throwable {
		Calendar firstDay = this._statsManager.getFirstCalendarDay();
		String start = DateConverter.getFormattedDate(firstDay.getTime(), JpStatsSystemConstants.DATE_FORMAT);
		String end = "";
		this.setUserOnSession("admin");
		String result = this.executeViewStats(start, end);
		assertEquals(Action.SUCCESS, result);
		StatisticsAction action = (StatisticsAction) this.getAction();
		DatasetProducer accessChart =  (DatasetProducer) action.getHitsTimeData();
		DatasetProducer mostVisitedPagesChart = (DatasetProducer) action.getMostVisitedPagestimeData();
		TopContentsCDP topContents = (TopContentsCDP) action.getTopContentsDataset();//request.getAttribute("TopContents");
		assertNotNull(accessChart);
		assertNotNull(mostVisitedPagesChart);
		assertNotNull(topContents);
		DefaultIntervalCategoryDataset data = (DefaultIntervalCategoryDataset) topContents.produceDataset(null);
		List categories = data.getCategories();
		assertTrue(categories.size()>0);
	}
	
	public void testPerformErrorFormatDatesView() throws Throwable {
		String start = "01/01/2006";
		String end = "";
		this.setUserOnSession("admin");
		String result = this.executeViewStats(start, end);
		assertEquals(Action.SUCCESS, result);
		StatisticsAction action = (StatisticsAction) this.getAction();
		DatasetProducer accessChart =  (DatasetProducer) action.getHitsTimeData();
		DatasetProducer mostVisitedPagesChart = (DatasetProducer) action.getMostVisitedPagestimeData();
		
		assertNotNull(accessChart);
		assertNotNull(mostVisitedPagesChart);
		
		Date s = action.getStartDate();
		Date e = action.getEndDate();
		
		String startString = DateConverter.getFormattedDate(s, JpStatsSystemConstants.DATE_FORMAT);
		assertEquals("01/01/2006", startString);
		
		String endString = DateConverter.getFormattedDate(e, JpStatsSystemConstants.DATE_FORMAT);
		assertEquals(endString, DateConverter.getFormattedDate(new Date(), JpStatsSystemConstants.DATE_FORMAT));
		
		result = this.executeViewStats(startString, endString);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testPerformErrorDatesOrderView() throws Throwable {
		String start = "01/01/2007";
		String end = "01/01/2006";
		this.setUserOnSession("admin");
		String result = this.executeViewStats(start, end);
		assertEquals(Action.INPUT, result);
		StatisticsAction action = (StatisticsAction) this.getAction();
		
		DatasetProducer accessChart = (DatasetProducer) action.getHitsTimeData();
		DatasetProducer mostVisitedPagesChart = (DatasetProducer) action.getMostVisitedPagestimeData();
		
		assertNotNull(accessChart);
		assertNotNull(mostVisitedPagesChart);
		assertEquals(1, action.getFieldErrors().size());
	}
	
	private String executeViewStats(String start, String end) throws Throwable {
		this.initAction("/do/jpstats/Statistics", "view");
		this.addParameter("start", start);
		this.addParameter("end", end);
		this.addParameter("selectedTypes", new ArrayList<String>().add("Contenuti"));
		return this.executeAction();
	}
	
}
