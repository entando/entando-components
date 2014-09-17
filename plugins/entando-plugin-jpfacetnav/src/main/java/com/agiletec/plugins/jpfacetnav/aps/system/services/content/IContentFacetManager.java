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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * 
 * @author E.Santoboni
 */
public interface IContentFacetManager {
	
	/**
	 * Returns contents id
	 * @param contentTypeCodes
	 * @param facetNodeCodes 
	 * @param groupCodes
	 * @return contents id
	 * @throws ApsSystemException
	 */
	public List<String> loadContentsId(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException;
	
	/**
	 * Returns occurrences
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return occurrences
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException;
	
}
