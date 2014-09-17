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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content.showlet;

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



	/**
	 * Returns search result
	 */
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
		String paramName = JpFacetNavSystemConstants.CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME;
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

	/**
	 * Returns occurrences
	 */
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
		List<Group> groups = authManager.getGroupsOfUser(currentUser);
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
