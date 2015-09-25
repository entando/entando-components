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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.common.api.CollaborationAbstractApiInterface;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.api.JAXBIdeaInstance;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.portal.specialwidget.IdeaInstanceWidgetAction;

public class ApiIdeaInstanceInterface extends CollaborationAbstractApiInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiIdeaInstanceInterface.class);

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/ideaInstances?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<String> getIdeaInstancesForApi(Properties properties) throws Throwable {
		List<String> idList = new ArrayList<String>();
		try {
			Collection<String> groupCodes = this.extractGroups(properties);
			String codeParam = properties.getProperty("code");
			if (StringUtils.isNotBlank(codeParam)) codeParam = URLDecoder.decode(codeParam, "UTF-8");
			idList = this.getIdeaInstanceManager().getIdeaInstances(groupCodes, codeParam);
		} catch (Throwable t) {
			_logger.error("Error extracting ideaInstances", t);
			throw new ApsSystemException("Error extracting ideaInstances", t);
		}
		return idList;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/ideaInstances?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBIdeaInstance getIdeaInstanceForApi(Properties properties) throws Throwable {
		JAXBIdeaInstance jaxbIdeaInstance = null;
		try {
			String codeParam = properties.getProperty("code");
			if (StringUtils.isNotBlank(codeParam)) codeParam = URLDecoder.decode(codeParam, "UTF-8");

			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
			//TODO CREATE ROLE
			List<Integer> ideaStateFilter = new ArrayList<Integer>();
			ideaStateFilter.add(IIdea.STATUS_APPROVED);

			if (null != user && !this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)) {
				ideaStateFilter.clear();
			}
			IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(codeParam , ideaStateFilter);
			if (null == ideaInstance) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + codeParam + "' does not exist", Response.Status.CONFLICT);
			}
			if (!isAuthOnInstance(user, ideaInstance)) {
				_logger.warn("the current user is not granted to any group required by instance {}", codeParam);
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + codeParam + "' does not exist", Response.Status.CONFLICT);
			}
			jaxbIdeaInstance = new JAXBIdeaInstance(ideaInstance);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error extracting ideaInstance", t);
			throw new ApsSystemException("Error extracting idea instance", t);
		}
		return jaxbIdeaInstance;
	}

	public StringApiResponse addIdeaInstance(JAXBIdeaInstance jaxbIdeaInstance, Properties properties) throws Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String code = jaxbIdeaInstance.getCode();
			
			IdeaInstance clone = this.getIdeaInstanceManager().getIdeaInstance(code);
			if (null != clone) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Code already in use", Response.Status.CONFLICT);									
			}
			Set<String> parsedGroups = this.validateGroups(jaxbIdeaInstance);
			if (null == parsedGroups || parsedGroups.isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "At least one group is mandatory", Response.Status.CONFLICT);									
			}
			IdeaInstance ideainstance = jaxbIdeaInstance.getIdeaInstance();
			ideainstance.setGroups(new ArrayList<String>(parsedGroups));
			this.getIdeaInstanceManager().addIdeaInstance(ideainstance);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error adding an ideaInstance", t);
			throw t;
		}
		return response;
	}

	public StringApiResponse updateIdeaInstance(JAXBIdeaInstance jaxbIdeaInstance, Properties properties) throws Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String code = jaxbIdeaInstance.getCode();
			IdeaInstance clone = this.getIdeaInstanceManager().getIdeaInstance(code);
			if (null == clone) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "No ideaInstance found with id " + code, Response.Status.CONFLICT);									
			}
			Set<String> parsedGroups = this.validateGroups(jaxbIdeaInstance);
			if (null == parsedGroups || parsedGroups.isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "At least one group is mandatory", Response.Status.CONFLICT);									
			}
			IdeaInstance ideainstance = jaxbIdeaInstance.getIdeaInstance();
			ideainstance.setGroups(new ArrayList<String>(parsedGroups));
			this.getIdeaInstanceManager().updateIdeaInstance(ideainstance);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error updating an ideaInstance", t);
			throw t;
		}
		return response;
	}

	public StringApiResponse deleteIdeaInstance(Properties properties) throws Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String code = properties.getProperty("code");
			if (StringUtils.isNotBlank(code)) code = URLDecoder.decode(code, "UTF-8");
			IdeaInstance clone = this.getIdeaInstanceManager().getIdeaInstance(code);
			if (null == clone) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "No ideaInstance found with id " + code, Response.Status.CONFLICT);									
			}
			IPage page = this.getPageWithInstance(code);
			if (null != page) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Cannod dete the intance with code '" + code + "'. It's published in page " + page.getCode(), Response.Status.CONFLICT);													
			}
			this.getIdeaInstanceManager().deleteIdeaInstance(code);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error deleting an ideaInstance", t);
			throw t;
		}
		return response;
	}

	private Set<String> validateGroups(JAXBIdeaInstance jaxbIdeaInstance) {
		Set<String> parsedGroups = new HashSet<String>();
		List<String> groups = jaxbIdeaInstance.getGroups();
		if (null == groups || groups.isEmpty()) return parsedGroups;
		for (int i = 0; i < groups.size(); i++) {
			String groupName = groups.get(i);
			if (null != this.getGroupManager().getGroup(groupName)) {
				parsedGroups.add(groupName);
			}
		}
		return parsedGroups;
	}

	protected IPage getPageWithInstance(String code) {
		IPage page = null;
		try {
			List<IPage> pages = this.getPageManager().getWidgetUtilizers(IdeaInstanceWidgetAction.WIDGET_CODE);
			if (null != pages && !pages.isEmpty()) {
				Iterator<IPage> it = pages.iterator();
				while (it.hasNext()) {
					IPage currentPage = it.next();
					Widget[] widgets = currentPage.getWidgets();
					for (int i = 0; i < widgets.length; i++) {
						Widget currentWidget = widgets[i];
						if (null != currentWidget && currentWidget.getType().getCode().equals(IdeaInstanceWidgetAction.WIDGET_CODE)) {
							ApsProperties config = currentWidget.getConfig();
							if (null != config) {
								String value = config.getProperty(IdeaInstanceWidgetAction.WIDGET_PARAM_IDEA_INSTANCE);
								if (StringUtils.isNotBlank(value) && value.equals(code)) {
									page = currentPage;
									break;
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking if idea instance is published", t);
			throw new RuntimeException("Error checking if idea instance is published" );
		}
		return page;
	}
	
	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	private IPageManager _pageManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private IGroupManager _groupManager;

}
