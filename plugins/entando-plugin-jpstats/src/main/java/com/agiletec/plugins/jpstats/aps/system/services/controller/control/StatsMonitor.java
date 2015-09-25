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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsManager;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsRecord;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Statistic Service. The following informations are collected:<br>
 *  <code>ip</code>(request IP address)<br>
 *  <code>referer</code>(request URL)<br>
 *  <code>session_id</code>(the session ID)<br>
 *  <code>role</code>(role of the user that performs the request)<br>
 *  <code>timestamp</code>(timestamp value of the request)<br>
 *  <code>year</code>(year of the request (yyyy))<br>
 *  <code>month</code>(month of the request (mm))<br>
 *  <code>day</code>(day of the request (dd))<br>
 *  <code>hour</code>(hour of the request)<br>
 *  <code>pagecode</code>(code of the page)<br>
 *  <code>content</code>(the code of the content rendered in the main frame of the page)<br>
 *  <code>langcode</code>(code of the language og the page)<br>
 *  <code>useragent</code>(useragent)<br>
 *  <code>browserlang</code>(useragent lang)<br>
 *  This informations are collected in a HashMap and then sent to the StatsManager
 *  to be stored in the data base
 * @version 1.2
 * @author Manuela Lisci
 */
public class StatsMonitor implements ControlServiceInterface {

	private static final Logger _logger = LoggerFactory.getLogger(StatsMonitor.class);
	
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} init", this.getClass().getName());
	}

	/**
	 * Execution. The operation details are described on class documentation.
	 * @param reqCtx the request context
	 * @param status the exit status of the previous service
	 * @return the exit status
	 */
	@Override
	public int service(RequestContext reqCtx, int status) {
		ApsSystemUtils.getLogger().trace("Invoked " + this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			StatsRecord statsRecord = this.loadStatsRecord(reqCtx);
			this.getStatsManager().addStatsRecord(statsRecord);
			retStatus = ControllerManager.CONTINUE;
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			_logger.error("An error occurred in statistics control", t);
			//ApsSystemUtils.logThrowable(t, this, "service", "An error occurred in statistics control");
		}
		return retStatus;
	}

	/**
	 * Load the information in the HashMap
	 * @param reqCtx the request context
	 * @return statsRecord HashMap with the info ready to be sent to the StatsManager
	 */
	private StatsRecord loadStatsRecord(RequestContext reqCtx) {
		HttpServletRequest req = reqCtx.getRequest();
		HttpSession session = req.getSession();
		StatsRecord statsRecord = new StatsRecord(Calendar.getInstance());
		Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		Page page = (Page) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		statsRecord.setIp(reqCtx.getRequest().getRemoteAddr());
		statsRecord.setReferer(reqCtx.getRequest().getHeader("Referer"));
		statsRecord.setSessionId(session.getId());
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		StringBuilder rolesBuffer = new StringBuilder();
		boolean isFirstRole = true;
		List<Authorization> authorizations = currentUser.getAuthorizations();
		if (null != authorizations && !authorizations.isEmpty()) {
			for (int i = 0; i < authorizations.size(); i++) {
				Authorization authorization = authorizations.get(i);
				if (null != authorization && null != authorization.getRole()) {
					Role role = authorization.getRole();
					if (!isFirstRole) rolesBuffer.append(" - ");
					rolesBuffer.append(role.getName());
					isFirstRole = false;
				}
			}
		}
		statsRecord.setRole(rolesBuffer.toString());
		NumberFormat formato = NumberFormat.getIntegerInstance(java.util.Locale.ITALIAN);
		formato.setMinimumIntegerDigits(2);
		statsRecord.setPageCode(page.getCode());
		statsRecord.setLangcode(lang.getCode());
		statsRecord.setUseragent(reqCtx.getRequest().getHeader("User-Agent"));
		statsRecord.setBrowserLang(reqCtx.getRequest().getHeader("accept-language"));
		statsRecord.setContentId(this.getContentId(page));
		return statsRecord;
	}

	/**
	 * If exists, returns the the code of the content
	 * rendered in the main frame of the page
	 * @param page The page to check for
	 * @return String the content id rendered in the main frame of the page, or null
	 */
	private String getContentId(Page page) {
		String contentId = null;
		int mainFrame = page.getModel().getMainFrame();
		if (mainFrame >= 0) {
			Widget widget = page.getWidgets()[mainFrame];
			ApsProperties config = (null != widget) ? widget.getConfig() : null;
			contentId = (null != config) ? config.getProperty("contentId") : null;
		}
		return contentId;
	}

	protected StatsManager getStatsManager() {
		return _statsManager;
	}
	public void setStatsManager(StatsManager statsManager) {
		this._statsManager = statsManager;
	}

	private StatsManager _statsManager;

}
