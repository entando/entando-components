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
package com.agiletec.plugins.jpstats.aps.system.services.controller.control;

import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.util.CalendarConverter;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

/**
 * Test the Stats Monitor.
 * @author IFTS
 */
public class TestStatsMonitor extends ApsPluginBaseTestCase {

	public void testInit(){
		assertNotNull(_statMonitor);
	}

	public void testService() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		
		User user =  new User();
		
		reqCtx.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);

		Page page = new Page();
		page.setCode("login");
		page.setGroup("free");
		page.setModel(new PageModel());

		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);

		Lang lang = new Lang();
		lang.setCode("it");
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);

		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setRemoteAddr(ip);
		request.addHeader("Referer", "homeTest");
		request.addHeader("User-Agent", "Mio Test Browser");
		request.addHeader("accept-language", "sardo");

		int status = _statMonitor.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		this.deleteStatsRecord();
	}


	public void testServiceFailure() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		
		User user =  new User();
		reqCtx.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);

		Page page = new Page();
		page.setCode("login");
		page.setGroup("free");
		page.setModel(new PageModel() );

		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);

		Lang lang = new Lang();
		lang.setCode("it");
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);

		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setRemoteAddr("255.255.255.255");
		request.addHeader("Referer", "homeTest");
		request.addHeader("User-Agent", "Mio Test Browser");
		request.addHeader("accept-language", "sardo");

		int status = _statMonitor.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		this.deleteStatsRecord();
	}

	private void deleteStatsRecord() throws ApsSystemException {
		String today = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		Calendar start = CalendarConverter.getCalendarDay(today, 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay(today, 23, 59, 59, 999);
		_statsManager.deleteStatsRecord(start.getTime(), end.getTime());
	}

	@Override
	protected void init() throws Exception {
		super.init();
		this._statMonitor = (StatsMonitor) this.getApplicationContext().getBean(JpStatsSystemConstants.STATS_MONITOR_CONTROL_SERVICE);
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			TestStatsUtils.cleanDB(ip, dataSource);
			this._statMonitor = null;
			super.tearDown();
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	private StatsMonitor _statMonitor = null;
	private static final String ip = "255.255.255.255";
	
}
