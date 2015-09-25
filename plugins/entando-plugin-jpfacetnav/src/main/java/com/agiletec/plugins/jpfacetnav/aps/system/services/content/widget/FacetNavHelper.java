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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;
import com.agiletec.plugins.jpfacetnav.aps.system.services.content.IContentFacetManager;

/**
 * @author E.Santoboni
 */
public class FacetNavHelper implements IFacetNavHelper {
	
	@Override
	public List<String> getSearchResult(List<String> selectedFacetNodes, RequestContext reqCtx) throws ApsSystemException {
		List<String> contentTypesFilter = this.getContentTypesFilter(reqCtx);
		List<String> userGroupCodes = new ArrayList<String>(this.getAllowedGroups(reqCtx));
		return this.getContentFacetManager().loadContentsId(contentTypesFilter, selectedFacetNodes, userGroupCodes);
	}
	
	/**
	 * Returns Content types filter
	 * @param reqCtx
	 * @return content types filter
	 * @throws ApsSystemException
	 */
	private List<String> getContentTypesFilter(RequestContext reqCtx) throws ApsSystemException {
		List<String> contentTypes = new ArrayList<String>();
		Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		if (null == currentShowlet.getConfig()) return contentTypes;
		String paramName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
		String contentTypesParamValue = currentShowlet.getConfig().getProperty(paramName);
		if (null != contentTypesParamValue) {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, reqCtx.getRequest());
			String[] contentTypesArray = contentTypesParamValue.split(",");
			for (int i=0; i<contentTypesArray.length; i++) {
				String contentTypeCode = contentTypesArray[i].trim();
				if (null != contentManager.getSmallContentTypesMap().get(contentTypeCode)) {
					contentTypes.add(contentTypeCode);
				}
			}
		}
		return contentTypes;
	}

	@Override
	public Map<String, Integer> getOccurences(List<String> selectedFacetNodes, RequestContext reqCtx) throws ApsSystemException {
		List<String> contentTypesFilter = this.getContentTypesFilter(reqCtx);
		List<String> userGroupCodes = new ArrayList<String>(this.getAllowedGroups(reqCtx));
		return this.getContentFacetManager().getOccurrences(contentTypesFilter, selectedFacetNodes, userGroupCodes);
	}

	/**
	 * Returns allowed groups
	 * @param reqCtx The request context
	 * @return allowed groups
	 */
	private Collection<String> getAllowedGroups(RequestContext reqCtx) {
		IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, reqCtx.getRequest());
		UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		List<Group> groups = authManager.getUserGroups(currentUser);
		Set<String> allowedGroup = new HashSet<String>();
		Iterator<Group> iter = groups.iterator();
		while (iter.hasNext()) {
			Group group = iter.next();
			allowedGroup.add(group.getName());
		}
		allowedGroup.add(Group.FREE_GROUP_NAME);
		return allowedGroup;
	}

	public IContentFacetManager getContentFacetManager() {
		return _contentFacetManager;
	}
	public void setContentFacetManager(IContentFacetManager contentFacetManager) {
		this._contentFacetManager = contentFacetManager;
	}

	@Override
	public ITreeNodeManager getTreeNodeManager() {
		return _treeNodeManager;
	}
	public void setTreeNodeManager(ITreeNodeManager treeNodeManager) {
		this._treeNodeManager = treeNodeManager;
	}

	private IContentFacetManager _contentFacetManager;
	private ITreeNodeManager _treeNodeManager;

}
