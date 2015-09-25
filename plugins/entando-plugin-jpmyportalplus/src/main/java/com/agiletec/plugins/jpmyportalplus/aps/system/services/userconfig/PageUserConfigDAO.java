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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public class PageUserConfigDAO extends AbstractDAO implements IPageUserConfigDAO {

	private static final Logger _logger = LoggerFactory.getLogger(PageUserConfigDAO.class);
	
	@Override
	public void syncCustomization(List<WidgetType> allowedWidgets, String voidWidgetCode) {
		Set<String> allowedWidgetCodes = this.getAllowedWidgetCodes(allowedWidgets);
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_CONFIGURED_WIDGET_CODE);
			res = stat.executeQuery();
			while (res.next()) {
				String currentCode = res.getString(1);
				WidgetType currentConfiguredShowlet = this.getWidgetTypeManager().getWidgetType(currentCode);
				if (null == currentConfiguredShowlet) {
					_logger.warn("{}: deleting unknown widget '{}' from the configuration bean", JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM, currentCode);
					this.purgeConfigurationFromInvalidWidgets(conn, currentCode);
				} else {
					if (!allowedWidgetCodes.contains(currentCode) && !currentCode.equals(voidWidgetCode)) {
						_logger.info("{}: removing the no longer configurable Widget '{}' from the configuration bean", JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM, currentCode);
						this.purgeConfigurationFromInvalidWidgets(conn, currentCode);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while cleaning the configuration",  t);
			throw new RuntimeException("Error while cleaning the configuration", t);
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
	}

	private Set<String> getAllowedWidgetCodes(List<WidgetType> allowedWidgets) {
		Set<String> codes = new HashSet<String>();
		if (null == allowedWidgets) {
			return codes;
		}
		for (int i = 0; i < allowedWidgets.size(); i++) {
			WidgetType type = allowedWidgets.get(i);
			if (null != type) {
				codes.add(type.getCode());
			}
		}
		return codes;
	}

	private void purgeConfigurationFromInvalidWidgets(Connection conn, String code) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_USER_CONFIG_BY_WIDGETS_CODE);
			stat.setString(1, code);
			stat.execute();
		} catch (Throwable t) {
			throw new ApsSystemException("ERROR: could not remove invalid widgets from the customization database",t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	@Deprecated
	public List<WidgetType> buildCustomizableShowletsList(MyPortalConfig config) {
		return this.buildCustomizableWidgetsList(config);
	}

	@Override
	public List<WidgetType> buildCustomizableWidgetsList(MyPortalConfig config) {
		List<WidgetType> result = new ArrayList<WidgetType>();
		List<WidgetType> allShowlets = null;
		if (null == config) {
			return result;
		}
		allShowlets = this.getWidgetTypeManager().getWidgetTypes();
		if (null != allShowlets) {
			Set<String> allowedWidgetsCode = config.getAllowedWidgets();
			if (allowedWidgetsCode != null && allowedWidgetsCode.size() > 0) {
				for (WidgetType currentType : allShowlets) {
					if (allowedWidgetsCode.contains(currentType.getCode())) {
						result.add(currentType);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void updateUserPageConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfos) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteUserConfig(username, page, updateInfos, conn);
			this.addUserConfig(username, page, updateInfos, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating user config",  t);
			throw new RuntimeException("Error while updating user config", t);
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void deleteUserConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfos, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_PAGE_USER_CONFIG);
			for (int i = 0; i < updateInfos.length; i++) {
				WidgetUpdateInfoBean infoBean = updateInfos[i];
				stat.setString(1, username);
				stat.setString(2, page.getCode());
				stat.setInt(3, infoBean.getFramePos());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Error while deleting a user config record for {}", username,  t);
			throw new RuntimeException("Error while deleting a user config record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void addUserConfig(String username, IPage page, WidgetUpdateInfoBean[] updateInfos, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_USER_CONFIG);
			for (int i = 0; i < updateInfos.length; i++) {
				WidgetUpdateInfoBean infoBean = updateInfos[i];
				stat.setString(1, username);
				stat.setString(2, page.getCode());
				stat.setInt(3, infoBean.getFramePos());
				stat.setString(4, infoBean.getShowlet().getType().getCode());
				stat.setNull(5, java.sql.Types.CHAR);
				stat.setInt(6, infoBean.getStatus());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Error while adding a user config record",  t);
			throw new RuntimeException("Error while adding a user config record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	@Override
	public PageUserConfigBean getUserConfig(String username) {
		Connection conn = null;
		PageUserConfigBean result = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USER_CONFIG);
			stat.setString(1, username);
			res = stat.executeQuery();
			result = this.createPageUserConfigBeanFromResultSet(username, res);
		} catch (Throwable t) {
			_logger.error("Error loading user config for user {}", username, t);
			throw new RuntimeException("Error loading user config", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return result;
	}

	private int getPageModelframe(String pageCode) throws ApsSystemException {
		PageModel current = this.getPageManager().getPage(pageCode).getModel();
		try {
			return current.getFrames().length;
		} catch (Throwable t) {
			throw new ApsSystemException("ERROR: unknown page code '" + pageCode + "'");
		}
	}

	/**
	 * Create a widget with a given code and optional configuration.
	 * @param widgetcode the code of the widget
	 * @param config the configuration for the current widget
	 * @return a new object with the given code and configuration
	 * @throws ApsSystemException if the given code is unknown or faulting XML configuration
	 */
	private Widget createWidgetFromRecord(String widgetcode, String config) throws ApsSystemException {
		Widget newShowlet = new Widget();
		WidgetType inheritedType = this.getWidgetTypeManager().getWidgetType(widgetcode);
		newShowlet.setType(inheritedType);
		ApsProperties properties = null;
		newShowlet.setConfig(properties);
		return newShowlet;
	}

	private PageUserConfigBean createPageUserConfigBeanFromResultSet(String username, ResultSet res) {
		PageUserConfigBean pageUserBean = new PageUserConfigBean(username);
		try {
			while (res.next()) {
				String currentPageCode = res.getString(1);
				CustomPageConfig pageConfig = pageUserBean.getConfig().get(currentPageCode);
				if (null == pageConfig) {
					int frames = this.getPageModelframe(currentPageCode);
					pageConfig = new CustomPageConfig(currentPageCode, frames);
					pageUserBean.getConfig().put(currentPageCode, pageConfig);
				}
				int currentFramePos = res.getInt(2);
				if (currentFramePos >= pageConfig.getConfig().length) {
					_logger.error("invalid position {} in pagecode {} - user {}", currentFramePos, currentPageCode, username);
					continue;
				}
				String currentShowletCode = res.getString(3);
				String currentConfig = res.getString(4);
				Widget widget = this.createWidgetFromRecord(currentShowletCode, currentConfig);
				pageConfig.getConfig()[currentFramePos] = widget;
				int status = res.getInt(5);
				pageConfig.getStatus()[currentFramePos] = status;
			}
		} catch (Throwable t) {
			_logger.error("Error while parsing the resultset",  t);
			throw new RuntimeException("Error while parsing the resultset", t);
		}
		return pageUserBean;
	}

	@Override
	public void removeUserPageConfig(String username, String pageCode, Integer framePosition) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			String query = (null == username) ? DELETE_PAGE_CONFIGS : DELETE_PAGE_USER_CONFIGS;
			if (null != framePosition) {
				query += " AND framepos = ?";
			}
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(query);
			int index = 0;
			if (null != username) {
				stat.setString(++index, username);
			}
			stat.setString(++index, pageCode);
			if (null != framePosition) {
				stat.setInt(++index, framePosition);
			}
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating user configs",  t);
			throw new RuntimeException("Error while updating user configs", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void removeUnauthorizedShowlet(String username, String showletCode) {
		this.removeUnauthorizedWidget(username, showletCode);
	}

	@Override
	public void removeUnauthorizedWidget(String username, String widgetCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_UNAUTHORIZATED_WIDGETS);
			stat.setString(1, username);
			stat.setString(2, widgetCode);
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while removing unauthorized widget",  t);
			throw new RuntimeException("Error while removing unauthorized widget", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	private IPageManager _pageManager;
	private IPageModelManager _pageModelManager;

	private IWidgetTypeManager _widgetTypeManager;

	private final String ADD_USER_CONFIG =
		"INSERT INTO jpmyportalplus_userpageconfig (username, pagecode, framepos, widgetcode, config, closed) VALUES (?, ?, ?, ?, ?, ?)";

	private final String DELETE_PAGE_USER_CONFIG =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND pagecode = ? AND framepos = ?";

	private final String DELETE_PAGE_CONFIGS =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE pagecode = ? ";

	private final String DELETE_PAGE_USER_CONFIGS =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND pagecode = ? ";

	private final String GET_USER_CONFIG =
		"SELECT pagecode, framepos, widgetcode, config, closed FROM jpmyportalplus_userpageconfig WHERE username = ? ORDER BY pagecode";

	private final String GET_CONFIGURED_WIDGET_CODE =
		"SELECT widgetcode FROM jpmyportalplus_userpageconfig GROUP BY widgetcode";

	private final String DELETE_USER_CONFIG_BY_WIDGETS_CODE =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE widgetcode = ? ";

	private final String DELETE_UNAUTHORIZATED_WIDGETS =
			"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND widgetcode = ? ";

}