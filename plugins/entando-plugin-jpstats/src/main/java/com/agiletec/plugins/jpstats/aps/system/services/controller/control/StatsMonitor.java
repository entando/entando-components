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

import java.text.NumberFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsManager;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsRecord;


/**
 * Statistic Service. The following informations are collected:<br>
 *  <code>ip</code>(request IP address)<br>
 *  <code>referer</code>(request URL)<br>
 *  <code>session_id</code>(the session ID)<br>
 *  <code>role</code>(jAPS role of the user that performs the request)<br>
 *  <code>timestamp</code>(timestamp value of the request)<br>
 *  <code>year</code>(year of the request (yyyy))<br>
 *  <code>month</code>(month of the request (mm))<br>
 *  <code>day<code>(day of the request (dd))<br>
 *  <code>hour</code>(hour of the request)<br>
 *  <code>pagecode</code>(jAPS code of the page)<br>
 *  <code>content</code>(the code of the content rendered in the main frame of the page)<br>
 *  <code>langcode</code>(jAPS code of the language og the page)<br>
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
		StringBuffer rolesBuffer = new StringBuffer();
		boolean isFirstRole = true;
		IApsAuthority[] auths = currentUser.getAuthorities();
		for (int i=0; i<auths.length; i++) {
			if (auths[i] instanceof Role) {
				Role role = (Role) auths[i];
				if (!isFirstRole) rolesBuffer.append(" - ");
				rolesBuffer.append(role.getName());
				isFirstRole = false;
			}
		}
		statsRecord.setRole(rolesBuffer.toString());
		NumberFormat formato = NumberFormat.getIntegerInstance(java.util.Locale.ITALIAN);
		formato.setMinimumIntegerDigits(2);
		String contentId = this.getContentId(page);
		statsRecord.setPageCode(page.getCode());
		statsRecord.setLangcode(lang.getCode());
		statsRecord.setUseragent(reqCtx.getRequest().getHeader("User-Agent"));
		statsRecord.setBrowserLang(reqCtx.getRequest().getHeader("accept-language"));
		statsRecord.setContentId(contentId);
		return statsRecord;
	}

	/**
	 * If exists, returns the the code of the content
	 * rendered in the main frame of the page
	 * @param page The page to check for
	 * @return String the content id rendered in the main frame of the page, or null
	 */
	private String getContentId(Page page) {
		String content = null;
		int mainFrame = page.getModel().getMainFrame();
		if(mainFrame >= 0) {
			Widget widget = page.getWidgets()[mainFrame];
			if(null != widget && null != widget.getPublishedContent()) {
				content = widget.getPublishedContent();
			}
		}
		return content;
	}

	protected StatsManager getStatsManager() {
		return _statsManager;
	}
	public void setStatsManager(StatsManager statsManager) {
		this._statsManager = statsManager;
	}

	private StatsManager _statsManager;

}
