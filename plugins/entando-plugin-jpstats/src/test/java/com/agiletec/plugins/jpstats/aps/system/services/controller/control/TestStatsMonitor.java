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
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.util.CalendarConverter;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

/**
 * Test the Stats Monitor.
 * @version 1.0
 * @author IFTS
 */
public class TestStatsMonitor extends ApsPluginBaseTestCase {

	public void testInit(){
		assertNotNull(_statMonitor);
	}

	public void testService() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();

		Role role = new Role();
		role.setName("testRole");

		User user =  new User();
		user.addAutority(role);

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

		Role role = new Role();
		role.setName("testRole");
		User user =  new User();
		user.addAutority(role);
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
	private String ip = "255.255.255.255";

}
