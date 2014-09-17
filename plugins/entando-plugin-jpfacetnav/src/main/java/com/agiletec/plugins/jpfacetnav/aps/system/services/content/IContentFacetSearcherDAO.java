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

import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;

/**
 * 
 * @author E.Santoboni
 */
public interface IContentFacetSearcherDAO extends IEntitySearcherDAO {
	
	/**
	 * Returns contents id
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return contents id
	 */
	public List<String> loadContentsId(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes);
	
	/**
	 * Returns occurrences
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return occurrences
	 */
	public Map<String, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes);
	
}
