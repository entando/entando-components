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

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * 
 * @author E.Santoboni
 */
public interface IFacetNavHelper {

	/**
	 * Returns search result
	 * @param selectedFacetNodes
	 * @param reqCtx the request context
	 * @return search result
	 * @throws ApsSystemException
	 */
	public List<String> getSearchResult(List<String> selectedFacetNodes, RequestContext reqCtx) throws ApsSystemException;

	/**
	 * Returns occurrences
	 * @param selectedFacetNodes
	 * @param reqCtx
	 * @return occurrences
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getOccurences(List<String> selectedFacetNodes, RequestContext reqCtx) throws ApsSystemException;

	/**
	 * Returns tree node manager
	 * @return tree node manager
	 */
	public ITreeNodeManager getTreeNodeManager();

}
